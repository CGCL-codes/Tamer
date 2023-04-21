package net.sf.etl.parsers.internal.term_parser.states;

/**
 * States that extend this class might affect state of phrase parser. The states
 * cannot be reordered with any other state of the grammar including phrase
 * parser states itself.
 * 
 * @author const
 */
public abstract class PhraseParserState extends DeterministicState {

    /**
	 * A constructor from fields
	 * 
	 * @param nextState
	 *            a next state.
	 */
    public PhraseParserState(State nextState) {
        super(nextState);
    }
}
