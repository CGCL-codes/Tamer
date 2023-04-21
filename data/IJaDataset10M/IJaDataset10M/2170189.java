package net.sf.etl.parsers.internal.term_parser.states;

import java.util.HashMap;
import net.sf.etl.parsers.PhraseParser;
import net.sf.etl.parsers.TokenKey;

/**
 * This choice node selects between state by token key.
 * 
 * @author const
 * 
 */
public class TokenKeyChoice extends ChoiceState {

    /** a map from kind to state */
    private final HashMap<TokenKey, State> keyToState = new HashMap<TokenKey, State>();

    /**
	 * 
	 * @param fallback
	 *            a fallback choice
	 */
    public TokenKeyChoice(State fallback) {
        super(fallback);
    }

    /**
	 * Specify state where parser should go if specific key is detected
	 * 
	 * @param kind
	 *            a key to detect
	 * @param state
	 *            a state where to go
	 */
    public void onKey(TokenKey kind, State state) {
        keyToState.put(kind, state);
    }

    /**
	 * Get state by token key
	 * 
	 * @param key
	 *            a kind to check
	 * @return a state associated with key
	 */
    public State getStateByKind(TokenKey key) {
        return keyToState.get(key);
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public boolean run(PhraseParser parser, StateMachinePeer peer, Activation activation) {
        assert parser.current().hasToken() : "The parser must have " + "phrase token that wraps normal token. Current phrase " + "token is " + parser.current();
        State state = keyToState.get(parser.current().token().key());
        if (state == null) {
            state = getFallbackState();
        }
        activation.changeState(state);
        return false;
    }
}
