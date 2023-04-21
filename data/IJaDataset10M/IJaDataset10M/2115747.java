package org.blueoxygen.lotion.correspondence.actions;

import java.sql.Timestamp;
import org.blueoxygen.cimande.LogInformation;
import org.blueoxygen.cimande.security.SessionCredentials;
import org.blueoxygen.cimande.security.SessionCredentialsAware;
import org.blueoxygen.lotion.Correspondence;

public class AddCorrespondence extends CorrespondenceForm implements SessionCredentialsAware {

    private SessionCredentials sess;

    public String execute() {
        if (correspondence.getSubject().equalsIgnoreCase("")) {
            addActionError("subject can't be empty");
            return INPUT;
        }
        if (getId().equalsIgnoreCase("")) {
            logInfo = new LogInformation();
            logInfo.setActiveFlag(1);
            logInfo.setCreateBy(sess.getCurrentUser().getId());
            logInfo.setCreateDate(new Timestamp(System.currentTimeMillis()));
        } else {
            Correspondence co = correspondence;
            correspondence = (Correspondence) pm.getById(Correspondence.class, getId());
            correspondence.setDescription(co.getDescription());
            correspondence.setSubject(co.getSubject());
            correspondence.setEmail(co.getEmail());
            correspondence.setMail(co.getMail());
            logInfo = correspondence.getLogInformation();
            logInfo.setActiveFlag(1);
            logInfo.setLastUpdateBy(sess.getCurrentUser().getId());
            logInfo.setLastUpdateDate(new Timestamp(System.currentTimeMillis()));
        }
        correspondence.setLogInformation(logInfo);
        pm.save(correspondence);
        return SUCCESS;
    }

    public void setSessionCredentials(SessionCredentials sessionCredentials) {
        this.sess = sessionCredentials;
    }
}
