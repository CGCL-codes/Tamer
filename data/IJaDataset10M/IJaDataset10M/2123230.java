package uchicago.src.sim.engine;

/**
 * Interface used for asynchronous agents.
 * 
 * @see uchicago.src.sim.engine.AsynchSchedule
 * @see uchicago.src.sim.engine.AsynchAgent
 *
 * @author Jerry Vos
 * @version $Revision: 1.3 $ $Date: 2005/08/12 16:13:29 $
 * @since 3.0
 */
public interface IAsynchAgent {

    /**
	 * @return the next time the agent is available to perform an action
	 */
    public abstract double getNextAvailableTime();

    /**
	 * @return 
	 */
    public abstract double findNextTaskCompletionTime();
}
