package study.c;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Map1 {

    public static void main(String[] args) {
        Map m = new HashMap();
        Set s = m.keySet();
        m.put("a", 1);
        System.out.println(s.toString());
        System.out.println(m.get("a"));
        s.remove("a");
        System.out.println(s.toString());
        System.out.println(m.get("a"));
    }
}
