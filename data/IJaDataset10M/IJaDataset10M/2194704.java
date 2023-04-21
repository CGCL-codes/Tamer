package com.kwoksys.action.admin;

import com.kwoksys.action.common.template.FooterTemplate;
import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.biz.ServiceAPI;
import com.kwoksys.biz.admin.AdminService;
import com.kwoksys.biz.admin.AdminUtils;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.files.FileUtils;
import com.kwoksys.biz.system.SystemService;
import com.kwoksys.biz.system.dto.Link;
import com.kwoksys.framework.configs.AppPaths;
import com.kwoksys.framework.configs.AppProperties;
import com.kwoksys.framework.configs.LogConfigs;
import com.kwoksys.framework.connection.database.DatabaseManager;
import com.kwoksys.framework.system.Access;
import com.kwoksys.framework.system.FeaturesEnabler;
import com.kwoksys.framework.system.Localization;
import com.kwoksys.framework.util.StringUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Action class for config system page.
 */
public class ConfigSystemAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        AccessUser user = Access.getUser(request);
        SystemService systemService = ServiceAPI.getSystemService();
        AdminService adminService = ServiceAPI.getAdminService();
        Map dbmap = systemService.getDatabaseInfo();
        Object[] argsOs = { System.getProperty("os.name"), System.getProperty("os.version") };
        request.setAttribute("os", Localization.getContent(request, "admin.configData.os.value", argsOs));
        request.setAttribute("osArch", System.getProperty("os.arch"));
        request.setAttribute("jvmVersion", System.getProperty("java.version"));
        request.setAttribute("jvmVendor", System.getProperty("java.vendor"));
        request.setAttribute("jvmHome", System.getProperty("java.home"));
        request.setAttribute("jvmFreeMemory", FileUtils.formatFileSize(Runtime.getRuntime().freeMemory(), request));
        request.setAttribute("jvmTotalMemory", FileUtils.formatFileSize(Runtime.getRuntime().totalMemory(), request));
        request.setAttribute("jvmMaxMemory", FileUtils.formatFileSize(Runtime.getRuntime().maxMemory(), request));
        request.setAttribute("dbProductName", dbmap.get("DatabaseProductName"));
        request.setAttribute("dbProductVersion", dbmap.get("DatabaseProductVersion"));
        if (!FeaturesEnabler.isMultiAppsInstance()) {
            request.setAttribute("databases", StringUtils.join(adminService.getDatabases(), "database_name", ", "));
        }
        request.setAttribute("dbHost", AppProperties.get(AppProperties.DB_SERVERHOST_KEY));
        request.setAttribute("dbPort", AppProperties.get(AppProperties.DB_SERVERPORT_KEY));
        request.setAttribute("dbName", AppProperties.get(AppProperties.DB_NAME_KEY));
        request.setAttribute("dbPoolSizeCurrent", DatabaseManager.getConns().size());
        request.setAttribute("backupPath", AppPaths.ROOT + AppPaths.ADMIN_CONFIG + "?cmd=" + AdminUtils.ADMIN_DB_BACKUP_CMD);
        if (Access.hasPermission(user, AppPaths.ADMIN_CONFIG_WRITE)) {
            request.setAttribute("loggingPath", AppPaths.ROOT + AppPaths.ADMIN_CONFIG_WRITE + "?cmd=" + AdminUtils.ADMIN_LOGGING_EDIT_CMD);
        }
        request.setAttribute("databaseAccessLogLevel", LogConfigs.getDatabaseAccessLogLevel());
        request.setAttribute("ldapLogLevel", LogConfigs.getLdapLogLevel());
        HeaderTemplate header = new HeaderTemplate();
        if (Access.hasPermission(user, AppPaths.ADMIN_INDEX)) {
            Link link = new Link();
            link.setPath(AppPaths.ROOT + AppPaths.ADMIN_INDEX);
            link.setTitleKey("admin.index.title");
            header.addNavCmds(link);
        }
        Link link = new Link();
        link.setTitleKey("admin.configHeader.system_info");
        header.addNavCmds(link);
        header.apply(request);
        new FooterTemplate().apply(request);
        return mapping.findForward(AdminUtils.ADMIN_SYSTEM_INFO_CMD);
    }
}
