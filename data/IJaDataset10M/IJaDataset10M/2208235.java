package net.sf.jtreefield;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JButton;

/**
 * JButton showing an arrow.
 * @author Ignacio Merani
 */
public class ArrowButton extends JButton {

    private int direction;

    /**
     * Creates a new instance of ArrowButton
     * @param direction direction of the arrow
     */
    public ArrowButton(int direction) {
        super();
        this.direction = direction;
    }

    /**
     * Invoked by Swing to draw components.
     * Applications should not invoke <code>paint</code> directly,
     * but should instead use the <code>repaint</code> method to
     * schedule the component for redrawing.
     * <p>
     * This method actually delegates the work of painting to three
     * protected methods: <code>paintComponent</code>,
     * <code>paintBorder</code>,
     * and <code>paintChildren</code>.  They're called in the order
     * listed to ensure that children appear on top of component itself.
     * Generally speaking, the component and its children should not
     * paint in the insets area allocated to the border. Subclasses can
     * just override this method, as always.  A subclass that just
     * wants to specialize the UI (look and feel) delegate's
     * <code>paint</code> method should just override
     * <code>paintComponent</code>.
     *
     * @param g  the <code>Graphics</code> context in which to paint
     */
    public void paint(Graphics g) {
        super.paint(g);
        int mid, i, j, x, y;
        int w, h, size;
        w = getSize().width;
        h = getSize().height;
        size = Math.min((h - 4) / 3, (w - 4) / 3);
        size = Math.max(size, 2);
        x = (w - size) / 2;
        y = (h - size) / 2;
        j = 0;
        size = Math.max(size, 2);
        mid = (size / 2) - 1;
        g.translate(x, y);
        switch(direction) {
            case NORTH:
                for (i = 0; i < size; i++) {
                    g.drawLine(mid - i, i, mid + i, i);
                }
                if (!isEnabled()) {
                    g.setColor(getForeground());
                    g.drawLine(mid - i + 2, i, mid + i, i);
                }
                break;
            case SOUTH:
                if (!isEnabled()) {
                    g.translate(1, 1);
                    g.setColor(getForeground());
                    for (i = size - 1; i >= 0; i--) {
                        g.drawLine(mid - i, j, mid + i, j);
                        j++;
                    }
                    g.translate(-1, -1);
                    g.setColor(getForeground());
                }
                j = 0;
                for (i = size - 1; i >= 0; i--) {
                    g.drawLine(mid - i, j, mid + i, j);
                    j++;
                }
                break;
            case WEST:
                for (i = 0; i < size; i++) {
                    g.drawLine(i, mid - i, i, mid + i);
                }
                if (!isEnabled()) {
                    g.setColor(getForeground());
                    g.drawLine(i, mid - i + 2, i, mid + i);
                }
                break;
            case EAST:
                if (!isEnabled()) {
                    g.translate(1, 1);
                    g.setColor(getForeground());
                    for (i = size - 1; i >= 0; i--) {
                        g.drawLine(j, mid - i, j, mid + i);
                        j++;
                    }
                    g.translate(-1, -1);
                    g.setColor(getForeground());
                }
                j = 0;
                for (i = size - 1; i >= 0; i--) {
                    g.drawLine(j, mid - i, j, mid + i);
                    j++;
                }
                break;
        }
    }

    /**
     * If the <code>preferredSize</code> has been set to a
     * non-<code>null</code> value just returns it.
     * If the UI delegate's <code>getPreferredSize</code>
     * method returns a non <code>null</code> value then return that;
     * otherwise defer to the component's layout manager.
     *
     * @return the value of the <code>preferredSize</code> property
     */
    public Dimension getPreferredSize() {
        return new Dimension(24, 24);
    }
}
