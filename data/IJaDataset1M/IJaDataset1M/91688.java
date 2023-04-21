package net.sf.etl.parsers.internal.term_parser.fsm;

/**
 * A choice state allows choosing between serveral alternatives. There always is
 * a fallback alternative. This alternative is used if all other explicitly
 * specified choices fail.
 * 
 * @author const
 */
public abstract class ChoiceState extends State {

    /**
	 * This is is special transition that should be used if no options specified
	 * explicitly works. A fallback choice has lower priority than explicit
	 * choices.
	 */
    private State fallbackState;

    /**
	 * A constructor
	 * 
	 * @param fallbackState
	 *            fallback state
	 */
    public ChoiceState(State fallbackState) {
        super();
        this.fallbackState = fallbackState;
    }

    /**
	 * @return Returns the fallbackState.
	 */
    public State getFallbackState() {
        return fallbackState;
    }

    /**
	 * @param fallbackState
	 *            The fallbackState to set.
	 */
    public void setFallbackState(State fallbackState) {
        this.fallbackState = fallbackState;
    }
}
