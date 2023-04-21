package org.blueoxygen.komodo.contributor.actions;

import org.blueoxygen.cimande.LogInformation;
import org.blueoxygen.cimande.persistence.PersistenceAware;
import org.blueoxygen.cimande.persistence.PersistenceManager;
import org.blueoxygen.komodo.Contributor;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author harry
 * email :  harry@intercitra.com
 */
public class ContributorForm extends ActionSupport implements PersistenceAware {

    protected PersistenceManager pm;

    protected Contributor contributor;

    protected LogInformation logInfo;

    private String id = "";

    private String contributorName = "";

    private String contributorOrganization = "";

    private String contributorService = "";

    private String contributorId = "";

    private int activeFlag = 1;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getActiveFlag() {
        return activeFlag;
    }

    public void setActiveFlag(int activeFlag) {
        this.activeFlag = activeFlag;
    }

    public String getContributorId() {
        return contributorId;
    }

    public void setContributorId(String contributorId) {
        this.contributorId = contributorId;
    }

    public Contributor getContributor() {
        return contributor;
    }

    public void setContributor(Contributor contributor) {
        this.contributor = contributor;
    }

    public String getContributorName() {
        return contributorName;
    }

    public void setContributorName(String contributorName) {
        this.contributorName = contributorName;
    }

    public String getContributorOrganization() {
        return contributorOrganization;
    }

    public void setContributorOrganization(String contributorOrganization) {
        this.contributorOrganization = contributorOrganization;
    }

    public String getContributorService() {
        return contributorService;
    }

    public void setContributorService(String contributorService) {
        this.contributorService = contributorService;
    }

    public LogInformation getLogInfo() {
        return logInfo;
    }

    public void setLogInfo(LogInformation logInfo) {
        this.logInfo = logInfo;
    }

    public void setPersistenceManager(PersistenceManager persistenceManager) {
        this.pm = persistenceManager;
    }
}
