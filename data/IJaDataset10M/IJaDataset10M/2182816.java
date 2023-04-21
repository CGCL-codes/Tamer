package mindbright.security;

public class InvalidKeyException extends KeyException {

    public InvalidKeyException() {
        super();
    }

    public InvalidKeyException(String msg) {
        super(msg);
    }
}
