package org.spice.servlet.dispatcher;

public class XSDElements {

    private String scanpath;

    private String jndi;

    private String jspPrefix;

    private String jspSuffix;

    public String getScanpath() {
        return scanpath;
    }

    public void setScanpath(String scanpath) {
        this.scanpath = scanpath;
    }

    public String getJndi() {
        return jndi;
    }

    public void setJndi(String jndi) {
        this.jndi = jndi;
    }

    public String getJspPrefix() {
        return jspPrefix;
    }

    public void setJspPrefix(String jspPrefix) {
        this.jspPrefix = jspPrefix;
    }

    public String getJspSuffix() {
        return jspSuffix;
    }

    public void setJspSuffix(String jspSuffix) {
        this.jspSuffix = jspSuffix;
    }
}
