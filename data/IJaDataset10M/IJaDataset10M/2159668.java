package agent;

import jason.asSemantics.Agent;
import jason.asSyntax.Literal;

/**
 * is used to filter percepts of the agent.
 * @author lacay
 *
 */
public interface PerceptFilter {

    /**
	 * Method recieves the Agent class and the associated percepts to that agent in the 
	 * current execution cycle. 
	 * @param percepts
	 * @param agt
	 * @return return the percepts that is going to be used in the jason cycle. Filtered percepts 
	 * needs to be deleted if it is going to interferer with the Jason cycle. It is also a good practice
	 * for improving the efficiency of the execution cycle
	 */
    java.util.List<Literal> filterPercepts(java.util.List<Literal> percepts, Agent agt);
}
