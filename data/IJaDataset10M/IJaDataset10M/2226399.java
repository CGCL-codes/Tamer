package net.sf.julie.types;

import net.sf.julie.Interpretable;

public class LiteralBoolean extends SelfEvaluating<Boolean> {

    public static LiteralBoolean TRUE = new LiteralBoolean(Boolean.TRUE);

    public static LiteralBoolean FALSE = new LiteralBoolean(Boolean.FALSE);

    private LiteralBoolean(Boolean delegate) {
        this.delegate = delegate;
    }

    public static LiteralBoolean getInstance(String string) {
        if (string.equals("#t")) {
            return TRUE;
        } else {
            return FALSE;
        }
    }

    @Override
    public String toString() {
        return Boolean.TRUE.equals(delegate) ? "#t" : "#f";
    }

    @Override
    public boolean isTrue() {
        return delegate.equals(Boolean.TRUE);
    }

    @Override
    public boolean isFalse() {
        return delegate.equals(Boolean.FALSE);
    }

    /**
     * Returns LiteralBoolean.TRUE if value counts as true, and returns
     * LiteralBoolean.FALSE if value counts as false. Note that only only #f
     * counts as false.
     * 
     * @param testResult
     * @return
     */
    public static boolean isTrue(Interpretable interpretable) {
        return !isFalse(interpretable);
    }

    public static LiteralBoolean valueOf(boolean b) {
        return b ? LiteralBoolean.TRUE : LiteralBoolean.FALSE;
    }

    public static boolean isFalse(Interpretable interpretable) {
        return LiteralBoolean.FALSE.equals(interpretable);
    }

    public Interpretable not() {
        return valueOf(isFalse(this));
    }
}
