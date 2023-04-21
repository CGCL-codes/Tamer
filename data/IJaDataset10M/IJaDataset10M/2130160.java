package org.epo.jdist.dmsinterface;

/**
 * Class containing constants related to DMS
 * Creation date: 06/2004
 * @author Infotel Conseil
 */
public final class DMSConstants {

    /**
	* Team
	*/
    public static final String TEAM = "T";

    /**
	* Member
	*/
    public static final String MEMBER = "M";

    /**
	* User
	*/
    public static final String USER = "U";

    /**
	* Owner
	*/
    public static final String OWNER = "O";

    /**
	* Permanent ownership feature
	*/
    public static final String PERMANENT = "P";

    /**
	* Temporary ownership feature
	*/
    public static final String TEMPORARY = "T";

    public static final String PERMANENT_TEAM = PERMANENT + TEAM;

    public static final String PERMANENT_OWNER_TEAM = PERMANENT + OWNER + TEAM;

    public static final String PERMANENT_MEMBER = PERMANENT + MEMBER;

    public static final String PERMANENT_OWNER_MEMBER = PERMANENT + OWNER + MEMBER;

    public static final String PERMANENT_MEMBER_USER = PERMANENT + USER;

    public static final String TEMPORARY_TEAM = TEMPORARY + TEAM;

    public static final String TEMPORARY_OWNER_TEAM = TEMPORARY + OWNER + TEAM;

    public static final String TEMPORARY_MEMBER = TEMPORARY + MEMBER;

    public static final String TEMPORARY_OWNER_MEMBER = TEMPORARY + OWNER + MEMBER;

    public static final String TEMPORARY_MEMBER_USER = TEMPORARY + USER;
}
