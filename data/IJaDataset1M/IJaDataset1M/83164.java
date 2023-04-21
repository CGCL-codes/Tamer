package org.pointrel.pointrel20110330.archives;

public class ChangeInArchive {

    String type;

    String resourceReference;

    String timestamp;

    String responsible;

    public ChangeInArchive(String type, String resourceReference) {
        this.type = type;
        this.resourceReference = resourceReference;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getResourceReference() {
        return resourceReference;
    }

    public void setResourceReference(String resourceReference) {
        this.resourceReference = resourceReference;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getResponsible() {
        return responsible;
    }

    public void setResponsible(String responsible) {
        this.responsible = responsible;
    }
}
