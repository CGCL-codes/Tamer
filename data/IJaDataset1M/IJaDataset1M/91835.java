package net.sf.etl.parsers.internal.term_parser.states;

import net.sf.etl.parsers.PhraseParser;
import net.sf.etl.parsers.PropertyName;
import net.sf.etl.parsers.Terms;

/**
 * This node reports property
 * 
 * @author const
 */
public class ReportProperty extends ReportState {

    /** a context */
    private final PropertyName name;

    /** true if object should be started at mark */
    private final boolean atMark;

    /**
	 * A constructor from fields
	 * 
	 * @param nextState
	 *            a next state
	 * @param kind
	 *            a kind
	 * @param name
	 *            a name of property
	 * @param atMark
	 *            true if object should be started at mark
	 */
    public ReportProperty(State nextState, Terms kind, PropertyName name, boolean atMark) {
        super(nextState, kind);
        this.name = name;
        this.atMark = atMark;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public boolean run(PhraseParser parser, StateMachinePeer peer, Activation activation) {
        activation.changeState(getNextState());
        return peer.reportProperty(kind, name, atMark);
    }
}
