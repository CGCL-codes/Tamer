package eu.popeye.ui.laf;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

/*******************************************************************************
 * @class PopeyeResizablePopeyeFrame
 * @extends JFrame
 *
 * @author christian.melchiorre, Paolo Gianrossi
 *
 * This class represent the toplevel ResizablePopeyeFrame window for PopeyeUser interface.
 * The original code is by Christian Melchiorre.
 * It was ported to the JavaBeans architecture by Paolo Gianrossi.
 *
 */
public class PopeyeDialog extends JDialog implements Serializable {

    private Image transparent;

    private Image scaledBitmap;

    private PopeyeDialogBackgroundPanel jpl;

    private static Dimension d = Toolkit.getDefaultToolkit().getScreenSize();

    private Robot r;

    private Rectangle rect = new Rectangle(0, 0, d.width, d.height);

    private PopeyeDialogMover FrameMover;

    private PropertyChangeSupport propertySupport;

    public static final String PROP_BITMAP = "bitmap";

    private Image bitmap;

    public PopeyeDialog(java.awt.Frame f, boolean b) {
        super(f, b);
        if (f != null) {
            this.setLocation(f.getX() + f.getWidth() / 3, f.getY() + f.getHeight());
        }
        setUndecorated(true);
        try {
            r = new Robot();
            transparent = r.createScreenCapture(rect);
        } catch (AWTException awe) {
            awe.printStackTrace();
            System.out.println("error reading screen");
            System.exit(0);
        }
        jpl = new PopeyeDialogBackgroundPanel(transparent, scaledBitmap, this);
        jpl.setOpaque(false);
        setContentPane(jpl);
        requestFocus();
        this.FrameMover = new PopeyeDialogMover(this);
        this.addMouseListener(FrameMover);
        this.addMouseMotionListener(FrameMover);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }

    public int getShadowSize() {
        return this.jpl.shadowSize;
    }

    public void setShadowSize(int s) {
        this.jpl.shadowSize = s;
    }

    public void moveBck(Point to) {
        transparent = r.createScreenCapture(new Rectangle(to.x, to.y, this.getWidth(), this.getHeight()));
    }
}

/*******************************************************************************
 *
 * @class ResizablePopeyeFrameBackgroundPanel
 *
 * This class contains the background panel management for irregular frame shape
 * handling through transparency.
 *
 */
class PopeyeDialogBackgroundPanel extends JPanel {

    Image image1, image2;

    Window parent;

    private static final Color popeyeDarkGreen = new Color(0x7bbd42, false);

    private static final Color popeyeMediumGreen = new Color(0x8cce5a, false);

    private static final Color popeyeLightGreen = new Color(0xb8e691, false);

    public int shadowSize = 3;

    public PopeyeDialogBackgroundPanel(Image _image1, Image _image2, Window _parent) {
        this.image1 = _image1;
        this.image2 = _image2;
        this.parent = _parent;
    }

    public void paintComponent(Graphics g) {
        g.drawImage(this.image1, 0, 0, getWidth(), getHeight(), this.parent.getX(), this.parent.getY(), this.parent.getX() + getWidth(), this.parent.getY() + getHeight(), this);
        Graphics2D g2d = (Graphics2D) g;
        Insets vInsets = this.getInsets();
        int w = this.getWidth() - (vInsets.left + vInsets.right);
        int h = this.getHeight() - (vInsets.top + vInsets.bottom);
        int x = vInsets.left;
        int y = vInsets.top;
        int arc = 16;
        Shape vButtonShape = new RoundRectangle2D.Double((double) x, (double) y + 2, (double) w, (double) h - 2, (double) arc, (double) arc);
        Shape vOldClip = g.getClip();
        Color shade = new Color(0x9e303030, true);
        g2d.setColor(shade);
        g2d.fillRoundRect(x + shadowSize, y + shadowSize, w - 4, h - 4, arc, arc);
        GradientPaint vPaint = new GradientPaint(x, y, this.popeyeMediumGreen, x + w / 2, y + h / 2, Color.WHITE);
        g2d.setPaint(vPaint);
        g2d.fillRoundRect(x, y, w - 4, h - 4, arc, arc);
        vPaint = new GradientPaint(x, y, Color.BLACK, x + w, y + h, popeyeDarkGreen);
        g2d.setPaint(vPaint);
        g2d.drawRoundRect(x, y, w - 4, h - 4, arc, arc);
        g2d.setClip(vOldClip);
        g2d.setColor(new Color(0x330000ff, true));
    }

    public void setImg(Image i) {
        this.image1 = i;
        repaint();
    }

    public void setSkin(Image img) {
        this.image2 = img;
        this.repaint();
    }
}

;

/*******************************************************************************
 *
 * @class RszFrameMover
 *
 * Mouse event handler that allows the dragging of the frame by clicking in any
 * point of the window area.
 *
 */
class PopeyeDialogMover extends MouseInputAdapter {

    Point point;

    PopeyeDialog frame;

    /**
     * Constructor.
     */
    public PopeyeDialogMover(PopeyeDialog frame) {
        this.frame = frame;
        this.point = new Point(0, 0);
    }

    /**
     * Mouse Pressed.
     */
    public void mousePressed(MouseEvent e) {
        this.point.x = e.getX();
        this.point.y = e.getY();
    }

    /**
     * Mouse Dragged.
     */
    public void mouseDragged(MouseEvent e) {
        Point location = frame.getLocation();
        Point moveTo = new Point(location.x + e.getX() - this.point.x, location.y + e.getY() - this.point.y);
        frame.setLocation(moveTo);
        frame.validate();
        frame.repaint();
    }

    public void mouseReleased(MouseEvent e) {
        Point location = frame.getLocation();
        Point moveTo = new Point(location.x + e.getX() - this.point.x, location.y + e.getY() - this.point.y);
        frame.setLocation(moveTo);
        frame.invalidate();
        frame.validate();
        frame.repaint();
    }
}
