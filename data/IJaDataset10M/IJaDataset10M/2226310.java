package hoverball.net;

public class Message {

    public Address sender = null;

    public Address receiver = null;

    public String data = null;

    public Message(Address sender, Address receiver, String data) {
        this.sender = sender;
        this.receiver = receiver;
        this.data = data;
    }

    public String toString() {
        return "[" + sender + "->" + receiver + "] " + data;
    }
}
