package ui;

/**
 * Interface for observers interested in mouse events from the GobanPanel.
 * @author TKington
 *
 */
public interface IGobanListener {

    /**
	 * The mouse was clicked on the GobanPanel.
	 * @param x the x location in board coordinates
	 * @param y the y location in board coordinates
	 * @param modifiers the modifiers
	 */
    void mouseClicked(int x, int y, int modifiers);

    /**
	 * The mouse wheel was moved while on the GobanPanel.
	 * @param wheelRotation the rotation distance
	 */
    void mouseWheelMoved(int wheelRotation);
}
