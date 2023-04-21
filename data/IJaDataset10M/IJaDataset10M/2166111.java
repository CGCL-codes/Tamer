package systems.dispatcher;

import infrastructureAPI.APIResponse;
import infrastructureAPI.ResponseException;

/**
 * Description: 
 *
 */
public class RequestAborted extends ResponseException implements APIResponse {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public RequestAborted() {
    }

    /**
	 * @param message
	 */
    public RequestAborted(String message) {
        super(message);
    }

    /**
	 * @param cause
	 */
    public RequestAborted(Throwable cause) {
        super(cause);
    }

    /**
	 * @param message
	 * @param cause
	 */
    public RequestAborted(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public Object getResponse() throws ResponseException {
        throw this;
    }
}
