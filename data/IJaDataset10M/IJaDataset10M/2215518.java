package inline.sys;

import java.util.*;

public class Globalization extends Hashtable {

    private static Hashtable mytable;

    public static final int ENGLISH_LCID = 1033;

    public static final int RUSSIAN_LCID = 1049;

    private static int system_lcid;

    protected static final String[] langstmap = { "en-en", "ru-ru" };

    protected static int[] langintmap = { ENGLISH_LCID, RUSSIAN_LCID };

    public void remap(Integer[] langs, String[] table) {
        int syslcid = getSystemLCID();
        int id = 0;
        if (langs == null || table == null) {
            return;
        }
        for (int i = 0; i < langs.length; i++) {
            if (langs[i].intValue() == syslcid) {
                id = i;
                break;
            }
        }
        clear();
        for (int i = 0; i < table.length; i = i + 3) {
            String td = table[i];
            String tx = table[i + 1 + id];
            if (tx == null) tx = table[i + 1];
            if (put(td, tx) != null) {
                Log.fire("Dublicate lang string at " + Integer.toHexString(td.charAt(0)));
            }
        }
    }

    public String take(int code) {
        String res = (String) get("" + (char) code);
        if (res == null) res = "?";
        return res;
    }

    public static void setSystemLCID(int olcid) {
        system_lcid = olcid;
    }

    public static int getSystemLCID() {
        int syslcid = ENGLISH_LCID;
        if (system_lcid != 0) {
            syslcid = system_lcid;
        } else {
            String locale = System.getProperty("microedition.locale").toLowerCase();
            for (int i = 0; i < langstmap.length; i++) {
                if (langstmap[i].compareTo(locale) == 0) {
                    syslcid = langintmap[i];
                    break;
                }
            }
            system_lcid = syslcid;
        }
        ;
        return syslcid;
    }
}
