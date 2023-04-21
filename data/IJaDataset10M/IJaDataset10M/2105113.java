package uk.ac.ebi.intact.application.graph2MIF.exception;

/**
 * This Exception should be thrown, if an error occours, but the above Element has
 * to determine if it is an fatal one or PSI Format without this Element would also be valid.
 * @author Henning Mersch <hmersch@ebi.ac.uk>
 * @version $Id: ElementNotParseableException.java 2189 2003-10-30 15:46:05Z skerrien $
 */
public class ElementNotParseableException extends Exception {

    public ElementNotParseableException() {
        super();
    }

    public ElementNotParseableException(String message) {
        super(message);
    }
}
