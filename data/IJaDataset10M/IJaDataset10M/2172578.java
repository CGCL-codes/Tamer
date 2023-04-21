package org.kablink.teaming;

import org.kablink.util.api.ApiErrorCode;

/**
 * @author jong
 *
 */
public class ApplicationExistsException extends ObjectExistsException {

    private static final String errorCode = "errorcode.application.exists";

    public ApplicationExistsException() {
        super(errorCode);
    }

    public ApplicationExistsException(Throwable cause) {
        super(errorCode, null, cause);
    }

    public ApplicationExistsException(Object[] errorArgs, Throwable cause) {
        super(errorCode, errorArgs, cause);
    }

    @Override
    public ApiErrorCode getApiErrorCode() {
        return ApiErrorCode.APPLICATION_EXISTS;
    }
}
