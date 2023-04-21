package org.snova.framework.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import org.arch.util.ListSelector;

/**
 * @author qiyingwang
 *
 */
public class HostsHelper {

    private static Map<String, ListSelector<String>> hostsMappingTable = new HashMap<String, ListSelector<String>>();

    static {
        InputStream is = HostsHelper.class.getResourceAsStream("/hosts.conf");
        Properties props = new Properties();
        try {
            props.load(is);
            Set<Entry<Object, Object>> entries = props.entrySet();
            for (Entry<Object, Object> entry : entries) {
                String key = (String) entry.getKey();
                String value = (String) entry.getValue();
                key = key.trim();
                value = value.trim();
                String[] splits = value.split("[,|;|\\|]");
                List<String> mappings = Arrays.asList(splits);
                hostsMappingTable.put(key, new ListSelector<String>(mappings));
            }
            is.close();
        } catch (IOException e) {
        }
    }

    public static String getMappingHost(String host) {
        ListSelector<String> selector = hostsMappingTable.get(host);
        if (null == selector) {
            return host;
        }
        return selector.select().trim();
    }

    public static void removeMapping(String host, String mapping) {
        ListSelector<String> selector = hostsMappingTable.get(host);
        if (null != selector) {
            selector.remove(mapping);
        }
    }
}
