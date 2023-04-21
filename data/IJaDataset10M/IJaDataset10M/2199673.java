package si.mk.k3.util;

public class StrStrPair implements Comparable<StrStrPair> {

    private String m_key;

    private String m_value;

    public StrStrPair(String key, String value) {
        m_key = key;
        m_value = value;
    }

    public String getKey() {
        return m_key;
    }

    public String getValue() {
        return m_value;
    }

    /**
     * Compares only 'key' part.
     * 
     * @param sp
     * @return
     */
    public int compareTo(StrStrPair sp) {
        return m_key.compareTo(sp.m_key);
    }
}
