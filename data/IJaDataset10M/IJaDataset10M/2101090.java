package jargs;

/**
 * Thrown when the parsed commandline contains multiple concatenated
 * short options, such as -abcd, where one or more requires a value.
 * <code>getMessage()</code> returns an english human-readable error
 * string.
 * @author Vidar Holen
 */
public class NotFlagException extends UnknownOptionException {

    private static final long serialVersionUID = -5484703640126410616L;

    private char notflag;

    NotFlagException(String option, char unflaggish) {
        super(option, "Illegal option: '" + option + "', '" + unflaggish + "' requires a value");
        notflag = unflaggish;
    }

    /**
     * @return the first character which wasn't a boolean (e.g 'c')
     */
    public char getOptionChar() {
        return notflag;
    }
}
