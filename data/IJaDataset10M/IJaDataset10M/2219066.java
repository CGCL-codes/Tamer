package yospace;

/**
 * Wrapper that is capable of notifying the changes to the listeners
 * @author Jesús Martínez
 *
 */
public interface SensorWrapper {

    /**
	 * Add a listener for the entity changes
	 * @params listener The object to be notified
	 */
    public void addWrapperListener(SensorWrapperListener listener);

    /**
	 * Remove a listener for the entity changes
	 * @params listener The object that will no longer be notified
	 */
    public void removeWrapperListener(SensorWrapperListener listener);
}
