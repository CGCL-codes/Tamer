package org.jSyncManager.SJS.Adapters.SMTPServer.Config;

import net.sourceforge.c4j.ContractReference;

/**
 * This class represents an email domain used for sending,
 * receiving, and storing messages
 *
 * @author $author$
 * @version $Revision$
 */
@ContractReference(contractClassName = "org.jSyncManager.SJS.Adapters.SMTPServer.Config.contracts.DomainContract")
public class Domain {

    /** string type of domain */
    private String domain;

    /**
    * Constructor, default and empty
    */
    public Domain() {
    }

    /**
    * Constructor
    * @param domain the name of the domain
    */
    public Domain(String domain) {
        this.domain = domain;
    }

    /**
    * Gets the domain of the email address
    *
    * @return string type of domain
    */
    public String getDomain() {
        return domain;
    }

    /**
    * sets the domain for the email address
    *
    * @param domain parameter value
    */
    public void setDomain(String domain) {
        this.domain = domain;
    }

    /**
    * Returns the domain name.
    *
    * @return returned
    */
    public String toString() {
        return this.domain;
    }
}
