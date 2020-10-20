package oct_13;


import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.Arrays;

public class  Window extends JFrame  {
    int centerX = 450;
    int centerY = 300;
    boolean flag=true;
    int scale=30;//деления на оси
    int x,y;
    double prev_x, prev_y;
    double precision = 1e-3;
    Polynom res;
    int index =0;//для подсчета количества узлов(значений)
    double pointsX[]=new double[30] ;//узловые точки
    double pointsY[]=new double[30];//значения в узловых точках

    public Window(){
        this.setBounds(500, 100, 910, 610);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLayout((LayoutManager)null);
        this.setVisible(true);
        Graphics g= this.getGraphics();//объект для рисования на окне
        g.clearRect(0,0,600,600);//очищаем форму
        drawY(g);//ось ординат
        drawX(g);//ось абцисс

        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
               // g.fillOval(e.getX() - 10/2, e.getY()-10/2, 10 ,10);
                x = e.getX();
                y = e.getY();
                for (int i=0;i<index;i++){//отлавливает одинаковые х и у
                    if ((pointsX[i]!=tranX(x))&&((pointsY[i]!=tranY(y)))){flag=true;}
                    else {flag=false;break;}
                }
                if(flag){
                    pointsX[index]=tranX(x);
                    pointsY[index]=tranY(y);
                        /*int k, u;
                        for ( k = u = 0; u < index; ++u){
                            if (pointsX[k]!=tranX(x)) {pointsX[k++] = pointsX[u];
                                pointsX = Arrays.copyOf(pointsX, k);
                                pointsY = Arrays.copyOf(pointsY, k);
                            }
                    }*/
                    index++;
                    g.clearRect(0,0,910,610);//очищаем форму

                    try {
                        res=lagrange(Arrays.copyOfRange(pointsX,0,index),Arrays.copyOfRange(pointsY,0,index));
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }

                    int wight=4;
                    g.setColor(Color.BLACK);
                    drawX(g);//ось абцисс
                    drawY(g);//ось ординат
                    System.out.println("x = " + tranX(x) + " | y = " + tranY(y));//выводим координаты
                    g.setColor(Color.GREEN);
                    for (int i=0;i<index;i++){
                        g.fillOval(untranX(pointsX[i])-wight/2,untranY(pointsY[i])-wight/2,wight,wight);
                    }
                    g.setColor(Color.CYAN);
                    prev_x = -10;//отрисовка самого полинома
                    prev_y = res.getValue(-14);
                    for(double i = -14; i < 14; i+=precision) {
                        double curr_x = i;//текущая
                        double curr_y = res.getValue(i);
                        g.drawLine(untranX(prev_x), untranY(prev_y),
                                untranX(curr_x), untranY(curr_y));
                        prev_x = curr_x;//ранее(запоминаем предыдущие координаты)
                        prev_y = curr_y;
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }
    public void drawX(Graphics g) {
        g.drawLine(0,centerY,2*centerX,centerY);
        g.drawString("X",2*(centerX-10),centerY+20);
        g.drawLine(2*(centerX-10),centerY-10,2*centerX,centerY);//стрелка
        g.drawLine(2*(centerX-10),centerY+10,2*centerX,centerY);
        for (int i=0;i<(int)(2 * centerX/scale);i++){//рисуем деления
            //g.drawLine(2 * centerX - 80, centerY - 10, 2 * centerX - 50, centerY);
            // g.drawLine(2 * centerX - 80, centerY+ 10, 2 * centerX - 50, centerY);
            g.drawString(""+(i-(int)(2 * centerX/(2*scale))),scale*i-2*5, centerY-5);//подпись значений
            g.drawLine(scale*i, centerY-5,scale*i, centerY+5);
        }
    }
    public void drawY(Graphics g) {
        g.drawLine(centerX,3,centerX,2*centerY);
        g.drawString("Y",centerX+10,43);
        g.drawLine(centerX,30,centerX-10,55);
        g.drawLine(centerX,30,centerX+10,55);
        for (int i=0;i<(int)(centerX*2/scale);i++){
            //g.drawLine(2 * centerX - 80, centerY - 10, 2 * centerX - 50, centerY);
            // g.drawLine(2 * centerX - 80, centerY+ 10, 2 * centerX - 50, centerY);
            if(((int)(centerY*2/(2*scale))-i)==0){//чтобы ноль не отрисовывался два раза
                continue;
            }
            g.drawString(""+((int)(2*centerY/(2*scale))-i),centerX-3*5,scale*i-5);
            g.drawLine(centerX-5,scale*i,centerX+5,scale*i);
        }
    }

    public double tranX(int x){//переводим координаты в "понятные"
        return (x-(double)centerX)/scale;
    }
    public double tranY(int y){
        return -(y-(double)centerY)/scale;
    }
    public int untranX(double x){//переводим координаты обратно
        return (int)((x*scale)+centerX);
    }
    public int untranY(double y){
        return (int)((-y*scale)+centerY);
    }
   /* public void paint(Graphics g) {
        double precision = 1e-3;
        double prev_x = -14;
        double prev_y = res.getValue(-14);
        for(double i = -14; i < 14; i+=precision){
            double curr_x = i;
            double curr_y = res.getValue(i);
            g.drawLine(untranX(prev_x), untranY(prev_y),
                    untranX(curr_x), untranY(curr_y));
            prev_x = curr_x;
            prev_y = curr_y;
        }
       // System.out.println(poly);
    }*/
    public static Polynom lagrange(double[]x, double[] f) throws IOException {//принимает на входы два массива с узлами и значениями
       /* Polynom[] p=new Polynom[x.length];
        for (int i=0;i<p.length;i++){
            p[i]=new Polynom(new double[]{1});
            for (int j=0;j<p.length;j++)
            {
                if(i!=j){
                    p[i]=Polynom.multiplication(1/(x[i]-x[j]),p[i]);
                    p[i]=Polynom.multiplication(p[i],new Polynom(new double[]{1.0,-1*x[j]}));
                }
            }
        }*/
        Polynom []l=new Polynom[x.length];
        //product;
        for (int i=0; i< l.length;i++){
            Polynom res=new Polynom(new double[]{1});
            for (int j=0;j< l.length;j++) {
                if (i != j) {
                    res=Polynom.multiplication(res, Polynom.multiplication(1 / (x[i] - x[j]),
                            new Polynom(new double[]{1, -1 * x[j]})));
                }
            }
            l[i]=res;
        }
        Polynom L=new Polynom(new double[]{0});
        for (int i=0;i< l.length;i++){

            L=Polynom.sum(L,Polynom.multiplication(f[i],l[i]));
        }
        System.out.println(L);
        return L;
    }
}