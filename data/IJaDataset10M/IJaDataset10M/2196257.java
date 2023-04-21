package org.osmius.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Notifications type
 */
public class OsmNTypnotification implements java.io.Serializable {

    private String typNotifi;

    private String desNotifi;

    private String numWait;

    private String txtMessage;

    private Set osmNTypnotificationsDatas = new HashSet(0);

    private Set osmNSubscriptions = new HashSet(0);

    private Set osmNNotifications = new HashSet(0);

    private static final long serialVersionUID = 3693834261393412633L;

    public OsmNTypnotification() {
    }

    public OsmNTypnotification(String typNotifi, String desNotifi, String numWait, String txtMessage) {
        this.typNotifi = typNotifi;
        this.desNotifi = desNotifi;
        this.numWait = numWait;
        this.txtMessage = txtMessage;
    }

    public OsmNTypnotification(String typNotifi, String desNotifi, String numWait, String txtMessage, Set osmNTypnotificationsDatas, Set osmNSubscriptions, Set osmNNotifications) {
        this.typNotifi = typNotifi;
        this.desNotifi = desNotifi;
        this.numWait = numWait;
        this.txtMessage = txtMessage;
        this.osmNTypnotificationsDatas = osmNTypnotificationsDatas;
        this.osmNSubscriptions = osmNSubscriptions;
        this.osmNNotifications = osmNNotifications;
    }

    public String getTypNotifi() {
        return typNotifi;
    }

    public void setTypNotifi(String typNotifi) {
        this.typNotifi = typNotifi;
    }

    public String getDesNotifi() {
        return desNotifi;
    }

    public void setDesNotifi(String desNotifi) {
        this.desNotifi = desNotifi;
    }

    public String getNumWait() {
        return numWait;
    }

    public void setNumWait(String numWait) {
        this.numWait = numWait;
    }

    public String getTxtMessage() {
        return txtMessage;
    }

    public void setTxtMessage(String txtMessage) {
        this.txtMessage = txtMessage;
    }

    public Set getOsmNTypnotificationsDatas() {
        return osmNTypnotificationsDatas;
    }

    public void setOsmNTypnotificationsDatas(Set osmNTypnotificationsDatas) {
        this.osmNTypnotificationsDatas = osmNTypnotificationsDatas;
    }

    public Set getOsmNSubscriptions() {
        return osmNSubscriptions;
    }

    public void setOsmNSubscriptions(Set osmNSubscriptions) {
        this.osmNSubscriptions = osmNSubscriptions;
    }

    public Set getOsmNNotifications() {
        return osmNNotifications;
    }

    public void setOsmNNotifications(Set osmNNotifications) {
        this.osmNNotifications = osmNNotifications;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OsmNTypnotification that = (OsmNTypnotification) o;
        if (desNotifi != null ? !desNotifi.equals(that.desNotifi) : that.desNotifi != null) return false;
        if (numWait != null ? !numWait.equals(that.numWait) : that.numWait != null) return false;
        if (txtMessage != null ? !txtMessage.equals(that.txtMessage) : that.txtMessage != null) return false;
        if (typNotifi != null ? !typNotifi.equals(that.typNotifi) : that.typNotifi != null) return false;
        return true;
    }

    public int hashCode() {
        int result;
        result = (typNotifi != null ? typNotifi.hashCode() : 0);
        result = 31 * result + (desNotifi != null ? desNotifi.hashCode() : 0);
        result = 31 * result + (numWait != null ? numWait.hashCode() : 0);
        result = 31 * result + (txtMessage != null ? txtMessage.hashCode() : 0);
        return result;
    }

    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("OsmNTypnotification");
        sb.append("{typNotifi='").append(typNotifi).append('\'');
        sb.append(", desNotifi='").append(desNotifi).append('\'');
        sb.append(", numWait='").append(numWait).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
