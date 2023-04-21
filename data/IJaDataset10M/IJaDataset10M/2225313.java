package net.sourceforge.olduvai.lrac.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Modified from code taken from Java Forum: 
 * http://www.java-forums.org/awt-swing/5842-how-draw-arrow-mark-using-java-swing.html 
 * 
 * @author peter
 *
 */
public class Arrow extends JPanel implements ChangeListener {

    private static final long serialVersionUID = -7376943939766136639L;

    GeneralPath arrow;

    double theta = 0;

    Dimension prevSize = new Dimension(0, 0);

    int margin = 20;

    public void stateChanged(ChangeEvent e) {
        int value = ((JSlider) e.getSource()).getValue();
        theta = Math.toRadians(value);
        repaint();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (arrow == null || !prevSize.equals(getSize())) {
            arrow = createArrow();
            prevSize = getSize();
        }
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int cx = getWidth() / 2;
        int cy = getHeight() / 2;
        AffineTransform at = AffineTransform.getTranslateInstance(cx, cy);
        at.rotate(theta);
        at.scale(2.0, 2.0);
        Shape shape = at.createTransformedShape(arrow);
        g2.setPaint(Color.gray);
        g2.setStroke(new BasicStroke(5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER, 10f, new float[] { 1f, 1f }, 0f));
        g2.draw(shape);
    }

    private GeneralPath createArrow() {
        int length = (getWidth() / 2) - margin;
        int barb = 15;
        double angle = Math.toRadians(20);
        GeneralPath path = new GeneralPath();
        path.moveTo(-length / 2, 0);
        path.lineTo(length / 2, 0);
        double x = length / 2 - barb * Math.cos(angle);
        double y = barb * Math.sin(angle);
        path.moveTo(length / 2 - 1, 0);
        path.lineTo((float) x, (float) y);
        x = length / 2 - barb * Math.cos(-angle);
        y = barb * Math.sin(-angle);
        path.moveTo(length / 2 - 1, 0);
        path.lineTo((float) x, (float) y);
        return path;
    }

    private JSlider getSlider() {
        JSlider slider = new JSlider(-180, 180, 0);
        slider.addChangeListener(this);
        return slider;
    }

    public static void main(String[] args) {
        Arrow test = new Arrow();
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(test);
        f.add(test.getSlider(), "Last");
        f.setSize(400, 400);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}
