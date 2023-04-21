package esferacore.message.user;

/**
 *
 * @author neo
 */
public class MessageUserRequest extends MessageUser {

    private String content;

    public MessageUserRequest(String pn, String cont) {
        peerName = pn;
        content = cont;
        text = "<UserRequest peerName='" + peerName + "' Content='" + content + "' />";
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
