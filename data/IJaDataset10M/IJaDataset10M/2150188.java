package org.base.apps.beans.util;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

/**
 * 
 *
 * @author Kevan Simpson
 */
public class Expr implements CharSequence {

    static final String SEP = "/";

    private StringBuffer mBuffer;

    protected Expr() {
        mBuffer = new StringBuffer();
    }

    protected Expr(CharSequence path) {
        this();
        mBuffer.append(ObjectUtils.toString(path, ""));
    }

    public Expr step(CharSequence step) {
        mBuffer.append(SEP).append(step);
        return this;
    }

    public Expr[] toSteps() {
        return toSteps(0);
    }

    public Expr[] toSteps(int stepIndex) {
        if (stepIndex >= 0) {
            String[] strs = StringUtils.split(mBuffer.toString(), SEP);
            Expr[] steps = new Expr[(strs.length - stepIndex)];
            for (int i = stepIndex, I = strs.length; i < I; i++) {
                steps[(i - stepIndex)] = newExpr(strs[i]);
            }
            return steps;
        }
        return new Expr[0];
    }

    /** @see java.lang.CharSequence#charAt(int) */
    @Override
    public char charAt(int index) {
        if (index < 0 || index >= length()) {
            throw new IndexOutOfBoundsException("Expr index out of bounds: " + index);
        }
        return mBuffer.charAt(index);
    }

    /** @see java.lang.CharSequence#length() */
    @Override
    public int length() {
        return mBuffer.length();
    }

    /** @see java.lang.CharSequence#subSequence(int, int) */
    @Override
    public CharSequence subSequence(int start, int end) {
        return mBuffer.subSequence(start, end);
    }

    /** @see java.lang.Object#equals(java.lang.Object) */
    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Expr && StringUtils.equals(obj.toString(), this.toString()));
    }

    /** @see java.lang.Object#hashCode() */
    @Override
    public int hashCode() {
        return toString().hashCode() * 13 + 19;
    }

    /** @see java.lang.Object#toString() */
    @Override
    public String toString() {
        return mBuffer.toString();
    }

    public static Expr newExpr(CharSequence... steps) {
        Expr expr = null;
        if (!ArrayUtils.isEmpty(steps)) {
            for (CharSequence step : steps) {
                if (expr == null) {
                    expr = new Expr(step);
                } else {
                    expr.step(step);
                }
            }
        }
        return expr;
    }
}
