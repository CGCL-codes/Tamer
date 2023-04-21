package net.sf.mailsomething.mail;

/**
 * @author kris
 * 
 */
public class MalformedMailAddressException extends Exception {

    public MalformedMailAddressException() {
    }

    public MalformedMailAddressException(String s) {
        super(s);
    }
}
