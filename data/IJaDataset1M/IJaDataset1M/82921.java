package de.javacus.grafmach.tutorial;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;
import temp.GrafPanel;
import de.javacus.grafmach.twoD.IPoint;
import de.javacus.grafmach.twoD.plain.Point;

public class Chap02_4_drawing_a_point {

    private static int wWindow = 600;

    private static int hWindow = 400;

    private static void openFrame(final JPanel panel) {
        JFrame jFrame = new JFrame();
        jFrame.setTitle("drawing points");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setLocationByPlatform(true);
        jFrame.setSize(new Dimension(wWindow, hWindow));
        jFrame.add(panel);
        jFrame.setVisible(true);
    }

    /**
	 * defining points is a good start for a grafical symbol
	 * 
	 * @param args
	 *            : not used.
	 */
    public static void main(String[] args) {
        IPoint p1 = new Point(100, 100);
        p1.setGrafNamesVisible(true);
        IPoint p2 = new Point(200, 100);
        p2.setGrafNamesVisible(true);
        IPoint p3 = new Point(150, 150);
        p3.setGrafNamesVisible(true);
        GrafPanel panel = new GrafPanel();
        panel.add(p1);
        panel.add(p2);
        panel.add(p3);
        IPoint p4 = new Point();
        p4.copyDeepFrom(p1);
        p4.setY(p4.getY() + 100);
        panel.add(p4);
        IPoint p5 = new Point();
        p5.copyDeepFrom(p2);
        p5.setY(p5.getY() + 100);
        p5.setGrafName("p5");
        panel.add(p5);
        IPoint p6 = new Point();
        p6.copyDeepFrom(p3);
        p6.setY(p6.getY() + 100);
        p6.setGrafName("p6=" + p6.getGrafName());
        panel.add(p6);
        openFrame(panel);
    }
}
