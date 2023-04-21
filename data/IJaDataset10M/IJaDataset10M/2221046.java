package com.bluebrim.base.shared;

import javax.swing.*;
import com.bluebrim.resource.shared.*;

/**
 * An implementation of <code>CoListElementViewIF</code>, i e a 
 * serializable class to be used when we need to cache display data
 * on the client..
 * <br>
 * Creation date: (2000-03-13 14:56:31)
 * @author: Lasse Svad�ngs
 */
public class CoListElementView implements CoListElementViewIF, CoObjectIF {

    private Object m_element;

    private Icon m_icon;

    private String m_text;

    public CoListElementView(Class anchor, String iconName, String text, Object element) {
        this(iconName != null && anchor != null ? CoResourceLoader.loadIcon(anchor, iconName) : null, text, element);
    }

    public CoListElementView(Icon icon, String text, Object element) {
        m_icon = icon;
        m_text = text;
        m_element = element;
    }

    public void addPropertyChangeListener(CoPropertyChangeListener l) {
    }

    public boolean equals(Object o) {
        if (o == null) return false;
        if (o.getClass() != getClass()) return false;
        return m_element == ((CoListElementViewIF) o).getElement() || m_element != null && m_element.equals(((CoListElementViewIF) o).getElement());
    }

    public Object getElement() {
        return m_element;
    }

    public javax.swing.Icon getIcon() {
        return m_icon;
    }

    public String getText() {
        return m_text;
    }

    public int hashCode() {
        return m_element.hashCode();
    }

    public void removePropertyChangeListener(CoPropertyChangeListener l) {
    }
}
