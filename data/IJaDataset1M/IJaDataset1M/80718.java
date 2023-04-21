package com.liferay.portlet.messageboards.service.persistence;

import com.liferay.util.StringPool;
import java.io.Serializable;

/**
 * <a href="MBMessagePK.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 *
 */
public class MBMessagePK implements Comparable, Serializable {

    public String topicId;

    public String messageId;

    public MBMessagePK() {
    }

    public MBMessagePK(String topicId, String messageId) {
        this.topicId = topicId;
        this.messageId = messageId;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public int compareTo(Object obj) {
        if (obj == null) {
            return -1;
        }
        MBMessagePK pk = (MBMessagePK) obj;
        int value = 0;
        value = topicId.compareTo(pk.topicId);
        if (value != 0) {
            return value;
        }
        value = messageId.compareTo(pk.messageId);
        if (value != 0) {
            return value;
        }
        return 0;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        MBMessagePK pk = null;
        try {
            pk = (MBMessagePK) obj;
        } catch (ClassCastException cce) {
            return false;
        }
        if ((topicId.equals(pk.topicId)) && (messageId.equals(pk.messageId))) {
            return true;
        } else {
            return false;
        }
    }

    public int hashCode() {
        return (topicId + messageId).hashCode();
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(StringPool.OPEN_CURLY_BRACE);
        sb.append("topicId");
        sb.append(StringPool.EQUAL);
        sb.append(topicId);
        sb.append(StringPool.COMMA);
        sb.append(StringPool.SPACE);
        sb.append("messageId");
        sb.append(StringPool.EQUAL);
        sb.append(messageId);
        sb.append(StringPool.CLOSE_CURLY_BRACE);
        return sb.toString();
    }
}
