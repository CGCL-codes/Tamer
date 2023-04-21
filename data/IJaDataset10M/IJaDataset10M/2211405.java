package com.kyte.api.model;

import com.kyte.api.rest.KyteSession;
import com.kyte.api.util.ApiUtil;
import com.kyte.api.util.StructHelper;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import static com.kyte.api.rest.KyteInvoker.*;

/**
 *  Message resource
 *
 */
public class Message implements ApiModel {

    public static final String ATTRIBUTE_FROM_NAME = "fromName";

    public static final String ATTRIBUTE_MEDIA_URI = "mediaUri";

    public static final String ATTRIBUTE_MESSAGE_INDEX = "messageIndex";

    public static final String ATTRIBUTE_TEXT = "text";

    public static final String ATTRIBUTE_TIME = "time";

    protected String fromName;

    protected String mediaUri;

    protected Integer messageIndex;

    protected String text;

    protected Date time;

    /**
     *  Default constructor
     */
    public Message() {
    }

    /**
     *  constructs from a Map
     */
    public Message(Map<String, Object> map) {
        fromName = (String) map.get(ATTRIBUTE_FROM_NAME);
        mediaUri = (String) map.get(ATTRIBUTE_MEDIA_URI);
        messageIndex = StructHelper.getIntegerObjectAttribute(map, ATTRIBUTE_MESSAGE_INDEX);
        text = (String) map.get(ATTRIBUTE_TEXT);
        time = StructHelper.getDateObjectAttribute(map, ATTRIBUTE_TIME);
    }

    static final String[] _downNames = { ATTRIBUTE_FROM_NAME, ATTRIBUTE_MEDIA_URI, ATTRIBUTE_MESSAGE_INDEX, ATTRIBUTE_TEXT, ATTRIBUTE_TIME };

    /**
     * @return member names for serialization from server
     */
    public String[] _downMemberNames() {
        return _downNames;
    }

    static final String[] _upNames = {};

    /**
     * @return member names for serialization to server
     */
    public String[] _upMemberNames() {
        return _upNames;
    }

    public String getFromName() {
        return fromName;
    }

    public String getMediaUri() {
        return mediaUri;
    }

    public Integer getMessageIndex() {
        return messageIndex;
    }

    public String getText() {
        return text;
    }

    public Date getTime() {
        return time;
    }

    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Message [");
        sb.append("fromName=").append(fromName);
        sb.append(", mediaUri=").append(mediaUri);
        sb.append(", messageIndex=").append(messageIndex);
        sb.append(", text=").append(text);
        sb.append(", time=").append(time);
        sb.append("]");
        return sb.toString();
    }
}
