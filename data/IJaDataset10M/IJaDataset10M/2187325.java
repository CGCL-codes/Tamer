package net.llando;

public class Notification extends Message {

    @LlandoProperty(name = "type")
    public String type;

    String getType() {
        return type;
    }

    void setType(String type) {
        this.type = type;
    }
}
