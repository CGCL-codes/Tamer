package gumbo.json;

import gumbo.core.util.AssertUtils;
import gumbo.net.msg.MessageIOException;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * An immutable Java "ping response" message. Response message sent by a remote network
 * node when a ping request message is received, which indicates that the remote node is
 * alive.
 * @author Jon Barrilleaux (jonb@jmbaai.com) of JMB and Associates Inc.
 */
public class JsonPingResponse extends JsonTimedMessage {

    /**
	 * Used ONLY for de-serialization.
	 */
    public JsonPingResponse() {
        super(null);
    }

    /**
	 * Creates an instance. The ID is that of the request. The current time is
	 * the start time.
	 * @param request Request message prompting this response message. Never
	 * null.
	 */
    public JsonPingResponse(JsonPingRequest request) {
        super(request.getId());
        AssertUtils.assertNonNullArg(request);
        _request = request;
    }

    /**
	 * Gets the request message that prompted this response message.
	 * @return The result.  Never null.
	 */
    public JsonPingRequest getRequestMsg() {
        return _request;
    }

    @Override
    public String reportTransitTime() {
        return reportTransitTime(_request.getStartTime(), getFinishTime());
    }

    @Override
    public void deserializeJson(JSONObject objState) {
        AssertUtils.assertNonNullArg(objState);
        try {
            String thatType = objState.getString(JsonMessage.JSON_TYPE_KEY);
            if (!JSON_TYPE.equals(thatType)) MessageIOException.doThrow(this, "Wrong type. this=" + JSON_TYPE + " that=" + thatType);
            super.deserializeJson(objState.getJSONObject(JsonMessage.JSON_SUPER_KEY));
            _request = new JsonPingRequest();
            _request.deserializeJson(objState.getJSONObject("request"));
        } catch (JSONException ex) {
            MessageIOException.doThrow(ex, this, "Missing entry in JSON object.");
        }
    }

    @Override
    public String serializeJson() {
        return "{" + JSONObject.quote(JsonMessage.JSON_TYPE_KEY) + ":" + JSONObject.quote(JSON_TYPE) + "," + JSONObject.quote(JsonMessage.JSON_SUPER_KEY) + ":" + super.serializeJson() + "," + JSONObject.quote("request") + ":" + _request.serializeJson() + "}";
    }

    @Override
    public String toString() {
        return "JsonPingResponse: id=" + getId() + " " + reportTransitTime();
    }

    private JsonPingRequest _request;

    public static final String JSON_TYPE = "JsonPingStatus";
}
