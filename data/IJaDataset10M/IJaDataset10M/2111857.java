package com.openospc.system;

/**
 *  @author TeraKhir Wih
 *  @version  V 1.0.0.0 2007/03/10 khir
 */
public class Version {

    private String versionName;

    private String description;

    private String prgVersion;

    private String dbVersion;

    public void setVersionName(String s) {
        versionName = s;
    }

    public void setDescriotion(String s) {
        description = s;
    }

    public void setPrgVersion(String s) {
        prgVersion = s;
    }

    public void setDBVersion(String s) {
        dbVersion = s;
    }

    public String getVersionName() {
        return versionName;
    }

    public String getDesc() {
        return description;
    }

    public String getPrgVersion() {
        return prgVersion;
    }

    public String getDBVersion() {
        return dbVersion;
    }
}
