package it.unipr.ce.dsg.s2p.message;

/**
 * Class <code>BasicMessage</code> implements a basic
 * message for peer. The field are:<br>
 * -type: for the type of message (es.PING)<br> 
 * -timestamp: instant delivery<br>
 * -payload: data structure to hold any information
 *  
 * @author Fabrizio Caramia
 *
 */
public class BasicMessage {

    private String type;

    private long timestamp;

    private Payload payload;

    /**
	 * Create a new BasicMessage
	 * 
	 */
    public BasicMessage() {
    }

    /**
	 * Create a new BasicMessage
	 * 
	 * @param type type of the message
	 * @param payload payload of the message
	 */
    public BasicMessage(String type, Payload payload) {
        setType(type);
        setPayload(payload);
        setTimestamp(System.currentTimeMillis());
    }

    /**
	 * Get the type of the message
	 * 
	 * @return String type
	 */
    public String getType() {
        return type;
    }

    /**
	 * Set the type of the message 
	 * 
	 * @param type type of the message
	 */
    public void setType(String type) {
        this.type = type;
    }

    /**
	 * Get the timestamp of the message
	 * 
	 * @return long timestamp
	 */
    public long getTimestamp() {
        return timestamp;
    }

    /**
	 * Set the timestamp of the message 
	 * 
	 * @param timestamp instant delivery
	 */
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    /**
	 * Get the payload of the message
	 * 
	 * @return payload payload of the message
	 */
    public Payload getPayload() {
        return payload;
    }

    /**
	 * Set the payload of the message 
	 * 
	 * @param payload payload of the message 
	 */
    public void setPayload(Payload payload) {
        this.payload = payload;
    }

    /**
	 * Return a String with all informations
	 * 
	 */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        String NEW_LINE = System.getProperty("line.separator");
        result.append("timestamp: " + getTimestamp() + NEW_LINE);
        result.append("type: " + getType() + NEW_LINE);
        result.append("payload: " + getPayload().toString() + NEW_LINE);
        return result.toString();
    }
}
