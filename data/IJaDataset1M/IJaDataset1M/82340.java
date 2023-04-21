package net.kornr.abstractcanvas.awt.command;

import net.kornr.abstractcanvas.awt.HTMLColor;

public class AbstractColorContainer {

    protected java.awt.Color m_value;

    AbstractColorContainer(String htmlValue) {
        m_value = HTMLColor.getColor(htmlValue);
    }

    AbstractColorContainer(java.awt.Color value) {
        m_value = value;
    }

    public void setValue(java.awt.Color v) {
        m_value = v;
    }

    public java.awt.Color getValue() {
        return m_value;
    }
}
