package com.genia.toolbox.spring.provider.init.bean.impl;

import java.beans.PropertyEditorSupport;
import com.genia.toolbox.spring.provider.init.bean.RegistrablePropertyEditor;

/**
 * basic implementation for the {@link RegistrablePropertyEditor}.
 */
public abstract class AbstractRegistrablePropertyEditor extends PropertyEditorSupport implements RegistrablePropertyEditor {

    /**
   * Set the property value by parsing a given String. May raise
   * java.lang.IllegalArgumentException if either the String is badly formatted
   * or if this kind of property can't be expressed as text.
   * 
   * @param text
   *          The string to be parsed.
   * @see java.beans.PropertyEditorSupport#setAsText(java.lang.String)
   */
    @Override
    public abstract void setAsText(String text);

    /**
   * the order property to sort the <code>SpringInitializable</code>.
   */
    private int order;

    /**
   * getter for the order property.
   * 
   * @return the order
   */
    public int getOrder() {
        return order;
    }

    /**
   * setter for the order property.
   * 
   * @param order
   *          the order to set
   */
    public void setOrder(final int order) {
        this.order = order;
    }
}
