package uk.org.ogsadai.dqp.lqp.udf.scalar;

import java.sql.Time;
import org.joda.time.DateTime;
import uk.org.ogsadai.dqp.lqp.udf.FunctionType;
import uk.org.ogsadai.dqp.lqp.udf.LogicalExecutableFunctionBase;
import uk.org.ogsadai.tuple.TupleTypes;

/**
 * CurrentTime function.
 * <p>
 * Function will return the current time as a java.sql.Time type.
 * <p>
 * When the function is created, the current time will be set to ensure
 * a consistent time used throughout the query processing.
 * <p>
 * @author The OGSA-DAI Project Team
 */
public class CurrentTime extends LogicalExecutableFunctionBase {

    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2009.";

    /** return value initialise on creation. */
    private DateTime mDate = null;

    /**
     * Constructor for CurrentDate.
     * 
     * Sets current time in constructor.
     */
    public CurrentTime() {
        super(0);
        mDate = new DateTime(System.currentTimeMillis());
    }

    /**
     * Constructor used to clone expressions.  Creates new instance in the
     * same state as the given instance was after the <tt>configure</tt>
     * method was called.
     * 
     * @param currentTime instance from which to copy state.
     */
    public CurrentTime(CurrentTime currentTime) {
        this();
    }

    /**
     * {@inheritDoc}
     */
    public String getName() {
        return "CurrentTime";
    }

    /**
     * {@inheritDoc}
     */
    public String toString() {
        return "CurrentTime";
    }

    /**
     * {@inheritDoc}
     */
    public FunctionType getType() {
        return FunctionType.SQL_SCALAR;
    }

    /**
     * {@inheritDoc}
     */
    public void configure(int... types) {
    }

    /**
     * {@inheritDoc}
     */
    public int getOutputType() {
        return TupleTypes._TIME;
    }

    /**
     * {@inheritDoc}
     */
    public Object getResult() {
        return new Time(mDate.getMillis());
    }

    /**
     * {@inheritDoc}
     */
    public void put(Object... parameters) {
    }
}
