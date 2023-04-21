package ru.cybersms.model.obj;

/**
 *
 * @author Андрей Шерцингер <support@cybersms.ru>
 */
public class Message {

    private String recipients;

    private String message;

    private String sender;

    private Integer lifetime;

    public String getRecipients() {
        return recipients;
    }

    public void setRecipients(String recipients) {
        this.recipients = recipients;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Integer getLifetime() {
        return lifetime;
    }

    public void setLifetime(Integer lifetime) {
        this.lifetime = lifetime;
    }

    @Override
    public String toString() {
        return "Message{" + "recipients=" + recipients + ", message=" + message + ", sender=" + sender + ", lifetime=" + lifetime + '}';
    }
}
