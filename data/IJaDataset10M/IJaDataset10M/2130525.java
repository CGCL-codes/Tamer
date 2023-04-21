package uk.org.ogsadai.client.toolkit.activities.sql;

import java.util.NoSuchElementException;
import uk.org.ogsadai.activity.ActivityName;
import uk.org.ogsadai.client.toolkit.ActivityOutput;
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
import uk.org.ogsadai.data.IntegerData;
import uk.org.ogsadai.data.StringData;

/**
 * An activity that executes SQL updates on the target data resource and 
 * produces the resulting update counts as output.
 * <p>
 * Activity inputs:
 * </p>
 * <ul>
 * <li>
 * <code>expression</code>. Type: {@link java.lang.String}. SQL update 
 * expression.
 * </li>
 * </ul>
 * <p>
 * Activity outputs:
 * </p>
 * <ul>
 * <li>
 * <code>result</code>. Type: {@link java.lang.Integer}. Update count 
 * specifying how many rows were modified by the update expression.
 * </li>
 * </ul>
 * <p>
 * Expected name of activity on server:
 * </p>
 * <ul>
 * <li>
 * <code>uk.org.ogsadai.SQLUpdate</code>
 * </li>
 * </ul>
 * <p>
 * Activity input/output ordering: none.
 * </p>
 * <p>
 * Activity contracts: none.
 * </p>
 * <p>
 * Target data resource:
 * <ul>
 * <li>
 * This activity must be targeted at a relational data resource.
 * </li>
 * </ul>
 * </p>
 * <p>
 * Behaviour: 
 * </p>
 * <ul>
 * <li>
 * This activity accepts a sequence of SQL update expressions as input and is 
 * targeted at a relational data resource.  In each iteration one input update
 * is processed by executing the update on the target data resource. The results
 * of each iteration is an update count which specifies the number of modified
 * rows in the database.
 * </li>
 * <li>
 * An <code>ActivitySQLUserException</code> is raised if there was an access 
 * error at the data resource for example syntax errors in the SQL  expression
 * or if the target table does not exist.  
 * </li>
 * </ul>
 * 
 * @author The OGSA-DAI Project Team.
 */
public class SQLUpdate extends BaseResourceActivity implements ResourceActivity {

    /** Copyright statement. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2007";

    /** Default activity name. */
    private static final ActivityName DEFAULT_ACTIVITY_NAME = new ActivityName("uk.org.ogsadai.SQLUpdate");

    /** Expression input name. */
    public static final String EXPRESSION_INPUT = "expression";

    /** operation status output name. */
    public static final String UPDATE_RESULT = "result";

    /** Expression input. */
    private final ActivityInput mExpressionInput;

    /** Data output. */
    private final ActivityOutput mResultOutput;

    /**
     * Constructor.
     */
    public SQLUpdate() {
        super(DEFAULT_ACTIVITY_NAME);
        mExpressionInput = new SimpleActivityInput(EXPRESSION_INPUT);
        mResultOutput = new SimpleActivityOutput(UPDATE_RESULT);
    }

    /**
     * Adds a new SQL update expression.
     * 
     * @param expression SQL expression.
     */
    public void addExpression(final String expression) {
        mExpressionInput.add(new StringData(expression));
    }

    /**
     * Connects the expression input to the given output.
     * 
     * @param output output to connect to.
     */
    public void connectExpressionInput(final SingleActivityOutput output) {
        mExpressionInput.connect(output);
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
        return new ActivityInput[] { mExpressionInput };
    }

    /**
     * {@inheritDoc}
     */
    protected ActivityOutput[] getOutputs() {
        return new ActivityOutput[] { mResultOutput };
    }

    /**
     * Gets the result output.
     * 
     * @return the output.
     */
    public SingleActivityOutput getResultOutput() {
        return mResultOutput.getSingleActivityOutputs()[0];
    }

    /**
     * Determine if another SQL update result is available
     * 
     * @return <code>true</code> if more data available, <code>false</code>
     *         otherwise
     * 
     * @throws DataStreamErrorException
     *             if there is an error on the data stream.
     * @throws UnexpectedDataValueException
     *             if there is an unexpected data value on the data stream.
     * @throws DataSourceUsageException
     *             if there is an error reading from a data source.
     */
    public boolean hasNextResult() throws DataStreamErrorException, UnexpectedDataValueException, DataSourceUsageException {
        return mResultOutput.getDataValueIterator().hasNext();
    }

    /**
     * Returns the result of the next SQL update operation.
     * 
     * @return the number of rows which were affected by the SQL statement
     * 
     * @throws DataStreamErrorException
     *             if there is an error on the data stream.
     * @throws UnexpectedDataValueException
     *             if there is an unexpected data value on the data stream.
     * @throws DataSourceUsageException
     *             if there is an error reading from a data source.
     * @throws NoSuchElementException
     *             if there are no more result values available
     */
    public int nextResult() throws DataStreamErrorException, UnexpectedDataValueException, DataSourceUsageException {
        if (!hasNextResult()) {
            throw new NoSuchElementException();
        }
        IntegerData id = null;
        id = (IntegerData) mResultOutput.getDataValueIterator().next();
        return id.getInteger();
    }
}
