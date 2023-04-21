package be.fedict.eid.dss.protocol.simple.client;

/**
 * Thrown in case something went wrong during the processing of the signature
 * response message.
 * 
 * @author Frank Cornelis
 * 
 */
public class SignatureResponseProcessorException extends Exception {

    private static final long serialVersionUID = 1L;

    public SignatureResponseProcessorException(String message) {
        super(message);
    }
}
