package de.shandschuh.jaolt.gui.core.htmleditor;

import javax.swing.JEditorPane;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class UnderlineStyledTextAction extends SimpleStyledTextAction {

    private static final long serialVersionUID = 1L;

    public UnderlineStyledTextAction() {
        super("icons/text_underline.png");
    }

    @Override
    protected void applyChange(JEditorPane editor, MutableAttributeSet mutableAttributeSet) {
        SimpleAttributeSet simpleAttributeSet = new SimpleAttributeSet();
        StyleConstants.setUnderline(simpleAttributeSet, !StyleConstants.isUnderline(mutableAttributeSet));
        setCharacterAttributes(editor, simpleAttributeSet, false);
    }
}
