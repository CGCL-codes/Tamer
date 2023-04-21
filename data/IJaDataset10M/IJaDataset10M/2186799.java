package edu.vub.at.doc.tmpl;

/**
 * Exception thrown when a invalid template is trying to be used. 
 *  
 * @author bcorne
 */
public class InvalidTemplate extends Exception {

    private static final long serialVersionUID = 13320303030L;

    public InvalidTemplate(String msg) {
        super(msg);
    }

    public InvalidTemplate(String msg, Throwable cause) {
        super(msg, cause);
    }
}
