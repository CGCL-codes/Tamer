package com.mockturtlesolutions.snifflib.statmodeltools.database;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.awt.Color;
import java.awt.Font;
import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;
import com.mockturtlesolutions.snifflib.reposconfig.database.*;
import com.mockturtlesolutions.snifflib.graphics.*;
import com.mockturtlesolutions.snifflib.guitools.components.DomainNameFilter;
import com.mockturtlesolutions.snifflib.guitools.components.LeastPartDomainNameFilter;
import com.mockturtlesolutions.snifflib.sqldig.database.SQLConfig;
import com.mockturtlesolutions.snifflib.reposconfig.database.ReposPreferences;

public class ParameterSetStoragePrefs extends SQLConfig implements ReposPreferences {

    public ParameterSetStoragePrefs() {
        this.ConfigEnvironmentVariable = "PARAMETERSETPREFS";
        this.UsrConfigFile = (String) System.getProperty("user.home").concat(File.separator).concat(".myparametersetprefs");
        this.SysConfigFile = System.getenv(this.ConfigEnvironmentVariable);
        this.setSplitString(",");
    }

    public HashSet getFileChooserConfigs() {
        HashSet out = super.getFileChooserConfigs();
        out.add("iconmapfile");
        return (out);
    }

    public LinkedHashMap getDefaultConfig() {
        LinkedHashMap configmap = new LinkedHashMap();
        configmap.put("lastrepository", "default");
        configmap.put("domainname", "com.yourdomain");
        configmap.put("iconmapfile", System.getProperty("user.home").concat(File.separator).concat(".pHtoolsDomainIconMappings"));
        configmap.put("createdon", "2009-00-00");
        configmap.put("createdby", "nobody");
        return (configmap);
    }
}
