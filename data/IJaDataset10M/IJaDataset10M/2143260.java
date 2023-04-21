package name.dlazerka.gm;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class Mark extends HashMap<Object, Object> {

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Object, Object> entry : this.entrySet()) {
            sb.append(',');
            Object key = entry.getKey();
            Object value = entry.getValue();
            if (key != null) {
                sb.append(key).append('=');
            }
            sb.append(value);
        }
        sb.deleteCharAt(0);
        return sb.toString();
    }
}
