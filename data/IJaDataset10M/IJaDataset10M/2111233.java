package net.benojt.test;

import java.awt.event.MouseEvent;
import net.benojt.FractalWindow;
import net.benojt.display.Display;
import net.benojt.tools.*;

public class ColorTest {

    Display d;

    public ColorTest() {
        FractalWindow fw = new FractalWindow();
        fw.setLocationRelativeTo(null);
        fw.setVisible(true);
        RepaintThread rt = new RepaintThread(fw.getFractalPanel());
        rt.setUpdate(true);
        rt.start();
        for (int i = 0; i < 255; i++) for (int j = 0; j < 255; j++) {
            d.setPixel(i, j, i);
        }
        fw.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                System.out.println(Integer.MIN_VALUE + " " + ((255 * 256 * 256 + 255 * 256 + 255) & d.getPixel(e.getX(), e.getY())));
            }
        });
    }

    public static void main(String[] args) {
        new ColorTest();
    }
}
