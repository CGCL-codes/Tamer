package org.adapit.wctoolkit.models.util.infra;

import java.util.ArrayList;
import org.adapit.wctoolkit.uml.ext.core.TaggedValue;

@SuppressWarnings({ "unchecked" })
public class TaggedValueUtil {

    public TaggedValueUtil() {
        super();
    }

    public static Object[] getBodyValuesFromTaggedValue(TaggedValue tv) {
        ArrayList arr = new ArrayList();
        if (tv != null) {
            int valueBegin = tv.getValue().indexOf("(");
            int valueEnd = tv.getValue().lastIndexOf(")");
            String value = tv.getValue().substring(valueBegin + 1, valueEnd);
            String[] values = value.split(",");
            for (int i = 0; i < values.length; i++) {
                try {
                    String s = values[i];
                    int sBegin = s.indexOf("'");
                    if (sBegin >= 0) {
                        int sEnd = s.lastIndexOf("'");
                        arr.add(s.substring(sBegin + 1, sEnd));
                    } else {
                        int sEnd = s.lastIndexOf("'");
                        if (sEnd > -1) arr.add(s.substring(0, sEnd)); else arr.add(s);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else return null;
        String[] strs = new String[arr.size()];
        for (int i = 0; i < arr.size(); i++) strs[i] = (String) arr.get(i);
        return strs;
    }

    protected static ArrayList cutString(String value, char cInit, char cFin) {
        int begin = 0;
        int end = 0;
        ArrayList sb = new ArrayList();
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (c == cInit) {
                begin = i;
            }
            if (c == cFin) {
                end = i;
                sb.add(value.substring(begin + 1, end));
            }
        }
        return sb;
    }

    public static ArrayList getValuesTVFromTextValue(String stValue) {
        ArrayList arr = new ArrayList();
        if (stValue != null) {
            arr = cutString(stValue, '{', '}');
        } else return null;
        return arr;
    }
}
