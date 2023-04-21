package pl.mn.communicator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The class represents Gadu-Gadu user.
 * 
 * @author <a href="mailto:mnaglik@gazeta.pl">Marcin Naglik</a>
 * @author <a href="mailto:mati@sz.home.pl">Mateusz Szczap</a>
 * @version $Id: User.java,v 1.1 2005/11/05 23:34:52 winnetou25 Exp $
 */
public class User implements IUser {

    private static final Log LOGGER = LogFactory.getLog(User.class);

    private int m_uin = -1;

    private UserMode m_userMode = null;

    public User(int uin) {
        this(uin, UserMode.BUDDY);
    }

    public User(int uin, UserMode userMode) {
        if (uin < 0) throw new IllegalArgumentException("uin cannot be less than 0");
        if (userMode == null) throw new NullPointerException("userMode cannot be null");
        m_uin = uin;
        m_userMode = userMode;
    }

    public int getUin() {
        return m_uin;
    }

    /**
	 * @see pl.mn.communicator.IUser#getUserMode()
	 */
    public UserMode getUserMode() {
        return m_userMode;
    }

    public void setUserMode(UserMode userMode) {
        if (userMode == null) throw new NullPointerException("userMode cannot be null");
        m_userMode = userMode;
    }

    /**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
    public boolean equals(Object o) {
        if (o instanceof IUser) {
            IUser user = (IUser) o;
            if (user.getUin() == m_uin) return true;
        }
        return false;
    }

    /**
	 * @see java.lang.Object#hashCode()
	 */
    public int hashCode() {
        return m_uin;
    }

    /**
	 * @see java.lang.Object#toString()
	 */
    public String toString() {
        return "[Uin: " + m_uin + ", userMode: " + m_userMode + "]";
    }

    public static class UserMode {

        private String m_type = null;

        private UserMode(String type) {
            m_type = type;
        }

        public static final UserMode BUDDY = new UserMode("user_mode_buddy");

        public static final UserMode FRIEND = new UserMode("user_mode_friend");

        public static final UserMode BLOCKED = new UserMode("user_mode_blocked");

        public static final UserMode UNKNOWN = new UserMode("user_mode_unknown");

        /**
		 * @see java.lang.Object#toString()
		 */
        public String toString() {
            return m_type;
        }

        /**
		 * @see java.lang.Object#hashCode()
		 */
        public int hashCode() {
            return (m_type.hashCode() * 37);
        }
    }
}
