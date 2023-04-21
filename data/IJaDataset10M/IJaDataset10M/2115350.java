package net.sourceforge.jcoupling2.persistence;

import java.sql.Timestamp;
import org.apache.log4j.Logger;

/**
 * @author 
 * @version $Id: Message.java 16468 2010-03-09 16:13:15Z tbe $
 */
public class Message {

    private Integer messageID = null;

    private Integer channelID = null;

    private Timestamp timeStamp = null;

    private String body = null;

    private String attachement = null;

    private static final Logger logger = Logger.getLogger(Message.class);

    public Message() {
    }

    public Message(Integer msgID, Integer chID, Timestamp tStamp) {
        this();
        messageID = msgID;
        channelID = chID;
        timeStamp = tStamp;
    }

    public Message(Integer msgID) {
        this();
        messageID = msgID;
    }

    public Integer getID() {
        return messageID;
    }

    public void setID(Integer id) {
        messageID = id;
    }

    public Integer getChannelID() {
        return channelID;
    }

    public void setBody(String strBody) {
        body = strBody;
    }

    public String getBody() {
        return body;
    }

    public void setTimeStamp(Timestamp timestamp) {
        timeStamp = timestamp;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setAttachement(String attachement) {
        this.attachement = attachement;
    }

    public String getAttachement() {
        return attachement;
    }
}
