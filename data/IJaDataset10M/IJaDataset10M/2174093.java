package common.elearning.courses.event.request;

import common.elearning.courses.event.CourseElearningEvent;
import common.elearning.event.EventsHierarchy;

/**
 * The Class MailLoginEvent.
 */
public class LoginEvent extends CourseElearningEvent {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /** The user. */
    private String user;

    /** The password. */
    private String password;

    /**
	 * Instantiates a new mail login event.
	 * 
	 * @param user
	 *            the user
	 * @param password
	 *            the password
	 */
    public LoginEvent(String user, String password) {
        this.user = user;
        this.password = password;
    }

    /**
	 * Gets the id.
	 * 
	 * @return the id
	 */
    @Override
    public long getId() {
        return EventsHierarchy.EVENT_REQUEST_COURSE_LOGIN_EVENT;
    }

    /**
	 * Gets the user.
	 * 
	 * @return the user
	 */
    public String getUser() {
        return user;
    }

    /**
	 * Gets the password.
	 * 
	 * @return the password
	 */
    public String getPassword() {
        return password;
    }
}
