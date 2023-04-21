package messenger;

public class MessengerException extends Exception {

    private String msg = null;

    private Exception orig = null;

    public MessengerException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public MessengerException(String msg, Exception orig) {
        super(msg, orig);
        this.msg = msg;
        this.orig = orig;
    }
}
