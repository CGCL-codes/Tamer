package de.beas.explicanto.client.rcp.components;

import de.beas.explicanto.client.Resources;
import de.beas.explicanto.client.rcp.generic.GenericEditorAction;

/**
 * Moves a component element above the upper one.
 *
 * @author alexandru.georgescu
 * @version 1.0
 *
 */
public class MoveUpAction extends GenericEditorAction {

    public MoveUpAction(ScrollBarEditor editor) {
        super(editor, "editors.actions.moveUp", "editors.actions.moveUp", "rcp/resources/images/compUp.png", null);
        setDisabledImageDescriptor(Resources.getImageDescriptor("rcp/resources/images/compUpDisabled.png"));
    }

    public void run() {
        editor.moveUpSelectedComp();
    }

    public boolean isEnabled() {
        return editor.getSelectionIndex() > 0;
    }
}
