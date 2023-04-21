package net.wastl.webmail.exceptions;

/**
 * BinaryNotFoundException.java
 *
 * Created: Thu May  4 15:38:04 2000
 *
 * @author Sebastian Schaffert
 */
public class BinaryNotFoundException extends WebMailException {

    static final long serialVersionUID = -7512762391532112873L;

    public BinaryNotFoundException() {
        super();
    }

    public BinaryNotFoundException(String s) {
        super(s);
    }
}
