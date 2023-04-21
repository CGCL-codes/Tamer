package com.baculsoft.lang;

/**
 * 
 * @author Natalino Nugeraha
 * @version 1.0.0
 * @category Internal Utility
 */
final class Or implements Cloneable {

    /** Singleton **/
    static final Or INSTANCE = new Or();

    private static final String OPOR_SYMBOL = "|";

    private Or() {
    }

    @Override
    public final boolean equals(final Object obj) {
        return super.equals(obj);
    }

    @Override
    public final int hashCode() {
        return super.hashCode();
    }

    @Override
    public final String toString() {
        return OPOR_SYMBOL;
    }

    @Override
    protected final Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
