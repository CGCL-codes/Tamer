package com.choicemaker.datagen.pred;

import java.lang.reflect.Method;
import java.util.EnumSet;
import java.util.Set;
import java.util.logging.Logger;
import net.sf.adatagenerator.api.EvaluationException;
import net.sf.adatagenerator.pred.ExportException;
import net.sf.adatagenerator.pred.PredicateExporter.EXPORT_TYPE_JAVA;
import net.sf.adatagenerator.pred.ReflectionUtils;
import net.sf.adatagenerator.pred.StringPredicate;
import com.choicemaker.matching.gen.EditDistance;
import com.choicemaker.matching.gen.EditDistance2;

/**
 * A predicate that checks whether the (String) values of a particular field for
 * a pair of records have an edit d1 strictly less than the specified maximum.
 * There are two different edit d1 algorithms available. The desired algorithm
 * is specified when an instance is constructed.
 * 
 * @author rphall
 * 
 * @param <R>
 *            the type of record to be compared
 */
public class EditDistancePredicate<R> extends StringPredicate<R> {

    protected static Logger logger = Logger.getLogger(EditDistancePredicate.class.getName());

    public static final int MINIMUM_MAX_DISTANCE = 1;

    public static final int INVALID_EDIT_DISTANCE = Integer.MAX_VALUE;

    /**
	 * The type edit d1 algorithms available for comparing String values.
	 * <ul>
	 * <li/><code>ONE</code> see
	 * {@link com.choicemaker.matching.gen.EditDistance EditDistance}
	 * <li/><code>TWO</code> see
	 * {@link com.choicemaker.matching.gen.EditDistance2 EditDistance2}
	 * </ul>
	 * 
	 * @author rphall
	 * 
	 */
    enum TYPE {

        ONE, TWO
    }

    private final TYPE type;

    private final int maxDistance;

    private String cluemaker25EvaluationLogicAsString;

    private String cluemaker25ValidityLogicAsString;

    /**
	 * Constructs a type <code>ONE</code> edit distance predicate.
	 * 
	 * @param predicateName
	 *            the name of this predicate
	 * @param fieldAccessor
	 *            a field accessor returning a String value
	 * @param maxDistance
	 *            the maximum edit d1 (exclusive) between field values in order
	 *            for them to be evaluated as significantly correlated
	 */
    public EditDistancePredicate(String predicateName, Method fieldAccessor, int maxDistance) {
        this(TYPE.ONE, predicateName, fieldAccessor, maxDistance);
    }

    /**
	 * Constructs an edit d1 predicate of the specified type.
	 * 
	 * @param type
	 *            the type of edit d1 use for comparison
	 * @param predicateName
	 *            the name of this predicate
	 * @param fieldAccessor
	 *            a field accessor returning a String value
	 * @param maxDistance
	 *            the maximum edit d1 (exclusive) between field values in order
	 *            for them to be evaluated as significantly correlated
	 */
    public EditDistancePredicate(TYPE type, String predicateName, Method fieldAccessor, int maxDistance) {
        super(predicateName, fieldAccessor);
        if (maxDistance < MINIMUM_MAX_DISTANCE) {
            throw new IllegalArgumentException("maxDistance (" + maxDistance + ") is less than the minimum allowed (" + MINIMUM_MAX_DISTANCE + ")");
        }
        this.type = type;
        this.maxDistance = maxDistance;
    }

    /**
	 * 
	 * @param type
	 *            the type of edit d1 use for comparison
	 * @param predicateName
	 *            the name of this predicate
	 * @param recordClass
	 * @param fieldName
	 *            the name of the field for which values should be
	 *            compared
	 * @param maxDistance
	 *            the maximum edit d1 (exclusive) between field values in order
	 *            for them to be evaluated as significantly correlated
	 */
    public EditDistancePredicate(TYPE type, String predicateName, Class<? extends R> recordClass, String fieldName, int maxDistance) {
        this(predicateName, ReflectionUtils.getFieldAccessor(recordClass, fieldName, null), maxDistance);
    }

    public boolean isFieldValidForEvaluation(String s) {
        return s != null;
    }

    public int editDistance(R q, R m) throws EvaluationException {
        if (!isRecordValidForEvaluation(q)) {
            throw new IllegalArgumentException("not valid for evaluation: " + q);
        }
        if (!isRecordValidForEvaluation(m)) {
            throw new IllegalArgumentException("not valid for evaluation: " + m);
        }
        int retVal = INVALID_EDIT_DISTANCE;
        try {
            String qVal = (String) getFieldAccessor().invoke(q, (Object[]) null);
            String mVal = (String) getFieldAccessor().invoke(m, (Object[]) null);
            retVal = editDistance(qVal, mVal);
        } catch (Exception e) {
            throw new EvaluationException(e.toString(), e);
        }
        return retVal;
    }

    public int editDistance(String qVal, String mVal) {
        int retVal;
        if (!isFieldValidForEvaluation(qVal)) {
            logger.warning("not valid for evaluation: " + qVal);
            retVal = INVALID_EDIT_DISTANCE;
        }
        if (!isFieldValidForEvaluation(mVal)) {
            logger.warning("not valid for evaluation: " + mVal);
            retVal = INVALID_EDIT_DISTANCE;
        } else {
            retVal = unsafeEditDistance(qVal, mVal);
        }
        return retVal;
    }

    protected int unsafeEditDistance(String qVal, String mVal) {
        int retVal;
        switch(this.type) {
            case ONE:
                retVal = EditDistance.editDistance(qVal, mVal);
                break;
            case TWO:
                retVal = EditDistance2.editDistance2(qVal, mVal);
                break;
            default:
                throw new IllegalStateException("unexpected type: " + type.name());
        }
        return retVal;
    }

    public boolean evaluateFieldValues(String qVal, String mVal) {
        assert qVal != null;
        assert mVal != null;
        int editDistance = unsafeEditDistance(qVal, mVal);
        boolean retVal = editDistance < getMaxDistance();
        return retVal;
    }

    public int getMaxDistance() {
        return maxDistance;
    }

    public Set<EXPORT_TYPE_JAVA> getSupportedExportTypes() {
        Set<EXPORT_TYPE_JAVA> retVal = EnumSet.of(EXPORT_TYPE_JAVA.CLUEMAKER_25X);
        return retVal;
    }

    public String exportEvaluationLogicAsString(EXPORT_TYPE_JAVA type) throws ExportException {
        String retVal;
        switch(type) {
            case CLUEMAKER_25X:
                retVal = exportEvaluationLogicToClueMaker();
                break;
            default:
                retVal = exportEvaluationLogicToNonChoiceMaker(type);
        }
        return retVal;
    }

    public String exportValidityLogicToClueMaker() throws ExportException {
        if (this.cluemaker25ValidityLogicAsString == null) {
            cluemaker25ValidityLogicAsString = new StringBuilder().append(q).append(" != null && ").append(m).append(" != null && ").append(qFld).append(" != null && ").append(mFld).append(" != null").toString();
        }
        return this.cluemaker25ValidityLogicAsString;
    }

    public String exportEvaluationLogicToClueMaker() throws ExportException {
        if (this.cluemaker25EvaluationLogicAsString == null) {
            String ed = null;
            switch(this.type) {
                case ONE:
                    ed = "EditDistance.editDistance(";
                    break;
                case TWO:
                    ed = "EditDistance2.editDistance2(";
                    break;
                default:
                    throw new IllegalStateException("unexpected type: " + type.name());
            }
            this.cluemaker25EvaluationLogicAsString = new StringBuilder().append(ed).append(qFld).append(", ").append(mFld).append(") < ").append(this.getMaxDistance()).toString();
        }
        return this.cluemaker25EvaluationLogicAsString;
    }
}
