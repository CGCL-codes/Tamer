package uk.org.ogsadai.client.toolkit.activities.delivery;

import java.util.NoSuchElementException;
import uk.org.ogsadai.activity.ActivityName;
import uk.org.ogsadai.client.toolkit.ActivityOutput;
import uk.org.ogsadai.client.toolkit.SingleActivityOutput;
import uk.org.ogsadai.client.toolkit.activity.ActivityInput;
import uk.org.ogsadai.client.toolkit.activity.BaseActivity;
import uk.org.ogsadai.client.toolkit.activity.SimpleActivityInput;
import uk.org.ogsadai.client.toolkit.activity.SimpleActivityOutput;
import uk.org.ogsadai.client.toolkit.exception.ActivityIOIllegalStateException;
import uk.org.ogsadai.client.toolkit.exception.DataSourceUsageException;
import uk.org.ogsadai.client.toolkit.exception.DataStreamErrorException;
import uk.org.ogsadai.client.toolkit.exception.UnexpectedDataValueException;
import uk.org.ogsadai.data.DataValue;
import uk.org.ogsadai.data.DataValueGetObjectVisitor;
import uk.org.ogsadai.data.StringData;

/**
 * Client toolkit proxy for
 * <code>uk.org.ogsadai.activity.delivery.ObtainFromSessionActivity</code>.
 * Obtains any data that is stored in the associated session resource based
 * on the provided key.
 * <p>
 * Activity inputs:
 * </p>
 * <ul>
 * <li> <code>attribute</code>. Type: {@link java.lang.String}.
 * The key whose value will be retrieved from the associated session resource.  
 * </li>
 * </ul>
 * <p>
 * Activity outputs:
 * </p>
 * <ul>
 * <li><code>data</code>. Type: {@link java.lang.Object}.
 * The data associated with the provided key that is stored in the 
 * associated session resource.
 * </li>
 * </ul>
 * <p>
 * Expected name on server:
 * </p>
 * <ul>
 * <li><code>uk.org.ogsadai.ObtainFromSession</code>
 * </li>
 * </ul>
 * <p>
 * Activity input/output ordering: none
 * </p>
 * <p>
 * Target data resource: none
 * </p>
 * <p>
 * Behaviour:
 * </p>
 * <ul>
 * <li>This activity obtains the value that is associated to the key provided 
 * from the <code>attribute</code> input and that is stored to the associate 
 * session resource.
 * </li>
 * <li>If the key provided doesn't exist a 
 * <code>SessionKeyUnknownException</code> exception will be thrown.
 * </li>
 * </ul>
 *
 * @author The OGSA-DAI Project Team
 */
public class ObtainFromSession extends BaseActivity {

    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2007-2008.";

    /** Default activity name. */
    private static final ActivityName DEFAULT_ACTIVITY_NAME = new ActivityName("uk.org.ogsadai.ObtainFromSession");

    /**
     * Activity input name (<code>attribute</code>) - key
     * whose value will be retrieved.
     * (<code>String</code>).
     */
    public static final String KEY = "attribute";

    /**
     * Activity output name (<code>data</code>) - Obtained data from
     * the associated session resource(<code>Object</code>).
     */
    public static final String VALUE = "data";

    /** Atrribute input. */
    private SimpleActivityInput mAttributeInput;

    /** Data output. */
    private SimpleActivityOutput mDataOutput;

    /**
     * Constructor.
     */
    public ObtainFromSession() {
        super(DEFAULT_ACTIVITY_NAME);
        mAttributeInput = new SimpleActivityInput(KEY);
        mDataOutput = new SimpleActivityOutput(VALUE);
    }

    /**
     * Adds an attribute to use as the key when reading from the session.
     * 
     * @param attribute
     *            attribute used to store value in the session.
     */
    public void addAttribute(final String attribute) {
        mAttributeInput.add(new StringData(attribute));
    }

    /**
     * Connects the attribute input to the given output.
     * 
     * @param output
     *            output to connect to.
     */
    public void connectAttributeInput(final SingleActivityOutput output) {
        mAttributeInput.connect(output);
    }

    /**
     * Gets the data output.
     * 
     * @return data output.
     */
    public SingleActivityOutput getDataOutput() {
        return mDataOutput.getSingleActivityOutputs()[0];
    }

    /**
     * Determine if the data output has any more data.
     * 
     * @return <code>true</code> if the data output has more data,
     *         <code>false</code> otherwise.
     * 
     * @throws DataStreamErrorException
     *             if there is an error on the data stream.
     * @throws UnexpectedDataValueException
     *             if there is an unexpected data value on the data stream.
     * @throws DataSourceUsageException
     *             if there is an error reading from a data source.
     */
    public boolean hasNextData() throws DataStreamErrorException, UnexpectedDataValueException, DataSourceUsageException {
        return mDataOutput.getDataValueIterator().hasNext();
    }

    /**
     * Reads the next object from the data value output.  The object will be
     * one of:
     * <ul>
     *   <li><code>byte[]</code></li>
     *   <li><code>char[]</code></li>
     *   <li><code>String</code></li>
     *   <li><code>Integer</code></li>
     *   <li><code>Long</code></li>
     *   <li><code>Float</code></li>
     *   <li><code>Double</code></li>
     *   <li><code>Boolean</code></li>
     * </ul>
     * If the object retrieved is not one of these objects then 
     * <code>null</code> will be returned.
     * 
     * @return the next data object.
     * 
     * @throws DataStreamErrorException 
     *             if there is an error on the data stream.
     * @throws UnexpectedDataValueException
     *             if there is an unexpected data value on the data stream.
     * @throws DataSourceUsageException
     *             if there is an error reading from a data source.
     * @throws NoSuchElementException if the is no more data.
     */
    public Object nextData() throws DataStreamErrorException, UnexpectedDataValueException, DataSourceUsageException {
        DataValue dataValue = mDataOutput.getDataValueIterator().next();
        DataValueGetObjectVisitor visitor = new DataValueGetObjectVisitor();
        dataValue.accept(visitor);
        return visitor.getObject();
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
        return new ActivityInput[] { mAttributeInput };
    }

    /**
     * {@inheritDoc}
     */
    protected ActivityOutput[] getOutputs() {
        return new ActivityOutput[] { mDataOutput };
    }
}
