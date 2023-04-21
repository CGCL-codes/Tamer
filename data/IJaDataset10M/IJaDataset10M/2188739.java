package praktikumid.k09.p16;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Ivor
 *
 */
public class Mapid {

    static Map<String, Integer> map;

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        map = new HashMap<String, Integer>();
        map.put("seitse", 8);
        map.put("kaheksa", 8);
        map.put("viis", 4);
        System.out.println(map);
        System.out.println(map.get("kaks"));
        for (String key : map.keySet()) {
            System.out.println(key + "=" + map.get(key));
        }
    }
}
