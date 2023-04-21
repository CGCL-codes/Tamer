package org.gjt.sp.jedit.actions;

import java.awt.event.ActionEvent;
import org.gjt.sp.jedit.textarea.Gutter;
import org.gjt.sp.jedit.textarea.JEditTextArea;
import org.gjt.sp.jedit.EditAction;
import org.gjt.sp.jedit.View;

public class toggle_gutter extends EditAction {

    public void actionPerformed(ActionEvent evt) {
        getView(evt).getTextArea().getGutter().toggleCollapsed();
    }

    public boolean isToggle() {
        return true;
    }

    public boolean isSelected(java.awt.Component comp) {
        return !getView(comp).getTextArea().getGutter().isCollapsed();
    }
}
