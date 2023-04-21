package org.openkonnect.api.model;

public class OpenKonnectEvent {

    protected String okObjectID;

    protected String creatorID;

    protected String creatorObjectID;

    protected String okName;

    private String eventID;

    public OpenKonnectEvent(String action) {
        okName = null;
        eventID = action;
    }

    public String getOkName() {
        return okName;
    }

    public String getEventID() {
        return eventID;
    }

    public String getOkObjectID() {
        return okObjectID;
    }

    public String getCreatorID() {
        return creatorID;
    }

    public String getCreatorObjectID() {
        return creatorObjectID;
    }

    public void setOkObjectID(String okObjectID) {
        this.okObjectID = okObjectID;
    }

    public void setCreatorID(String creatorID) {
        this.creatorID = creatorID;
    }

    public void setCreatorObjectID(String creatorObjectID) {
        this.creatorObjectID = creatorObjectID;
    }
}
