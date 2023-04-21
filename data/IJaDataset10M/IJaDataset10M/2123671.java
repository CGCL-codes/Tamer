package de.grogra.util;

/**
 * This class implements a MIME type as specified by RFC 2045 and 2046.
 * A MIME type consists of the specification of a top-level media type
 * and a subtype, e.g., <code>text/plain</code>, and a (possibly empty)
 * set of parameters. In the string representation of a MIME type,
 * parameters are specified as the parameter <code>charset</code> in
 * <code>text/plain; charset=iso-8859-1</code>.
 * 
 * @author Ole Kniemeyer
 */
public final class MimeType implements java.io.Serializable {

    private static final long serialVersionUID = -2211034175691969125L;

    private static final StringMap EMPTY = new StringMap();

    public static final MimeType INVALID = new MimeType("invalid/invalid", null);

    /**
	 * The MIME type <code>text/plain</code> without parameters.
	 */
    public static final MimeType TEXT_PLAIN = new MimeType("text/plain", null);

    /**
	 * The MIME type <code>text/xml</code> without parameters.
	 */
    public static final MimeType TEXT_XML = new MimeType("text/xml", null);

    /**
	 * The MIME type <code>text/html</code> without parameters.
	 */
    public static final MimeType TEXT_HTML = new MimeType("text/html", null);

    /**
	 * The MIME type <code>application/xml</code> without parameters.
	 */
    public static final MimeType APPLICATION_XML = new MimeType("application/xml", null);

    /**
	 * The MIME type <code>application/octet-stream</code> without parameters.
	 */
    public static final MimeType OCTET_STREAM = new MimeType("application/octet-stream", null);

    /**
	 * The MIME type <code>application/x-gzip</code> without parameters.
	 */
    public static final MimeType GZIP = new MimeType("application/x-gzip", null);

    /**
	 * The MIME type <code>application/x-jar</code> without parameters.
	 */
    public static final MimeType JAR = new MimeType("application/x-jar", null);

    /**
	 * The MIME type <code>text/x-csv</code> without parameters.
	 */
    public static final MimeType CSV = new MimeType("text/x-csv", null);

    /**
	 * The MIME type <code>application/pdf</code> without parameters.
	 */
    public static final MimeType PDF = new MimeType("application/pdf", null);

    /**
	 * The MIME type <code>application/postscript</code> without parameters.
	 */
    public static final MimeType POSTSCRIPT = new MimeType("application/postscript", null);

    public static final String WRAPPED_TYPE_PARAM = "wrapped-type";

    /**
	 * The name <code>class</code> of the parameter which indicates the
	 * Java class of the MIME type {@link #JAVA_OBJECT}.
	 */
    public static final String CLASS_PARAM = "class";

    /**
	 * The MIME type of local Java objects,
	 * <code>application/x-java-jvm-local-objectref</code>, as string.
	 */
    public static final String JAVA_OBJECT = java.awt.datatransfer.DataFlavor.javaJVMLocalObjectMimeType;

    private final String mediaType;

    private final String primaryType;

    private final String subType;

    private final String comment;

    private final StringMap params;

    private final Class clazz;

    private transient int pos;

    private transient String buf;

    private static final String TSPECIALS = "()<>@,;:\\\"/[]?=";

    private transient StringBuffer cbuf = null;

    private transient boolean inComment = false;

    private int peekc() {
        if (pos >= buf.length()) {
            return -1;
        }
        if (!inComment && (buf.charAt(pos) == '(')) {
            if (cbuf == null) {
                cbuf = new StringBuffer();
            } else {
                cbuf.append(' ');
            }
            inComment = true;
            parseComment();
            inComment = false;
            return peekc();
        }
        return buf.charAt(pos);
    }

    private int getc() {
        int c = peekc();
        pos++;
        return c;
    }

    private void consumeWhitespace() {
        while (Character.isWhitespace((char) peekc())) {
            getc();
        }
    }

    private void consume(char c) {
        if (getc() != c) {
            throw new IllegalArgumentException(buf);
        }
    }

    private String parseToken() {
        StringBuffer b = new StringBuffer();
        while (true) {
            int c = peekc();
            if ((c > 32) && (c < 127) && (TSPECIALS.indexOf(c) < 0)) {
                b.append((char) getc());
            } else if (b.length() == 0) {
                throw new IllegalArgumentException(buf);
            } else {
                return b.toString();
            }
        }
    }

    private String parseQuoted() {
        consume('"');
        StringBuffer b = new StringBuffer();
        int c;
        while ((c = getc()) != '"') {
            if (c < 0) {
                throw new IllegalArgumentException(buf);
            }
            b.append((char) ((c == '\\') ? getc() : c));
        }
        return b.toString();
    }

    private void parseComment() {
        consume('(');
        int c;
        while ((c = peekc()) != ')') {
            if (c < 0) {
                throw new IllegalArgumentException(buf);
            }
            if (c == '(') {
                cbuf.append('(');
                parseComment();
                cbuf.append(')');
            } else {
                pos++;
                cbuf.append((char) ((c == '\\') ? getc() : c));
            }
        }
        consume(')');
    }

    /**
	 * Constructs a new <code>MimeType</code> for the given parameters.
	 * 
	 * @param mediaType the media type (e.g., <code>text/plain</code>)
	 * @param params the parameters of the MIME type
	 * @param cls the representation class of data of this MIME type 
	 */
    public MimeType(String mediaType, StringMap params, Class cls) {
        this.mediaType = mediaType;
        this.clazz = cls;
        int p = mediaType.indexOf('/');
        this.primaryType = mediaType.substring(0, p);
        this.subType = mediaType.substring(p + 1);
        if (params != null) {
            this.params = params.dup();
            if (cls != null) {
                this.params.remove(CLASS_PARAM);
            }
        } else {
            this.params = EMPTY;
        }
        this.comment = null;
    }

    /**
	 * Constructs a new <code>MimeType</code> for the given parameters.
	 * 
	 * @param mediaType the media type (e.g., <code>text/plain</code>)
	 * @param params the parameters of the MIME type
	 */
    public MimeType(String mediaType, StringMap params) {
        this(mediaType, params, null);
    }

    /**
	 * Parses <code>mimeType</code> and constructs a new <code>MimeType</code>
	 * using the parsed information. The complete syntax of RFC 2045 is
	 * supported.
	 * 
	 * @param mimeType MIME type specification according to RFC 2045
	 */
    public MimeType(String mimeType) {
        pos = 0;
        buf = mimeType;
        consumeWhitespace();
        primaryType = parseToken().toLowerCase();
        consume('/');
        subType = parseToken().toLowerCase();
        consumeWhitespace();
        StringMap p = null;
        while (getc() == ';') {
            consumeWhitespace();
            String name = parseToken().toLowerCase();
            consumeWhitespace();
            consume('=');
            consumeWhitespace();
            String value = (peekc() == '"') ? parseQuoted() : parseToken();
            consumeWhitespace();
            if (p == null) {
                p = new StringMap();
            }
            p.put(name, value);
        }
        if (pos < buf.length()) {
            throw new IllegalArgumentException(buf);
        }
        cbuf = null;
        buf = null;
        this.mediaType = primaryType + '/' + subType;
        this.clazz = null;
        this.params = (p != null) ? p : EMPTY;
        this.comment = (cbuf == null) ? null : cbuf.toString();
    }

    /**
	 * Returns a MIME type of media type
	 * {@link #JAVA_OBJECT} and representation class <code>cls</code>.
	 * 
	 * @param cls class of Java objects 
	 * @return MIME type corresponding to local Java objects
	 * of class <code>cls</code>
	 */
    public static MimeType valueOf(Class cls) {
        return new MimeType(JAVA_OBJECT, null, cls);
    }

    /**
	 * Parses <code>mimeType</code> and constructs a new
	 * <code>MimeType</code> as in {@link #MimeType(String)};
	 * if <code>mimeType</code> is <code>null</code>,
	 * <code>null</code> is returned.
	 * 
	 * @param mimeType MIME type specification according to RFC 2045
	 * @return <code>MimeType</code> instance or <code>null</code>
	 */
    public static MimeType valueOf(String mimeType) {
        return (mimeType == null) ? null : new MimeType(mimeType);
    }

    /**
	 * Returns the media type of this MIME type as <code>String</code>,
	 * e.g., <code>text/plain</code>. This does not contain parameters.
	 * 
	 * @return media type of this MIME type
	 */
    public String getMediaType() {
        return mediaType;
    }

    /**
	 * Returns the primary type of this MIME type, e.g.,
	 * <code>text</code> for the media type <code>text/plain</code>.
	 * 
	 * @return primary type of this MIME type
	 */
    public String getPrimaryType() {
        return primaryType;
    }

    /**
	 * Returns the subtype of this MIME type, e.g.,
	 * <code>plain</code> for the media type <code>text/plain</code>.
	 * 
	 * @return subtype of this MIME type
	 */
    public String getSubType() {
        return subType;
    }

    /**
	 * Returns the comment which has been parsed from the
	 * MIME type specification.
	 * 
	 * @return MIME type comment
	 */
    public String getComment() {
        return comment;
    }

    public String getParameter(String name) {
        return (CLASS_PARAM.equals(name) && (clazz != null)) ? clazz.getName() : (String) params.get(name);
    }

    public StringMap getParameters() {
        StringMap p = params.dup();
        if (clazz != null) {
            p.put(CLASS_PARAM, clazz.getName());
        }
        return p;
    }

    public Class getRepresentationClass() {
        return clazz;
    }

    public boolean isXMLMimeType() {
        return mediaType.equals(TEXT_XML) || mediaType.equals(APPLICATION_XML) || subType.endsWith("+xml");
    }

    public boolean isAssignableFrom(MimeType s) {
        return isAssignableFrom(mediaType, s.mediaType) && ((getParameter(WRAPPED_TYPE_PARAM) == null) || isAssignableFrom(getParameter(WRAPPED_TYPE_PARAM), s.getParameter(WRAPPED_TYPE_PARAM))) && ((clazz == null) || ((s.clazz != null) && clazz.isAssignableFrom(s.clazz)));
    }

    public static boolean isAssignableFrom(String target, String source) {
        return (target.equals(source) || target.equals(OCTET_STREAM.getMediaType())) && !(target.equals("invalid/invalid") || source.equals("invalid/invalid"));
    }

    @Override
    public boolean equals(Object o) {
        return (o == this) || ((o instanceof MimeType) && ((MimeType) o).mediaType.equals(mediaType) && Utils.equal(((MimeType) o).clazz, clazz));
    }

    @Override
    public int hashCode() {
        return mediaType.hashCode() ^ ((clazz != null) ? clazz.hashCode() : 0);
    }

    @Override
    public String toString() {
        StringMap p = params;
        if ((clazz == null) && (p.size() == 0)) {
            return mediaType;
        }
        StringBuffer b = new StringBuffer(mediaType);
        for (int i = 0; i < p.size(); i++) {
            String v = (String) p.getValueAt(i);
            if (v != null) {
                b.append("; ").append(p.getKeyAt(i)).append('=').append(quoteParameter(v));
            }
        }
        if (clazz != null) {
            b.append("; ").append(CLASS_PARAM).append('=').append(quoteParameter(clazz.getName()));
        }
        if (comment != null) {
            b.append(" (").append(comment).append(')');
        }
        return b.toString();
    }

    public static String quoteParameter(String s) {
        if (s.length() == 0) {
            return "\"\"";
        }
        for (int i = s.length() - 1; i >= 0; i--) {
            char c = s.charAt(i);
            if ((c <= 32) || (c >= 127) || (TSPECIALS.indexOf(c) >= 0)) {
                return Utils.quote(s);
            }
        }
        return s;
    }
}
