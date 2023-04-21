package com.dcivision.user.web;

import java.sql.Connection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.dcivision.framework.ApplicationException;
import com.dcivision.framework.Crypt;
import com.dcivision.framework.ErrorConstant;
import com.dcivision.framework.GlobalConstant;
import com.dcivision.framework.MessageResourcesFactory;
import com.dcivision.framework.SessionContainer;
import com.dcivision.framework.SystemParameterConstant;
import com.dcivision.framework.SystemParameterFactory;
import com.dcivision.framework.Utility;
import com.dcivision.framework.web.AbstractActionForm;
import com.dcivision.framework.web.AbstractMaintAction;
import com.dcivision.user.UserHomePreferenceConstant;
import com.dcivision.user.bean.PersonalHomePreference;
import com.dcivision.user.bean.UserRecord;
import com.dcivision.user.core.PreferenceManager;
import com.dcivision.user.dao.PersonalHomePreferenceDAObject;
import com.dcivision.user.dao.UserRecordDAObject;

/**
  MaintPersonalHomePreferenceAction.java

  This class is for maint user role.

    @author          Tony Chen
    @company         DCIVision Limited
    @creation date   20/05/2004
    @version         $Revision: 1.8.2.6 $
*/
public class MaintPersonalHomePreferenceAction extends AbstractMaintAction {

    public static final String REVISION = "$Revision: 1.8.2.6 $";

    public MaintPersonalHomePreferenceAction() {
        super();
    }

    /**
   * getMajorDAOClassName
   *
   * @return  The class name of the major DAObject will be used in this action.
   */
    public String getMajorDAOClassName() {
        return ("com.dcivision.user.dao.PersonalHomePreferenceDAObject");
    }

    /**
   * getFunctionCode
   *
   * @return  The corresponding system function code of action.
   */
    public String getFunctionCode() {
        return (null);
    }

    /**
   * setPageTitle
   * set the extend page title and page path.
   * default page path/title will be created by navmode and functionCode
   */
    public void setPageTitle(HttpServletRequest request, HttpServletResponse response, ActionForm form, ActionMapping mapping, ActionForward actionForward) {
        String extendTitle = MessageResourcesFactory.getMessage(this.getSessionContainer(request).getSessionLocale(), "common.label.preferences");
        request.setAttribute(GlobalConstant.EXTEND_PAGE_TITLE, extendTitle);
        request.setAttribute(GlobalConstant.EXTEND_PAGE_TITLE_SHOW_ONLY, new Boolean(true));
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException {
        ActionForward forward = this.retrieveFunctionCode(request, response, mapping);
        if (forward != null) {
            return forward;
        }
        String opMode = request.getParameter("opMode");
        String navMode = request.getParameter("navMode");
        PreferenceManager preferenceMg = new PreferenceManager(this.getSessionContainer(request), this.getConnection(request));
        ((MaintPersonalHomePreferenceForm) form).setPreferenceMg(preferenceMg);
        if ((opMode == null && navMode == null) || (navMode == GlobalConstant.NAV_MODE_EDIT)) {
            this.selectForEditRecord(mapping, form, request, response);
            forward = mapping.findForward(GlobalConstant.NAV_MODE_EDIT);
        } else {
            forward = super.execute(mapping, form, request, response);
        }
        return forward;
    }

    /**
	* Insert a record into the CALENDAR_PERFERENCE table
	* @param mapping            ActionMapping
	* @param form               AbstractActionForm
	* @param request            HttpServletRequest
	* @param response           HttpServletResponse
	* @throws                   ApplicationException
	*/
    public void insertRecord(ActionMapping mapping, AbstractActionForm form, HttpServletRequest request, HttpServletResponse response) throws ApplicationException {
        MaintPersonalHomePreferenceForm maintForm = (MaintPersonalHomePreferenceForm) form;
        PersonalHomePreference personalPreference = (PersonalHomePreference) maintForm.getFormData();
        PersonalHomePreferenceDAObject personalPreferenceDAO = (PersonalHomePreferenceDAObject) super.getMajorDAObject(request);
        SessionContainer se = this.getSessionContainer(request);
        UserRecordDAObject userRecordDAO = new UserRecordDAObject(se, this.getConnection(request));
        Integer userRecordID = se.getUserRecordID();
        UserRecord userRecord = (UserRecord) userRecordDAO.getObjectByID(userRecordID);
        userRecord.setLoginPwd(null);
        if (!Utility.isEmpty(maintForm.getPreference())) {
            userRecord.setPreference(Integer.valueOf(maintForm.getPreference()));
        } else {
            userRecord.setPreference(Integer.valueOf(SystemParameterFactory.getSystemParameter(SystemParameterConstant.PREFERENCE)));
        }
        if (!Utility.isEmpty(maintForm.getLocale())) {
            userRecord.setLocale(maintForm.getLocale());
        } else {
            userRecord.setLocale(SystemParameterFactory.getSystemParameter(SystemParameterConstant.LOCALE));
        }
        userRecordDAO.updateObject(userRecordDAO.updateObject(userRecord));
        personalPreferenceDAO.deleteByUserRecordID(personalPreference);
        String disableChannel = maintForm.getDisableViewChannel();
        personalPreference = (PersonalHomePreference) personalPreferenceDAO.insertObject(personalPreference);
        maintForm.setFormData(personalPreference);
        maintForm.setLocale(userRecord.getLocale());
        maintForm.setPreference(userRecord.getPreference().toString());
        se.getUserRecord().setPreference(userRecord.getPreference());
        se.getUserRecord().setLocale(userRecord.getLocale());
        if (Utility.isEmpty(maintForm.getLocale())) {
            se.setLocale(Utility.getLocaleByString(SystemParameterFactory.getSystemParameter(SystemParameterConstant.LOCALE)));
        } else {
            se.setLocale(Utility.getLocaleByString(maintForm.getLocale()));
        }
        super.selectRecord(mapping, form, request, response);
        if (!StringUtils.isEmpty(request.getParameter("loginPwd"))) {
            this.updateUserRecord(mapping, form, request, response);
        }
    }

    /**
	* Select for the edit record
	* @param mapping          ActionMapping
	* @param form             ActionForm
	* @param request          HttpServletRequest
	* @param response         HttpServletResponse
	* @throws                 ApplicationException
	*/
    public void selectForEditRecord(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ApplicationException {
        MaintPersonalHomePreferenceForm maintForm = (MaintPersonalHomePreferenceForm) form;
        SessionContainer sessionCon = this.getSessionContainer(request);
        PersonalHomePreference personalPreference = null;
        PersonalHomePreferenceDAObject personalPreferenceDAO = (PersonalHomePreferenceDAObject) super.getMajorDAObject(request);
        personalPreference = (PersonalHomePreference) personalPreferenceDAO.getByObjectByUserRecordID(sessionCon.getUserRecordID());
        if (Utility.isEmpty(personalPreference)) {
            personalPreference = new PersonalHomePreference();
            personalPreference.setUserRecordID(sessionCon.getUserRecordID());
            personalPreference.setDisableViewChannel(UserHomePreferenceConstant.SYSTEMPREFERENCESTR);
        }
        ((MaintPersonalHomePreferenceForm) form).setFormData(personalPreference);
        if (Utility.isEmpty(sessionCon.getUserRecord().getLocale())) {
            ((MaintPersonalHomePreferenceForm) form).setLocale(SystemParameterFactory.getSystemParameter(SystemParameterConstant.LOCALE));
        } else {
            ((MaintPersonalHomePreferenceForm) form).setLocale(sessionCon.getUserRecord().getLocale());
        }
        if (Utility.isEmpty(sessionCon.getUserRecord().getPreference())) {
            ((MaintPersonalHomePreferenceForm) form).setPreference(SystemParameterFactory.getSystemParameter(SystemParameterConstant.PREFERENCE));
        } else {
            ((MaintPersonalHomePreferenceForm) form).setPreference("" + sessionCon.getUserRecord().getPreference());
        }
    }

    /**
   * select User Info
   *
   */
    private void selectUserRecord(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    }

    /**
   * update User Pwd info
   * @throws ApplicationException
   *
   */
    private void updateUserRecord(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ApplicationException {
        Connection conn = this.getConnection(request);
        SessionContainer sessionCtn = this.getSessionContainer(request);
        log.debug("updateRecord in maintUserPwd");
        UserRecordDAObject userRecordDAO = new UserRecordDAObject(sessionCtn, conn);
        UserRecord userRecord = (UserRecord) userRecordDAO.getObjectByID(sessionCtn.getUserRecordID());
        String sOrigPwd = request.getParameter("origPwd");
        String sNewPwd = request.getParameter("loginPwd");
        String encrptedPwd = Crypt.encrypt(sOrigPwd, SystemParameterFactory.getSystemParameter(SystemParameterConstant.CRYPTO_SALT));
        log.debug(userRecord.getLoginPwd() + "vvv" + encrptedPwd);
        if (!userRecord.getLoginPwd().equals(encrptedPwd) || Utility.isEmpty(encrptedPwd)) {
            throw new ApplicationException(ErrorConstant.LOGIN_ORIGINAL_PASSWORD_INCORRECT);
        } else {
            userRecord.setLoginPwd(sNewPwd);
            userRecord.setLastPwdUpdateDate(Utility.getCurrentTimestamp());
            userRecord = (UserRecord) userRecordDAO.updateObject(userRecord);
        }
        log.debug("before catch");
    }
}
