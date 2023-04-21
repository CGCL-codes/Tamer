package com.loribel.tools.xml.swing;

import org.w3c.dom.Element;
import com.loribel.commons.swing.editor.GB_EditorXmlDefault;

/**
 * Component with a text Area to represent the source of XML Node (with all children).
 *
 * @author Gr�gory Borelli
 * @version 2003/09/25 - 17:43:47 - gen 7.11
 */
public class GB_ElementHtmlTextArea extends GB_EditorXmlDefault {

    /**
     * Constructor of GB_XmlTextArea with parameter(s).
     */
    public GB_ElementHtmlTextArea(Element a_root) {
        super(a_root);
    }
}
