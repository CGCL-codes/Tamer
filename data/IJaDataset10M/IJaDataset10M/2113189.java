package org.formaria.aria;

/**
 * A generic interface for components that display a text label
 * <p>Copyright: Copyright (c) Formaria Ltd., 2002-2004</p>
 * $Revision: 2.2 $
 */
public interface TextHolder {

    /**
   * Set the text/label of a component
   * @param text the new text
   */
    public void setText(String text);

    /**
   * Get the text/label of a component
   * @return the component's text
   */
    public String getText();
}
