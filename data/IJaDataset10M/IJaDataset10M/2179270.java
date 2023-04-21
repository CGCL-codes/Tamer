package org.kablink.teaming.fi;

import org.kablink.util.api.ApiErrorCode;

public class ReadOnlyException extends FIException {

    private static final long serialVersionUID = 1L;

    private static final String ERROR_CODE = "fi.error.read.only";

    public ReadOnlyException(String driverTitle, String operationName) {
        super(ERROR_CODE, operationName, driverTitle);
    }

    public int getHttpStatusCode() {
        return 403;
    }

    @Override
    public ApiErrorCode getApiErrorCode() {
        return ApiErrorCode.MIRRORED_READONLY_DRIVER;
    }
}
