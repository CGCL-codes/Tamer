package com.itextpdf.text.xml.simpleparser;

/**
 * The handler for the events fired by <CODE>SimpleXMLParser</CODE>.
 * @author Paulo Soares
 */
public interface SimpleXMLDocHandlerComment {

    /**
     * Called when a comment is found.
     * @param text the comment text
     */
    public void comment(String text);
}
