package org.kablink.teaming.domain;

import org.kablink.teaming.NoObjectByTheIdException;
import org.kablink.util.api.ApiErrorCode;

/**
 * @author jong
 *
 */
public class NoRoleConditionByTheIdException extends NoObjectByTheIdException {

    private static final long serialVersionUID = 1L;

    private static final String errorCode = "errorcode.no.condition.by.the.id";

    public NoRoleConditionByTheIdException(Long roleConditionId) {
        super(errorCode, roleConditionId);
    }

    @Override
    public ApiErrorCode getApiErrorCode() {
        return ApiErrorCode.ROLE_CONDITION_NOT_FOUND;
    }
}
