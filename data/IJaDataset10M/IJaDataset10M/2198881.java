package org.jtools.util.auth.digest;

class Message {

    protected static enum Required {

        OPTIONAL, MANDATORY
    }

    protected static enum Quoted {

        QUOTED, UNQUOTED
    }

    public static Algorithm getAlgorithm(String literal) {
        if (literal == null || literal.isEmpty()) return null;
        for (Algorithm algorithm : Algorithm.values()) if (algorithm.getLiteral().equals(literal)) return algorithm;
        throw new RuntimeException("unsupported algorithm '" + literal + "'");
    }

    public static Qop getQop(String literal) {
        if (literal == null || literal.isEmpty()) return null;
        for (Qop qop : Qop.values()) if (qop.getLiteral().equals(literal)) return qop;
        return null;
    }

    protected static final String PREFIX = "Digest ";

    protected static final String INFOPREFIX = "Authentication-Info:";

    protected static final String[] REALM = { " realm=\"", ",realm=\"" };

    protected static final String[] DOMAIN = { " domain=\"", ",domain=\"" };

    protected static final String[] NONCE = { " nonce=\"", ",nonce=\"" };

    protected static final String[] NEXTNONCE = { " nextnonce=\"", ",nextnonce=\"" };

    protected static final String[] OPAQUE = { " opaque=\"", ",opaque=\"" };

    protected static final String[] STALE = { " stale=", ",stale=" };

    protected static final String[] ALGORITHM = { " algorithm=", ",algorithm=" };

    protected static final String[] QOP_VALUES = { " qop=\"", ",qop=\"" };

    protected static final String[] QOP = { " qop=", ",qop=" };

    protected static final String[] CNONCE = { " cnonce=\"", ",cnonce=\"" };

    protected static final String[] NC = { " nc=", ",nc=" };

    protected static final String[] RESPONSE = { " response=\"", ",response=\"" };

    protected static final String[] RSPAUTH = { " rspauth=\"", ",rspauth=\"" };

    protected static final String[] USERNAME = { " username=\"", ",username=\"" };

    protected static final String[] URI = { " uri=\"", ",uri=\"" };

    protected String parse(Required required, Quoted quoted, String src, String pre[], String soll) {
        String result = Quoted.QUOTED.equals(quoted) ? parse(src, pre, soll) : parsenq(src, pre);
        if (Required.MANDATORY.equals(required) && result == null) throw new NullPointerException(pre[0]);
        return result;
    }

    private String parse(String src, String pre[], String soll) {
        String p = null;
        int i = -1;
        for (int j = 0; i < 0 && j < pre.length; j++) {
            p = pre[j];
            i = src.indexOf(p);
        }
        if (i < 0) return null;
        int j = src.indexOf('"', i + p.length());
        if (j < 0) j = src.length();
        if (soll != null && j != i + p.length() + soll.length()) throw new RuntimeException();
        String r = src.substring(i + p.length(), j);
        if (soll != null && !soll.equals(r)) throw new RuntimeException();
        return r;
    }

    private String parsenq(String src, String[] pre) {
        String p = null;
        int i = -1;
        for (int j = 0; i < 0 && j < pre.length; j++) {
            p = pre[j];
            i = src.indexOf(p);
        }
        if (i < 0) return null;
        if (src.length() == (i + p.length())) return "";
        if (src.charAt(i + p.length()) == '"') return parse(src, new String[] { p + "\"" }, null);
        int j = src.indexOf(',', i + p.length());
        if (j < 0) j = src.length();
        String r = src.substring(i + p.length(), j);
        return r;
    }
}
