package com.loribel.tools.xml.template.vm;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import org.w3c.dom.Node;
import com.loribel.commons.abstraction.GB_LabelIcon;
import com.loribel.commons.swing.editor.GB_EditorXsmlDefault;
import com.loribel.commons.util.GB_IconTools;
import com.loribel.commons.util.GB_LabelIconTools;

/**
 * ViewManager to represent a node.
 *  - Represents as xsml in textArea
 *
 * @author Gr�gory Borelli
 */
public class GB_XsmlEditorVM extends GB_AbstractXmlVM {

    public GB_XsmlEditorVM(Node a_node) {
        super(a_node, null, 0);
    }

    /**
     * Return the view of this ViewManager
     */
    protected JComponent buildView() {
        GB_XsmlEditorView retour = new GB_XsmlEditorView(this);
        return retour;
    }

    /**
     * Return the label icon associated to this ViewManager
     */
    public GB_LabelIcon getLabelIcon() {
        Icon l_icon = GB_IconTools.get(AA.ICON.X16_PAS_CYAN);
        GB_LabelIcon retour = GB_LabelIconTools.newLabelIcon("gb-XML", l_icon);
        return retour;
    }

    protected GB_XsmlEditorView getMyView() {
        return (GB_XsmlEditorView) view;
    }

    /**
     * Save the content of this view.
     */
    public boolean saveDataFromView() {
        GB_EditorXsmlDefault l_editor = getMyView().getEditor();
        if (!l_editor.isModified()) {
            return true;
        }
        int q = JOptionPane.showConfirmDialog(this.getView(), AA.MSG_TEXT_CHANGES, AA.MSG_TITLE_XML, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (q == JOptionPane.YES_OPTION) {
            boolean l_updateOK = l_editor.parseText();
            if (!l_updateOK) {
                JOptionPane.showMessageDialog(this.getView(), "Parse error", AA.MSG_TITLE_XML, JOptionPane.WARNING_MESSAGE);
                return false;
            }
            return true;
        } else if (q == JOptionPane.NO_OPTION) {
            return true;
        }
        return false;
    }
}
