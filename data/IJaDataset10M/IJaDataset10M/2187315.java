package net.sf.etl.parsers.internal.term_parser.states;

import net.sf.etl.parsers.PhraseParser;
import net.sf.etl.parsers.Terms;

/**
 * This class reports simple events that do no carry any associated information.
 * 
 * @author const
 */
public class ReportTermState extends ReportState {

    /**
	 * A constructor
	 * 
	 * @param nextState
	 *            a next state
	 * @param kind
	 *            a kind
	 */
    public ReportTermState(State nextState, Terms kind) {
        super(nextState, kind);
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public boolean run(PhraseParser parser, StateMachinePeer peer, Activation activation) {
        activation.changeState(getNextState());
        return peer.reportTermScope(kind);
    }
}
