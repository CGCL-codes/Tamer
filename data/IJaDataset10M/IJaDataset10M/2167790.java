package org.kablink.teaming.util;

import org.kablink.util.VibeRuntimeException;
import org.kablink.util.api.ApiErrorCode;

public class ThumbnailException extends VibeRuntimeException {

    public ThumbnailException() {
        super();
    }

    public ThumbnailException(String message) {
        super(message);
    }

    public ThumbnailException(String message, Throwable cause) {
        super(message, cause);
    }

    public ThumbnailException(Throwable cause) {
        super(cause);
    }

    @Override
    public int getHttpStatusCode() {
        return 500;
    }

    @Override
    public ApiErrorCode getApiErrorCode() {
        return ApiErrorCode.DOC_CONVERSION_ERROR;
    }
}
