package si.mk.k3.model;

/**
 * This class contains short and long description of library, K3Class
 * or K3Element.  The long description may start with '<html>' tag,
 * anf may contain html formatting tags.
 */
public class Description {

    private String m_short;

    private StringBuilder m_long;

    public Description(String shortDesc) {
        m_short = shortDesc;
        m_long = new StringBuilder();
    }

    public Description(String shortDesc, String longDesc) {
        m_short = shortDesc;
        m_long = new StringBuilder(longDesc);
    }

    public void appendLong(char text[], int start, int len) {
        m_long.append(text, start, len);
    }

    public String getLong() {
        return m_long.toString();
    }

    public String getShort() {
        return m_short;
    }
}
