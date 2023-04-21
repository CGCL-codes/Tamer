package com.jcorporate.eforum.dbobj;

import com.jcorporate.expresso.core.db.DBException;
import com.jcorporate.expresso.core.dbobj.SecuredDBObject;
import com.jcorporate.expresso.core.dbobj.DBField;

/**
 * @author Luka Pavli� (luka.pavlic@cit.si)
 *
 * Created 2004.7.13 14:34:55
 * 
 * Language description:
 * 	Projects repository
 * 
 */
public class ProjectData extends SecuredDBObject {

    public static final String thisClass = ProjectData.class.getName();

    public static String TABLE_NAME = "ePj_forum_pDT";

    public static String TABLE_DESCRIPTION = "eForumProjectDataTable";

    /**
	 * @throws DBException
	 */
    public ProjectData() throws DBException {
        super();
    }

    public static final String FLD_PID = "ProjectID";

    public static final String FLD_ENABLED = "Enabled";

    public static final String FLD_TYPE = "Type";

    /**
	 * @see com.jcorporate.expresso.core.dbobj.DBObject#setupFields()
	 */
    public void setupFields() throws DBException {
        setTargetTable(TABLE_NAME);
        setDescription(TABLE_DESCRIPTION);
        setCharset("utf-8");
        addField(FLD_PID, DBField.INT_TYPE, 0, false, "eForumProjectDataProjectID");
        addField(FLD_ENABLED, DBField.BOOLEAN_TYPE, 0, false, "eForumProjectDataEnabled");
        addField(FLD_TYPE, DBField.CHAR_TYPE, 3, false, "eForumProjectDataType");
        addKey("ProjectID");
    }

    /**
	 * Get project
	 * @param PID ID
	 * @return ProjectData object
	 * @throws DBException
	 */
    public static ProjectData getProjectData(int PID) throws DBException {
        ProjectData pd = new ProjectData();
        pd.setField(FLD_PID, PID);
        return (ProjectData) pd.searchAndRetrieveList().get(0);
    }

    /**
	 * Is project enabled?
	 * @return true if enabled
	 * @throws DBException
	 */
    public boolean isProjectEnabled() throws DBException {
        return getFieldBoolean(FLD_ENABLED);
    }

    /**
	 * Is project Public?
	 * @return true if public
	 * @throws DBException
	 */
    public boolean isProjectTypePublic() throws DBException {
        return getField(FLD_TYPE).equals("PUB");
    }

    /**
	 * Is project Public for registered only?
	 * @return true if reg.only public
	 * @throws DBException
	 */
    public boolean isProjectTypeRegistered() throws DBException {
        return getField(FLD_TYPE).equals("REG");
    }

    /**
	 * Is project environment private?
	 * @return true if private
	 * @throws DBException
	 */
    public boolean isProjectTypePrivate() throws DBException {
        return getField(FLD_TYPE).equals("PRI");
    }

    /**
	 * Is given user leader in this project?
	 * @param UID User ID
	 * @return true if leader, false if not
	 * @throws DBException
	 */
    public boolean isLeader(int UID) throws DBException {
        AllowedUser au = new AllowedUser();
        au.setField(AllowedUser.FLD_PID, getFieldInt(FLD_PID));
        au.setField(AllowedUser.FLD_UID, UID);
        if (au.find()) return (au.getField(AllowedUser.FLD_GROUP).equals("PVN_LEADER"));
        return false;
    }

    /**
	 * Is given user member in this project?
	 * @param UID User ID
	 * @return true if member, false if not
	 * @throws DBException
	 */
    public boolean isMember(int UID) throws DBException {
        AllowedUser au = new AllowedUser();
        au.setField(AllowedUser.FLD_PID, getFieldInt(FLD_PID));
        au.setField(AllowedUser.FLD_UID, UID);
        if (au.find()) return (au.getField(AllowedUser.FLD_GROUP).equals("PVN_MEMBER"));
        return false;
    }

    /**
	 * Is given user observer in this project?
	 * @param UID User ID
	 * @return true if observer, false if not
	 * @throws DBException
	 */
    public boolean isObserver(int UID) throws DBException {
        AllowedUser au = new AllowedUser();
        au.setField(AllowedUser.FLD_PID, getFieldInt(FLD_PID));
        au.setField(AllowedUser.FLD_UID, UID);
        if (au.find()) return (au.getField(AllowedUser.FLD_GROUP).equals("PVN_OBSRVR"));
        return false;
    }
}
