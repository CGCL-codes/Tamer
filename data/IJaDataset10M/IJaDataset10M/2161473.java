package com.vangent.hieos.DocViewer.client.model.authentication;

import java.util.Date;
import java.util.List;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 *
 * @author Anand Sastry
 */
public class AuthenticationContext implements IsSerializable {

    public static final String PERMISSION_VIEWDOCS = "ViewDocs";

    public static final String PERMISSION_VIEWCONSENT = "ViewConsent";

    public static final String PERMISSION_EDITCONSENT = "EditConsent";

    private boolean successStatus;

    private Date creationDate;

    private UserProfile userProfile;

    /**
     * 
     */
    public AuthenticationContext() {
    }

    /**
     * 
     * @param creationDate
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * 
     * @return
     */
    public Date getCreationDate() {
        return this.creationDate;
    }

    /**
     * 
     * @param successFlag
     */
    public void setSuccessFlag(boolean successFlag) {
        this.successStatus = successFlag;
    }

    /**
     * 
     * @return
     */
    public boolean getSuccessStatus() {
        return this.successStatus;
    }

    /**
     * 
     * @param userProfile
     */
    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    /**
     * 
     * @return
     */
    public UserProfile getUserProfile() {
        return this.userProfile;
    }

    /**
     * 
     * @return
     */
    public boolean hasPermissionToApplication() {
        List<Permission> permissions = this.getUserProfile().getPermissions();
        boolean permitted = false;
        for (Permission perm : permissions) {
            String permissionName = perm.getName();
            if (permissionName.equals(PERMISSION_VIEWDOCS) || permissionName.equals(PERMISSION_VIEWCONSENT) || permissionName.equals(PERMISSION_EDITCONSENT)) {
                permitted = true;
                break;
            }
        }
        return permitted;
    }

    /**
     * 
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("CreationDate [").append(creationDate).append("], Success Status [").append(this.successStatus).append("], User Profile [").append(this.userProfile).append("]");
        return buf.toString();
    }
}
