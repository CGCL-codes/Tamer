package mcapl;

/**
 * Interface for objects that want to listen to changed perceptions
 * in the environment.  Based on Message Listeners in Jason by Jomi
 * Hubner, Rafael Bordini et al.
 * 
 * @author louiseadennis
 */
public interface MCAPLWakeAgentListener {

    /**
	 * What the listener should do if perceptions have changed.
	 * 
	 * @param s
	 */
    public void wakeAgents();

    /**
     * What the listener should do if a particular agent's perceptions have changed - e.g. it has received a message.
     * @param agName
     */
    public void wakeAgent(String agName);
}
