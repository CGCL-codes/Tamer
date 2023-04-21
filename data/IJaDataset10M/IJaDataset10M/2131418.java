package org.ikasan.tools.messaging.model;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

public class MessageWrapper {

    private Logger logger = Logger.getLogger(MessageWrapper.class);

    protected Map<String, Object> properties = new HashMap<String, Object>();

    protected Map<String, Object> messagingProperties = new HashMap<String, Object>();

    protected String messageId;

    protected Long timestamp;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        logger.info("called with messageId:" + messageId);
        this.messageId = messageId;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public Map<String, Object> getMessagingProperties() {
        return messagingProperties;
    }

    public MessageWrapper(Map<String, Object> properties) {
        this.properties = properties;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((messageId == null) ? 0 : messageId.hashCode());
        result = prime * result + ((properties == null) ? 0 : properties.hashCode());
        result = prime * result + ((messagingProperties == null) ? 0 : messagingProperties.hashCode());
        result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        MessageWrapper other = (MessageWrapper) obj;
        if (messageId == null) {
            if (other.messageId != null) return false;
        } else if (!messageId.equals(other.messageId)) return false;
        if (properties == null) {
            if (other.properties != null) return false;
        } else if (!properties.equals(other.properties)) return false;
        if (messagingProperties == null) {
            if (other.messagingProperties != null) return false;
        } else if (!messagingProperties.equals(other.messagingProperties)) return false;
        if (timestamp == null) {
            if (other.timestamp != null) return false;
        } else if (!timestamp.equals(other.timestamp)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "MessageWrapper [messageId=" + messageId + ", properties=" + properties + ", messagingProperties=" + messagingProperties + ", timestamp=" + timestamp + "]";
    }

    public void setMessagingProperties(Map<String, Object> messagingProperties) {
        this.messagingProperties = messagingProperties;
    }
}
