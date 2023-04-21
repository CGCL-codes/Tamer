package com.baculsoft.lang;

/**
 * 
 * @author Natalino Nugeraha
 * @version 1.0.0
 * @category Internal Utility
 */
final class Not implements Cloneable {

    /** Singleton **/
    static final Not INSTANCE = new Not();

    private static String OP_SYMBOL = "!";

    private Not() {
    }

    @Override
    public final int hashCode() {
        return super.hashCode();
    }

    @Override
    public final String toString() {
        return OP_SYMBOL;
    }

    @Override
    public final boolean equals(final Object obj) {
        return super.equals(obj);
    }

    @Override
    public final Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
