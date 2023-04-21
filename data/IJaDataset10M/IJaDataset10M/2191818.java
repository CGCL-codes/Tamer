package net.sf.dub.application.sqlmore;

/**
 * Handler for on screen text formatting
 */
public class Text {

    protected static boolean trailingSemicolon(String p_sql) {
        int v_len = p_sql.length() - 1;
        if (v_len > 0 && p_sql.charAt(v_len) == ';') return true;
        return false;
    }

    protected static boolean trailingSlash(String p_sql) {
        int v_len = p_sql.length() - 1;
        if (v_len > 0 && p_sql.charAt(v_len) == '/') return true;
        return false;
    }

    protected static String rpad(String p_src, int p_len) {
        if (p_src == null) return p_src;
        StringBuffer v_pad = new StringBuffer(p_len);
        v_pad.setLength(p_len);
        for (int i = 0; i < v_pad.length(); i++) {
            v_pad.setCharAt(i, ' ');
        }
        int v_len = p_src.length();
        if (p_len <= v_len) return p_src;
        v_pad.insert(0, p_src);
        String v_rval = v_pad.toString();
        return v_rval;
    }
}
