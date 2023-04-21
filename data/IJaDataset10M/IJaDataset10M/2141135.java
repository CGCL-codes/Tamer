package com.jchapman.jempire.aiutils;

import java.util.*;
import com.jchapman.jempire.map.MapLocation;

/**
 *
 * @author Jeff Chapman
 * @version 1.0
 */
public class ContinentInfoManager {

    private Map continentInfoMap = new HashMap();

    public ContinentInfoManager() {
        super();
    }

    public ContinentInfo getContinentInfo(int id) {
        Integer key = new Integer(id);
        ContinentInfo cInfo = (ContinentInfo) continentInfoMap.get(key);
        if (cInfo == null && id != MapLocation.NON_CONTINENT_LOCATION) {
            cInfo = new ContinentInfoImpl(id);
            continentInfoMap.put(key, cInfo);
        }
        return cInfo;
    }

    public void clearFriendlyUnitCounts() {
        Iterator iter = continentInfoMap.values().iterator();
        while (iter.hasNext()) {
            ContinentInfoImpl contInfo = (ContinentInfoImpl) iter.next();
            contInfo.clearFriendlyUnitCount();
        }
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        Iterator iter = continentInfoMap.values().iterator();
        while (iter.hasNext()) {
            ContinentInfoImpl contInfo = (ContinentInfoImpl) iter.next();
            buf.append(contInfo.toString());
            buf.append("\n-------------------------------------\n");
        }
        return buf.toString();
    }
}
