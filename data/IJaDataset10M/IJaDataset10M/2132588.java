package com.dotmarketing.beans;

import java.util.Map;

/**
 *
 * @author  David
 */
public class Host extends Inode {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public Host() {
        this.setType("host");
    }

    private String hostname;

    private boolean isDefault;

    private String aliases;

    public String getAliases() {
        return aliases;
    }

    public void setAliases(String aliases) {
        this.aliases = aliases;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public Map<String, Object> getMap() {
        Map<String, Object> retMap = super.getMap();
        retMap.put("hostname", hostname);
        retMap.put("isDefault", isDefault);
        retMap.put("aliases", aliases);
        return retMap;
    }
}
