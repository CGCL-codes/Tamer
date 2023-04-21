package net.sf.planetbaron.swing.translucent;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

/**
 * 
 * Use of this component requires installation of the
 * {@link TranslucentRepaintManager} repaint manager.
 * 
 * @author Tim Halloran
 * 
 * @see TranslucentRepaintManager
 */
public final class DraggableTranslucentPanel extends TranslucentPanel {

    public DraggableTranslucentPanel(String title) {
        Border b = new LineBorder(Color.black, 1, true);
        setBorder(BorderFactory.createTitledBorder(b, title));
        addMouseListener(f_mouse);
        addMouseMotionListener(f_mouse);
    }

    /**
	 * This event handler allows the panel to be dragged around on the screen.
	 */
    private final MouseAdapter f_mouse = new MouseAdapter() {

        boolean f_inWindowMove = false;

        int f_clickX, f_clickY;

        @Override
        public void mouseDragged(MouseEvent e) {
            if (f_inWindowMove) {
                final int x = e.getX() + getX() - f_clickX;
                final int y = e.getY() + getY() - f_clickY;
                setLocation(x, y);
            } else {
                f_inWindowMove = true;
                f_clickX = e.getX();
                f_clickY = e.getY();
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            f_inWindowMove = false;
        }
    };

    private static final long serialVersionUID = 1546585435668242548L;
}
