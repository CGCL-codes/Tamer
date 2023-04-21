package com.util;

public class HtmlEncode {

    String re;

    public String replace(String con, String tag, String rep) {
        if (con != null) {
            int j = 0;
            int i = 0;
            String RETU = "";
            String temp = con;
            int tagc = tag.length();
            while (i < con.length()) {
                if (con.substring(i).startsWith(tag)) {
                    temp = con.substring(j, i) + rep;
                    RETU += temp;
                    i += tagc;
                    j = i;
                } else {
                    i += 1;
                }
            }
            RETU += con.substring(j);
            return RETU;
        } else {
            return null;
        }
    }

    public String HtmlEncodeme(String s) {
        re = replace(s, "<", "&lt;");
        re = replace(re, ">", "&gt;");
        re = replace(re, "\n", "<br />");
        re = replace(re, " ", "&nbsp;");
        re = replace(re, "'", "&#39");
        re = replace(re, "\"", "&#34");
        re = replace(re, "<p>", "<br />");
        re = replace(re, "</p>", "<br />");
        return re;
    }

    public String SimpleEncodeme(String s) {
        re = replace(s, "'", "&#39");
        return re;
    }
}
