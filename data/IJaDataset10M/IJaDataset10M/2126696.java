package com.bluebrim.text.impl.client.actions;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import com.bluebrim.swing.client.*;
import com.bluebrim.text.impl.shared.*;

/**
 * Text editor action that set character attribute CoEnumValue values.
 * 
 * @author: Dennis Malmstr�m
 */
public class CoEnumCharacterAction extends CoEnumAction {

    public CoEnumCharacterAction(Object attribute, String actionName) {
        super(attribute, actionName);
    }

    public void doit(JTextPane editor, ActionEvent e) {
        int start = editor.getSelectionStart();
        int end = editor.getSelectionEnd();
        MutableAttributeSet attr;
        if (start != end) {
            attr = new com.bluebrim.text.shared.CoSimpleAttributeSet();
        } else {
            attr = getStyledEditorKit(editor).getInputAttributes();
        }
        String s = e.getActionCommand();
        Component c = (Component) e.getSource();
        if (c instanceof Co3DToggleButton) {
            if (!((Co3DToggleButton) c).isSelected()) s = null;
        }
        CoEnumValue ev = CoEnumValue.getEnumValue(s);
        if (ev == null) {
            clearCharacterAttribute(editor, m_attribute);
        } else {
            CoStyleConstants.set(attr, m_attribute, ev);
            setCharacterAttributes(editor, attr, false);
        }
    }
}
