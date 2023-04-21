package geovista.jts.io;

import geovista.jts.JUMPException;

/**
 *Simple exception class to express problems parsing data.
 */
public class ParseException extends JUMPException {

    public String fname;

    public int lineno;

    public int cpos;

    /** construct exception with a message*/
    public ParseException(String message) {
        super(message);
    }

    /**
     *  More explictly construct a parse exception.
     *  Resulting message will be :message + " in file '" + newFname +"', line " + newLineno + ", char " + newCpos
     * @param message information about the type of error
     * @param newFname filename the error occurred in
     * @param newLineno line number the error occurred at
     * @param newCPos character position on the line
     *
     **/
    public ParseException(String message, String newFname, int newLineno, int newCpos) {
        super(message + " in file '" + newFname + "', line " + newLineno + ", char " + newCpos);
        fname = newFname;
        lineno = newLineno;
        cpos = newCpos;
    }
}
