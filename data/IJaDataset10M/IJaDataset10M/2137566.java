package uk.org.ogsadai.client.toolkit.activities.transform;

import java.io.IOException;
import java.io.Reader;
import uk.org.ogsadai.activity.ActivityName;
import uk.org.ogsadai.client.toolkit.Activity;
import uk.org.ogsadai.client.toolkit.ActivityOutput;
import uk.org.ogsadai.client.toolkit.DataValueReader;
import uk.org.ogsadai.client.toolkit.SingleActivityOutput;
import uk.org.ogsadai.client.toolkit.Utilities;
import uk.org.ogsadai.client.toolkit.activity.ActivityInput;
import uk.org.ogsadai.client.toolkit.activity.BaseActivity;
import uk.org.ogsadai.client.toolkit.activity.SimpleActivityInput;
import uk.org.ogsadai.client.toolkit.activity.SimpleActivityOutput;
import uk.org.ogsadai.client.toolkit.exception.ActivityIOIllegalStateException;
import uk.org.ogsadai.client.toolkit.exception.DataSourceUsageException;
import uk.org.ogsadai.client.toolkit.exception.DataStreamErrorException;
import uk.org.ogsadai.client.toolkit.exception.UnexpectedDataValueException;
import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.data.ListBegin;
import uk.org.ogsadai.data.ListEnd;

/**
 * Client toolkit proxy for
 * <code>uk.org.ogsadai.activity.transform.XSLTransformActivity</code>. A
 * converter activity to perform XSL transformations.
 * <p>
 * Activity inputs:
 * </p>
 * <ul>
 * <li> <code>xml</code>. Type: One of OGSA-DAI list of <code>char[]</code>,
 * OGSA-DAI list of <code>byte[]</code>, {@link java.sql.Clob}, or
 * {@link java.sql.Blob}. The data in XML format to be converted. This is a
 * mandatory input. </li>
 * 
 * <li> <code>xslt</code>. Type: One of OGSA-DAI list of <code>char[]</code>,
 * OGSA-DAI list of <code>byte[]</code>, {@link java.sql.Clob}, or
 * {@link java.sql.Blob}. The XSLT document to be used for the conversion. This
 * is a mandatory input.</li>
 * </ul>
 * <p>
 * Activity outputs:
 * </p>
 * <ul>
 * <li> <code>result</code>. Type: OGSA-DAI list of <code>char[]</code>.
 * The converted document. </li>
 * </ul>
 * <p>
 * Expected name on server:
 * </p>
 * <ul>
 * <li> <code>uk.org.ogsadai.XSLTransform</code> </li>
 * </ul>
 * <p>
 * Activity input/output ordering:
 * </p>
 * <ul>
 * <li> The XSLT document is read in full at the beginning of each iteration and
 * a validation check is performed. Then the XML document is read and the
 * transformation performed. </li>
 * <li> A deadlock could occur if the XML data input is provided before the XSLT
 * input. </li>
 * </ul>
 * <p>
 * Target data resource: none.
 * </p>
 * <p>
 * Behaviour:
 * </p>
 * <ul>
 * <li> In each iteration, an input XML document is read from the
 * <code>xml</code> input and transformed using the XSL
 * transformation instructions from the <code>xslt</code>.
 * </li>
 * <li> A user exception is raised if any problems occurred during the XSLT
 * processing, for example if the input XSLT document is not valid or if the
 * input document to transform is not an XML document. </li>
 * <li>Note that this implementation stores the entire transformed result in
 * memory, so the size of transformation result is restricted by available
 * memory. There are other streaming approaches but this is not implemented
 * here. </li>
 * </ul>
 * 
 * @author The OGSA-DAI Project Team.
 */
public class XSLTransform extends BaseActivity implements Activity {

    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2007.";

    /** Default activity name. */
    private static final ActivityName DEFAULT_ACTIVITY_NAME = new ActivityName("uk.org.ogsadai.XSLTransform");

    /** Logger. */
    private static final DAILogger LOG = DAILogger.getLogger(XSLTransform.class);

    /**
     * Activity input name(<code>xslt</code>) - The XSLT document itself.
     * (One of OGSA-DAI list of <code>char[]</code>, OGSA-DAI list of
     * <code>byte[]</code>, {@link java.sql.Clob}, or {@link java.sql.Blob}).
     */
    public static final String INPUT_XSLT = "xslt";

    /** XSLT Input. */
    private ActivityInput mXSLTInput;

    /**
     * Activity input name(<code>xml</code>) - Data to perform XSL transformation onto.
     * (One of OGSA-DAI list of <code>char[]</code>, OGSA-DAI list of
     * <code>byte[]</code>, {@link java.sql.Clob}, or {@link java.sql.Blob}).
     */
    public static final String INPUT_XML = "xml";

    /** XML input. */
    private ActivityInput mXMLInput;

    /** 
     * Activity output name(<code>result</code>) - 
     * The transformed document
     * (OGSA-DAI list of <code>cahr[]</code>).
     */
    public static final String DATA_OUTPUT = "result";

    /** Data output. */
    private ActivityOutput mDataOutput;

    /** Block size for sending the blocks. */
    public static final int BLOCK_SIZE = 2048;

    /**
     * Constructor.
     */
    public XSLTransform() {
        super(DEFAULT_ACTIVITY_NAME);
        mXSLTInput = new SimpleActivityInput(INPUT_XSLT);
        mXMLInput = new SimpleActivityInput(INPUT_XML);
        mDataOutput = new SimpleActivityOutput(DATA_OUTPUT);
    }

    /**
     * Adds an XSL input document provided by a <code>java.io.Reader</code>.
     * 
     * @param reader
     *      Reader with xsl transform definition.
     * @throws IOException
     *      When there is a problem reading data.
     */
    public void addXSLT(final Reader reader) throws IOException {
        mXSLTInput.add(ListBegin.VALUE);
        Utilities.readCharData(mXSLTInput, reader, BLOCK_SIZE);
        mXSLTInput.add(ListEnd.VALUE);
    }

    /**
     * Adds an XML document provided by a <code>java.io.Reader</code>.
     * 
     * @param reader
     *      Reader with XML data.
     * @throws IOException
     *      When there is a problem reading data.
     */
    public void addXML(final Reader reader) throws IOException {
        mXMLInput.add(ListBegin.VALUE);
        Utilities.readCharData(mXMLInput, reader, BLOCK_SIZE);
        mXMLInput.add(ListEnd.VALUE);
    }

    /**
     * Connects the XSLT input to the given output.
     * 
     * @param output output to connect to.
     */
    public void connectXSLTInput(final SingleActivityOutput output) {
        mXSLTInput.connect(output);
    }

    /**
     * Connects the XML input to the given output.
     * 
     * @param output output to connect to.
     */
    public void connectXMLInput(final SingleActivityOutput output) {
        mXMLInput.connect(output);
    }

    /**
     * Gets the data output. This output will contain the data returned
     * from the transformation.
     * 
     * @return data output.
     */
    public SingleActivityOutput getResultOutput() {
        return mDataOutput.getSingleActivityOutputs()[0];
    }

    /**
     * Indicates whether there is another result available.
     * 
     * @return <code>true</code> if there is another result, <code>false</code>
     *         otherwise.
     *         
     * @throws DataStreamErrorException 
     *             if there is an error on the data stream.
     * @throws UnexpectedDataValueException
     *             if there is an unexpected data value on the data stream.
     * @throws DataSourceUsageException
     *             if there is an error reading from a data source.
     */
    public boolean hasNextResult() throws DataStreamErrorException, UnexpectedDataValueException, DataSourceUsageException {
        return mDataOutput.getDataValueIterator().hasNext();
    }

    /**
     * Gets the reader that gives access to the next char array.
     * 
     * @return result set giving access to the next result.
     */
    public Reader nextResult() {
        return new DataValueReader(mDataOutput.getDataValueIterator(), 1);
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
        return new ActivityInput[] { mXSLTInput, mXMLInput };
    }

    /**
     * {@inheritDoc}
     */
    protected ActivityOutput[] getOutputs() {
        return new ActivityOutput[] { mDataOutput };
    }
}
