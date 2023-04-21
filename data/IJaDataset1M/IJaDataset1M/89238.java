package com.google.visualization.datasource.base;

/**
 * An exception to be used by callers and callees of the library.
 * Each exception has a type taken from <code>ReasonType</code>, and a
 * message that can be used to output an appropriate message to the user.
 *
 * Example:
 * new DataSourceException(ReasonType.InvalidQuery, "The query cannot be empty")
 *
 * @author Hillel M.
 */
public class DataSourceException extends Exception {

    /**
   * The reason for this exception. Used to set the reason type of this
   * execution response by the thrower of this exception.
   */
    private ReasonType reasonType;

    /**
   * The error message to return to the user.
   */
    private String messageToUser = null;

    /**
   * A private constructor to prevent using this exception with no message.
   */
    private DataSourceException() {
    }

    /**
   * Constructs a new exception with a single message for the user.
   *
   * @param reasonType The reason type of the exception.
   * @param messageToUser The message for the user.
   */
    public DataSourceException(ReasonType reasonType, String messageToUser) {
        super(messageToUser);
        this.messageToUser = messageToUser;
        this.reasonType = reasonType;
    }

    /**
   * Returns the message for the user.
   *
   * @return The message for the user.
   */
    public String getMessageToUser() {
        return messageToUser;
    }

    /**
   * Returns the reason type of this exception.
   *
   * @return The reason type of this exception.
   */
    public ReasonType getReasonType() {
        return reasonType;
    }

    @Override
    @Deprecated
    public String getMessage() {
        return super.getMessage();
    }
}
