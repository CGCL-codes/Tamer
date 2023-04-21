package examples.carcontrol;

public interface CarGUI {

    /**
     * The path in the {@link de.fhg.igd.semoa.server.Environment
     * environment} where the {@link CarGUI} will be published.<p>  
     *
     * Value: <code> /local/public/car_gui </code>
     */
    public static final String PUBLISH_KEY = "/local/public/car_gui";

    /**
     * Sets the door status of the gui repaint it
     * 
     * @param status the current status representing door position
     */
    public void setDoorStatus(byte status);

    /**
	 * Sets the lock status of the gui and repaint it
	 *
	 * @param status the current status of the centralised door locking
	 */
    public void setLockStatus(boolean isOpen);

    /**
	 * Sets the window status of the gui and repaint it
	 * 
	 * @param status the current status representing window position
	 */
    public void setWindowStatus(byte status);
}
