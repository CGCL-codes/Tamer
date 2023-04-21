package com.kwoksys.action.admin.config;

import com.kwoksys.action.base.BaseAction;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.AdminUtils;
import com.kwoksys.biz.admin.dto.SystemConfig;
import com.kwoksys.framework.configs.SystemConfigNames;
import com.kwoksys.framework.system.AppPaths;
import com.kwoksys.framework.system.Localizer;
import com.kwoksys.framework.util.NumberUtils;
import com.kwoksys.framework.util.StringUtils;
import org.apache.struts.action.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Action class for editing application configurations.
 */
public class ConfigAppEdit2Action extends BaseAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ConfigForm actionForm = (ConfigForm) form;
        ActionMessages errors = new ActionMessages();
        if (!AdminUtils.validCurrencySymbol(actionForm.getCurrency())) {
            errors.add("invalidCurrency", new ActionMessage("admin.config.error.invalidCurrency"));
        }
        if (!NumberUtils.isInteger(actionForm.getNumberOfPastYears())) {
            errors.add("invalidNumPastYears", new ActionMessage("admin.config.error.invalidNumPastYears"));
        }
        if (!NumberUtils.isInteger(actionForm.getNumberOfFutureYears())) {
            errors.add("invalidNumFutureYears", new ActionMessage("admin.config.error.invalidNumFutureYears"));
        }
        if (!NumberUtils.isInteger(actionForm.getSoftwareLicneseNotesNumChars())) {
            errors.add("invalidSoftwareLicneseNotesNumChars", new ActionMessage("admin.config.error.invalidSoftwareLicneseNotesNumChars"));
        }
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return mapping.findForward(AdminUtils.ADMIN_APP_EDIT_2_CMD);
        }
        String issuesColumns = StringUtils.join(actionForm.getIssuesColumns(), ",");
        String hardwareColumns = StringUtils.join(actionForm.getHardwareColumns(), ",");
        String softwareColumns = StringUtils.join(actionForm.getSoftwareColumns(), ",");
        String contractColumns = StringUtils.join(actionForm.getContractColumns(), ",");
        String kbColumns = StringUtils.join(actionForm.getKbColumns(), ",");
        List list = new ArrayList();
        list.add(new SystemConfig(SystemConfigNames.SYSTEM_LICENSE_KEY, actionForm.getLicenseKey().trim()));
        list.add(new SystemConfig(SystemConfigNames.APP_URL, actionForm.getApplicationUrl().trim()));
        list.add(new SystemConfig(SystemConfigNames.CURRENCY_OPTION, actionForm.getCurrency().trim()));
        list.add(new SystemConfig(SystemConfigNames.LOCAL_TIMEZONE, actionForm.getTimezone()));
        list.add(new SystemConfig(SystemConfigNames.LOCALE, actionForm.getLocale()));
        list.add(new SystemConfig(SystemConfigNames.SHORT_DATE, actionForm.getShortDateFormat()));
        list.add(new SystemConfig(SystemConfigNames.TIME_FORMAT, actionForm.getTimeFormat()));
        list.add(new SystemConfig(SystemConfigNames.USER_ROWS, String.valueOf(actionForm.getUsersNumRows())));
        list.add(new SystemConfig(SystemConfigNames.USER_NAME_DISPLAY, actionForm.getUserNameDisplay()));
        list.add(new SystemConfig(SystemConfigNames.COMPANY_ROWS, String.valueOf(actionForm.getCompaniesNumRows())));
        list.add(new SystemConfig(SystemConfigNames.CONTACT_ROWS, String.valueOf(actionForm.getContactsNumRows())));
        list.add(new SystemConfig(SystemConfigNames.CONTRACT_COLUMNS, contractColumns));
        list.add(new SystemConfig(SystemConfigNames.CONTRACT_ROWS, String.valueOf(actionForm.getContractsNumRows())));
        list.add(new SystemConfig(SystemConfigNames.CONTRACT_EXPIRE_COUNTDOWN, String.valueOf(actionForm.getContractsExpireCountdown())));
        list.add(new SystemConfig(SystemConfigNames.ISSUE_ROWS, String.valueOf(actionForm.getIssuesNumRows())));
        list.add(new SystemConfig(SystemConfigNames.ISSUE_COLUMNS, issuesColumns));
        list.add(new SystemConfig(SystemConfigNames.ISSUE_GUEST_SUBMIT_MODULE_ENABLED, actionForm.getIssuesGuestSubmitModuleEnabled()));
        list.add(new SystemConfig(SystemConfigNames.ISSUE_GUEST_SUBMIT_FOOTER_ENABLED, actionForm.getIssuesGuestSubmitEnabled()));
        list.add(new SystemConfig(SystemConfigNames.KB_ARTICLE_COLUMNS, kbColumns));
        list.add(new SystemConfig(SystemConfigNames.HARDWARE_ROWS, String.valueOf(actionForm.getHardwareNumRows())));
        list.add(new SystemConfig(SystemConfigNames.HARDWARE_COLUMNS, hardwareColumns));
        list.add(new SystemConfig(SystemConfigNames.HARDWARE_WARRANTY_EXPIRE_COUNTDOWN, String.valueOf(actionForm.getHardwareExpireCountdown())));
        list.add(new SystemConfig(SystemConfigNames.BLOG_NUM_POSTS, String.valueOf(actionForm.getBlogPostsListNumRows())));
        list.add(new SystemConfig(SystemConfigNames.BLOG_NUM_POST_CHARS, String.valueOf(actionForm.getBlogPostCharactersList())));
        list.add(new SystemConfig(SystemConfigNames.NUM_PAST_YEARS, actionForm.getNumberOfPastYears()));
        list.add(new SystemConfig(SystemConfigNames.NUM_FUTURE_YEARS, actionForm.getNumberOfFutureYears()));
        list.add(new SystemConfig(SystemConfigNames.SOFTWARE_COLUMNS, softwareColumns));
        list.add(new SystemConfig(SystemConfigNames.SOFTWARE_ROWS, String.valueOf(actionForm.getSoftwareNumRows())));
        list.add(new SystemConfig(SystemConfigNames.SOFTWARE_LICENSE_NOTES_NUM_CHARS, String.valueOf(actionForm.getSoftwareLicneseNotesNumChars())));
        list.add(new SystemConfig(SystemConfigNames.HARDWARE_CHECK_UNIQUE_NAME, actionForm.getCheckUniqueHardwareName()));
        AdminService adminService = ServiceProvider.getAdminService();
        errors = adminService.updateConfig(list);
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return mapping.findForward(AdminUtils.ADMIN_APP_EDIT_2_CMD);
        } else {
            Localizer.setSessionLocale(request.getSession(), actionForm.getLocale());
            return redirect(AppPaths.ADMIN_CONFIG + "?cmd=" + AdminUtils.ADMIN_APP_CMD);
        }
    }
}
