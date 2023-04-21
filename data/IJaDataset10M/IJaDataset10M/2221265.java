package de.jformular.exception;

/**
 * Class declaration
 * @author Frank Dolibois, fdolibois@itzone.de, http://www.itzone.de
 * @version $Id: FormularException.java,v 1.5 2002/10/14 14:01:35 fdolibois Exp $
 */
public class FormularException extends Exception {

    /**
     * Constructor declaration
     */
    public FormularException() {
        super();
    }

    /**
     * Constructor declaration
     *
     * @param s
     */
    public FormularException(String s) {
        super(s);
    }
}
