package com.baculsoft.lang;

/**
 * 
 * @author Muhammad Edwin
 * @version 1.0.0
 */
final class RegexException extends RuntimeException {

    RegexException() {
    }

    /**
	 * 
	 * @param str
	 */
    RegexException(final String str) {
        super(str);
    }

    ;

    /**
	 * 
	 * @param t
	 */
    RegexException(final Throwable t) {
        super(t);
    }

    @Override
    public final String toString() {
        return super.toString();
    }

    @Override
    public final int hashCode() {
        return super.hashCode();
    }

    @Override
    public final boolean equals(Object obj) {
        return super.equals(obj);
    }
}
