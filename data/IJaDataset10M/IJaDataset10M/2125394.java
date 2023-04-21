package net.sourceforge.aqueduct.state;

import net.sourceforge.aqueduct.InputSource;

/**
 * Represents a terminal state in a state machine. A terminal state does not
 * transition to another state; when it is the current state of a state machine,
 * that state machine's processing is complete.
 * 
 * @author keith
 * 
 * @param <T>
 */
public class TerminalState<T extends InputSource> extends AbstractState<T> {

    public TerminalState(String id) {
        super(id, true);
    }

    /**
	 * Returns the next transition name. For a TerminalState, this method always
	 * returns null.
	 */
    public String nextTransition(T inputSource) {
        return null;
    }
}
