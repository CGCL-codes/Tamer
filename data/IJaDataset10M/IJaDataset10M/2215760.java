package org.horen.ui.editors.filters.operators;

import java.util.HashSet;
import java.util.Set;
import org.horen.task.Label;
import org.horen.ui.editors.columns.ColumnProvider;
import org.horen.ui.editors.filters.decides.FilterDecide;
import org.horen.ui.resources.Resources;

/**
 * A filter operator for label lists that checks whether a set of labels is NOT present.
 * 
 * @author Steffen
 */
public class LabelsHasNoneOfOperator implements IFilterOperator {

    @Override
    public String getName() {
        return Resources.getDefaultBundle().getString("Filter.Operator.LabelsHasNoneOf");
    }

    @Override
    public FilterDecide getDecide(ColumnProvider column, Object operand) {
        return new FilterDecide(column, this, operand) {

            private Set<Label> m_Cache = new HashSet<Label>();

            @Override
            protected boolean matchValue(Object value) {
                Label[] op = (Label[]) getOperand();
                Label[] val = (Label[]) value;
                m_Cache.clear();
                for (Label lbl : op) {
                    m_Cache.add(lbl);
                }
                for (Label lbl : val) {
                    if (m_Cache.contains(lbl)) {
                        return false;
                    }
                }
                return true;
            }
        };
    }

    @Override
    public Class<?> getOperandClass() {
        return Label[].class;
    }
}
