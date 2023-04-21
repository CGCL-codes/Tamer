package org.progeeks.mparse.exp;

import java.io.*;
import org.progeeks.util.ObjectUtils;
import org.progeeks.mparse.*;
import org.progeeks.mparse.io.*;

/**
 *
 *  @version   $Revision: 4124 $
 *  @author    Paul Speed
 */
public class Repeat extends AbstractExpression {

    private Expression child;

    private int min;

    private int max;

    public Repeat(Expression exp, int min, int max) {
        this.child = exp;
        this.min = min;
        this.max = max;
        setProducer(Producers.stringBuilder());
    }

    public Repeat(Expression exp, int min) {
        this(exp, min, Integer.MAX_VALUE);
    }

    public int getMinimum() {
        return min;
    }

    public int getMaximum() {
        return max;
    }

    public Expression getChild() {
        return child;
    }

    /**
     *  Calls resolve on the child expression and replaces
     *  it with the resolved version.
     */
    protected Expression doResolve() {
        Expression resolved = child.resolve();
        child = resolved;
        return this;
    }

    public boolean equals(Object o) {
        if (!super.equals(o)) return false;
        Repeat other = (Repeat) o;
        if (other.min != min) return false;
        if (other.max != max) return false;
        return ObjectUtils.areEqual(child, other.child);
    }

    public int hashCode() {
        return super.hashCode() + child.hashCode() + min;
    }

    public ResultType parse(ParseContext context, CharStream in) throws IOException {
        if (max == 1) {
            ResultType result = child.parse(context, in);
            if (result == ResultType.UNMATCHED && min == 0) return ResultType.MATCHED_EMPTY;
            return result;
        }
        for (int i = 0; i < max; i++) {
            ResultType t = child.parse(context, in);
            if (t == ResultType.INCOMPLETE) {
                return ResultType.INCOMPLETE;
            }
            if (t == ResultType.UNMATCHED) {
                if (i < min) {
                    if (i > 0) return ResultType.INCOMPLETE;
                    return ResultType.UNMATCHED;
                }
                if (i == 0 && min == 0) return ResultType.MATCHED_EMPTY;
                break;
            }
            if (t == ResultType.MATCHED_EMPTY) {
                return ResultType.MATCHED_EMPTY;
            }
        }
        return ResultType.MATCHED;
    }

    protected String suffix() {
        if (min == 0 && max == Integer.MAX_VALUE) return "*";
        if (min == 1 && max == Integer.MAX_VALUE) return "+";
        if (min == 0 && max == 1) return "?";
        return "{" + min + "," + max + "}";
    }

    public String toString(int depth) {
        if (depth == 0) return nameString() + "[some child]" + suffix();
        return nameString() + child.toString(depth - 1) + suffix();
    }
}
