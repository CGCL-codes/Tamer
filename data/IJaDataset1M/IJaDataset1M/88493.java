package net.sf.jmoney.qif.parser;

/**
 * This exception will be thrown only if
 * 
 * <UL>
 * <LI>The date format passed to the QifFile constructor
 * was DetermineFromFile</LI>
 * <LI>The file contained at least one date but no
 * dates where the day of the month was more than 12</LI>
 * </UL>
 * 
 * If this exception is throw then the caller should make some
 * attempt at the date format, perhaps by looking at the machine's
 * locale or perhaps by asking the user, then calling the constructor
 * again with a specific date format.
 */
public class AmbiguousDateException extends Exception {

    private static final long serialVersionUID = 1L;

    public AmbiguousDateException() {
        super();
    }
}
