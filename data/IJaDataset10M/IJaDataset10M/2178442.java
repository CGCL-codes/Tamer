package com.newisys.dv.ifgen.schema;

import java.util.LinkedList;
import java.util.List;

/**
 * A set of expressions and ranges.
 * 
 * @author Jon Nall
 */
public final class IfgenSet {

    List<IfgenExpression> expressions = new LinkedList<IfgenExpression>();

    List<IfgenRange> ranges = new LinkedList<IfgenRange>();

    public IfgenSet() {
    }

    public IfgenSet(IfgenRange range) {
        addRange(range);
    }

    public void addAll(IfgenSet set) {
        for (final IfgenExpression e : set.expressions) {
            addExpression(e);
        }
        for (final IfgenRange r : set.ranges) {
            addRange(r);
        }
    }

    public void addExpression(IfgenExpression expr) {
        expressions.add(expr);
    }

    public void addRange(IfgenRange range) {
        ranges.add(range);
    }

    public List<IfgenExpression> getExpressions() {
        return expressions;
    }

    public List<IfgenRange> getRanges() {
        return ranges;
    }
}
