package uk.ac.ed.ph.jqtiplus.node.expression.outcome;

import uk.ac.ed.ph.jqtiplus.control.ProcessingContext;
import uk.ac.ed.ph.jqtiplus.control.TestProcessingContext;
import uk.ac.ed.ph.jqtiplus.node.expression.ExpressionParent;
import uk.ac.ed.ph.jqtiplus.state.AssessmentItemRefState;
import uk.ac.ed.ph.jqtiplus.value.IntegerValue;
import java.util.List;

/**
 * This expression, which can only be used in outcomes processing, calculates the number of items in
 * A given sub-set that have been selected for presentation to the candidate, regardless of whether
 * the candidate has attempted them or not.
 * <p>
 * The result is an integer with single cardinality.
 *
 * @see uk.ac.ed.ph.jqtiplus.value.Cardinality
 * @see uk.ac.ed.ph.jqtiplus.value.BaseType
 * 
 * @author Jiri Kajaba
 */
public class NumberSelected extends ItemSubset {

    private static final long serialVersionUID = 1L;

    /** Name of this class in xml schema. */
    public static final String CLASS_TAG = "numberSelected";

    /**
     * Constructs expression.
     *
     * @param parent parent of this expression
     */
    public NumberSelected(ExpressionParent parent) {
        super(parent);
    }

    @Override
    public String getClassTag() {
        return CLASS_TAG;
    }

    @Override
    protected IntegerValue evaluateSelf(ProcessingContext context, int depth) {
        TestProcessingContext testContext = (TestProcessingContext) context;
        List<AssessmentItemRefState> itemRefStates = testContext.lookupItemRefStates();
        int selectedCount = itemRefStates.size();
        return new IntegerValue(selectedCount);
    }
}
