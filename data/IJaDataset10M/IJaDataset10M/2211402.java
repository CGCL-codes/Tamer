package scam.share;

import java.io.*;
import java.util.*;

/**
 * Represents exceptions while processing User methods.
 * @author J�ran Stark
 * @author Jan Danils
 * @version $version
 */
public class UserException extends Exception implements Serializable {

    /** Object caussing the exception */
    private User iUser = null;

    public static final String S_INVALID_USERNAME = "Wrong username";

    public static final String S_INVALID_PASSWORD = "Wrong password";

    public static final UserException INVALID_USERNAME = new UserException(S_INVALID_USERNAME);

    public static final UserException INVALID_PASSWORD = new UserException(S_INVALID_PASSWORD);

    /**
     * Constructs an UserException with no specified message.
     */
    public UserException() {
        super();
    }

    /**
     * Constructs an UserException with the specified detail message.
     * @param mess the detail message.
     */
    public UserException(String mess) {
        super(mess);
    }

    /**
     * Constructs an UserException with the specified detail message and user
     * causing the exception.
     * @param mess the detail message.
     * @param user the user causing the exception.
     */
    public UserException(String mess, User user) {
        super(mess);
        iUser = user;
    }

    /**
     * Returns the object causing the exception.
     * @return the object causing the exception.
     */
    public User user() {
        return iUser;
    }
}
