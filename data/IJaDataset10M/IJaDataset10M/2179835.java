package com.mockturtlesolutions.snifflib.mcmctools.database;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;
import com.mockturtlesolutions.snifflib.sqldig.database.*;
import com.mockturtlesolutions.snifflib.reposconfig.database.ReposPreferences;

/**
.
*/
public class MCMCToolsPrefs extends SQLConfig implements ReposPreferences {

    public MCMCToolsPrefs() {
        this.ConfigEnvironmentVariable = "MCMCTOOLSPREFS";
        this.UsrConfigFile = (String) System.getProperty("user.home").concat(File.separator).concat(".mymcmctoolsprefs");
        this.SysConfigFile = System.getenv(this.ConfigEnvironmentVariable);
        this.setSplitString(",");
    }

    public HashSet getFileChooserConfigs() {
        HashSet out = super.getFileChooserConfigs();
        out.add("iconmapfile");
        return (out);
    }

    public HashSet getDomainNameFilterConfigs() {
        HashSet out = super.getDomainNameFilterConfigs();
        out.add("statisticalmodelconfigs");
        out.add("proposaldistconfigs");
        return (out);
    }

    public LinkedHashMap getDefaultConfig() {
        LinkedHashMap configmap = new LinkedHashMap();
        configmap.put("lastrepository", "default");
        configmap.put("domainname", "com.yourdomain");
        configmap.put("iconmapfile", System.getProperty("user.home").concat(File.separator).concat(".pHtoolsDomainIconMappings"));
        configmap.put("statisticalmodelconfigs", "com.mockturtlesolutions.snifflib.mcmctools.database.StatisticalModelToolsConfig");
        configmap.put("proposaldistconfigs", "com.mockturtlesolutions.snifflib.mcmctools.database.ProposalDistributionConfig");
        configmap.put("createdon", "2009-00-00");
        configmap.put("createdby", "nobody");
        return (configmap);
    }
}
