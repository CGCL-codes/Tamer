package org.eclipsetrader.core.internal.markets;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class WeekdaysAdapter extends XmlAdapter<String, Set<Integer>> {

    public WeekdaysAdapter() {
    }

    @Override
    public String marshal(Set<Integer> v) throws Exception {
        if (v == null) return null;
        StringBuilder result = new StringBuilder();
        result.append(v.contains(Calendar.SUNDAY) ? 'S' : '-');
        result.append(v.contains(Calendar.MONDAY) ? 'M' : '-');
        result.append(v.contains(Calendar.TUESDAY) ? 'T' : '-');
        result.append(v.contains(Calendar.WEDNESDAY) ? 'W' : '-');
        result.append(v.contains(Calendar.THURSDAY) ? 'T' : '-');
        result.append(v.contains(Calendar.FRIDAY) ? 'F' : '-');
        result.append(v.contains(Calendar.SATURDAY) ? 'S' : '-');
        return result.toString();
    }

    @Override
    public Set<Integer> unmarshal(String v) throws Exception {
        if (v == null) return null;
        Set<Integer> result = new HashSet<Integer>();
        if (v.charAt(0) != '-') result.add(Calendar.SUNDAY);
        if (v.charAt(1) != '-') result.add(Calendar.MONDAY);
        if (v.charAt(2) != '-') result.add(Calendar.TUESDAY);
        if (v.charAt(3) != '-') result.add(Calendar.WEDNESDAY);
        if (v.charAt(4) != '-') result.add(Calendar.THURSDAY);
        if (v.charAt(5) != '-') result.add(Calendar.FRIDAY);
        if (v.charAt(6) != '-') result.add(Calendar.SATURDAY);
        return result;
    }
}
