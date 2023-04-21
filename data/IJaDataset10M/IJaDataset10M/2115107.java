package com.phloc.commons.log.filter;

import com.phloc.commons.log.LogRecordField;
import com.phloc.commons.log.ctx.ILogContext;

/**
 * This is a special filter rule that compares whether the unformatted value is
 * smaller or equal than the specified values. That means a record matches a
 * filter, when the context value is smaller or equal than the specified value.
 * 
 * @author philip
 */
public class LogFilterRuleContextSourceValueSmallerEqual extends AbstractBasicLogFilterRule {

    /**
   * Constructor.
   * 
   * @param aCtx
   *          the context
   * @param aValue
   *          desired value
   */
    public LogFilterRuleContextSourceValueSmallerEqual(final ILogContext<?> aCtx, final Comparable<?> aValue) {
        super("ctxle", aCtx, aValue);
    }

    @Override
    public boolean valueMatches(final LogRecordField<?> aField) {
        return aField.compareValue(m_aValue) <= 0;
    }
}
