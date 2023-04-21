package org.p2pws.loaddistribution.message;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.math.RandomUtils;
import org.p2pws.loaddistribution.message.P2PMessage;

/**
 * Default implementation for any P2P message.
 * @author panisson
 *
 */
public class DefaultP2PMessage implements P2PMessage {

    public static final String ID_ELEMENT = "id";

    private String id = Long.toHexString(RandomUtils.nextLong());

    private Map<String, String> elements = new HashMap<String, String>();

    public DefaultP2PMessage() {
        this(null);
    }

    public DefaultP2PMessage(String id) {
        if (id != null) {
            this.id = id;
        } else {
            id = Long.toHexString(RandomUtils.nextLong());
        }
        this.addElement(ID_ELEMENT, id);
    }

    /**
	 * Returns false, as this is not a system message
	 */
    public boolean isSystemMessage() {
        return false;
    }

    public String getElement(String key) {
        return elements.get(key);
    }

    public void addElement(String key, String value) {
        elements.put(key, value);
    }

    public byte[] getByteArrayElement(String key) {
        return elements.get(key).getBytes();
    }

    public void addByteArrayElement(String key, byte[] value) {
        elements.put(key, new String(value));
    }

    public Map<String, String> getElements() {
        return elements;
    }

    /**
	 * 
	 * @return an unique id for the message
	 */
    public String getMessageId() {
        return id;
    }

    public boolean isForceExecution() {
        return false;
    }
}
