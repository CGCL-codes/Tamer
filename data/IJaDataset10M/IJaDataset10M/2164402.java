package ca.sqlpower.architect.enterprise;

import org.json.JSONArray;
import org.json.JSONObject;
import ca.sqlpower.dao.MessageSender;
import ca.sqlpower.dao.SPPersistenceException;
import ca.sqlpower.dao.json.SPJSONMessageDecoder;

/**
 * Sends JSON messages directly to the JSON decoder
 */
public class DirectJsonMessageSender implements MessageSender<JSONObject> {

    private final SPJSONMessageDecoder decoder;

    private JSONArray array;

    public DirectJsonMessageSender(SPJSONMessageDecoder decoder) {
        this.decoder = decoder;
        this.array = new JSONArray();
    }

    public void clear() {
    }

    public void flush() throws SPPersistenceException {
        decoder.decode(array.toString());
        array = new JSONArray();
    }

    public void send(JSONObject content) throws SPPersistenceException {
        array.put(content);
    }
}
