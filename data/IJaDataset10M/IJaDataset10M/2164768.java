package net.sf.jncu.fdil;

/**
 * Expected large binary object.<br>
 * <tt>kFD_ExpectedLargeBinary (kFD_ErrorBase - 27)</tt>
 * 
 * @author moshe
 */
public class ExpectedLargeBinaryException extends ExpectedBinaryException {

    public ExpectedLargeBinaryException() {
        super();
    }

    public ExpectedLargeBinaryException(String message) {
        super(message);
    }
}
