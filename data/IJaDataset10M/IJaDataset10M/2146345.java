package com.jvantage.ce.acl;

/**
 *
 * @author  Brent Clay
 */
public class ACLConstants {

    public static final int NO_ACCESS = 0;

    public static final int CREATE_ACCESS = 1;

    public static final int READ_ACCESS = 2;

    public static final int UPDATE_ACCESS = 4;

    public static final int DELETE_ACCESS = 8;

    public static final int LIST_ACCESS = 16;

    public static final int ALL_ACCESS = CREATE_ACCESS | READ_ACCESS | UPDATE_ACCESS | DELETE_ACCESS | LIST_ACCESS;

    public static final String NO_ACCESS_STRING = "NO ACCESS";

    public static final String CREATE_ACCESS_STRING = "CREATE";

    public static final String READ_ACCESS_STRING = "READ";

    public static final String UPDATE_ACCESS_STRING = "UPDATE";

    public static final String DELETE_ACCESS_STRING = "DELETE";

    public static final String LIST_ACCESS_STRING = "LIST";

    public static final int GRANT = 1;

    public static final int REVOKE = 2;

    public static final String GRANT_STRING = "GRANT";

    public static final String REVOKE_STRING = "REVOKE";

    public static final String ALL_GROUPS = "ALL GROUPS";

    public static final ACLGroup OWNER_GROUP = new ACLGroup(ACLGroupEnum.Owner.getName());

    /**
     *  The immutable name of the ADMIN group.  A group is a collection of users.
     */
    public static final ACLGroup ADMIN_GROUP = new ACLGroup(ACLGroupEnum.Admin.getName());

    /**
     *  The immutable name of the GUEST group.  A group is a collection of users.
     */
    public static final ACLGroup GUEST_GROUP = new ACLGroup(ACLGroupEnum.Guest.getName());

    /** Creates a new instance of ACLConstants */
    public ACLConstants() {
    }
}
