package org.jfree.report.expressions.sys;

import org.jfree.report.DataSourceException;
import org.jfree.report.expressions.AbstractExpression;

/**
 * Creation-Date: 02.12.2006, 22:07:57
 *
 * @author Thomas Morgner
 */
public class GetValueExpression extends AbstractExpression {

    private String field;

    public GetValueExpression() {
    }

    public GetValueExpression(final String field) {
        this.field = field;
    }

    public String getField() {
        return field;
    }

    public void setField(final String field) {
        this.field = field;
    }

    /**
   * Return the current expression value. <P> The value depends (obviously) on
   * the expression implementation.
   *
   * @return the value of the function.
   */
    public Object computeValue() throws DataSourceException {
        return getDataRow().get(getField());
    }
}
