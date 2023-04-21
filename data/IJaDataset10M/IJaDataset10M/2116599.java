package com.loribel.tools.xml.template.bo;

import gb.fwk.gui.editor.GB_PanelEditor;
import javax.swing.JComponent;
import org.w3c.dom.Node;
import com.loribel.commons.abstraction.GB_LabelIcon;
import com.loribel.commons.swing.GB_PanelRowsTitle;
import com.loribel.commons.util.GB_LabelIconTools;
import com.loribel.commons.xml.GB_ElementTools;
import com.loribel.commons.xml.GB_NodeList;
import com.loribel.tools.xml.swing.GB_ElementValueTextArea;
import com.loribel.tools.xml.template.vm.GB_GenericXmlVM;

/**
 * ViewManager to represent a node.
 */
public class GB_ObjectGenericVM extends GB_GenericXmlVM {

    public GB_ObjectGenericVM(Node a_node) {
        super(a_node);
    }

    public GB_LabelIcon getLabelIcon() {
        return GB_LabelIconTools.newLabelIcon("Object");
    }

    /**
     * Add the children to retour
     * @return true if this method add something to the retour
     */
    protected boolean addChildrenElement(GB_PanelRowsTitle retour, Node a_node, int a_maxLevel) {
        boolean flagRetour = false;
        GB_NodeList l_children = GB_ElementTools.getChildElements(a_node);
        if (l_children == null) {
            return flagRetour;
        }
        int len = l_children.size();
        if (len == 0) {
            if (a_node.getFirstChild() == null) {
            } else {
                GB_ElementValueTextArea l_textArea = new GB_ElementValueTextArea(a_node);
                l_textArea.setWrapLines();
                retour.addRowFill2(l_textArea);
                flagRetour = false;
            }
        } else {
            for (int i = 0; i < len; i++) {
                Node l_child = l_children.getNode(i);
                String l_name = l_child.getNodeName();
                if (l_name.equals("property")) {
                } else {
                    boolean l_flagWithValue = (GB_ElementTools.getElementTextValueSafe(l_child) != null);
                    if (!l_flagWithValue) {
                        if (a_maxLevel > 0) {
                            JComponent l_panel = buildPanel(l_child, a_maxLevel - 1);
                            GB_PanelEditor l_panelEditor = new GB_PanelEditor(l_panel);
                            retour.addRowFill2(l_child.getNodeName(), l_panelEditor);
                        }
                    } else {
                        flagRetour = false;
                        GB_ElementValueTextArea l_textArea = new GB_ElementValueTextArea(l_child);
                        l_textArea.setWrapLines();
                        retour.addRowFill2(l_name, l_textArea);
                    }
                }
            }
        }
        return flagRetour;
    }
}
