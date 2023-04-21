package net.sf.josas.om;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Transient;
import net.sf.josas.model.AccountingManager;
import net.sf.josas.model.MembershipManager;

/**
 * This class represents any association managed by the Josas application.
 * @author frederic
 */
@Entity
@DiscriminatorValue("ASSO")
public class Association extends Addressable {

    /** Unique ID. */
    private static final long serialVersionUID = 1L;

    /** Agreement reference. */
    private String agreement = null;

    /** Email address. */
    private String email = null;

    /** Web site. */
    private String webSite = null;

    /** Second part of the name. */
    private String nameTwo = null;

    /** Association objective. */
    private String objective = null;

    /** Association reference. */
    private String reference = null;

    /** Accounting manager. */
    private transient AccountingManager accountingManager;

    /** Membership manager. */
    private transient MembershipManager membershipManager;

    /**
     * @return agreement
     */
    public final String getAgreement() {
        return agreement;
    }

    /**
     * @return email
     */
    public final String getEmail() {
        return email;
    }

    /**
     * @return web site
     */
    public final String getWebSite() {
        return webSite;
    }

    /**
     * @return nameTwo
     */
    public final String getNameTwo() {
        return nameTwo;
    }

    /**
     * @return objective
     */
    @Lob
    public final String getObjective() {
        return objective;
    }

    /**
     * @return reference
     */
    public final String getReference() {
        return reference;
    }

    /**
     * @param anAgreement
     *            value to set
     */
    public final void setAgreement(final String anAgreement) {
        this.agreement = anAgreement;
    }

    /**
     * @param anEmail
     *            value to set
     */
    public final void setEmail(final String anEmail) {
        this.email = anEmail;
    }

    /**
     * @param siteName
     *            the webSite to set
     */
    public final void setWebSite(final String siteName) {
        this.webSite = siteName;
    }

    /**
     * @param aName2
     *            value to set
     */
    public final void setNameTwo(final String aName2) {
        this.nameTwo = aName2;
    }

    /**
     * @param anObjective
     *            value to set
     */
    public final void setObjective(final String anObjective) {
        this.objective = anObjective;
    }

    /**
     * @param aReference
     *            value to set
     */
    public final void setReference(final String aReference) {
        this.reference = aReference;
    }

    /**
     * @param mngr
     *            accounting manager
     */
    public final void setAccountingManager(final AccountingManager mngr) {
        accountingManager = mngr;
    }

    /**
     * @return the accountingManager
     */
    @Transient
    public final AccountingManager getAccountingManager() {
        return accountingManager;
    }

    /**
     * @return the membershipManager
     */
    @Transient
    public final MembershipManager getMembershipManager() {
        return membershipManager;
    }

    /**
     * @param mngr
     *            the membershipManager to set
     */
    public final void setMembershipManager(final MembershipManager mngr) {
        this.membershipManager = mngr;
    }
}
