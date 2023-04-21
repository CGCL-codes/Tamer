package uk.org.ogsadai.activity.pipeline;

import uk.org.ogsadai.config.KeyValueProperties;
import uk.org.ogsadai.config.SimpleKeyValueProperties;

/**
 * A simple literal input constructed from a literal value composition.
 * 
 * @author The OGSA-DAI Project Team
 */
public abstract class ActivityInputBase implements ActivityInput {

    /** Copyright statement. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2002 - 2008.";

    /** Parent activity descriptor that consumes the input data. */
    private ActivityDescriptor mParent;

    /** activity input name. */
    private String mInputName;

    /** Attributes associated with the input. */
    private final KeyValueProperties mAttributes;

    /**
     * Creates a new activity input.
     * 
     * @param inputName
     *            name of the input
     */
    public ActivityInputBase(final String inputName) {
        mInputName = inputName;
        mAttributes = new SimpleKeyValueProperties();
    }

    /**
     * {@inheritDoc}
     */
    public String getInputName() {
        return mInputName;
    }

    /**
     * {@inheritDoc}
     */
    public void moveInput(final ActivityDescriptor newParent, final String newInputName) {
        mParent.removeInput(this);
        newParent.addInput(this);
        mParent = newParent;
        mInputName = newInputName;
    }

    /**
     * {@inheritDoc}
     */
    public ActivityDescriptor getTargetActivity() {
        return mParent;
    }

    /**
     * {@inheritDoc}
     */
    public void setTargetActivity(ActivityDescriptor activity) {
        mParent = activity;
    }

    /**
     * {@inheritDoc}
     */
    public boolean hasTarget() {
        return mParent != null;
    }

    /**
     * {@inheritDoc}
     */
    public KeyValueProperties getAttributes() {
        return mAttributes;
    }
}
