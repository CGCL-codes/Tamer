package genericAlgorithm.framework.errorHandling.exception;

/**
 *
 * @author OZS
 */
public class ApplicationControllerException extends GenericAlgorithmException {

    public ApplicationControllerException(String errorCode, String[] params) {
        super(errorCode, params);
    }
}
