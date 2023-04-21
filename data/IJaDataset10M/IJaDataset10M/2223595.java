package test.com.dcivision.framework;

import java.sql.Connection;
import java.util.Hashtable;
import java.util.List;
import org.apache.struts.util.MessageResources;
import com.dcivision.framework.ApplicationException;
import com.dcivision.framework.DataSourceFactory;
import com.dcivision.framework.GlobalConstant;
import com.dcivision.framework.MessageResourcesFactory;
import com.dcivision.framework.SessionContainer;
import com.dcivision.framework.SystemParameterFactory;
import com.dcivision.framework.UserInfoFactory;
import com.dcivision.framework.Utility;
import com.dcivision.framework.dao.GlobalReferenceDAObject;
import com.dcivision.user.bean.UserGroup;
import com.dcivision.user.bean.UserRecord;
import com.dcivision.user.dao.UserGroupDAObject;
import com.dcivision.user.dao.UserRoleDAObject;
import com.dcivision.workflow.web.ListWorkflowProgressForm;
import com.dcivision.workflow.web.ListWorkflowRecordForm;

public class DcivisionManage {

    public void initSystemDAO(Connection connection) throws Exception {
        GlobalReferenceDAObject gloRefDAO = new GlobalReferenceDAObject(connection);
        Hashtable sysParaHash = gloRefDAO.getAllSysParameter();
        sysParaHash.putAll(Utility.parseResourceBundle(GlobalConstant.CONFIG_FILENAME));
        SystemParameterFactory.init(sysParaHash);
        Hashtable userInfoHash = gloRefDAO.getAllUserInfo();
        UserInfoFactory.init(userInfoHash);
        DcivisionDataSource ds = new DcivisionDataSource(DatabaseManage.getCurrentConnection());
        DataSourceFactory.init(ds, new DcivisionLog());
    }

    public static SessionContainer getSessionContainer(Connection connection, UserRecord userRecord) {
        SessionContainer sessionCtn = new SessionContainer();
        UserGroupDAObject userGroupDAO = new UserGroupDAObject(sessionCtn, connection);
        try {
            List userGroups = userGroupDAO.getListByUserRecordIDGroupType(userRecord.getID(), com.dcivision.user.bean.UserGroup.GROUP_TYPE_PUBLIC);
            sessionCtn.getPermissionManager().setUserGroups(userGroups);
            UserRoleDAObject userRoleDAO = new UserRoleDAObject(sessionCtn, connection);
            List userRoles = userRoleDAO.getListByUserRecordID(userRecord.getID());
            for (int i = 0; i < userGroups.size(); i++) {
                UserGroup tmpUserGroup = (UserGroup) userGroups.get(i);
                List userRolesByGroup = userRoleDAO.getListByUserGroupID(tmpUserGroup.getID());
                for (int j = 0; j < userRolesByGroup.size(); j++) {
                    if (!userRoles.contains(userRolesByGroup.get(j))) {
                        userRoles.add(userRolesByGroup.get(j));
                    }
                }
            }
            sessionCtn.getPermissionManager().setUserRoles(userRoles);
            sessionCtn.setUserRecord(userRecord);
        } catch (ApplicationException e) {
            e.printStackTrace();
        }
        return sessionCtn;
    }

    public ListWorkflowProgressForm getListWorkflowProgressForm() {
        ListWorkflowProgressForm searchForm = new ListWorkflowProgressForm();
        searchForm.setCurStartRowNo("1");
        searchForm.setPage(0);
        searchForm.setPageOffset("100");
        searchForm.setShowFullList("N");
        searchForm.setSortAttribute("UPDATE_DATE");
        searchForm.setSortOrder("DESC");
        return searchForm;
    }

    public ListWorkflowRecordForm getListWorkflowRecordForm() {
        ListWorkflowRecordForm searchForm = new ListWorkflowRecordForm();
        searchForm.setCurStartRowNo("1");
        searchForm.setPage(0);
        searchForm.setPageOffset("100");
        searchForm.setSortAttribute("WORKFLOW_CODE");
        searchForm.setSortOrder("DESC");
        return searchForm;
    }

    public UserRecord getUserRecord() {
        UserRecord userRecord = new UserRecord();
        userRecord.setID(new Integer("1"));
        userRecord.setFullName("samlin.zhang");
        return userRecord;
    }

    public void initMessageFactory(Connection connection) {
        DcivisionMessageResourcesFactory refactory = new DcivisionMessageResourcesFactory();
        MessageResources tt = new DcivisionMessageResources(refactory, "config");
        MessageResourcesFactory.init(tt, tt);
    }
}
