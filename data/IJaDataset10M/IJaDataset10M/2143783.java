package mdes.slick.sui.event;

/**
 * An interface to receive mouse events.
 *
 * @author davedes
 * @since b.0.2
 */
public interface MouseListener extends Listener {

    /**
     * Notification that the mouse has moved.
     *
     * @param e the event associated with this listener
     */
    public void mouseMoved(MouseEvent e);

    /**
     * Notification that the mouse has been dragged.
     * Dragging the mouse will not call a mouseMoved 
     * event, and will instead call mouseDragged.
     *
     * @param e the event associated with this listener
     */
    public void mouseDragged(MouseEvent e);

    /**
     * Notification that the mouse has been pressed.
     *
     * @param e the event associated with this listener
     */
    public void mousePressed(MouseEvent e);

    /**
     * Notification that the mouse has been released.
     *
     * @param e the event associated with this listener
     */
    public void mouseReleased(MouseEvent e);

    /**
     * Notification that the mouse has entered the bounds
     * of the component.
     *
     * @param e the event associated with this listener
     */
    public void mouseEntered(MouseEvent e);

    /**
     * Notification that the mouse has exited the bounds
     * of the component.
     *
     * @param e the event associated with this listener
     */
    public void mouseExited(MouseEvent e);
}
