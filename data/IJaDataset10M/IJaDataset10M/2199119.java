package chat.messaging;

import java.util.StringTokenizer;

public class MsgText extends ChatMessage {

    private String text = "";

    public MsgText(String sender, String target, String text) {
        super(MessageConst.MSG_TEXT, sender, target);
        this.text = text;
    }

    public MsgText(String message) {
        super(message);
        StringTokenizer st = new StringTokenizer(message, MessageConst.MESSAGE_SEP);
        st.nextToken();
        st.nextToken();
        st.nextToken();
        text = st.nextToken();
    }

    public String toString() {
        String ret = super.toString() + text + MessageConst.MESSAGE_SEP;
        return ret;
    }

    public String getText() {
        return text;
    }
}
