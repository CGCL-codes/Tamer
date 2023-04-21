package org.ttt.salt.editor.termentry;

import org.w3c.dom.Element;
import org.flyingtitans.xml.TreeView;

/**
 * @author Lance Finn Helsten
 * @version $Id: Element_adminGrp.java 1 2008-05-23 03:51:58Z lanhel $
 */
public class Element_adminGrp extends TermEntryElement {

    /** SCM information. */
    public static final String RCSID = "$Id: Element_adminGrp.java 1 2008-05-23 03:51:58Z lanhel $";

    /**
     * @param elem Element to represent.
     * @param window Window to display the element.
     * @param title Name to display in window.
     */
    public Element_adminGrp(Element elem, TreeView window, String title) {
        super(elem, window, title, false);
        addAttributeTextField(BUNDLE, "id");
    }
}
