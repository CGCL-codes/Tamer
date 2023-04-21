package net.sf.gridarta.textedit.textarea.tokenmarker;

/**
 * Stores information about tokenized lines.
 * @author Slava Pestov
 * @author Andreas Kirschbaum
 */
public class LineInfo {

    /**
     * Creates a new LineInfo object with token = Token.NULL and obj = null.
     */
    public LineInfo() {
    }

    /**
     * Creates a new LineInfo object with the specified parameters.
     */
    public LineInfo(final byte token, final Object obj) {
        this.token = token;
        this.obj = obj;
    }

    /**
     * The id of the last token of the line.
     */
    private byte token;

    /**
     * This is for use by the token marker implementations themselves. It can be
     * used to store anything that is an object and that needs to exist on a
     * per-line basis.
     */
    private Object obj;

    public byte getToken() {
        return token;
    }

    public void setToken(final byte token) {
        this.token = token;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(final Object obj) {
        this.obj = obj;
    }
}
