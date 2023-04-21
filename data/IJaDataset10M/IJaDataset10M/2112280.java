package uk.org.ogsadai.client.toolkit.activities.util;

import uk.org.ogsadai.activity.ActivityName;
import uk.org.ogsadai.client.toolkit.ActivityOutput;
import uk.org.ogsadai.client.toolkit.SingleActivityOutput;
import uk.org.ogsadai.client.toolkit.activity.ActivityInput;
import uk.org.ogsadai.client.toolkit.activity.BaseActivity;
import uk.org.ogsadai.client.toolkit.activity.SimpleActivityOutput;
import uk.org.ogsadai.client.toolkit.exception.ActivityIOIllegalStateException;

/**
 * Client toolkit proxy for
 * <code>uk.org.ogsadai.activity.file.CloneableObjectProducerActivity</code>.
 * The server-side activity outputs an object 
 * for which the implementation of hashCode is the default version provided by 
 * the Object class.  The object is also clonable.
 * <p>
 * Activity inputs: none.
 * </p>
 * <p>
 * Activity outputs:
 * </p>
 * <ul>
 * <li><code>output</code>. Type: <code>uk.org.ogsadai.activity.util.CloneableObject</code>.
 * A new Object for which the implementation of hashCode is the default version provided by 
 * the Object class.  The object is also clonable.
 * </li>
 * </ul>
 * <p>
 * Expected name on server:
 * </p>
 * <ul>
 * <li><code>uk.org.ogsadai.CloneableObjectProducer</code>
 * </li>
 * </ul>
 * <p>
 * Activity input/output  ordering: none.
 * </p>
 * <p>
 * Target data resource: none.
 * </p>
 * <p>
 * Behaviour:
 * </p>
 * <ul>
 * <li>This activity outputs an object 
 * for which the implementation of hashCode is the default version provided by 
 * the Object class.  The object is also clonable.
 * </li>
 * <li>This activity is primarily used for testing purposes.
 * </li>
 * </ul>
 *
 * @author The OGSA-DAI Project Team.
 */
public class CloneableObjectProducer extends BaseActivity {

    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh 2002 - 2007.";

    /** Default activity name. */
    private static final ActivityName DEFAULT_ACTIVITY_NAME = new ActivityName("uk.org.ogsadai.CloneableObjectProducer");

    /** Output */
    private SimpleActivityOutput mOutput;

    /**
     * Constructor.
     */
    public CloneableObjectProducer() {
        super(DEFAULT_ACTIVITY_NAME);
        mOutput = new SimpleActivityOutput("output");
    }

    /**
     * Gets the output.
     * 
     * @return output.
     */
    public SingleActivityOutput getOutput() {
        return mOutput.getSingleActivityOutputs()[0];
    }

    /**
     * {@inheritDoc}
     */
    protected void validateIOState() throws ActivityIOIllegalStateException {
    }

    /**
     * {@inheritDoc}
     */
    protected ActivityInput[] getInputs() {
        return new ActivityInput[] {};
    }

    /**
     * {@inheritDoc}
     */
    protected ActivityOutput[] getOutputs() {
        return new ActivityOutput[] { mOutput };
    }
}
