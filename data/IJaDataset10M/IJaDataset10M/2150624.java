package org.jcompany.jdoc.config;

import java.io.Serializable;

public class PlcHibernateJcsClassCache implements Serializable {

    private String cacheClass;

    private String region;

    private String usage;

    public PlcHibernateJcsClassCache() {
    }

    public String getCacheClass() {
        return cacheClass;
    }

    public void setCacheClass(String newCacheClass) {
        cacheClass = newCacheClass;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String newRegion) {
        region = newRegion;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String newUsage) {
        usage = newUsage;
    }
}
