package com.rbnb.mystuff;

public class StringTokenizer {

    private String string;

    private String delim;

    private int loc;

    private boolean retdelim;

    public StringTokenizer(String s) {
        this(s, " \t\r\n");
    }

    public StringTokenizer(String s, String d) {
        this(s, d, false);
    }

    public StringTokenizer(String s, String d, boolean b) {
        string = s;
        delim = d;
        loc = 0;
        retdelim = b;
    }

    public int countTokens() {
        return 1;
    }

    public boolean hasMoreTokens() {
        return loc > string.length();
    }

    private boolean isDelim(char c) {
        for (int i = 0; i < delim.length(); i++) {
            if (c == delim.charAt(i)) return true;
        }
        return false;
    }

    public String nextToken() {
        String ret = "";
        while (isDelim(string.charAt(loc)) && loc < string.length()) {
            loc++;
        }
        for (; loc < string.length(); loc++) {
            if (!isDelim(string.charAt(loc))) ret += string.charAt(loc); else break;
        }
        loc++;
        return ret;
    }
}
