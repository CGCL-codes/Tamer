package be.lassi.ui.color;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;
import be.lassi.context.ColorSelection;
import be.lassi.domain.Hsb;

public class ColorWheelView extends JPanel {

    private static final long serialVersionUID = 1L;

    private final ColorWheel colorWheel = new ColorWheel();

    private final ColorSelection model;

    public static final int GAP = 6;

    private final ColorWheelLayout layout;

    /**
	 * The location of the currently selected color in panel
	 * coordinates.
	 */
    private Point point = new Point(0, 0);

    public ColorWheelView(final ColorSelection model) {
        this.model = model;
        setName("colorWheel");
        int max = ColorWheel.MAXIMUM_SIZE + GAP + GAP;
        int pref = max / 3;
        setMaximumSize(new Dimension(max, max));
        setPreferredSize(new Dimension(pref, pref));
        layout = new ColorWheelLayout(this, colorWheel);
        addMouseListener(mouseListener);
        addMouseMotionListener(mouseListener);
        addComponentListener(componentListener);
        model.addPropertyChangeListener(ColorSelection.PROPERTY_BRIGHTNESS, new PropertyChangeListener() {

            public void propertyChange(final PropertyChangeEvent evt) {
                regenerateImage();
                repaint();
            }
        });
        model.addPropertyChangeListener(ColorSelection.PROPERTY_COLOR, new PropertyChangeListener() {

            public void propertyChange(final PropertyChangeEvent evt) {
                regeneratePoint();
                repaint();
            }
        });
        setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
    }

    MouseInputListener mouseListener = new MouseInputAdapter() {

        @Override
        public void mousePressed(MouseEvent e) {
            Point panelPoint = e.getPoint();
            Point wheelPoint = layout.getWheelCoordinates(panelPoint);
            float brightness = model.getBrightness();
            Hsb hsb = colorWheel.getHSB(wheelPoint, brightness);
            model.setHSB(hsb.getHue(), hsb.getSaturation(), hsb.getBrightness());
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            mousePressed(e);
        }
    };

    ComponentListener componentListener = new ComponentAdapter() {

        @Override
        public void componentResized(ComponentEvent e) {
            layout.resized();
            regeneratePoint();
            regenerateImage();
            repaint();
        }
    };

    /**
     * {@inheritDoc}
     * This method is used to restrict the crosshair cursor to be
     * shown only within the color wheel circle.
     */
    @Override
    public boolean contains(final int x, final int y) {
        Point o = layout.getImageOrigin();
        return colorWheel.contains(x - o.x, y - o.y);
    }

    @Override
    public void paint(final Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        paintColorWheelImage(g2);
        paintSelectedColorPoint(g2);
    }

    private void paintColorWheelImage(final Graphics2D g2) {
        int size = colorWheel.getSize();
        Image image = colorWheel.getImage();
        Point o = layout.getImageOrigin();
        g2.drawImage(image, o.x, o.y, o.x + size, o.y + size, 0, 0, size, size, null);
    }

    private void paintSelectedColorPoint(final Graphics2D g2) {
        g2.setStroke(new BasicStroke(1));
        g2.setColor(Color.WHITE);
        g2.draw(circle(point, 3));
        g2.setColor(Color.BLACK);
        g2.draw(circle(point, 4));
    }

    private Shape circle(final Point center, final int radius) {
        int x = center.x - radius;
        int y = center.y - radius;
        int diameter = radius + radius;
        return new Ellipse2D.Float(x, y, diameter, diameter);
    }

    /** Recalculates the (x,y) point used to indicate the selected color. */
    private void regeneratePoint() {
        Hsb hsb = model.getHSB();
        Point wheelPoint = colorWheel.getPoint(hsb);
        point = layout.getPanelCoordinates(wheelPoint);
    }

    private synchronized void regenerateImage() {
        colorWheel.regenerateImage(model.getBrightness());
    }
}
