package mcapl;

import mcapl.psl.MCAPLFormula;

/**
 * Interface to be implemented by agents in specific APLs.
 * 
 * @author louiseadennis
 *
 */
public interface MCAPLLanguageAgent {

    /**
	 * One reasoning step.  This defines the transition between which
	 * properties should be checked.  In AIL this is one full reasoning
	 * cycle, but this need not be the case.
	 * 
	 * @param flag Flag indicating whether or not this is a model checking run.
	 */
    public void MCAPLreason(int debuglevel);

    /**
	 * Indicates whether the agent should continue reasoning or not.
	 * 
	 * @return whether or not the agent should continue reasoning.
	 */
    public boolean MCAPLcontinueRunning();

    /**
	 * The name of the agent.
	 * 
	 * @return the name of the agent.
	 */
    public String getMCAPLAgName();

    /**
	 * Whether the agent believes a formula phi.  The implementation
	 * of this method defines the semantics of belief in the 
	 * Property Specification Language.
	 * 
	 * @param phi	The formula to be checked for belief.
	 * @return		whether the agent believes the formula.
	 */
    public boolean MCAPLbelieves(MCAPLFormula phi);

    /**
	 * Succeeds if the agent has a goal phi.  The implementation of this
	 * method defines the semantics of goals in the Property Specification
	 * Language.
	 * 
	 * @param phi the goal.
	 * @return whether the agent has the goal.
	 */
    public boolean MCAPLhasGoal(MCAPLFormula phi);

    /**
	 * Succeeds if the agent has an intention phi.  The implementation of this
	 * method defines the semantics of intentions in the Property Specificaiton
	 * Languages.
	 * @param phi the intention.
	 * @return
	 */
    public boolean MCAPLhasIntention(MCAPLFormula phi);

    /**
	 * Succeeds if the agent "wants to sleep" - typically envisaged for when the 
	 * agent has nothing left to do.
	 * @return
	 */
    public boolean MCAPLwantstosleep();

    /**
	 * Tells the agent it is now awake.
	 *
	 */
    public void MCAPLtellawake();
}
