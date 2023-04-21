package com.bluebrim.text.impl.client.actions;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import com.bluebrim.text.impl.shared.*;

/**
 * Text editor action that applies a delta onto character or (depending on selection) paragraph attribute float values.
 * 
 * @author: Dennis Malmstr�m
 */
public class CoDeltaFloatCharacterOrParagraphAction extends CoFloatCharacterOrParagraphAction {

    private float m_delta;

    private float m_min;

    private float m_max;

    private float m_scale;

    private com.bluebrim.text.shared.CoAttributeSetOperationIF m_op;

    public CoDeltaFloatCharacterOrParagraphAction(Object attribute, String actionName, float delta, float min, float max) {
        super(attribute, actionName);
        m_delta = delta;
        m_min = min;
        m_max = max;
        m_op = new com.bluebrim.text.shared.CoAttributeSetOperationIF() {

            public void apply(MutableAttributeSet as) {
                doit(as);
            }
        };
    }

    public void doit(JTextPane editor, ActionEvent e) {
        if ((e.getModifiers() & ActionEvent.ALT_MASK) != 0) {
            m_scale = 10;
        } else {
            m_scale = 1;
        }
        changeCharacterOrParagraphAttributes(editor, m_op);
    }

    private void doit(MutableAttributeSet as) {
        Object o = as.getAttribute(m_attribute);
        Number n = (Number) o;
        float f = CoViewStyleConstants.getDefaultFontSize();
        if (n != null) f = n.floatValue();
        f += m_delta * m_scale;
        if (f < m_min) f = m_min;
        if (f > m_max) f = m_max;
        Float val = new Float(f);
        as.addAttribute(m_attribute, val);
    }
}
