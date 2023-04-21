package de.javacus.grafmach.tutorial;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.List;
import javax.swing.JFrame;
import de.javacus.grafmach.twoD.Attributable;
import de.javacus.grafmach.twoD.IAGroup;
import de.javacus.grafmach.twoD.IALine;
import de.javacus.grafmach.twoD.IBasicGO;
import de.javacus.grafmach.twoD.ILine;
import de.javacus.grafmach.twoD.IPoint;
import de.javacus.grafmach.twoD.attr.AGroup;
import de.javacus.grafmach.twoD.attr.ALine;
import de.javacus.grafmach.twoD.plain.Line;
import de.javacus.grafmach.twoD.plain.Point;

public class Chap02_3_a_comfortable_screen {

    private static int wWindow = 600;

    private static int hWindow = 400;

    /**
	 * mirrows in a way, that -y gets + y
	 * 
	 * @param line
	 * @param hWindow
	 *            height of the window
	 * @return
	 */
    private static void mirrorY(ILine line, double hWindow) {
        double yBegNew = hWindow - line.getPBeg().getY();
        IPoint pBegNew = new Point(line.getPBeg().getX(), yBegNew);
        double yEndNew = hWindow - line.getPEnd().getY();
        IPoint pEndNew = new Point(line.getPEnd().getX(), yEndNew);
        line.setPBeg(pBegNew);
        line.setPEnd(pEndNew);
    }

    public static void setColorAndMore(Graphics2D g2D, Attributable attributable) {
        double lineWidth = 1.0;
        Color color = Color.BLUE;
        float transparency = 0.5f;
        color = attributable.getColor();
        lineWidth = attributable.getLineWidth();
        transparency = attributable.getTransparency();
        Stroke stroke = new BasicStroke((float) lineWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);
        g2D.setStroke(stroke);
        g2D.setColor(color);
        g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparency));
    }

    /**
	 */
    private static void paint(Graphics2D g2D, IBasicGO gO) {
        if (gO instanceof AGroup) {
            AGroup aGroup = (AGroup) gO;
            setColorAndMore(g2D, aGroup);
        } else if (gO instanceof ALine) {
            ALine aLine = (ALine) gO;
            mirrorY(aLine, hWindow);
            aLine.paint(g2D);
        } else if (gO instanceof Line) {
            Line line = (Line) gO;
            mirrorY(line, hWindow);
            line.paint(g2D);
        } else {
            System.err.println("Error: Cannot recognize a group! => nothing drawn.");
        }
    }

    @SuppressWarnings("serial")
    private static void draw(final IAGroup aGroup) {
        JFrame jFrame = new JFrame() {

            public void paint(Graphics g) {
                Graphics2D g2D = (Graphics2D) g;
                Chap02_3_a_comfortable_screen.paint(g2D, aGroup);
            }
        };
        jFrame.setTitle("drawing a group");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setSize(new Dimension(wWindow, hWindow));
        jFrame.setVisible(true);
    }

    /**
	 * draw a group.
	 * 
	 * @param args
	 *            : not used.
	 */
    public static void main(String[] args) {
        IAGroup aGroup = new AGroup();
        aGroup.setLineWidth(10.0);
        IPoint p1 = new Point(100, 100);
        IPoint p2 = new Point(400, 100);
        IPoint p3 = new Point(400, 300);
        ILine line1 = new Line(p1, p2);
        ILine line2 = new Line(p2, p3);
        IALine aLine2 = new ALine(line2);
        aLine2.setColor(Color.RED);
        aGroup.add(line1);
        aGroup.add(aLine2);
        draw(aGroup);
    }
}
