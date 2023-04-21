package core;

/**
 * <p>
 * Title: EventHandle interface
 * </p>
 * <p>
 * Description: This interface define the event handler method
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Zealot
 * @version 0.1
 */
public interface EventHandle {

    /**
     * Key press
     * 
     * @param event
     *            Event instance
     */
    public void keyPress(Event event);

    /**
     * Key release
     * 
     * @param event
     *            Event instance
     */
    public void keyRelease(Event event);

    /**
     * Key repeat
     * 
     * @param event
     *            Event instance
     */
    public void keyRepeat(Event event);
}
