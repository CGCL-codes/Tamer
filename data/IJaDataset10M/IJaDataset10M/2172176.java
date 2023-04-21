package com.google.api.ads.dfp.lib;

import java.io.IOException;

/**
 * An exception thrown when the ClientLogin API was unable to successfully
 * generate a token.
 *
 * @author api.arogal@gmail.com (Adam Rogal)
 */
public class AuthTokenException extends Exception {

    private final Integer httpStatusCode;

    private final String httpResponseBody;

    private final String errorCode;

    private final CaptchaInformation captchaInfo;

    private final IOException ioException;

    /**
   * @param httpStatusCode the HTTP status code
   * @param httpResponseBody the HTTP response body
   * @param errorCode the client login error code if present
   * @param captchaInfo the captcha information if present
   * @param ioException the {@code IOException} that caused the authentication
   *     failure
   */
    public AuthTokenException(Integer httpStatusCode, String httpResponseBody, String errorCode, CaptchaInformation captchaInfo, IOException ioException) {
        super(ioException);
        this.httpStatusCode = httpStatusCode;
        this.httpResponseBody = httpResponseBody;
        this.errorCode = errorCode;
        this.captchaInfo = captchaInfo;
        this.ioException = ioException;
    }

    /**
   * @return the httpStatusCode the HTTP status code
   */
    public Integer getHttpStatusCode() {
        return httpStatusCode;
    }

    /**
   * @return the httpResponseBody the HTTP response body
   */
    public String getHttpResponseBody() {
        return httpResponseBody;
    }

    /**
   * @return the errorCode the client login error code if present.
   */
    public String getErrorCode() {
        return errorCode;
    }

    /**
   * @return the captchaInfo the captcha information if present.
   */
    public CaptchaInformation getCaptchaInfo() {
        return captchaInfo;
    }

    /**
   * @return the ioException the {@code IOException} that caused the failure
   */
    public IOException getIoException() {
        return ioException;
    }

    /**
   * @see java.lang.Throwable#getMessage()
   */
    @Override
    public String getMessage() {
        if (errorCode != null) {
            return "Auth token could not be retrieved because of error: " + errorCode;
        } else if (httpStatusCode != null) {
            return "Auth token could not be retrieved because of http status code: " + httpStatusCode;
        } else if (ioException != null) {
            return "Auth token could not be retrieved because of IOException: " + ioException.getMessage();
        } else {
            return "Auth token could not be retrieved because of unkown reasons";
        }
    }
}
