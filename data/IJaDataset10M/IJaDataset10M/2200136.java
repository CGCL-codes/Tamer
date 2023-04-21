package javax.speech;

import java.util.Enumeration;
import java.util.Vector;

public abstract class SpeechEvent {

    private final Object source;

    private final int id;

    public static final int DISABLE_ALL = 0;

    public static final int ENABLE_ALL = -1;

    public SpeechEvent(Object source, int id) {
        this.source = source;
        this.id = id;
    }

    public Object getSource() {
        return source;
    }

    public int getId() {
        return id;
    }

    /**
     * Appends a human readable representation of the id to the given
     * representation.
     * @param str the current buffer. 
     */
    protected void id2String(StringBuffer str) {
        if (str.length() == 0) {
            str.append(id);
        }
    }

    /**
     * Checks if the given flag is set in the id and adds a human readable
     * description to the existing description if the flag is set.
     * @param str the existing description
     * @param flag the flag to check for
     * @param description the description to add
     */
    protected void maybeAddId(StringBuffer str, int flag, String description) {
        if ((id & flag) == flag) {
            if (str.length() > 0) {
                str.append('|');
            }
            str.append(description);
        }
    }

    /**
     * Creates a collection of all parameters.
     * 
     * @return collection of all parameters.
     */
    protected Vector getParameters() {
        final Vector parameters = new Vector();
        final Object source = getSource();
        parameters.addElement(source);
        final StringBuffer str = new StringBuffer();
        id2String(str);
        final String identifier = str.toString();
        parameters.addElement(identifier);
        return parameters;
    }

    public String paramString() {
        final StringBuffer str = new StringBuffer();
        final Vector parameters = getParameters();
        Enumeration enumeration = parameters.elements();
        while (enumeration.hasMoreElements()) {
            final Object parameter = enumeration.nextElement();
            str.append(parameter);
            if (enumeration.hasMoreElements()) {
                str.append(",");
            }
        }
        return str.toString();
    }

    public String toString() {
        StringBuffer str = new StringBuffer();
        str.append(getClass().getName());
        str.append("[");
        str.append(paramString());
        str.append("]");
        return str.toString();
    }
}
