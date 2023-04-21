package com.velocityme.www.dto;

/**
 *
 * @author  Robert
 */
public class ButtonValue implements java.io.Serializable {

    private boolean m_isEnabled;

    private boolean m_isVisible;

    private String m_name;

    private String m_label;

    /** Creates a new instance of ButtonValue */
    public ButtonValue() {
    }

    public ButtonValue(boolean isEnabled, boolean isVisible, String name, String label) {
        m_isEnabled = isEnabled;
        m_isVisible = isVisible;
        m_name = name;
        m_label = label;
    }

    public boolean getIsEnabled() {
        return m_isEnabled;
    }

    public void setIsEnabled(boolean isEnabled) {
        m_isEnabled = isEnabled;
    }

    public boolean getIsVisible() {
        return m_isVisible;
    }

    public void setIsVisible(boolean isVisible) {
        m_isVisible = isVisible;
    }

    public String getName() {
        return m_name;
    }

    public void setName(String name) {
        m_name = name;
    }

    public String getLabel() {
        return m_label;
    }

    public void setLabel(String label) {
        m_label = label;
    }
}
