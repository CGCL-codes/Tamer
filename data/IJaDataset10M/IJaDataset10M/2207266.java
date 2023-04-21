package net.sf.parser4j.parser.entity.parsenode.status.impl;

import net.sf.parser4j.parser.entity.parsenode.status.IParseNodeStatus;

/**
 * implementation of {@link IParseNodeStatus }
 * 
 * @author luc peuvrier
 * 
 */
public abstract class AbstractParseNodeStatus implements IParseNodeStatus {

    /** true if parse node is in error, false by default */
    private final boolean inError;

    public AbstractParseNodeStatus() {
        super();
        inError = false;
    }

    public AbstractParseNodeStatus(final boolean inError) {
        super();
        this.inError = inError;
    }

    /**
	 * @see net.sf.parser4j.parser.entity.parsenode.status.IParseNodeStatus#isInError()
	 */
    @Override
    public boolean isInError() {
        return inError;
    }
}
