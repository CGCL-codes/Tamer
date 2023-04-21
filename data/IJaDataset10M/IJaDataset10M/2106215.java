package org.blueoxygen.cimande.descriptorgroup.actions;

import java.sql.Timestamp;
import org.blueoxygen.cimande.LogInformation;
import org.blueoxygen.cimande.descriptors.DescriptorGroup;
import org.blueoxygen.cimande.security.SessionCredentials;
import org.blueoxygen.cimande.security.SessionCredentialsAware;

/**
 * @author Ikromy
 *
 */
public class AddDescriptorGroup extends DescriptorGroupForm implements SessionCredentialsAware {

    private SessionCredentials sessCredentials;

    public String execute() {
        DescriptorGroup desGroup = new DescriptorGroup();
        if (getGroupId().equalsIgnoreCase("")) {
            addActionError("Group Id can't be empty");
        }
        if (getFolder().equalsIgnoreCase("")) {
            addActionError("Folder can't be empty");
        }
        if (getDescription().equalsIgnoreCase("")) {
            addActionError("Description can't be empty");
        }
        if (hasErrors()) {
            return INPUT;
        } else {
            desGroup.setGroupId(getGroupId());
            desGroup.setFolder(getFolder());
            desGroup.setDescription(getDescription());
            LogInformation logInfo = new LogInformation();
            if (sessCredentials.getCurrentUser() != null) {
                logInfo.setCreateBy(sessCredentials.getCurrentUser().getId());
                logInfo.setLastUpdateBy(sessCredentials.getCurrentUser().getId());
            }
            logInfo.setCreateDate(new Timestamp(System.currentTimeMillis()));
            logInfo.setLastUpdateDate(new Timestamp(System.currentTimeMillis()));
            logInfo.setActiveFlag(getActiveFlag());
            if (getActiveFlag() == -1) {
                logInfo.setActiveFlag(LogInformation.INACTIVE);
            } else {
                logInfo.setActiveFlag(getActiveFlag());
            }
            desGroup.setLogInformation(logInfo);
            pm.save(desGroup);
            return SUCCESS;
        }
    }

    public void setSessionCredentials(SessionCredentials sessionCredentials) {
        this.sessCredentials = sessionCredentials;
    }
}
