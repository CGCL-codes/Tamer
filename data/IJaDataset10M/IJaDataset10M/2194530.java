package coyousoft.javaee.bookstore.exception;

/** 
 * This application exception indicates that a book has not been found.
 */
public class BookNotFoundException extends Exception {

    private static final long serialVersionUID = 1L;

    public BookNotFoundException() {
    }

    public BookNotFoundException(String msg) {
        super(msg);
    }
}
