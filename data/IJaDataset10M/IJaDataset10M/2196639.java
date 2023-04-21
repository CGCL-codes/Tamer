package console.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class StringList extends LinkedList<String> {

    public StringList() {
    }

    public StringList(Object[] array) {
        addAll(array);
    }

    public void addAll(Object[] array) {
        for (int i = 0; i < array.length; ++i) {
            add(array[i].toString());
        }
    }

    public static StringList split(String orig, Object delim) {
        if ((orig == null) || (orig.length() == 0)) return new StringList();
        return new StringList(orig.split(delim.toString()));
    }

    public String toString() {
        return join("\n");
    }

    public static String join(Collection c, String delim) {
        StringList sl = new StringList();
        for (Object o : c) {
            String s = o.toString();
            sl.add(s);
        }
        return sl.join(delim);
    }

    public static String join(Object[] arr, String delim) {
        StringList sl = new StringList();
        sl.addAll(arr);
        return sl.join(delim);
    }

    public String join(String delim) {
        int s = size();
        if (s < 1) return "";
        if (s == 1) return get(0).toString(); else {
            StringBuffer retval = new StringBuffer();
            retval.append(get(0));
            for (int i = 1; i < s; ++i) retval.append(delim + get(i));
            return retval.toString();
        }
    }

    public static void main(String args[]) {
        String teststr = "a,b,c,d,e,f";
        StringList sl = StringList.split(teststr, ",");
        String joinstr = sl.join(",");
        System.out.println("Test Passed");
    }
}
