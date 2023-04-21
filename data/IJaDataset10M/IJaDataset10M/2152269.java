package polr.client.ui.base;

import polr.client.ui.base.event.MouseAdapter;
import polr.client.ui.base.event.MouseEvent;
import polr.client.ui.base.event.MouseListener;

/**
 * A utility class for creating draggable components.
 * 
 * @author davedes
 */
public class DraggableContainer extends Container {

    protected MouseListener dragListener;

    /** Creates a new instance of DraggableContainer */
    public DraggableContainer() {
        dragListener = new DragListener(this);
        addMouseListener(dragListener);
    }

    public MouseListener getDragListener() {
        return dragListener;
    }

    public static MouseListener createDragListener(Component comp) {
        return new DragListener(comp);
    }

    protected static class DragListener extends MouseAdapter {

        private float lastX, lastY;

        private Component comp;

        public DragListener(Component comp) {
            this.comp = comp;
        }

        public void mousePressed(MouseEvent e) {
            lastX = e.getAbsoluteX();
            lastY = e.getAbsoluteY();
        }

        public void mouseDragged(MouseEvent e) {
            float abx = e.getAbsoluteX();
            float aby = e.getAbsoluteY();
            float x = comp.getX() + abx - lastX;
            float y = comp.getY() + aby - lastY;
            comp.setLocation(x, y);
            lastX = abx;
            lastY = aby;
        }
    }
}
