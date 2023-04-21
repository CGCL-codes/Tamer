package datatypes;

public class Message {

    private static int msg_Counter = 0;

    private Object content = null;

    private int MessageID = -1;

    private boolean type = false;

    public Message(Object content, boolean type) {
        this.content = content;
        this.MessageID = msg_Counter++;
        this.type = type;
    }

    public Message(Object content, boolean type, int ID) {
        this.content = content;
        this.MessageID = ID;
        this.type = type;
    }

    public int getMsgID() {
        return this.MessageID;
    }

    public Integer getMsgID_asInteger() {
        return new Integer(this.MessageID);
    }

    public boolean isDiscoveryMessage() {
        return this.type;
    }

    public boolean isRoutingMessage() {
        return !this.type;
    }

    public Object getContent() {
        return this.content;
    }
}
