package GGCApplet;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.MultipleGradientPaint.CycleMethod;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import javax.swing.JButton;
import javax.swing.SwingConstants;

/**
 * This class is to make custom JButton graphics that will be uniform in the whole application.
 * The button has three rounded rectangles, the outline is one color, the inner rectangle is a
 * gradient of two colors, and the inner most rectangle is a gradient of two colors.
 * @author Marcus Michalske
 *
 */
public class CustomJButton extends JButton implements MouseListener, MouseMotionListener {

    int state = 1;

    int inactive = 0;

    public CustomJButton(String s) {
        super(s);
        setHorizontalAlignment(SwingConstants.CENTER);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        Graphics2D g3 = (Graphics2D) g.create();
        AlphaComposite newComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.75f);
        g3.setComposite(newComposite);
        g2.setPaint(Color.LIGHT_GRAY);
        g2.setStroke(new BasicStroke(1));
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight(), 10, 10);
        g2.setPaint(Color.GRAY);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
        Point2D center = new Point2D.Float(getWidth() / 2, getHeight() / 2);
        float radius = getWidth();
        float[] dist = { 0.0f, 1.0f };
        Color[] colors = { Color.WHITE, new Color(0, 0, 0, 0) };
        RadialGradientPaint p = new RadialGradientPaint(center, radius, dist, colors, CycleMethod.NO_CYCLE);
        g3.setPaint(p);
        g3.fillRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 5, 5);
        if (inactive == 1) {
        }
        if (state == 3) {
            colors[0] = Color.DARK_GRAY;
            colors[1] = Color.WHITE;
            p = new RadialGradientPaint(center, radius - 10, dist, colors, CycleMethod.NO_CYCLE);
            g3.setPaint(p);
            g3.fillRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 10, 10);
        }
        g2.dispose();
        g3.dispose();
        super.paintComponent(g);
    }

    public void mouseClicked(MouseEvent e) {
        state = 2;
        repaint();
    }

    public void mousePressed(MouseEvent e) {
        state = 3;
        repaint();
    }

    public void mouseReleased(MouseEvent e) {
        state = 2;
        repaint();
    }

    public void mouseEntered(MouseEvent e) {
        state = 2;
        repaint();
    }

    public void mouseExited(MouseEvent e) {
        state = 1;
        repaint();
    }

    public void mouseDragged(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
    }
}
