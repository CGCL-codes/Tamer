package org.blueoxygen.komodo.contributor.actions;

import java.sql.Timestamp;
import org.blueoxygen.cimande.LogInformation;
import org.blueoxygen.cimande.security.SessionCredentials;
import org.blueoxygen.cimande.security.SessionCredentialsAware;
import org.blueoxygen.komodo.Contributor;

/**
 * @author harry
 * email :  harry@intercitra.com
 */
public class AddContributor extends ContributorForm implements SessionCredentialsAware {

    private SessionCredentials sess;

    private String id;

    public String execute() {
        if (getContributorName().equalsIgnoreCase("")) {
            addActionError("Please fill the contributor name");
            return INPUT;
        }
        contributor = new Contributor();
        contributor.setContributorName(getContributorName());
        contributor.setContributorOrganization(getContributorOrganization());
        contributor.setContributorService(getContributorService());
        logInfo = new LogInformation();
        logInfo.setCreateBy(sess.getCurrentUser().getId());
        logInfo.setCreateDate(new Timestamp(System.currentTimeMillis()));
        logInfo.setActiveFlag(getActiveFlag());
        contributor.setLogInformation(getLogInfo());
        pm.save(contributor);
        id = contributor.getId();
        return SUCCESS;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSessionCredentials(SessionCredentials sessionCredentials) {
        this.sess = sessionCredentials;
    }
}
