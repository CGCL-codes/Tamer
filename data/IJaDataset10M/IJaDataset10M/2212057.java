package com.jaeksoft.searchlib.web.controller;

import com.jaeksoft.searchlib.SearchLibException;
import com.jaeksoft.searchlib.user.Role;

public class IndexController extends CommonController {

    /**
	 * 
	 */
    private static final long serialVersionUID = -7590913483471357743L;

    public IndexController() throws SearchLibException {
        super();
    }

    @Override
    protected void reset() {
    }

    public String getIndexTitle() throws SearchLibException {
        String indexName = getIndexName();
        if (indexName == null) return "Indices";
        return " Index: " + indexName;
    }

    public boolean isCrawlerRights() throws SearchLibException {
        if (!isLogged() || !isInstanceValid()) return false;
        if (isNoUserList()) return true;
        return getLoggedUser().hasAnyRole(getIndexName(), Role.GROUP_WEB_CRAWLER, Role.GROUP_FILE_CRAWLER, Role.GROUP_DATABASE_CRAWLER);
    }

    public boolean isSchedulerRights() throws SearchLibException {
        return isQueryRights();
    }

    public boolean isReplicationRights() throws SearchLibException {
        return isQueryRights();
    }

    public boolean isRuntimeRights() throws SearchLibException {
        return isAdminOrMonitoringOrNoUser() || isInstanceValid();
    }

    public boolean isPrivilegeRights() throws SearchLibException {
        if (isAdmin()) return true;
        if (isNoUserList()) return true;
        return false;
    }
}
