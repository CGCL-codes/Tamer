package javaapplication3;

import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionListener;

public class Main {

    public static void main(String args[]) {
        Frame frame = new Frame("Maró hatású vicc");
        frame.setSize(800, 600);
        frame.setLocation(250, 100);
        frame.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                e.getWindow().dispose();
            }
        });
        final Button button = new Button("Katt, ha mersz!");
        button.setBounds(350, 275, 100, 50);
        frame.add(button);
        final Canvas canvas = new MyCanvas();
        frame.add(canvas);
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                button.setBackground(new Color((int) (Math.random() * 255 + 1), (int) (Math.random() * 255 + 1), (int) (Math.random() * 255 + 1)));
                canvas.repaint();
            }
        });
        frame.setVisible(true);
    }
}

class MyCanvas extends Canvas {

    int a = 0;

    public void paint(Graphics g) {
        g.setColor(new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)));
        g.fillRect(0, 0, 800, 600);
        if (a % 2 == 0) {
            g.setColor(new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)));
            String text2 = new String("- Hogy hívják azt az operációs rendszert,");
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawChars(text2.toCharArray(), 0, text2.length(), 20, 20);
            String text3 = new String(" amit a vegyi üzemben lévő számítógépeken használnak?");
            g.drawChars(text3.toCharArray(), 0, text3.length(), 20, 40);
            String text4 = new String("- Mar-DOS.");
            g.drawChars(text4.toCharArray(), 0, text4.length(), 20, 500);
        }
        if (a % 2 == 1) {
            g.setColor(new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)));
            String text2 = new String("- És a győztes op.rendszert?");
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawChars(text2.toCharArray(), 0, text2.length(), 20, 20);
            String text4 = new String("- WinDOS.");
            g.drawChars(text4.toCharArray(), 0, text4.length(), 20, 500);
        }
        g.setColor(new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)));
        g.drawArc(20, 100, 200, 200, 0, 360);
        g.drawArc(75, 160, 30, 30, 0, 360);
        g.drawArc(135, 160, 30, 30, 0, 360);
        g.drawArc(70, 200, 100, 50, 180, 180);
        a++;
    }
}
