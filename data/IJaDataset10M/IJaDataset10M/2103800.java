package com.restfb.exception;

import static java.lang.String.format;

/**
 * Indicates that the Facebook Graph API endpoint returned JSON which indicates
 * an error condition.
 * <p>
 * Example:<code>
  {
      "error": {
        "type": "Exception",
        "message": "..."
      }
  } </code>
 * 
 * @author <a href="http://restfb.com">Mark Allen</a>
 * @since 1.5
 */
public class FacebookGraphException extends FacebookException {

    /**
   * The Facebook Graph API error type.
   */
    private String errorType;

    /**
   * The Facebook API error message.
   */
    private String errorMessage;

    private static final long serialVersionUID = 1L;

    /**
   * Creates an exception with the given error type and message.
   * 
   * @param errorType
   *          Value of the Facebook response attribute {@code error.type}.
   * @param errorMessage
   *          Value of the Facebook response attribute {@code error.message}.
   */
    public FacebookGraphException(String errorType, String errorMessage) {
        super(format("Received Facebook error response of type %s: %s", errorType, errorMessage));
        this.errorType = errorType;
        this.errorMessage = errorMessage;
    }

    /**
   * Gets the Facebook Graph API error type.
   * 
   * @return The Facebook Graph API error type.
   */
    public String getErrorType() {
        return errorType;
    }

    /**
   * Gets the Facebook Graph API error message.
   * 
   * @return The Facebook Graph API error message.
   */
    public String getErrorMessage() {
        return errorMessage;
    }
}
