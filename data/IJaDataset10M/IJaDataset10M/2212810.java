package org.hardtokenmgmt.ws.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Object returned containing one row returned by the listUsers method. Contains the user data
 * and the users tokens if any has been generated.
 * 
 * 
 * @author Philip Vendil 19 apr 2010
 *
 * @version $Id$
 */
public class UserQueryDataWS {

    private UserDataVOWS userData;

    private List<TokenDataWS> tokens = new ArrayList<TokenDataWS>();

    private String orgId;

    public UserQueryDataWS() {
    }

    public void setUserData(UserDataVOWS userData) {
        this.userData = userData;
    }

    public UserDataVOWS getUserData() {
        return userData;
    }

    public void setTokens(List<TokenDataWS> tokens) {
        this.tokens = tokens;
    }

    public List<TokenDataWS> getTokens() {
        return tokens;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOrgId() {
        return this.orgId;
    }
}
