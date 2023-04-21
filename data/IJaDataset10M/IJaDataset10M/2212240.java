package com.loribel.commons.module.timezone;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;
import com.loribel.commons.util.CTools;

/**
 * Tools for TimeZone.
 *
 * @author Gregory Borelli
 */
public abstract class GB_TimeZoneTools {

    public static final String toString(TimeZone a_tz) {
        if (a_tz == null) {
            return "TimeZone = null";
        }
        String retour = "(GMT";
        int l_hourOffset = a_tz.getRawOffset() / 3600000;
        if (l_hourOffset == 0) {
            retour += ")";
        } else if (l_hourOffset > 0) {
            if (l_hourOffset > 9) {
                retour += "+" + l_hourOffset + ":00)";
            } else {
                retour += "+0" + l_hourOffset + ":00)";
            }
        } else {
            if (l_hourOffset < -9) {
                retour += l_hourOffset + ":00)";
            } else {
                retour += "-0" + Math.abs(l_hourOffset) + ":00)";
            }
        }
        retour += " " + a_tz.getDisplayName() + " [" + a_tz.getID() + "]";
        return retour;
    }

    public static final GB_TimeZone[] getAllAvailable() {
        String[] l_ids = TimeZone.getAvailableIDs();
        List l_idList = CTools.toList(l_ids);
        TreeMap l_map = new TreeMap();
        int len = CTools.getSize(l_idList);
        for (int i = 0; i < len; i++) {
            TimeZone l_tz = TimeZone.getTimeZone((String) l_idList.get(i));
            GB_TimeZone l_item = new GB_TimeZone(l_tz);
            l_map.put(l_item, l_tz);
        }
        Set l_keys = l_map.keySet();
        List retour = new ArrayList(l_keys);
        return (GB_TimeZone[]) retour.toArray(new GB_TimeZone[0]);
    }

    public static final Date changeTimeZoneFromLocal(Date a_date, TimeZone a_newTimeZone) {
        TimeZone tzLocal = TimeZone.getDefault();
        long l_time = a_date.getTime();
        int l_offset = tzLocal.getOffset(l_time);
        int l_newOffset = a_newTimeZone.getOffset(l_time);
        int l_diff = (l_newOffset - l_offset);
        if (l_diff == 0) {
            return a_date;
        }
        return new Date(l_time + l_diff);
    }

    public static final boolean hasSameRulesOrNull(TimeZone a_tz1, TimeZone a_tz2) {
        if (a_tz1 == null) {
            return true;
        }
        if (a_tz2 == null) {
            return true;
        }
        return a_tz1.hasSameRules(a_tz2);
    }
}
