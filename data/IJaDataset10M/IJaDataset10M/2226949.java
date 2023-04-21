package ch.articlefox;

/**
 * Constants used when working with a session.
 * 
 * @author Lukas Blunschi
 */
public interface SessionConstants {

    /**
	 * Attribute name of user ID of logged in user.
	 * <p>
	 * Value: Integer.
	 */
    public static final String A_USERID = "userid";

    /**
	 * Attribute name of language ID.
	 * <p>
	 * Value: String representation of an Integer.
	 */
    public static final String A_LANGUAGE = "language";

    /**
	 * Attribute name of last selected date.
	 * <p>
	 * Value: String in the format yyyy-MM-dd.
	 */
    public static final String A_DATE = "date";

    public static final String A_CATEGORY = "category";

    public static final String A_MESSAGE = "msg";
}
