package org.peaseplate.scriptengine;

public abstract class AbstractInstructionDefinition implements InstructionDefinition {

    public AbstractInstructionDefinition() {
        super();
    }

    /**
	 * Returns false, as this is the common case {@inheritDoc}
	 */
    @Override
    public boolean isBlockHead() {
        return false;
    }

    /**
	 * Returns false, as this is the common case {@inheritDoc}
	 */
    @Override
    public boolean isBlockTerminator() {
        return false;
    }

    /**
	 * Returns false, as this is the common case {@inheritDoc}
	 */
    @Override
    public boolean isAlternatable() {
        return false;
    }

    /**
	 * Returns false, as this is the common case {@inheritDoc}
	 */
    @Override
    public boolean isAlternation() {
        return false;
    }
}
