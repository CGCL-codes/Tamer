package isg3.mailBox;

import java.util.*;

public class Message {

    private String from;

    private String to;

    private String subject;

    private String content;

    private Date date;

    private String idMessage;

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getSubject() {
        return subject;
    }

    public String getContent() {
        return content;
    }

    public Date getDate() {
        return date;
    }

    public String getIdMessage() {
        return idMessage;
    }

    public void setIdMessage(String id) {
        this.idMessage = id;
    }

    public Message(String from, String to, String subject, String content, Date date) {
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.content = content;
        this.date = date;
    }
}
