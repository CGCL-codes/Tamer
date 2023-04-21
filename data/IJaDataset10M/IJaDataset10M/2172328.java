package herschel.phs.prophandler.tools.missionevolver.data;

import java.io.Serializable;

public class Info {

    public static final String FIELD = "Field";

    public static final String VALUE = "Value";

    public static final int N_FIELD = 0;

    public static final int N_VALUE = 1;

    public static final String[] COLUMNS = new String[] { FIELD, VALUE };

    /**
	 * 
	 */
    private static final long serialVersionUID = 3065849698934320412L;

    private String m_key;

    private Object m_value;

    public Info(String key, Object value) {
        m_key = key;
        m_value = value;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(m_key + " (" + m_value + ") ");
        return sb.toString();
    }

    public String get(String column) {
        String result = "-";
        if (column.equals(FIELD)) {
            result = m_key;
        } else if (column.equals(VALUE) && m_value != null) {
            result = m_value.toString();
        }
        return result;
    }

    public String get(int nColumn) {
        String result = "-";
        switch(nColumn) {
            case N_FIELD:
                result = m_key;
                break;
            case N_VALUE:
                if (m_value != null) result = m_value.toString();
                break;
            default:
                break;
        }
        return result;
    }

    public String[] getColumns() {
        return COLUMNS;
    }
}
