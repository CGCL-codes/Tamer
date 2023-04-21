package com.aimluck.eip.services.orgutils;

import java.util.Map;
import com.aimluck.eip.orm.Database;

/**
 * 
 */
public class ALOrgUtilsService {

    private ALOrgUtilsService() {
    }

    public static ALOrgUtilsHandler getService() {
        return ALOrgUtilsFactoryService.getInstance().getOrgUtilsHandler();
    }

    public static String getTheme() {
        return getService().getTheme(Database.getDomainName());
    }

    public static String getAlias() {
        return getService().getAlias(Database.getDomainName());
    }

    public static String getAliasjp() {
        return getService().getAliasjp(Database.getDomainName());
    }

    public static String getCopyright() {
        return getService().getCopyright(Database.getDomainName());
    }

    public static String getAliasCopyright() {
        return getService().getAliasCopyright(Database.getDomainName());
    }

    public static String getCopyrightShort() {
        return getService().getCopyrightShort(Database.getDomainName());
    }

    public static String getVersion() {
        return getService().getVersion(Database.getDomainName());
    }

    public static Map<String, String> getParameters() {
        return getService().getParameters(Database.getDomainName());
    }

    public static String getExternalResourcesUrl() {
        return getService().getExternalResourcesUrl(Database.getDomainName());
    }
}
