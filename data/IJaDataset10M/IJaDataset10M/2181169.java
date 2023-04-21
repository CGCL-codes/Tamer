package com.itextpdf.text;

import java.util.EventListener;

/**
 * A class that implements <CODE>ElementListener</CODE> will perform some
 * actions when an <CODE>Element</CODE> is added.
 *
 * @see		DocListener
 */
public interface ElementListener extends EventListener {

    /**
 * Signals that an <CODE>Element</CODE> was added to the <CODE>Document</CODE>.
 * 
 * @param element a high level object
 * @return	<CODE>true</CODE> if the element was added, <CODE>false</CODE> if not.
 * @throws	DocumentException	when a document isn't open yet, or has been closed
 */
    public boolean add(Element element) throws DocumentException;
}
