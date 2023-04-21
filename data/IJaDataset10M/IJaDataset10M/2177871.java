package com.arjuna.blue.bluefrontend.faces;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ServiceExtInfo extends BlueObject {

    private String hostname = "";

    private String serviceDescription = "";

    private String notes = "";

    private String notesURL = "";

    private String actionURL = "";

    private String iconImage = "";

    private String iconImageAlt = "";

    public ServiceExtInfo() {
    }

    public ServiceExtInfo(ServiceExtInfo sei) {
        super(sei);
        this.hostname = sei.hostname;
        this.serviceDescription = sei.serviceDescription;
        this.notes = sei.notes;
        this.notesURL = sei.notesURL;
        this.actionURL = sei.actionURL;
        this.iconImage = sei.iconImage;
        this.iconImageAlt = sei.iconImageAlt;
    }

    public String getActionURL() {
        return this.actionURL;
    }

    public void setActionURL(String actionURL) {
        this.actionURL = actionURL;
    }

    public String getHostname() {
        return this.hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getIconImage() {
        return this.iconImage;
    }

    public void setIconImage(String iconImage) {
        this.iconImage = iconImage;
    }

    public String getIconImageAlt() {
        return this.iconImageAlt;
    }

    public void setIconImageAlt(String iconImageAlt) {
        this.iconImageAlt = iconImageAlt;
    }

    public String getNotes() {
        return this.notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getNotesURL() {
        return this.notesURL;
    }

    public void setNotesURL(String notesURL) {
        this.notesURL = notesURL;
    }

    public String getServiceDescription() {
        return this.serviceDescription;
    }

    public void setServiceHost(String serviceHost) {
        String bits[] = serviceHost.split(",");
        setHostname(bits[0]);
        setServiceDescription(bits[1]);
    }

    public String getServiceHost() {
        return getHostname() + "," + getServiceDescription();
    }

    public void setServiceDescription(String serviceDescription) {
        this.serviceDescription = serviceDescription;
    }

    public HashMap<String, String> getObjectMapDetails() {
        HashMap<String, String> details = new HashMap<String, String>();
        details.put("id", String.valueOf(getId()));
        details.put("host_name", getHostname());
        details.put("service_description", getServiceDescription());
        details.put("notes", getNotes());
        details.put("notes_url", getNotesURL());
        details.put("action_url", getActionURL());
        details.put("icon_image", getIconImage());
        details.put("icon_image_alt", getIconImageAlt());
        return details;
    }

    public List<String> getObjectDetails() {
        List<String> details = new ArrayList<String>();
        details.add(String.valueOf(getId()));
        details.add("host_name");
        details.add(getHostname());
        details.add("service_description");
        details.add(getServiceDescription());
        details.add("notes");
        details.add(getNotes());
        details.add("notes_url");
        details.add(getNotesURL());
        details.add("action_url");
        details.add(getActionURL());
        details.add("icon_image");
        details.add(getIconImage());
        details.add("icon_image_alt");
        details.add(getIconImageAlt());
        return details;
    }
}
