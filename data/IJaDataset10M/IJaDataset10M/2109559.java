package de.spieleck.config;

import java.util.HashMap;
import java.util.Map;

/**
 * Easy implementation of ConfigParamMap
 * @author fsn
 */
public class SimpleParamMap implements ConfigParamMap, SimpleParamGetter {

    protected Map data;

    public SimpleParamMap() {
        data = new HashMap();
    }

    public String expand(String input) {
        return expand(input, this);
    }

    public void set(String key, String value) {
        data.put(key, value);
    }

    public String getParam(String key) {
        Object o = data.get(key);
        if (o == null) return (String) null; else return o.toString();
    }

    /**
     * static implementation of an expand method, that
     * resembles Ant ${param} behaviour. Actually code
     * is inspired but changed from code within Ant's 
     * ProjectHelper. (For example this code allows 
     * recursive evalutaion of expressions, whatever this
     * is good for.
     */
    public static String expand(String input, SimpleParamGetter pg) {
        if (input == null) return input;
        int pos = input.indexOf("$");
        if (pos < 0) return input;
        try {
            int len = input.length();
            int prev = 0;
            StringBuffer sb = new StringBuffer(len + 100);
            do {
                if (pos > prev) sb.append(input.substring(prev, pos));
                int p1 = pos + 1;
                if (p1 < len && input.charAt(p1) != '{') {
                    if (input.charAt(p1) == '$') sb.append('$'); else sb.append(input.substring(pos, pos + 2));
                    prev = pos + 2;
                } else {
                    int end = input.indexOf('}', pos);
                    if (end > -1) {
                        String key = expand(input.substring(pos + 2, end), pg);
                        String res = pg.getParam(key);
                        if (res == null) sb.append(key); else sb.append(res);
                        prev = end + 1;
                    } else {
                        sb.append("${");
                        prev = pos + 2;
                    }
                }
            } while ((pos = input.indexOf("${", prev)) >= 0);
            sb.append(input.substring(prev));
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return input;
        }
    }
}
