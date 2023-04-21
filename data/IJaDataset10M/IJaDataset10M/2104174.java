package jhomenet.commons.auth;

import java.io.Serializable;
import java.util.Date;

/**
 * TODO: Class description.
 *
 * @author Dave Irwin (jhomenet at gmail dot com)
 */
public final class UserStatus implements Serializable {

    /**
	 * Reference to the actual user object.
	 */
    private final User user;

    /**
	 * The login timestamp.
	 */
    private final Date loginTimestamp;

    /**
	 * 
	 */
    private final Long loginId;

    /**
	 * Constructor.
	 * 
	 * @param user
	 * @param authState
	 */
    public UserStatus(User user, Long loginId) {
        super();
        if (user == null) throw new IllegalArgumentException("User cannot be null!");
        this.user = user;
        this.loginId = loginId;
        this.loginTimestamp = new Date(System.currentTimeMillis());
    }

    /**
	 * @return the user
	 */
    public final User getUser() {
        return user;
    }

    /**
	 * @return the loginId
	 */
    public final Long getLoginId() {
        return loginId;
    }

    /**
	 * @return the loginTimestamp
	 */
    public final Date getLoginTimestamp() {
        return loginTimestamp;
    }
}
