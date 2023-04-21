package de.shandschuh.jaolt.gui.core.htmleditor;

import javax.swing.JEditorPane;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

public class HRStyledTextAction extends SimpleStyledTextAction {

    private static final long serialVersionUID = 1L;

    public HRStyledTextAction() {
        super("icons/text_horizontalrule.png");
    }

    @Override
    protected void applyChange(JEditorPane editor, MutableAttributeSet mutableAttributeSet) {
        try {
            ((HTMLEditorKit) editor.getEditorKit()).insertHTML((HTMLDocument) editor.getDocument(), editor.getCaretPosition(), "<hr/>", 0, 0, HTML.Tag.HR);
        } catch (Exception e) {
        }
    }
}
