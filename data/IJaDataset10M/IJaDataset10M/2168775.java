package ch.unibas.germa.view.balloon;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Window;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

@SuppressWarnings("serial")
public class BalloonTip extends Window implements ComponentListener, AncestorListener, MouseListener {

    JComponent controller;

    Point anchorPoint;

    JLabel label;

    boolean showing;

    public void setShowing(boolean b) {
        this.showing = b;
        if (timer != null) timer.cancel();
        updatePosition();
        if (showing) {
            timer = new Timer();
            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    setShowing(false);
                    cancel();
                }
            }, 60 * 1000);
        }
    }

    public void setVisible(boolean b) {
        if (!isVisible() && b) {
            try {
                image = new Robot().createScreenCapture(getBounds());
            } catch (AWTException e) {
                image = new BufferedImage(0, 0, BufferedImage.TYPE_INT_RGB);
            }
        }
        super.setVisible(b);
    }

    private void updatePosition() {
        if (showing && controller.isShowing()) {
            Dimension labelSize = label.getPreferredSize();
            this.setSize(labelSize.width + 51, labelSize.height + 51);
            label.setLocation(30, 25);
            label.setSize(labelSize);
            Rectangle visibleRect = controller.getVisibleRect();
            if (visibleRect.contains(getAnchorPoint())) {
                Point p = controller.getLocationOnScreen();
                p.move(p.x + anchorPoint.x, p.y + anchorPoint.y);
                setLocation(p);
                setVisible(true);
                toFront();
            } else {
                setVisible(false);
            }
        } else {
            setVisible(false);
        }
    }

    public BalloonTip(Window owner, JComponent controller) {
        super(owner);
        this.controller = controller;
        this.setLayout(null);
        label = new JLabel();
        label.setFont(new Font("SansSerif", Font.PLAIN, 18));
        label.setOpaque(false);
        this.add(label);
        controller.addComponentListener(this);
        controller.addAncestorListener(this);
        this.addMouseListener(this);
    }

    public static final BalloonTip createBalloon(JComponent component) {
        Component owner = component;
        while (owner != null && !(owner instanceof Window)) {
            owner = owner.getParent();
        }
        if (owner == null) throw new IllegalArgumentException("Component has no Window-Level-Parent");
        return new BalloonTip((Window) owner, component);
    }

    BufferedImage image = null;

    private Timer timer;

    @Override
    public void paint(Graphics g) {
        if (image != null) g.drawImage(image, 0, 0, this);
        g.setColor(Color.BLACK);
        g.drawRoundRect(0, 10, getWidth(), getHeight() - 11, 20, 20);
        g.setColor(new Color(0.9f, 0.9f, 1f, 0.85f));
        g.fillRoundRect(0, 10, getWidth(), getHeight() - 11, 20, 20);
        paintComponents(g);
    }

    public Point getAnchorPoint() {
        return anchorPoint;
    }

    public void setAnchorPoint(Point anchorPoint) {
        this.anchorPoint = anchorPoint;
    }

    public String getText() {
        return label.getText();
    }

    public void setText(String text) {
        label.setText(text);
    }

    public void componentHidden(ComponentEvent e) {
        this.setVisible(false);
    }

    public void componentMoved(ComponentEvent e) {
        this.setVisible(false);
        updatePosition();
    }

    public void componentResized(ComponentEvent e) {
        this.setVisible(false);
        updatePosition();
    }

    public void componentShown(ComponentEvent e) {
        this.setVisible(false);
        updatePosition();
    }

    public void ancestorAdded(AncestorEvent event) {
        this.setVisible(false);
        updatePosition();
    }

    public void ancestorMoved(AncestorEvent event) {
        this.setVisible(false);
        updatePosition();
    }

    public void ancestorRemoved(AncestorEvent event) {
        this.setVisible(false);
        updatePosition();
    }

    public void mouseClicked(MouseEvent e) {
        this.setShowing(false);
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }
}
