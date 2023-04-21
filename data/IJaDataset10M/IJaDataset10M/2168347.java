package de.hattrickorganizer.gui.model;

/**
 * DOCUMENT ME!
 *
 * @author thomas.werth
 * @version
 */
public class CBItem {

    private String m_sText;

    private int m_iId;

    /**
     * Creates new CBItem
     *
     * @param text TODO Missing Constructuor Parameter Documentation
     * @param id TODO Missing Constructuor Parameter Documentation
     */
    public CBItem(String text, int id) {
        m_sText = text;
        m_iId = id;
    }

    /**
     * TODO Missing Method Documentation
     *
     * @param id TODO Missing Method Parameter Documentation
     */
    public final void setId(int id) {
        m_iId = id;
    }

    /**
     * TODO Missing Method Documentation
     *
     * @return TODO Missing Return Method Documentation
     */
    public final int getId() {
        return m_iId;
    }

    /**
     * TODO Missing Method Documentation
     *
     * @param text TODO Missing Method Parameter Documentation
     */
    public final void setText(String text) {
        m_sText = text;
    }

    /**
     * TODO Missing Method Documentation
     *
     * @return TODO Missing Return Method Documentation
     */
    public final String getText() {
        return m_sText;
    }

    /**
     * TODO Missing Method Documentation
     *
     * @param obj TODO Missing Method Parameter Documentation
     *
     * @return TODO Missing Return Method Documentation
     */
    @Override
    public final boolean equals(Object obj) {
        if (obj instanceof CBItem) {
            final CBItem temp = (CBItem) obj;
            if (this.getId() == temp.getId()) {
                return true;
            }
        }
        return false;
    }

    /**
     * TODO Missing Method Documentation
     *
     * @return TODO Missing Return Method Documentation
     */
    @Override
    public final String toString() {
        return m_sText;
    }
}
