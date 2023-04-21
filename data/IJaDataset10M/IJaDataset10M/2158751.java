package org.horen.ui.editors.filters.operators;

import org.horen.ui.editors.columns.ColumnProvider;
import org.horen.ui.editors.filters.decides.FilterDecide;
import org.horen.ui.resources.Resources;

/**
 * A filter operator for strings that checks whether a string value
 * starts with another string (operand).
 * 
 * @author Steffen
 */
public class StringStartsWithOperator implements IFilterOperator {

    @Override
    public String getName() {
        return Resources.getDefaultBundle().getString("Filter.Operator.StringStarts");
    }

    @Override
    public FilterDecide getDecide(ColumnProvider column, Object operand) {
        return new FilterDecide(column, this, operand) {

            @Override
            protected boolean matchText(String value) {
                return value.startsWith(getOperand().toString());
            }
        };
    }

    @Override
    public Class<?> getOperandClass() {
        return String.class;
    }
}
