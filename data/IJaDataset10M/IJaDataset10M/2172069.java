package org.ttt.salt.editor.tbxedit;

import org.w3c.dom.Element;
import org.flyingtitans.xml.TreeView;

/**
 * @author Lance Finn Helsten
 * @version $Id: Element_revisionDesc.java 1 2008-05-23 03:51:58Z lanhel $
 */
public class Element_revisionDesc extends XMLElement {

    /** SCM information. */
    public static final String RCSID = "$Id: Element_revisionDesc.java 1 2008-05-23 03:51:58Z lanhel $";

    /**
     * @param elem The DOM element that has the data.
     * @param window The view this element displays in.
     * @param title The title to use in the view.
     */
    public Element_revisionDesc(Element elem, TreeView window, String title) {
        super(elem, window, title, false);
        addAttributeTextField(BUNDLE, "id");
        addXmlLangAttribute();
    }
}
