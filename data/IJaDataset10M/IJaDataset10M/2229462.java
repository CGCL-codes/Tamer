package org.alcibiade.sculpt.gui.action;

import java.awt.event.ActionEvent;
import javax.swing.text.JTextComponent;

public class ActionEditCopy extends ClipboardAction {

    private JTextComponent textarea_source;

    public ActionEditCopy(JTextComponent textarea_source) {
        super("action.edit.copy");
        this.textarea_source = textarea_source;
    }

    public void actionPerformed(ActionEvent e) {
        String selection = textarea_source.getSelectedText();
        setClipboardContents(selection);
    }
}
