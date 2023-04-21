package hu.sztaki.dsd.abilities.invitation.web;

import java.util.Map;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.apache.log4j.Logger;

/**
 *
 * @author Matetelki
 */
public class InvitationRequestMB {

    Logger log4j = Logger.getLogger(InvitationRequestMB.class);

    private InvitationMB invMB;

    private String userName;

    private String senderName;

    private String message;

    private String mode;

    /**
     * Creates a new instance of InvitationRequestMB
     */
    public InvitationRequestMB() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        log4j.debug("HTTP GET userName: " + userName);
        this.userName = userName;
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = (ExternalContext) facesContext.getExternalContext();
        Map sessionMap = externalContext.getSessionMap();
        String currentU = (String) sessionMap.get("eu.abilities.username");
        invMB = (InvitationMB) sessionMap.get("InvitationMB");
        if (invMB != null) {
            if (currentU != null) {
                if ((invMB.getCurrentUser() == null) || (!invMB.getCurrentUser().equals(currentU))) {
                    log4j.debug("Current user changed!!! User " + invMB.getCurrentUser() + " logged out and " + currentU + " logged in.");
                    invMB.resetBean();
                    invMB.setCurrentUser(currentU);
                } else {
                    log4j.debug("Current user is still " + invMB.getCurrentUser());
                }
            } else {
                log4j.error("No user logged in!");
            }
        } else {
            log4j.warn("InvitationMB not found");
        }
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        log4j.debug("HTTP GET sender: " + senderName);
        this.senderName = senderName;
        if (senderName != null) {
            invMB = (InvitationMB) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("InvitationMB");
            if (invMB != null) {
                invMB.setReceiver(senderName);
            } else {
                log4j.warn("InvitationMB not found");
            }
        } else {
            if (this.getMode() != null) {
                if (this.getMode().equals(invMB.getNEW())) {
                    log4j.warn("Receiver not specified yet! Can't send invitation.");
                    invMB.setErrorMessage("Receiver not specified! Can't send invitation.");
                }
            }
        }
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        log4j.debug("HTTP GET message: " + message);
        this.message = message;
        if (message != null) {
            invMB = (InvitationMB) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("InvitationMB");
            if (invMB != null) {
                invMB.setEsbMessage(message);
            } else {
                log4j.warn("InvitationMB not found");
            }
        }
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        log4j.debug("HTTP GET mode: " + mode);
        this.mode = mode;
        if (mode != null) {
            invMB = (InvitationMB) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("InvitationMB");
            if (invMB != null) {
                if (mode.equals(invMB.getMENU())) invMB.setMenuMode();
                if (mode.equals(invMB.getPENDINGINVLIST())) invMB.setPendingInvListMode();
                if (mode.equals(invMB.getAGREEDINVLIST())) invMB.setAgreedInvListMode();
                if (mode.equals(invMB.getNEW())) invMB.setNewInvitationMode();
            } else {
                log4j.warn("InvitationMB not found");
            }
        }
    }
}
