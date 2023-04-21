package pub.utils;

import java.util.*;

public class ArrayUtils {

    /** Does a linear search and returns true if and only if an
        'element' is within a 'list'. **/
    public static boolean inArray(Object element, Object[] list) {
        for (int i = 0; i < list.length; i++) {
            if (element.equals(list[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * Converts a list to an array of strings.
     */
    public static String[] listToStringArray(List l) {
        String[] results = new String[l.size()];
        for (int i = 0; i < l.size(); i++) {
            results[i] = (String) l.get(i);
        }
        return results;
    }

    public static String[] selectcol(java.sql.ResultSet rs) throws java.sql.SQLException {
        List l = new java.util.ArrayList();
        while (rs.next()) {
            l.add(rs.getString(1));
        }
        Object[] mystrings = l.toArray();
        String[] results = new String[mystrings.length];
        for (int i = 0; i < results.length; i++) {
            results[i] = (String) (mystrings[i]);
        }
        return results;
    }

    public static int[] selectcolAsInt(java.sql.ResultSet rs) throws java.sql.SQLException {
        String[] string_results = selectcol(rs);
        int[] int_results = new int[string_results.length];
        for (int i = 0; i < int_results.length; i++) int_results[i] = Integer.parseInt(string_results[i]);
        return int_results;
    }

    /** Given a list of strings, returns a new array of strings.
        Maintains the relative order. **/
    public static String[] uniq(String[] strings) {
        Map h = new HashMap();
        List l = new ArrayList();
        for (int i = 0; i < strings.length; i++) {
            if (h.containsKey(strings[i])) {
                ;
            } else {
                l.add(strings[i]);
                h.put(strings[i], "1");
            }
        }
        h = null;
        String[] results = new String[l.size()];
        for (int i = 0; i < l.size(); i++) {
            results[i] = (String) l.get(i);
        }
        return results;
    }
}
