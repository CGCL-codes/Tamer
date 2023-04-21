package fr.dauphine.ecommerce.exceptions;

/**
 *
 * @author DELL
 */
public class CartException extends Exception {

    public CartException(Throwable arg0) {
        super(arg0);
    }

    public CartException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public CartException(String arg0) {
        super(arg0);
    }

    public CartException() {
    }
}
