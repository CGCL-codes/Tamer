package net.sourceforge.magex.preparation;

/**
 *  MaGeXException is the abstract base class for all the exception classes
 *  in the project.
 */
public abstract class MaGeXException extends Exception {

    /** The exception code */
    protected int code;

    /** Creates the exception class, given its code.
     *
     *  @param code the exception code
     */
    public MaGeXException(int code) {
        this.code = code;
    }

    /** Returns the exception code, given in the constructor.
     *  
     *  @return the exception code
     */
    public int getCode() {
        return this.code;
    }

    /** Should return the exception text, to be overridden in
     *  derived classes.
     *
     *  @return the exception text, in user-selected language
     */
    public abstract String getText() throws GeneralException;
}
