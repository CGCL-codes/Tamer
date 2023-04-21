package net.sf.etl.parsers.internal.term_parser.fsm;

import net.sf.etl.parsers.PhraseParser;

/**
 * In this state the parser is advenced by one token. This state is usually
 * followed by skip ignorable token.
 * 
 * @author const
 * 
 */
public class AdvanceState extends PhraseParserState {

    /**
	 * A constructor from feilds
	 * 
	 * @param nextState
	 *            a next state.
	 */
    public AdvanceState(State nextState) {
        super(nextState);
    }

    /**
	 * @see net.sf.etl.parsers.internal.term_parser.fsm.State#run(net.sf.etl.parsers.PhraseParser,
	 *      net.sf.etl.parsers.internal.term_parser.fsm.StateMachinePeer,
	 *      net.sf.etl.parsers.internal.term_parser.fsm.Activation)
	 */
    @Override
    public boolean run(PhraseParser parser, StateMachinePeer peer, Activation activation) {
        parser.advance();
        activation.changeState(getNextState());
        return false;
    }
}
