package org.jpos.gl.rule;

import java.math.BigDecimal;

/**
 * Check that composite account's balance (in reporting currency) is not less than the ruleinfo param.
 *
 * @author <a href="mailto:apr@jpos.org">Alejandro Revilla</a>
 * @see org.jpos.gl.JournalRule
 * @see org.jpos.gl.RuleInfo
 */
public class CompositeMinBalance extends CompositeBalance {

    protected String getRuleName() {
        return "CompositeMinBalance";
    }

    protected boolean isError(BigDecimal balance, BigDecimal minBalance) {
        return balance.compareTo(minBalance) < 0;
    }
}
