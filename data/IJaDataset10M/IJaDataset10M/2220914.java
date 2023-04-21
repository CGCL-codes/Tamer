package org.merlotxml.merlot;

import java.beans.PropertyVetoException;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import org.merlotxml.util.xml.DTDElement;
import org.merlotxml.util.xml.GrammarComplexType;

/**
 * This is a null editor that suppresses certain types of elements from being
 * edited graphically.
 */
public class NullEditor implements MerlotDOMEditor {

    private static final String NULL_TYPES_PROP = "merlot.editor.null.types";

    private String[] _types;

    public NullEditor() {
        String nulltypes = XMLEditorSettings.getSharedInstance().getProperty(NULL_TYPES_PROP);
        Vector<String> v = new Vector<String>();
        if (nulltypes != null) {
            StringTokenizer tok = new StringTokenizer(nulltypes, ", ");
            while (tok.hasMoreTokens()) {
                String s = tok.nextToken();
                v.addElement(s);
            }
        }
        _types = new String[v.size()];
        for (int i = 0; i < _types.length; i++) {
            _types[i] = v.elementAt(i);
        }
    }

    public void grabFocus(JPanel p) {
    }

    public JMenuItem[] getMenuItems(MerlotDOMNode node) {
        return null;
    }

    /**
	 * returns a panel for editing this type of component.
	 */
    public JPanel getEditPanel(MerlotDOMNode node) {
        throw new RuntimeException("Can't edit " + node);
    }

    /**
	 * called by the editor when the user has chosen to save their
	 * changes in a panel. 
	 * @param p the panel that was retreived with getEditPanel(node);
	 *
	 */
    public void savePanel(JPanel p) throws PropertyVetoException {
    }

    /**
	 * Returns the element types that this editor handles
	 */
    public String[] getEditableTypes() {
        return _types;
    }

    /**
	 * returns true if this editor also edits it's children. <P>
	 * If this returns true, then the editsChild(childnode) is called
	 * for each child to see if this editor wants to edit that 
	 * particular child
	 * XXX currently not used on the editor level

	 */
    public boolean editsChildren() {
        return false;
    }

    /**
	 * Returns true if the component editor wants a particular node hidden
	 * from the user. If the editor wants to filter 
	 * what the user sees in their display, it should look at the
	 * given node, otherwise it should return false. This is usefull
	 * particularly if the editor handles its children. It can hide
	 * the children nodes from the user's view.
	 */
    public boolean suppressNode(MerlotDOMNode node) {
        return true;
    }

    /**
	 * allows the plugin to hide certain items on the add-> menu. For
	 * example, the plugin for the accessibility permissions might not
	 * want the user to be able to directly add an "access" element, so
	 * it can request that that be suppressed.
	 */
    public boolean suppressAddType(DTDElement el) {
        return true;
    }

    public boolean suppressAddType(GrammarComplexType el) {
        return true;
    }
}
