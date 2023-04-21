package org.subrecord.api.exception;

import org.apache.commons.httpclient.HttpMethod;
import org.subrecord.Constants;

/**
 * @author przemek
 * 
 */
public class SubRecordApiException extends RuntimeException {

    public SubRecordApiException(String msg) {
        super(msg);
    }

    /**
	 * @param e
	 */
    public SubRecordApiException(Exception e) {
        super(e);
    }

    /**
	 * @param response
	 */
    public SubRecordApiException(HttpMethod response) {
        super("Status=" + response.getStatusCode() + ", " + response.getResponseHeader(Constants.ERROR));
    }
}
