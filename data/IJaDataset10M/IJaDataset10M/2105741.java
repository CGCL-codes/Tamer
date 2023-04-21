package uk.org.ogsadai.dqp.lqp.udf.scalar;

import java.math.BigDecimal;
import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.dqp.lqp.udf.FunctionType;
import uk.org.ogsadai.dqp.lqp.udf.LogicalExecutableFunctionBase;
import uk.org.ogsadai.tuple.Null;
import uk.org.ogsadai.tuple.TupleTypes;
import uk.org.ogsadai.tuple.TypeConversionException;
import uk.org.ogsadai.tuple.TypeConverter;
import uk.org.ogsadai.tuple.TypeMismatchException;

/**
 * Scalar function to calculate arc cosine.
 * 
 * @author The OGSA-DAI Project Team.
 *
 */
public class Acos extends LogicalExecutableFunctionBase {

    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2011.";

    /** Logger object for logging in this class. */
    private static final DAILogger LOG = DAILogger.getLogger(Mod.class);

    /** The function's name. */
    private static final String FUNCTION_NAME = "ACOS";

    /** The data type of the input parameter. */
    private int mType = -1;

    /** The data type of the evaluation result. */
    private int mResultType = -1;

    /** The evaluation result. */
    private Object mResult;

    /**
     * Constructor.
     */
    public Acos() {
        super(1);
    }

    /**
     * Constructor. The constructor object copies the state of the object 
     * passed to it, i.e. the data type of the inputs and output.
     * 
     * @param mod
     */
    public Acos(Acos cos) {
        this();
        mType = cos.getParameterType();
        mResultType = cos.getOutputType();
    }

    /**
     * {@inheritDoc}
     */
    public String getName() {
        return FUNCTION_NAME;
    }

    /**
     * {@inheritDoc}
     */
    public FunctionType getType() {
        return FunctionType.UDF_SCALAR;
    }

    /**
     * Configures the function with the given data types.
     */
    public void configure(int... types) throws TypeMismatchException {
        switch(types[0]) {
            case TupleTypes._SHORT:
            case TupleTypes._LONG:
            case TupleTypes._INT:
            case TupleTypes._DOUBLE:
            case TupleTypes._FLOAT:
            case TupleTypes._BIGDECIMAL:
                mResultType = TupleTypes._DOUBLE;
                break;
            default:
                throw new TypeMismatchException(types[0]);
        }
        mType = types[0];
    }

    /**
     * Returns the type of X.
     * 
     * @return type.
     */
    protected int getParameterType() {
        return mType;
    }

    /**
     * {@inheritDoc}
     */
    public int getOutputType() {
        return mResultType;
    }

    /**
     * {@inheritDoc}
     */
    public Object getResult() {
        return mResult;
    }

    /**
     * Puts the input parameters X and computes acos(X).
     * 
     */
    public void put(Object... parameters) {
        if (parameters[0] == Null.VALUE) {
            mResult = Null.VALUE;
        } else {
            Object x = null;
            try {
                x = TypeConverter.convertObject(mType, mResultType, parameters[0]);
            } catch (TypeConversionException e) {
                LOG.error(e, true);
                throw new RuntimeException(e);
            }
            switch(mResultType) {
                case TupleTypes._DOUBLE:
                    mResult = Math.acos((Double) x);
                    break;
                case TupleTypes._BIGDECIMAL:
                    BigDecimal xb = (BigDecimal) x;
                    mResult = Math.acos(xb.doubleValue());
                    break;
            }
        }
    }
}
