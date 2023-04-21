package mhhc.htmlcomponents;

/**
 * Interface to support the Observer pattern. 
 * Classes that want to register themselves as event listeners on a component must implement this interface 
 */
public interface EventHandler {

    /**
     * Method called when the event has occoured
     */
    public void handleEvent(ComponentContext ctx);
}
