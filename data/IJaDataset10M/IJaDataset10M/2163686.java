package fr.inria.uml4tst.papyrus.message;

public class Message {

    private String location;

    private String description;

    private String severity;

    public Message() {
    }

    public Message(String d, String l, String s) {
        this.location = l;
        this.description = d;
        this.severity = s;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getSeverity() {
        return severity;
    }
}
