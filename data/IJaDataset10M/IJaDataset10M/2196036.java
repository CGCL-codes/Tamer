package cease.vo;

import net.sf.json.JSONObject;
import cease.json.JSONUtil;

/**
 * 私信
 * 
 * @author dhf
 */
public class Message {

    private String id;

    private UserInfo sender;

    private UserInfo recipient;

    private String sender_screen_name;

    private String recipient_id;

    private String recipient_screen_name;

    private String created_at;

    private String cursor;

    private String text;

    /**
     * @return the id
     */
    public String getId() {
        return this.id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the sender
     */
    public UserInfo getSender() {
        return this.sender;
    }

    /**
     * @param sender
     *            the sender to set
     */
    public void setSender(UserInfo sender) {
        this.sender = sender;
    }

    /**
     * @return the recipient
     */
    public UserInfo getRecipient() {
        return this.recipient;
    }

    /**
     * @param recipient
     *            the recipient to set
     */
    public void setRecipient(UserInfo recipient) {
        this.recipient = recipient;
    }

    /**
     * @return the sender_screen_name
     */
    public String getSender_screen_name() {
        return this.sender_screen_name;
    }

    /**
     * @param sender_screen_name
     *            the sender_screen_name to set
     */
    public void setSender_screen_name(String sender_screen_name) {
        this.sender_screen_name = sender_screen_name;
    }

    /**
     * @return the recipient_id
     */
    public String getRecipient_id() {
        return this.recipient_id;
    }

    /**
     * @param recipient_id
     *            the recipient_id to set
     */
    public void setRecipient_id(String recipient_id) {
        this.recipient_id = recipient_id;
    }

    /**
     * @return the recipient_screen_name
     */
    public String getRecipient_screen_name() {
        return this.recipient_screen_name;
    }

    /**
     * @param recipient_screen_name
     *            the recipient_screen_name to set
     */
    public void setRecipient_screen_name(String recipient_screen_name) {
        this.recipient_screen_name = recipient_screen_name;
    }

    /**
     * @return the created_at
     */
    public String getCreated_at() {
        return this.created_at;
    }

    /**
     * @param created_at
     *            the created_at to set
     */
    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    /**
     * @return the cursor
     */
    public String getCursor() {
        return this.cursor;
    }

    /**
     * @param cursor
     *            the cursor to set
     */
    public void setCursor(String cursor) {
        this.cursor = cursor;
    }

    /**
     * @return the text
     */
    public String getText() {
        return this.text;
    }

    /**
     * @param text
     *            the text to set
     */
    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return JSONObject.fromObject(this, JSONUtil.getJsonConfig()).toString();
    }
}
