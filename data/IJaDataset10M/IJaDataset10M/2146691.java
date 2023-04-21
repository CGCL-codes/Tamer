package org.kablink.teaming.domain;

import org.kablink.teaming.NoObjectByTheIdException;
import org.kablink.util.api.ApiErrorCode;

/**
 * @author jong
 *
 */
public class NoZoneByTheIdException extends NoObjectByTheIdException {

    private static final long serialVersionUID = 1L;

    private static final String errorCode = "errorcode.no.zone.by.the.id";

    public NoZoneByTheIdException(Long zoneId) {
        super(errorCode, zoneId);
    }

    @Override
    public ApiErrorCode getApiErrorCode() {
        return ApiErrorCode.ZONE_NOT_FOUND;
    }
}
