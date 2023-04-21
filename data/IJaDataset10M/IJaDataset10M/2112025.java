package effective.java.instance;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author WangShuai
 */
public class item5 {

    public static void main(String[] args) {
        Map m = new HashMap();
        m.put("key1", "value1");
        Set s = m.keySet();
        m.put("key2", "value2");
        for (Iterator i = s.iterator(); i.hasNext(); ) {
            System.out.println(i.next());
        }
    }
}
