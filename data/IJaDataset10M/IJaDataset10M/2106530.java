package org.moonwave.dconfig.util;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.moonwave.dconfig.model.DConfigKey;

/**
 *
 * @author Jonathan Luo
 */
public class DConfigKeyUtil {

    public DConfigKeyUtil() {
    }

    /**
     * 
     * @param keyList
     * @param level 0-indexed, level 0 is the root level.
     * @return array of key names for matched level.
     */
    public static String[] getKeyNameByLevel(List keyList, int level) {
        String[] prevKeys;
        String[] keys = new String[10];
        List resultList = new ArrayList();
        if (level > 0) {
            for (int i = 0; i < keyList.size(); i++) {
                DConfigKey regKey = (DConfigKey) keyList.get(0);
                prevKeys = StringUtils.split(regKey.getKeyName(), ".");
                String key = prevKeys[level - 1];
                if (!resultList.contains(key)) resultList.add(key);
            }
        }
        return (String[]) resultList.toArray();
    }
}
