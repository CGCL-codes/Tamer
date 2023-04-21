package openrpg2.common.core.group;

/**
 *
 * @author Snowdog
 */
public class Role implements MemberRole {

    /** ROLE_GHOST is a client who is invisible and cannot interact with others */
    public static final int ROLE_GHOST = 0;

    public static final String ROLESTRING_GHOST = "Ghost";

    /** ROLE_LURKER is the default role for a client (limited permissions and access to features)*/
    public static final int ROLE_LURKER = 1;

    public static final String ROLESTRING_LURKER = "Lurker";

    /** ROLE_PLAYER is a client who has access to player resources */
    public static final int ROLE_PLAYER = 2;

    public static final String ROLESTRING_PLAYER = "Player";

    /** ROLE_GM is a client who has access privliges to features to administer a gaming session/room */
    public static final int ROLE_GM = 3;

    public static final String ROLESTRING_GM = "GM";

    public static final int DEFAULT_ROLE = ROLE_LURKER;

    public static final String DEFAULT_ROLESTRING = ROLESTRING_LURKER;

    private int role = ROLE_LURKER;

    /** Creates a new instance of Role */
    public Role() {
        role = DEFAULT_ROLE;
    }

    public Role(int roleType) {
        role = roleType;
    }

    public void makePlayer() {
        role = ROLE_PLAYER;
    }

    public void makeGhost() {
        role = ROLE_GHOST;
    }

    public void makeLurker() {
        role = ROLE_LURKER;
    }

    public void makeGM() {
        role = ROLE_GM;
    }

    public int getRole() {
        return role;
    }

    public boolean equals(int roleType) {
        if (role == roleType) {
            return true;
        }
        return false;
    }

    public boolean isBelow(int roleType) {
        if (role < roleType) {
            return true;
        }
        return false;
    }

    public boolean isAbove(int roleType) {
        if (role > roleType) {
            return true;
        }
        return false;
    }

    public boolean equals(Role roleType) {
        return roleType.equals(role);
    }

    public boolean isBelow(Role roleType) {
        return !roleType.isBelow(role);
    }

    public boolean isAbove(Role roleType) {
        return !roleType.isAbove(role);
    }

    public boolean isGhost() {
        if (role == ROLE_GHOST) {
            return true;
        }
        return false;
    }

    public boolean isLurker() {
        if (role == ROLE_LURKER) {
            return true;
        }
        return false;
    }

    public boolean isPlayer() {
        if (role == ROLE_PLAYER) {
            return true;
        }
        return false;
    }

    public boolean isGM() {
        if (role == ROLE_GM) {
            return true;
        }
        return false;
    }

    public String getRoleString() {
        try {
            return getRoleString(role);
        } catch (InvalidRoleException e) {
        }
        return DEFAULT_ROLESTRING;
    }

    public static String getRoleString(int roleType) throws InvalidRoleException {
        switch(roleType) {
            case (ROLE_GHOST):
                {
                    return ROLESTRING_GHOST;
                }
            case (ROLE_LURKER):
                {
                    return ROLESTRING_LURKER;
                }
            case (ROLE_PLAYER):
                {
                    return ROLESTRING_PLAYER;
                }
            case (ROLE_GM):
                {
                    return ROLESTRING_GM;
                }
            default:
                {
                    throw new InvalidRoleException("No such role (" + roleType + ")");
                }
        }
    }
}
