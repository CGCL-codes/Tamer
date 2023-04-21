package testing;

import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.Map.Entry;

public class TestSystemProperties {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        Properties props = System.getProperties();
        Map<String, String> map = new TreeMap<String, String>();
        for (Entry<Object, Object> entry : props.entrySet()) {
            map.put((String) entry.getKey(), (String) entry.getValue());
        }
        for (Entry<String, String> entry : map.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }
    }
}
