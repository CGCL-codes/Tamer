package com.dcivision.user.web;

import java.sql.Connection;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.dcivision.framework.ApplicationException;
import com.dcivision.framework.ErrorConstant;
import com.dcivision.framework.SessionContainer;
import com.dcivision.framework.SystemFunctionConstant;
import com.dcivision.framework.Utility;
import com.dcivision.framework.web.AbstractActionForm;
import com.dcivision.framework.web.AbstractMaintAction;
import com.dcivision.user.bean.MtmUserGroupUserRole;
import com.dcivision.user.bean.UserMember;
import com.dcivision.user.dao.MtmUserGroupUserRoleDAObject;
import com.dcivision.user.dao.MtmUserRecordUserGroupDAObject;
import com.dcivision.user.dao.UserRoleDAObject;
import com.dcivision.workflow.dao.MtmWorkflowDynamicUserActorDAObject;
import com.dcivision.workflow.dao.MtmWorkflowStepUserActorDAObject;

public class MaintUserGroupAction extends AbstractMaintAction {

    public static final String REVISION = "$Revision: 1.13 $";

    public MaintUserGroupAction() {
        super();
    }

    /**
   * getMajorDAOClassName
   *
   * @return  The class name of the major DAObject will be used in this action.
   */
    public String getMajorDAOClassName() {
        return ("com.dcivision.user.dao.UserGroupDAObject");
    }

    /**
   * getFunctionCode
   *
   * @return  The corresponding system function code of action.
   */
    public String getFunctionCode() {
        return (SystemFunctionConstant.SETUP_USER_GROUP);
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException {
        UserRoleDAObject userRoleDAO = new UserRoleDAObject(this.getSessionContainer(request), this.getConnection(request));
        request.setAttribute("allRoleList", userRoleDAO.getFullList());
        return super.execute(mapping, form, request, response);
    }

    public void selectRecord(ActionMapping mapping, AbstractActionForm form, HttpServletRequest request, HttpServletResponse response) throws ApplicationException {
        super.selectRecord(mapping, form, request, response);
        MaintUserGroupForm userGroupForm = (MaintUserGroupForm) form;
        UserRoleDAObject userRoleDAO = new UserRoleDAObject(this.getSessionContainer(request), this.getConnection(request));
        List listRole = userRoleDAO.getListByUserGroupID(new Integer(userGroupForm.getID()));
        try {
            Integer[] iaRoleID = (Integer[]) Utility.getPropertyArray(listRole, "ID", new Integer[0]);
            String[] saRoleID = Utility.getStringArray(iaRoleID);
            userGroupForm.setUserRoles(saRoleID);
        } catch (Exception e) {
            log.error("Retrieve Role ID list.", e);
            throw new ApplicationException(ErrorConstant.COMMON_FATAL_ERROR, e);
        }
    }

    public void insertRecord(ActionMapping mapping, AbstractActionForm form, HttpServletRequest request, HttpServletResponse response) throws ApplicationException {
        super.insertRecord(mapping, form, request, response);
        MaintUserGroupForm userGroupForm = (MaintUserGroupForm) form;
        Connection connection = this.getConnection(request);
        SessionContainer sessionContainer = this.getSessionContainer(request);
        insertMtmGroupRole(userGroupForm, connection, sessionContainer);
    }

    /**
 * update the Mtm UserGroupUserRoleObject whthout request
 * @param userGroupForm
 * @param connection
 * @param sessionContainer
 * @throws ApplicationException
 */
    public void insertMtmGroupRole(MaintUserGroupForm userGroupForm, Connection connection, SessionContainer sessionContainer) throws ApplicationException {
        String[] userRoles = userGroupForm.getUserRoles();
        if (!Utility.isEmpty(userRoles)) {
            MtmUserGroupUserRoleDAObject mtmUserGroupUserRoleDAO = new MtmUserGroupUserRoleDAObject(sessionContainer, connection);
            for (int i = 0; i < userRoles.length; i++) {
                MtmUserGroupUserRole mtmRoleBean = new MtmUserGroupUserRole();
                mtmRoleBean.setUserGroupID(new Integer(userGroupForm.getID()));
                mtmRoleBean.setUserRoleID(new Integer(userRoles[i]));
                mtmUserGroupUserRoleDAO.insertObject(mtmRoleBean);
            }
        }
    }

    public void updateRecord(ActionMapping mapping, AbstractActionForm form, HttpServletRequest request, HttpServletResponse response) throws ApplicationException {
        super.updateRecord(mapping, form, request, response);
        MtmUserGroupUserRoleDAObject mtmUserGroupUserRoleDAO = new MtmUserGroupUserRoleDAObject(this.getSessionContainer(request), this.getConnection(request));
        updateMtmGroupRole(form, mtmUserGroupUserRoleDAO);
    }

    public void updateMtmGroupRole(AbstractActionForm form, MtmUserGroupUserRoleDAObject mtmUserGroupUserRoleDAO) throws ApplicationException {
        MaintUserGroupForm userGroupForm = (MaintUserGroupForm) form;
        Integer userGroupID = new Integer(userGroupForm.getID());
        mtmUserGroupUserRoleDAO.deleteListByUserGroupID(userGroupID);
        if (!Utility.isEmpty(userGroupForm.getUserRoles())) {
            for (int i = 0; i < userGroupForm.getUserRoles().length; i++) {
                MtmUserGroupUserRole mtmRoleBean = new MtmUserGroupUserRole();
                mtmRoleBean.setUserGroupID(new Integer(userGroupForm.getID()));
                mtmRoleBean.setUserRoleID(new Integer(userGroupForm.getUserRoles()[i]));
                mtmUserGroupUserRoleDAO.insertObject(mtmRoleBean);
            }
        }
    }

    public void deleteRecord(ActionMapping mapping, AbstractActionForm form, HttpServletRequest request, HttpServletResponse response) throws ApplicationException {
        MaintUserGroupForm userGroupForm = (MaintUserGroupForm) form;
        String SuserGroupID = userGroupForm.getID();
        if (!Utility.isEmpty(SuserGroupID)) {
            Integer userGroupID = new Integer(SuserGroupID);
            MtmWorkflowStepUserActorDAObject mtmWorkflowStepActorDAO = new MtmWorkflowStepUserActorDAObject(this.getSessionContainer(request), this.getConnection(request));
            MtmWorkflowDynamicUserActorDAObject dynamicUserActorDAO = new MtmWorkflowDynamicUserActorDAObject(this.getSessionContainer(request), this.getConnection(request));
            List userGroupList = mtmWorkflowStepActorDAO.getListWorkflowStepIDByActorIDAndActorType(userGroupID, UserMember.MEMBER_TYPE_GROUP);
            List dynamicUserGroupList = dynamicUserActorDAO.getListWorkflowStepIDByActorIDAndActorType(userGroupID, UserMember.MEMBER_TYPE_GROUP);
            if (Utility.isEmpty(userGroupList) && Utility.isEmpty(dynamicUserGroupList)) {
                super.deleteRecord(mapping, form, request, response);
                MtmUserGroupUserRoleDAObject mtmUserGroupUserRoleDAO = new MtmUserGroupUserRoleDAObject(this.getSessionContainer(request), this.getConnection(request));
                mtmUserGroupUserRoleDAO.deleteListByUserGroupID(userGroupID);
                MtmUserRecordUserGroupDAObject mtmUserRecordUserGroupDAO = new MtmUserRecordUserGroupDAObject(this.getSessionContainer(request), this.getConnection(request));
                mtmUserRecordUserGroupDAO.deleteListByUserGroupID(userGroupID);
            } else {
                throw new ApplicationException("common.message.cannot_deleted_record_operation_aborted");
            }
        } else {
            throw new ApplicationException("common.message.cannot_deleted_record_operation_aborted");
        }
    }
}
