package it.eg.sloth.db.datasource.table.filter;

import it.eg.sloth.common.FrameComponent;
import it.eg.sloth.common.base.BaseFunction;
import it.eg.sloth.db.datasource.DataSource;
import java.util.Stack;

/**
 * 
 * @author Enrico Grillini 
 *
 */
public class FilterRules extends FrameComponent {

    private static final long serialVersionUID = 1L;

    private Stack<FilterRule> stack;

    public FilterRules() {
        stack = new Stack<FilterRule>();
    }

    public void clearRule() {
        stack.clear();
    }

    public int size() {
        return stack.size();
    }

    public void pushRule(String fieldName, Object fieldValue) {
        stack.push(new FilterRule(fieldName, fieldValue));
    }

    public FilterRule popRule() {
        return (FilterRule) stack.pop();
    }

    public FilterRule peekRule() {
        return (FilterRule) stack.peek();
    }

    public boolean check(DataSource dataSource) {
        for (FilterRule filterRule : stack) {
            if (!BaseFunction.equals(filterRule.getFieldValue(), dataSource.getObject(filterRule.getFieldName()))) return false;
        }
        return true;
    }
}
