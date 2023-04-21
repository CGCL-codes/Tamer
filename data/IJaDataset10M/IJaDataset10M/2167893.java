package org.vikamine.kernel.data.discretization;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import org.vikamine.kernel.data.AttributeBuilder;
import org.vikamine.kernel.data.DerivedAttributeBuilder;
import org.vikamine.kernel.data.DerivedNominalAttribute;
import org.vikamine.kernel.data.DerivedNominalValue;
import org.vikamine.kernel.data.NumericAttribute;
import org.vikamine.kernel.data.Value;
import org.vikamine.kernel.formula.FormulaBooleanElement;
import org.vikamine.kernel.formula.FormulaNumber;
import org.vikamine.kernel.formula.constants.FormulaAttributePrimitive;
import org.vikamine.kernel.formula.operators.And;
import org.vikamine.kernel.formula.operators.Greater;
import org.vikamine.kernel.formula.operators.GreaterEquals;

/**
 * {@link DiscretizationAttributeBuilder} builds a nominal custom
 * {@link AttributeBuilder}.
 * 
 * @author lemmerich, atzmueller
 * @date 04/2009
 */
public class DiscretizationAttributeBuilder extends DerivedAttributeBuilder {

    private final List<Double> cutpoints;

    private final NumericAttribute na;

    private final DecimalFormat formatter = new DecimalFormat("#.#");

    public DiscretizationAttributeBuilder(List<Double> cutpoints, NumericAttribute na) {
        super();
        this.cutpoints = cutpoints;
        this.na = na;
    }

    public void buildNominalCustomAttribute(String attributeID) {
        List<Value> values = buildValues();
        attribute = new DerivedNominalAttribute(attributeID, values);
        for (Value v : values) {
            ((DerivedNominalValue) v).setAttribute(attribute);
        }
    }

    private List<Value> buildValues() {
        List<Value> result = new ArrayList<Value>();
        for (int i = 0; i < (cutpoints.size() - 1); i++) {
            double minOfInterval = (i == 0) ? Double.NEGATIVE_INFINITY : cutpoints.get(i);
            double maxOfInterval = (i == cutpoints.size() - 2) ? Double.POSITIVE_INFINITY : cutpoints.get(i + 1);
            String description = "[" + formatter.format(minOfInterval) + "; " + formatter.format(maxOfInterval) + "]";
            DerivedNominalValue value = new DerivedNominalValue(description, description);
            GreaterEquals left = new GreaterEquals();
            left.setArg1(new FormulaAttributePrimitive(na));
            left.setArg2(new FormulaNumber(minOfInterval));
            Greater right = new Greater();
            right.setArg1(new FormulaNumber(maxOfInterval));
            right.setArg2(new FormulaAttributePrimitive(na));
            FormulaBooleanElement formula = new And(left, right);
            value.setCondition(formula);
            result.add(value);
        }
        return result;
    }
}
