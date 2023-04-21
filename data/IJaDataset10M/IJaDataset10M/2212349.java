package pl.radical.open.gg;

/**
 * The class represents Gadu-Gadu user.
 * 
 * @author <a href="mailto:mnaglik@gazeta.pl">Marcin Naglik</a>
 * @author <a href="mailto:mati@sz.home.pl">Mateusz Szczap</a>
 */
public class User implements IUser {

    private int uin = -1;

    private UserMode userMode = null;

    public User(final int uin) {
        this(uin, UserMode.BUDDY);
    }

    public User(final int uin, final UserMode userMode) {
        if (uin < 0) {
            throw new IllegalArgumentException("uin cannot be less than 0");
        }
        if (userMode == null) {
            throw new IllegalArgumentException("userMode cannot be null");
        }
        this.uin = uin;
        this.userMode = userMode;
    }

    public int getUin() {
        return uin;
    }

    /**
	 * @see pl.radical.open.gg.IUser#getUserMode()
	 */
    public UserMode getUserMode() {
        return userMode;
    }

    public void setUserMode(final UserMode userMode) {
        if (userMode == null) {
            throw new IllegalArgumentException("userMode cannot be null");
        }
        this.userMode = userMode;
    }

    /**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
    @Override
    public boolean equals(final Object o) {
        if (o instanceof IUser) {
            final IUser user = (IUser) o;
            if (user.getUin() == uin) {
                return true;
            }
        }
        return false;
    }

    /**
	 * @see java.lang.Object#hashCode()
	 */
    @Override
    public int hashCode() {
        return uin;
    }

    /**
	 * @see java.lang.Object#toString()
	 */
    @Override
    public String toString() {
        return "[Uin: " + uin + ", userMode: " + userMode + "]";
    }

    public static class UserMode {

        private String type = null;

        private UserMode(final String type) {
            this.type = type;
        }

        public static final UserMode BUDDY = new UserMode("user_mode_buddy");

        public static final UserMode FRIEND = new UserMode("user_mode_friend");

        public static final UserMode BLOCKED = new UserMode("user_mode_blocked");

        public static final UserMode UNKNOWN = new UserMode("user_mode_unknown");

        /**
		 * @see java.lang.Object#toString()
		 */
        @Override
        public String toString() {
            return type;
        }

        /**
		 * @see java.lang.Object#hashCode()
		 */
        @Override
        public int hashCode() {
            return type.hashCode() * 37;
        }
    }
}
