package weibo4android;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import weibo4android.http.Response;
import weibo4android.org.json.JSONArray;
import weibo4android.org.json.JSONException;
import weibo4android.org.json.JSONObject;

/**
 * A data class representing sent/received direct message.
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class DirectMessage extends WeiboResponse implements java.io.Serializable {

    private Long id;

    private String text;

    private int sender_id;

    private int recipient_id;

    private Date created_at;

    private String sender_screen_name;

    private String recipient_screen_name;

    private static final long serialVersionUID = -3253021825891789737L;

    DirectMessage(Response res, Weibo weibo) throws WeiboException {
        super(res);
        init(res, res.asDocument().getDocumentElement(), weibo);
    }

    DirectMessage(Response res, Element elem, Weibo weibo) throws WeiboException {
        super(res);
        init(res, elem, weibo);
    }

    DirectMessage(JSONObject json) throws WeiboException {
        try {
            id = json.getLong("id");
            text = json.getString("text");
            sender_id = json.getInt("sender_id");
            recipient_id = json.getInt("recipient_id");
            created_at = parseDate(json.getString("created_at"), "EEE MMM dd HH:mm:ss z yyyy");
            sender_screen_name = json.getString("sender_screen_name");
            recipient_screen_name = json.getString("recipient_screen_name");
            if (!json.isNull("sender")) sender = new User(json.getJSONObject("sender"));
        } catch (JSONException jsone) {
            throw new WeiboException(jsone.getMessage() + ":" + json.toString(), jsone);
        }
    }

    private void init(Response res, Element elem, Weibo weibo) throws WeiboException {
        ensureRootNodeNameIs("direct_message", elem);
        sender = new User(res, (Element) elem.getElementsByTagName("sender").item(0), weibo);
        recipient = new User(res, (Element) elem.getElementsByTagName("recipient").item(0), weibo);
        id = getChildLong("id", elem);
        text = getChildText("text", elem);
        sender_id = getChildInt("sender_id", elem);
        recipient_id = getChildInt("recipient_id", elem);
        created_at = getChildDate("created_at", elem);
        sender_screen_name = getChildText("sender_screen_name", elem);
        recipient_screen_name = getChildText("recipient_screen_name", elem);
    }

    public long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public int getSenderId() {
        return sender_id;
    }

    public int getRecipientId() {
        return recipient_id;
    }

    /**
     * @return created_at
     * @since Weibo4J 1.1.0
     */
    public Date getCreatedAt() {
        return created_at;
    }

    public String getSenderScreenName() {
        return sender_screen_name;
    }

    public String getRecipientScreenName() {
        return recipient_screen_name;
    }

    private User sender;

    public User getSender() {
        return sender;
    }

    private User recipient;

    public User getRecipient() {
        return recipient;
    }

    static List<DirectMessage> constructDirectMessages(Response res, Weibo weibo) throws WeiboException {
        Document doc = res.asDocument();
        if (isRootNodeNilClasses(doc)) {
            return new ArrayList<DirectMessage>(0);
        } else {
            try {
                ensureRootNodeNameIs("direct-messages", doc);
                NodeList list = doc.getDocumentElement().getElementsByTagName("direct_message");
                int size = list.getLength();
                List<DirectMessage> messages = new ArrayList<DirectMessage>(size);
                for (int i = 0; i < size; i++) {
                    Element status = (Element) list.item(i);
                    messages.add(new DirectMessage(res, status, weibo));
                }
                return messages;
            } catch (WeiboException te) {
                if (isRootNodeNilClasses(doc)) {
                    return new ArrayList<DirectMessage>(0);
                } else {
                    throw te;
                }
            }
        }
    }

    static List<DirectMessage> constructDirectMessages(Response res) throws WeiboException {
        JSONArray list = res.asJSONArray();
        try {
            int size = list.length();
            List<DirectMessage> messages = new ArrayList<DirectMessage>(size);
            for (int i = 0; i < size; i++) {
                messages.add(new DirectMessage(list.getJSONObject(i)));
            }
            return messages;
        } catch (JSONException jsone) {
            throw new WeiboException(jsone);
        }
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        return obj instanceof DirectMessage && ((DirectMessage) obj).id == this.id;
    }

    @Override
    public String toString() {
        return "DirectMessage{" + "id=" + id + ", text='" + text + '\'' + ", sender_id=" + sender_id + ", recipient_id=" + recipient_id + ", created_at=" + created_at + ", sender_screen_name='" + sender_screen_name + '\'' + ", recipient_screen_name='" + recipient_screen_name + '\'' + ", sender=" + sender + ", recipient=" + recipient + '}';
    }
}
