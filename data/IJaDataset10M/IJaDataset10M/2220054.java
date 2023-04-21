package wave.test.client;

import java.util.Map;
import com.google.gwt.core.client.JavaScriptObject;

/**
 * Very basic and initial implementation to provide access to read and update the wave state for a gadget.
 *
 * @author amoffat Alex Moffat
 */
public class WaveState {

    /**
     * Get the current value of the state property with the provided key.
     *
     * @param key The key.
     * @return The value, which is null if it's not been set yet.
     */
    public static native String get(String key);

    /**
     * Submit updates to the values of some state properties.
     *
     * @param newValues Map from key to new value for each property to change.
     */
    public static void submitDelta(Map<String, String> newValues) {
        JavaScriptObject json = null;
        for (Map.Entry<String, String> entry : newValues.entrySet()) {
            json = addValue(json, entry.getKey(), entry.getValue());
        }
        submitDelta(json);
    }

    /**
     * Helper method to construct a JavaScript map.
     *
     * @param map The map to update, if null a new map is constructed and returned.
     * @param key The key.
     * @param value The value for the key.
     * @return The updated map.
     */
    private static native JavaScriptObject addValue(JavaScriptObject map, String key, String value);

    /**
     * Submit the JavaScript json object to update the wave state.
     *
     * @param json The updates.
     */
    private static native void submitDelta(JavaScriptObject json);
}
