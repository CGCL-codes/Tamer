package org.ip_super.api.ssb.ssbdeployment;

@SuppressWarnings("serial")
public class DeploymentException extends Exception {

    private String messageType;

    private String message;

    public void fromString(String data) {
        int pos = data.indexOf("<message-type>");
        if (pos > 0) {
            messageType = data.substring(pos + 14, data.indexOf('<', pos + 14));
        } else {
            messageType = "";
        }
        pos = data.indexOf("<loc-message>");
        if (pos > 0) {
            message = data.substring(pos + 13, data.indexOf('<', pos + 13));
        } else {
            message = "";
        }
    }

    public String getMessageType() {
        return this.messageType;
    }

    public String getMessage() {
        return this.message;
    }
}
