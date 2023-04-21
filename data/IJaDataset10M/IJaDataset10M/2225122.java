package exfex.common.displaysystem;

/** Basic interface for Event dispatcher.
 * 
 * TODO: this is just scratch.
 */
public interface IEventDispatcher {

    /** Starts to process events. 
	 * 
	 * @return Return code of dispatcher. 
	 */
    public int startLoop();

    /** Sends event to the dispatcher.
	 * @param event Event to send.
	 */
    public void sendEvent(IEvent event);
}
