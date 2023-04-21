package org.qtitools.qti.node.outcome.processing;

import org.qtitools.qti.exception.QTIProcessingInterrupt;
import org.qtitools.qti.node.AbstractObject;
import org.qtitools.qti.node.XmlObject;

/**
 * Abstract parent of all outcome rules.
 * 
 * @author Jiri Kajaba
 */
public abstract class OutcomeRule extends AbstractObject {

    private static final long serialVersionUID = 1L;

    /** Display name of this class. */
    public static final String DISPLAY_NAME = "outcomeRule";

    /**
	 * Creates rule.
	 *
	 * @param parent parent of this rule
	 */
    public OutcomeRule(XmlObject parent) {
        super(parent);
    }

    /**
	 * Evaluates this rule and all its children.
	 * @throws QTIProcessingInterrupt 
	 */
    public abstract void evaluate() throws QTIProcessingInterrupt;
}
