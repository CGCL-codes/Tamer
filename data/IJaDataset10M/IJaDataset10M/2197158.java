package org.stamppagetor.ui;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import org.stamppagetor.UiPlatform;

/**
 * Cancels the previous command 
 */
public class UiBundleUndo extends UiBundle {

    public UiBundleUndo() {
        super("UNDO", new RuleUndoPossible());
        addUiItem(new UiItemMenu(1100, "Edit", "Undo", KeyEvent.VK_Z, InputEvent.CTRL_MASK));
    }

    @Override
    public boolean doAction(UiPlatform platform) {
        return platform.getUndoStore().undo(platform);
    }

    @Override
    public boolean isUndoable() {
        return false;
    }
}

class RuleUndoPossible extends UiStateRule {

    @Override
    public boolean state(UiPlatform ui) {
        return ui.getUndoStore().canUndo();
    }
}
