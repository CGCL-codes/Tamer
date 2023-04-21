package systems.dispatcher;

import java.util.List;
import infrastructureAPI.APIResponse;
import infrastructureAPI.ResponseException;
import infrastructureAPI.algorithms.APIFunction;

/**
 * Description: 
 *
 */
public class NotFoundServices extends ResponseException implements APIResponse {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public NotFoundServices() {
    }

    /**
	 * @param message
	 */
    public NotFoundServices(String message) {
        super(message);
    }

    /**
	 * @param cause
	 */
    public NotFoundServices(Throwable cause) {
        super(cause);
    }

    /**
	 * @param message
	 * @param cause
	 */
    public NotFoundServices(String message, Throwable cause) {
        super(message, cause);
    }

    /**
	 * @param request
	 */
    public NotFoundServices(APIFunction request) {
        super("Couldn't find some Service(s) for request:\n" + request);
    }

    /**
	 * @param request
	 * @param missingServices
	 */
    public NotFoundServices(APIFunction request, List<Class<?>> missingServices) {
        super("\nMissing Service(s): " + missingServices + "\nfor request:" + request);
    }

    @Override
    public Object getResponse() throws ResponseException {
        throw this;
    }
}
