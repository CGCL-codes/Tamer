package fb4java.beans.interfaces;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * fb4java<br />
 * fb4java.beans.interfaces
 * 
 * @author Choongsan Ro
 * @version 1.0 2010. 2. 26.
 */
public interface Parameterable {

    public String getFields();

    public void setJSONObject(JSONObject jObj) throws JSONException;
}
