package com.antilia.web.filter;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 */
public class JunctionFilter implements IRestrictionFilter {

    private static final long serialVersionUID = 1L;

    private final List<IRestrictionFilter> filters = new ArrayList<IRestrictionFilter>();

    private final LogicalOperator op;

    protected JunctionFilter(LogicalOperator op) {
        this.op = op;
    }

    public JunctionFilter add(IRestrictionFilter filter) {
        filters.add(filter);
        return this;
    }

    /**
	 * @return the op
	 */
    public LogicalOperator getOp() {
        return op;
    }
}
