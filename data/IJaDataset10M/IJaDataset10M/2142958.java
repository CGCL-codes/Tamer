package org.kablink.teaming.module.admin;

import org.kablink.teaming.exception.UncheckedCodedException;
import org.kablink.util.api.ApiErrorCode;

public class ManageIndexException extends UncheckedCodedException {

    public ManageIndexException(String errorCode) {
        super(errorCode);
    }

    public ManageIndexException(String errorCode, Object[] errorArgs, Throwable cause) {
        super(errorCode, errorArgs, cause);
    }

    public int getHttpStatusCode() {
        return 500;
    }

    @Override
    public ApiErrorCode getApiErrorCode() {
        return ApiErrorCode.INDEX_MGT_ERROR;
    }
}
