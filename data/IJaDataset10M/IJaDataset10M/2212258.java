package uk.org.ogsadai.client.toolkit.activities.file;

import java.util.ArrayList;
import uk.org.ogsadai.activity.ActivityName;
import uk.org.ogsadai.client.toolkit.ActivityOutput;
import uk.org.ogsadai.client.toolkit.DataIterator;
import uk.org.ogsadai.client.toolkit.DataListIterator;
import uk.org.ogsadai.client.toolkit.ResourceActivity;
import uk.org.ogsadai.client.toolkit.SingleActivityOutput;
import uk.org.ogsadai.client.toolkit.activity.ActivityInput;
import uk.org.ogsadai.client.toolkit.activity.BaseResourceActivity;
import uk.org.ogsadai.client.toolkit.activity.SimpleActivityInput;
import uk.org.ogsadai.client.toolkit.activity.SimpleActivityOutput;
import uk.org.ogsadai.client.toolkit.exception.ActivityIOIllegalStateException;
import uk.org.ogsadai.client.toolkit.exception.DataSourceUsageException;
import uk.org.ogsadai.client.toolkit.exception.DataStreamErrorException;
import uk.org.ogsadai.client.toolkit.exception.UnexpectedDataValueException;
import uk.org.ogsadai.data.BooleanData;
import uk.org.ogsadai.data.StringData;

/**
 * An activity that list the contents of a directory under a file system 
 * resource. 
 * <p>
 * Activity inputs:
 * </p>
 * <ul>
 * <li>
 *   <code>directory</code>. Type: {@link java.lang.String}.  The directory to
 *   be listed.  This is an optional parameter and defaults to <code>"\"</code>.
 * </li>
 * <li>
 *   <code>includePath</code>. Type: {@link java.lang.Boolean}. Specifies if
 *   the full path should be included in the results.  This is an optional
 *   parameter and defaults to <code>Boolean.TRUE</code>.
 * </li>
 * <li>
 *   <code>recursive</code>. Type: {@link java.lang.Boolean}. Specifies if
 *   sub-directories should be recursively listed.  This is an optional
 *   parameter and defaults to <code>Boolean.FALSE</code>.
 * </li>
 * </ul>
 * <p>
 * Activity outputs:
 * </p>
 * <ul>
 * <li>
 * <code>data</code>.  Type: OGSA-DAI list of {@link java.lang.String}.  List
 * containing the filenames of the files within the directory, and possibly
 * also sub-directories.
 * </li>
 * </ul>
 * <p>
 * Expected name of activity on server:
 * </p>
 * <ul>
 * <li>
 * <code>uk.org.ogsadai.ListDirector</code>
 * </li>
 * </ul>
 * <p>
 * Activity input/output ordering:
 * </p>
 * <ul>
 * <li>
 * Values are read from the inputs in the following order: 
 * <code>directory</code>, <code>includePath</code> then <code>recursive</code>.
 * </li>
 * </ul>
 * <p>
 * Activity contracts: none.
 * </p>
 * <p>
 * Target data resource:
 * </p>
 * <ul>
 * <li>
 * This activity must be targeted at a data resource that implements the
 * <code>uk.org.ogsadai.resource.dataresource.jdbc.JDBCConnectionProvider</code>
 * interface.   
 * </li>
 * </ul>
 * <p>
 * Behaviour: 
 * </p>
 * <ul>
 * <li>
 * List the contents of a directory within a file system resource.
 * </li>
 * <li>
 * If no directory name is passed the root directory will be used.
 * </li>
 * <li>
 * By default the full path with in the resource is returned. 
 * Passing <code>Boolean.FALSE</code> for the <code>includePath</code> input
 * will result in the relative path being returned.
 * </li>
 * <li>
 * By  default sub-directories are not recursively listed, however passing 
 * <code>Boolean.TRUE</code> for the <code>recursive</code> parameter causes
 * the sub-directories to be recursed.
 * </li>
 * </ul>
 * 
 * @author The OGSA-DAI Project Team.
 */
public class ListDirectory extends BaseResourceActivity implements ResourceActivity {

    /** Copyright statement. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2007";

    /** Default activity name. */
    private static final ActivityName DEFAULT_ACTIVITY_NAME = new ActivityName("uk.org.ogsadai.ListDirectory");

    /** The directory input name. */
    public static final String DIRECTORY_INPUT = "directory";

    /** The include path input name. */
    public static final String INCLUDE_PATH_INPUT = "includePath";

    /** The recursive input name. */
    public static final String RECURSIVE_INPUT = "recursive";

    /** operation status output name. */
    public static final String OUTPUT_DATA = "data";

    /** Expression input. */
    private ActivityInput mDirectoryInput;

    /** Include path input. */
    private ActivityInput mIncludePathInput;

    /** Recurse directories input. */
    private ActivityInput mRecursiveInput;

    /** Data output. */
    private ActivityOutput mDataOutput;

    /**
     * Constructor.
     */
    public ListDirectory() {
        super(DEFAULT_ACTIVITY_NAME);
        mDirectoryInput = new SimpleActivityInput(DIRECTORY_INPUT, SimpleActivityInput.OPTIONAL);
        mIncludePathInput = new SimpleActivityInput(INCLUDE_PATH_INPUT, SimpleActivityInput.OPTIONAL);
        mRecursiveInput = new SimpleActivityInput(RECURSIVE_INPUT, SimpleActivityInput.OPTIONAL);
        mDataOutput = new SimpleActivityOutput(OUTPUT_DATA);
    }

    /**
     * Adds a new directory whose contents will be listed.
     * 
     * @param directory
     *      The directory to be listed
     */
    public void addDirectory(final String directory) {
        mDirectoryInput.add(new StringData(directory));
    }

    /**
     * heather to include the path in the output.
     * 
     * @param includePath
     *      true if path is to be included, false otherwise
     */
    public void addIncludePath(final boolean includePath) {
        mIncludePathInput.add(new BooleanData(includePath));
    }

    /**
     * Whether to recurse into sub-directories.
     * @param recursive
     *      true for recursive listing, false otherwise
     */
    public void addRecursive(final boolean recursive) {
        mRecursiveInput.add(new BooleanData(recursive));
    }

    /**
     * Connect the DIRECTORY_INPUT input to a supplied output.
     * 
     * @param output
     *      The ActivityOutput to be used
     */
    public void connectDirectoryInput(SingleActivityOutput output) {
        mDirectoryInput.connect(output);
    }

    /**
     * Connect the INCLUDE_PATH_INPUT input to a supplied output.
     * 
     * @param output
     *      The ActivityOutput to be used
     */
    public void connectIncludePathInput(SingleActivityOutput output) {
        mIncludePathInput.connect(output);
    }

    /**
     * Connect the RECURSIVE_INPUT to a supplied output.
     * 
     * @param output
     *      The ActivityOutput to be used
     */
    public void connectRecursiveInput(SingleActivityOutput output) {
        mRecursiveInput.connect(output);
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
        return new ActivityInput[] { mDirectoryInput, mIncludePathInput, mRecursiveInput };
    }

    /**
     * {@inheritDoc}
     */
    protected ActivityOutput[] getOutputs() {
        return new ActivityOutput[] { mDataOutput };
    }

    /**
     * Gets the data output.  This output can be used to connect activities
     * together.
     * 
     * @return the data output 
     */
    public SingleActivityOutput getDataOutput() {
        return mDataOutput.getSingleActivityOutputs()[0];
    }

    /**
     * Returns true if there is another list of data in the returned data set.
     * 
     * @return true if another list was found, false otherwise
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
    * Returns an iterator to the start of the next list in the 
    * returned data set.
    * 
    * @return
    *   returns an iterator to the next set of List data, if 
    *   more data was found. Otherwise, returns null.
    */
    public DataIterator nextData() {
        return new DataListIterator(mDataOutput.getDataValueIterator(), String.class);
    }

    /**
    * Returns the next list of data in the result data as a String array.
    * 
    * @return 
    *   the next set of data as a <code>String[]</code> if more data was found,
    *   <code>null</code> otherwise
    *   
    * @throws DataStreamErrorException 
    *             if there is an error on the data stream.
    * @throws UnexpectedDataValueException
    *             if there is an unexpected data value on the data stream.
    * @throws DataSourceUsageException
    *             if there is an error reading from a data source.
    */
    public String[] nextDataAsArray() throws DataStreamErrorException, UnexpectedDataValueException, DataSourceUsageException {
        if (!hasNextData()) {
            return null;
        }
        DataIterator iter = nextData();
        ArrayList stringList = new ArrayList();
        while (iter.hasNext()) {
            stringList.add(iter.next());
        }
        return (String[]) stringList.toArray(new String[stringList.size()]);
    }
}
