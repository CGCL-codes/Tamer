package mscheme.exceptions;

public final class NumberExpected extends TypeError {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String CVS_ID = "$Id: NumberExpected.java 743 2006-05-20 07:45:47Z sielenk $";

    public NumberExpected(Object cause) {
        super(cause, "number");
    }
}
