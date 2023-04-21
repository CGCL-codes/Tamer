package org.w3c.dom.html;

/**
 *  The document title. See the  TITLE element definition in HTML 4.0.
 * <p>See also the <a href='http://www.w3.org/TR/2000/CR-DOM-Level-2-20000510'>Document Object Model (DOM) Level 2 Specification</a>.
 */
public interface HTMLTitleElement extends HTMLElement {

    /**
     *  The specified title as a string. 
     */
    public String getText();

    public void setText(String text);
}
