package com.ext.portlet.entrepriseadmin.action;

import java.util.GregorianCalendar;
import java.util.List;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.context.ApplicationContext;
import com.ext.portlet.userhireext.service.AddUserExtrasLocalServiceUtil;
import com.liferay.portal.ContactFirstNameException;
import com.liferay.portal.ContactLastNameException;
import com.liferay.portal.DuplicateUserEmailAddressException;
import com.liferay.portal.DuplicateUserScreenNameException;
import com.liferay.portal.ModelListenerException;
import com.liferay.portal.NoSuchOrganizationException;
import com.liferay.portal.NoSuchUserException;
import com.liferay.portal.OrganizationParentException;
import com.liferay.portal.PortalException;
import com.liferay.portal.RequiredUserException;
import com.liferay.portal.ReservedUserEmailAddressException;
import com.liferay.portal.ReservedUserScreenNameException;
import com.liferay.portal.UserEmailAddressException;
import com.liferay.portal.UserIdException;
import com.liferay.portal.UserPasswordException;
import com.liferay.portal.UserScreenNameException;
import com.liferay.portal.UserSmsException;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Contact;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.User;
import com.liferay.portal.model.UserGroup;
import com.liferay.portal.model.impl.UserGroupImpl;
import com.liferay.portal.security.auth.PrincipalException;
import com.liferay.portal.service.UserGroupLocalServiceUtil;
import com.liferay.portal.service.UserServiceUtil;
import com.liferay.portal.service.impl.UserGroupLocalServiceImpl;
import com.liferay.portal.service.persistence.UserGroupUtil;
import com.liferay.portal.struts.PortletAction;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.WebKeys;
import com.liferay.portlet.CachePortlet;
import com.liferay.portlet.admin.util.AdminUtil;
import com.liferay.util.servlet.SessionErrors;
import fr.gfi.gfinet.server.AgenceService;
import fr.gfi.gfinet.server.ClassificationService;
import fr.gfi.gfinet.server.CollaboratorService;
import fr.gfi.gfinet.server.CostCenterService;
import fr.gfi.gfinet.server.StudyLevelService;
import fr.gfi.gfinet.server.info.Adresse;
import fr.gfi.gfinet.server.info.Agence;
import fr.gfi.gfinet.server.info.Collaborator;
import fr.gfi.gfinet.server.info.CostCenter;
import fr.gfi.gfinet.web.util.Tools;

/**
 * <a href="EditUserAction.java.html"><b><i>View Source</i></b></a>
 * 
 * @author Brian Wing Shun Chan
 * 
 */
public class EditUserAction extends PortletAction {

    private static Log logger = LogFactory.getLog(EditUserAction.class);

    private CollaboratorService collaboratorService;

    private CostCenterService costCenterService;

    private AgenceService agenceService;

    private ClassificationService classificationService;

    private StudyLevelService studyLevelService;

    public void processAction(ActionMapping mapping, ActionForm form, PortletConfig config, ActionRequest req, ActionResponse res) throws Exception {
        String cmd = ParamUtil.getString(req, Constants.CMD);
        try {
            User user = null;
            String oldScreenName = StringPool.BLANK;
            if (cmd.equals(Constants.ADD) || cmd.equals(Constants.UPDATE)) {
                Object[] returnValue = updateUser(req);
                user = (User) returnValue[0];
                oldScreenName = ((String) returnValue[1]);
            } else if (cmd.equals("comments")) {
                user = updateComments(req);
            } else if (cmd.equals(Constants.DEACTIVATE) || cmd.equals(Constants.DELETE) || cmd.equals(Constants.RESTORE)) {
                deleteUsers(req);
            } else if (cmd.equals("deleteRole")) {
                deleteRole(req);
            } else if (cmd.equals("display")) {
                user = updateDisplay(req);
            } else if (cmd.equals("im")) {
                user = updateIm(req);
            } else if (cmd.equals("password")) {
                user = updatePassword(req);
            } else if (cmd.equals("sms")) {
                user = updateSms(req);
            } else if (cmd.equals("unlock")) {
                user = updateLockout(req);
            }
            String redirect = null;
            if (user != null) {
                redirect = ParamUtil.getString(req, "redirect");
                if (Validator.isNotNull(oldScreenName)) {
                    ThemeDisplay themeDisplay = (ThemeDisplay) req.getAttribute(WebKeys.THEME_DISPLAY);
                    Group group = user.getGroup();
                    if (group.getGroupId() == themeDisplay.getPortletGroupId()) {
                        Layout layout = themeDisplay.getLayout();
                        String friendlyURLPath = group.getPathFriendlyURL(layout.isPrivateLayout(), themeDisplay);
                        redirect = StringUtil.replace(redirect, friendlyURLPath + StringPool.SLASH + oldScreenName, friendlyURLPath + StringPool.SLASH + user.getScreenName());
                    }
                }
                redirect += user.getUserId();
            }
            sendRedirect(req, res, redirect);
        } catch (Exception e) {
            if (e instanceof NoSuchUserException || e instanceof PrincipalException) {
                SessionErrors.add(req, e.getClass().getName());
                setForward(req, "portlet.enterprise_admin.error");
            } else if (e instanceof ContactFirstNameException || e instanceof ContactLastNameException || e instanceof DuplicateUserEmailAddressException || e instanceof DuplicateUserScreenNameException || e instanceof NoSuchOrganizationException || e instanceof OrganizationParentException || e instanceof RequiredUserException || e instanceof ReservedUserEmailAddressException || e instanceof ReservedUserScreenNameException || e instanceof UserEmailAddressException || e instanceof UserIdException || e instanceof UserPasswordException || e instanceof UserScreenNameException || e instanceof UserSmsException) {
                SessionErrors.add(req, e.getClass().getName(), e);
                if (e instanceof RequiredUserException) {
                    res.sendRedirect(ParamUtil.getString(req, "redirect"));
                }
            } else {
                throw e;
            }
        }
    }

    public ActionForward render(ActionMapping mapping, ActionForm form, PortletConfig config, RenderRequest req, RenderResponse res) throws Exception {
        try {
            PortalUtil.getSelectedUser(req);
        } catch (Exception e) {
            if (e instanceof PrincipalException) {
                SessionErrors.add(req, e.getClass().getName());
                return mapping.findForward("portlet.enterprise_admin.error");
            } else {
                throw e;
            }
        }
        return mapping.findForward(getForward(req, "portlet.enterprise_admin.edit_user"));
    }

    protected void deleteRole(ActionRequest req) throws Exception {
        User user = PortalUtil.getSelectedUser(req);
        long roleId = ParamUtil.getLong(req, "roleId");
        UserServiceUtil.deleteRoleUser(roleId, user.getUserId());
    }

    protected void deleteUsers(ActionRequest req) throws Exception {
        String cmd = ParamUtil.getString(req, Constants.CMD);
        long[] deleteUserIds = StringUtil.split(ParamUtil.getString(req, "deleteUserIds"), 0L);
        for (int i = 0; i < deleteUserIds.length; i++) {
            if (cmd.equals(Constants.DEACTIVATE) || cmd.equals(Constants.RESTORE)) {
                boolean active = !cmd.equals(Constants.DEACTIVATE);
                UserServiceUtil.updateActive(deleteUserIds[i], active);
            } else {
                UserServiceUtil.deleteUser(deleteUserIds[i]);
            }
        }
    }

    protected User updateComments(ActionRequest req) throws Exception {
        String comments = ParamUtil.getString(req, "comments");
        User user = PortalUtil.getSelectedUser(req);
        Contact contact = user.getContact();
        AdminUtil.updateUser(req, user.getUserId(), user.getScreenName(), user.getEmailAddress(), user.getLanguageId(), user.getTimeZoneId(), user.getGreeting(), comments, contact.getSmsSn(), contact.getAimSn(), contact.getIcqSn(), contact.getJabberSn(), contact.getMsnSn(), contact.getSkypeSn(), contact.getYmSn());
        return user;
    }

    protected User updateDisplay(ActionRequest req) throws Exception {
        PortletSession ses = req.getPortletSession();
        String languageId = ParamUtil.getString(req, "languageId");
        String timeZoneId = ParamUtil.getString(req, "timeZoneId");
        String greeting = ParamUtil.getString(req, "greeting");
        User user = PortalUtil.getSelectedUser(req);
        Contact contact = user.getContact();
        AdminUtil.updateUser(req, user.getUserId(), user.getScreenName(), user.getEmailAddress(), languageId, timeZoneId, greeting, user.getComments(), contact.getSmsSn(), contact.getAimSn(), contact.getIcqSn(), contact.getJabberSn(), contact.getMsnSn(), contact.getSkypeSn(), contact.getYmSn());
        HttpServletRequest httpReq = PortalUtil.getHttpServletRequest(req);
        HttpSession httpSes = httpReq.getSession();
        httpSes.removeAttribute(Globals.LOCALE_KEY);
        CachePortlet.clearResponses(ses);
        return user;
    }

    protected User updateIm(ActionRequest req) throws Exception {
        String aimSn = ParamUtil.getString(req, "aimSn");
        String icqSn = ParamUtil.getString(req, "icqSn");
        String jabberSn = ParamUtil.getString(req, "jabberSn");
        String msnSn = ParamUtil.getString(req, "msnSn");
        String skypeSn = ParamUtil.getString(req, "skypeSn");
        String ymSn = ParamUtil.getString(req, "ymSn");
        User user = PortalUtil.getSelectedUser(req);
        Contact contact = user.getContact();
        AdminUtil.updateUser(req, user.getUserId(), user.getScreenName(), user.getEmailAddress(), user.getLanguageId(), user.getTimeZoneId(), user.getGreeting(), user.getComments(), contact.getSmsSn(), aimSn, icqSn, jabberSn, msnSn, skypeSn, ymSn);
        return user;
    }

    protected User updateLockout(ActionRequest req) throws Exception {
        User user = PortalUtil.getSelectedUser(req);
        UserServiceUtil.updateLockout(user.getUserId(), false);
        return user;
    }

    protected User updatePassword(ActionRequest req) throws Exception {
        PortletSession ses = req.getPortletSession();
        String password1 = ParamUtil.getString(req, "password1");
        String password2 = ParamUtil.getString(req, "password2");
        boolean passwordReset = ParamUtil.getBoolean(req, "passwordReset");
        User user = PortalUtil.getSelectedUser(req);
        UserServiceUtil.updatePassword(user.getUserId(), password1, password2, passwordReset);
        if (user.getUserId() == PortalUtil.getUserId(req)) {
            ses.setAttribute(WebKeys.USER_PASSWORD, password1, PortletSession.APPLICATION_SCOPE);
        }
        return user;
    }

    protected User updateSms(ActionRequest req) throws Exception {
        String smsSn = ParamUtil.getString(req, "smsSn");
        User user = PortalUtil.getSelectedUser(req);
        Contact contact = user.getContact();
        AdminUtil.updateUser(req, user.getUserId(), user.getScreenName(), user.getEmailAddress(), user.getLanguageId(), user.getTimeZoneId(), user.getGreeting(), user.getComments(), smsSn, contact.getAimSn(), contact.getIcqSn(), contact.getJabberSn(), contact.getMsnSn(), contact.getSkypeSn(), contact.getYmSn());
        return user;
    }

    protected Object[] updateUser(ActionRequest req) throws Exception {
        String cmd = ParamUtil.getString(req, Constants.CMD);
        ThemeDisplay themeDisplay = (ThemeDisplay) req.getAttribute(WebKeys.THEME_DISPLAY);
        initGfinetBean();
        boolean autoPassword = true;
        String password1 = null;
        String password2 = null;
        boolean autoScreenName = false;
        String screenName = ParamUtil.getString(req, "screenName");
        String emailAddress = ParamUtil.getString(req, "emailAddress");
        String firstName = ParamUtil.getString(req, "firstName");
        String middleName = ParamUtil.getString(req, "middleName");
        String lastName = ParamUtil.getString(req, "lastName");
        int prefixId = ParamUtil.getInteger(req, "prefixId");
        int suffixId = ParamUtil.getInteger(req, "suffixId");
        boolean male = ParamUtil.get(req, "male", true);
        int birthdayMonth = ParamUtil.getInteger(req, "birthdayMonth");
        int birthdayDay = ParamUtil.getInteger(req, "birthdayDay");
        int birthdayYear = ParamUtil.getInteger(req, "birthdayYear");
        boolean sendEmail = true;
        String jobTitle = ParamUtil.getString(req, "jobTitle");
        long organizationId = ParamUtil.getLong(req, "organizationId");
        long locationId = ParamUtil.getLong(req, "locationId");
        long[] userGroups = ParamUtil.getLongValues(req, "check");
        int hireMonth = ParamUtil.getInteger(req, "hireMonth");
        int hireDay = ParamUtil.getInteger(req, "hireDay");
        int hireYear = ParamUtil.getInteger(req, "hireYear");
        boolean productif = ParamUtil.getBoolean(req, "etat");
        String email2 = ParamUtil.getString(req, "email2");
        String email3 = ParamUtil.getString(req, "email3");
        String phone = ParamUtil.getString(req, "phone");
        Long studyLevel = ParamUtil.getLong(req, "studyLevel");
        Long classification = ParamUtil.getLong(req, "classification");
        String rue = ParamUtil.getString(req, "rueCollab");
        String batiement = ParamUtil.getString(req, "batiementCollab");
        String complement = ParamUtil.getString(req, "complementCollab");
        String zip = ParamUtil.getString(req, "zipCollab");
        String ville = ParamUtil.getString(req, "cityCollab");
        User user = null;
        String oldScreenName = StringPool.BLANK;
        if (cmd.equals(Constants.ADD)) {
            user = UserServiceUtil.addUser(themeDisplay.getCompanyId(), autoPassword, password1, password2, autoScreenName, screenName, emailAddress, themeDisplay.getLocale(), firstName, middleName, lastName, prefixId, suffixId, male, birthdayMonth, birthdayDay, birthdayYear, jobTitle, organizationId, locationId, sendEmail);
        } else {
            user = PortalUtil.getSelectedUser(req);
            String password = AdminUtil.getUpdateUserPassword(req, user.getUserId());
            Contact contact = user.getContact();
            String tempOldScreenName = user.getScreenName();
            user = UserServiceUtil.updateUser(user.getUserId(), password, screenName, emailAddress, user.getLanguageId(), user.getTimeZoneId(), user.getGreeting(), user.getComments(), firstName, middleName, lastName, prefixId, suffixId, male, birthdayMonth, birthdayDay, birthdayYear, contact.getSmsSn(), contact.getAimSn(), contact.getIcqSn(), contact.getJabberSn(), contact.getMsnSn(), contact.getSkypeSn(), contact.getYmSn(), jobTitle, organizationId, locationId);
            if (!tempOldScreenName.equals(user.getScreenName())) {
                oldScreenName = tempOldScreenName;
            }
        }
        try {
            GregorianCalendar gc = new GregorianCalendar(hireYear, hireMonth, hireDay);
            java.sql.Date d = Tools.sqlDate(gc.getTime());
            Collaborator collab = null;
            CostCenter costCenter = null;
            Agence agence = null;
            try {
                collab = collaboratorService.getCollaboratorbyPortalId(String.valueOf(user.getUserId()));
            } catch (Exception e) {
                logger.error("Service COLLABORATEUR ERROR", e);
            }
            try {
                costCenter = costCenterService.getCostCenterByPortalId(String.valueOf(locationId));
            } catch (Exception e) {
                logger.error("Service COST CENTER ERROR", e);
            }
            try {
                agence = agenceService.getAgenceByPortalId(String.valueOf(organizationId));
            } catch (Exception e) {
                logger.error("Service AGENCE ERROR", e);
            }
            try {
                logger.info("AddUserExtrasLocalServiceUtil.setHire(" + user.getUserId() + "," + d + "," + email2 + "," + email3 + "," + phone + ");");
                AddUserExtrasLocalServiceUtil.setHire(user.getUserId(), d, email2, email3, phone);
            } catch (Exception e) {
                logger.error("impossible de mettre � jour la date d'embauche", e);
            }
            collab.setFirstName(user.getFirstName());
            collab.setLastName(user.getLastName());
            collab.setMale(user.getMale());
            collab.setPhoneNumber(phone);
            logger.info("birthe date de liferay" + user.getBirthday());
            collab.setBirthDate(Tools.sqlDate(user.getBirthday()));
            logger.info("birthe date apres conversion" + collab.getBirthDate());
            logger.info("collab.setDateEntreeGroupe(d) :" + d.toString());
            collab.setHiringDate(d);
            logger.info("collab.setEmailGfi(emailAddress); :" + emailAddress);
            collab.setEmailGfi(emailAddress);
            logger.info("collab.setEmailClient(email2); :" + email2);
            collab.setEmailClient(email2);
            logger.info("collab.setEmailPerso(email3); :" + email3);
            collab.setEmailPerso(email3);
            logger.info("collab.setIsProductive(productif); :" + productif);
            collab.setIsProductive(productif);
            logger.info("collab.setStudyLevel; :" + studyLevelService.getStudyLevel(studyLevel).getLevel());
            collab.setStudyLevel(studyLevelService.getStudyLevel(studyLevel));
            logger.info("collab.classification; :" + classificationService.getClassification(classification));
            collab.setClassification(classificationService.getClassification(classification));
            logger.info("collab.setLeCostCenter(costCenter) :" + costCenter.getName());
            collab.setLeCostCenter(costCenter);
            logger.info("collab.setLAgence(agence) :" + agence.getName());
            collab.setLAgence(agence);
            Adresse adresse = new Adresse();
            adresse.setBatiment(batiement);
            adresse.setComplement(complement);
            adresse.setRue(rue);
            adresse.setVille(ville);
            adresse.setCodePostal(zip);
            collab.setAddress(adresse);
            List<UserGroup> alluserGroups = (List<UserGroup>) UserGroupLocalServiceUtil.getUserGroups(themeDisplay.getCompanyId());
            for (UserGroup g : alluserGroups) {
                if (UserGroupUtil.containsUser(g.getPrimaryKey(), user.getPrimaryKey())) {
                    UserGroupUtil.removeUser(g.getPrimaryKey(), user.getPrimaryKey());
                }
            }
            if (userGroups.length == 0) {
                UserGroupUtil.addUser(10350, user.getPrimaryKey());
            }
            for (int i = 0; i < userGroups.length; i++) {
                UserGroupUtil.addUser(userGroups[i], user.getPrimaryKey());
            }
            collaboratorService.saveCollaborator(collab);
        } catch (Exception e) {
            logger.error("Erreur de sauvgarde user", e);
            new PortalException(e.getLocalizedMessage());
        }
        return new Object[] { user, oldScreenName };
    }

    protected void initGfinetBean() {
        ApplicationContext ctx = getWebApplicationContext();
        collaboratorService = (CollaboratorService) ctx.getBean("fr.gfi.gfinet.server.CollaboratorService");
        costCenterService = (CostCenterService) ctx.getBean("fr.gfi.gfinet.server.CostCenterService");
        agenceService = (AgenceService) ctx.getBean("fr.gfi.gfinet.server.AgenceService");
        classificationService = (ClassificationService) ctx.getBean("fr.gfi.gfinet.server.ClassificationService");
        studyLevelService = (StudyLevelService) ctx.getBean("fr.gfi.gfinet.server.StudyLevelService");
    }
}
