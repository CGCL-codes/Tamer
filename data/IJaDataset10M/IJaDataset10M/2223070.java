package org.marcont2.usermanagement;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.GetMethod;
import org.marcont2.gui.MarcOntClient;
import org.marcont2.sesame.SesameConnection;
import org.marcont2.sesame.consts.SesameSettings;
import org.marcont2.usermanagement.consts.FOAFSettings;

/**
 *
 * @author hoook
 */
public class DRMRights {

    private String friendshipLevel;

    private String logicOperator;

    private String distance;

    public DRMRights() {
    }

    public DRMRights(String distance, String logicOperator, String friendshipLevel) {
        this.setDistance(distance);
        this.setLogicOperator(logicOperator);
        this.setFriendshipLevel(friendshipLevel);
    }

    public String getFriendshipLevel() {
        return friendshipLevel;
    }

    public void setFriendshipLevel(String friendshipLevel) {
        this.friendshipLevel = friendshipLevel;
    }

    public String getLogicOperator() {
        return logicOperator;
    }

    public void setLogicOperator(String logicOperator) {
        this.logicOperator = logicOperator;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public static DRMRights getDrmRightsForThrerad(String URI) {
        SesameConnection sc = MarcOntClient.getApp().getMarcOntLogicBean().getSesameConnection();
        DRMRights drm = sc.getDRMRights(URI);
        if (drm.getDistance() == null) {
            return null;
        }
        return drm;
    }

    public static boolean isThreadVisible(String URI) {
        SesameConnection sc = MarcOntClient.getApp().getMarcOntLogicBean().getSesameConnection();
        String userMail = MarcOntClient.getApp().getMarcOntLogicBean().getUserFoaf().getEmail();
        String authorMail = sc.getAuthor(URI);
        if (userMail == null || authorMail == null) return false;
        if (userMail.compareTo(authorMail) == 0) {
            return true;
        } else {
            DRMRights drm = sc.getDRMRights(URI);
            if ("0".compareTo(drm.getDistance()) == 0) {
                return true;
            }
            if (drm.getDistance() == null) {
                return false;
            } else {
                try {
                    String operator;
                    if (drm.getLogicOperator().compareTo(SesameSettings.AND) == 0) {
                        operator = ".";
                    } else {
                        operator = ",";
                    }
                    String spec = FOAFSettings.FOAF_SOA_URL + userMail + "/inrole/F[mailto:" + authorMail + "]" + drm.getDistance() + operator + drm.getFriendshipLevel() + "/";
                    System.out.println(spec);
                    HttpClient client = new HttpClient();
                    HttpMethod method = new GetMethod();
                    method.setURI(new URI(spec, false));
                    int statusCode = client.executeMethod(method);
                    byte[] responseBody = method.getResponseBody();
                    String response = new String(responseBody);
                    System.out.println(response);
                    System.out.println(response.matches("(?i).*true.*"));
                    method.releaseConnection();
                    if (response.matches("(?i).*true.*")) {
                        return true;
                    } else {
                        return false;
                    }
                } catch (IOException ex) {
                    Logger.getLogger(DRMRights.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return true;
    }
}
