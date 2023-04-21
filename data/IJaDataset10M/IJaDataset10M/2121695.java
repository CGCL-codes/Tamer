package uk.org.ogsadai.client.toolkit.activities.sql;

import java.util.ArrayList;
import uk.org.ogsadai.activity.ActivityName;
import uk.org.ogsadai.client.toolkit.ActivityOutput;
import uk.org.ogsadai.client.toolkit.DataIterator;
import uk.org.ogsadai.client.toolkit.DataListIterator;
import uk.org.ogsadai.client.toolkit.ResourceActivity;
import uk.org.ogsadai.client.toolkit.SingleActivityOutput;
import uk.org.ogsadai.client.toolkit.activity.ActivityInput;
import uk.org.ogsadai.client.toolkit.activity.BaseResourceActivity;
import uk.org.ogsadai.client.toolkit.activity.SimpleActivityOutput;
import uk.org.ogsadai.client.toolkit.exception.ActivityIOIllegalStateException;
import uk.org.ogsadai.client.toolkit.exception.DataSourceUsageException;
import uk.org.ogsadai.client.toolkit.exception.DataStreamErrorException;
import uk.org.ogsadai.client.toolkit.exception.UnexpectedDataValueException;

/**
 * Client toolkit proxy for
 * <code>uk.org.ogsadai.activity.management.GetAvailableTablesActivity</code>.
 * An activity that retrieves the names of available tables from the database
 * indicated by the resource.
 * <p>
 * Activity inputs:
 * </p>
 * <ul>
 * <li>
 * None
 * </li>
 * </ul>
 * <p>
 * Activity outputs:
 * </p>
 * <ul>
 * <li>
 * <code>data</code>. Type: OGSA-DAI list of {@link java.lang.String}. 
 * The names of the available tables. 
 * </li>
 * </ul>
 * <p>
 * Expected name on server: 
 * </p>
 * <ul>
 * <li>
 * uk.org.ogsadai.GetAvailableTables
 * </li>
 * </ul>
 * <p>
 * Activity input/output ordering: none.
 * </p>
 * <p>
 * Target data resource:
 * </p>
 * <ul>
 * <li>
 * {@link uk.org.ogsadai.resource.dataresource.jdbc.JDBCConnectionProvider}. 
 * </li>
 * </ul>
 * <p>
 * Behaviour: 
 * </p>
 * <ul>
 * <li>
 * The activity queries the target data resource for meta-data in order to
 * retrieve the names of all available tables within the database. 
 * </li>
 * <li>
 * If there are no available tables, an empty OGSA-DAI list will be returned.
 * </li>
 * </ul>
 *
 * @author The OGSA-DAI Project Team.
 */
public class GetAvailableTables extends BaseResourceActivity implements ResourceActivity {

    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2007-2008.";

    /** Default activity name .*/
    private static final ActivityName DEFAULT_ACTIVITY_NAME = new ActivityName("uk.org.ogsadai.GetAvailableTables");

    /** 
     * Activity output name <code>data</code> - the available tables
     * (OGSA-DAI list of <code>String</code>). 
     */
    public static final String OUTPUT_DATA = "data";

    /** Data output. */
    private ActivityOutput mDataOutput;

    /**
     * Constructor.
     */
    public GetAvailableTables() {
        super(DEFAULT_ACTIVITY_NAME);
        mDataOutput = new SimpleActivityOutput(OUTPUT_DATA);
    }

    /**
     * Determine if more results are available.
     * 
     * @return 
     *   true if more data is available, false otherwise.
     *   
     * @throws DataStreamErrorException 
     *             if there is an error on the data stream.
     * @throws UnexpectedDataValueException
     *             if there is an unexpected data value on the data stream.
     * @throws DataSourceUsageException
     *             if there is an error reading from a data source.
     *         
     */
    public boolean hasNextData() throws DataStreamErrorException, UnexpectedDataValueException, DataSourceUsageException {
        return mDataOutput.getDataValueIterator().hasNext();
    }

    /**
     * Returns an iterator to the returned set of table names. The client then
     * uses this iterator to traverse the set.
     * 
     * @return an iterator for table names
     * 
     * @throws DataStreamErrorException
     *             if there is an error on the data stream.
     * @throws UnexpectedDataValueException
     *             if there is an unexpected data value on the data stream.
     * @throws DataSourceUsageException
     *             if there is an error reading from a data source.
     */
    public DataListIterator nextData() throws DataStreamErrorException, UnexpectedDataValueException, DataSourceUsageException {
        if (!mDataOutput.getDataValueIterator().hasNext()) {
            return null;
        }
        return new DataListIterator(mDataOutput.getDataValueIterator(), String.class);
    }

    /**
     * Returns the next list of data in the result data as a <code>String</code> array.
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
        return new ActivityOutput[] { mDataOutput };
    }

    /**
     * Gets the data output. This output will contain the data returned when the
     * activity is executed.
     * 
     * @return data output.
     */
    public SingleActivityOutput getDataOutput() {
        return mDataOutput.getSingleActivityOutputs()[0];
    }
}
