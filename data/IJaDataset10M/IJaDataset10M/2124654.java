package com.sourcesense.ant.dbdep.task.parser.ivy.vo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author Piergiorgio Lucidi
 *
 */
public class IvyDependency {

    private static Log log = LogFactory.getLog(IvyDependency.class);

    private String org;

    private String name;

    private String rev;

    public void setOrg(String org) {
        this.org = org;
        if (log.isDebugEnabled()) log.debug("org: " + org);
    }

    public void setName(String name) {
        this.name = name;
        if (log.isDebugEnabled()) log.debug("name: " + name);
    }

    public void setRev(String rev) {
        this.rev = rev;
        if (log.isDebugEnabled()) log.debug("rev: " + rev);
    }

    public String toString() {
        return "IvyDependency | org: " + this.org + " | name: " + this.name + " | rev: " + this.rev;
    }

    public String getOrg() {
        return org;
    }

    public String getName() {
        return name;
    }

    public String getRev() {
        return rev;
    }
}
