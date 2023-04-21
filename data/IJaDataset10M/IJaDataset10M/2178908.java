package org.eclipse.ui.internal;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;

/**
 * Closes the active editor.
 */
public class CloseEditorAction extends ActiveEditorAction {

    /**
     * Create an instance of this class.
     * 
     * @param window the window
     */
    public CloseEditorAction(IWorkbenchWindow window) {
        super(WorkbenchMessages.CloseEditorAction_text, window);
        setToolTipText(WorkbenchMessages.CloseEditorAction_toolTip);
        setId("close");
        window.getWorkbench().getHelpSystem().setHelp(this, IWorkbenchHelpContextIds.CLOSE_PART_ACTION);
        setActionDefinitionId("org.eclipse.ui.file.close");
    }

    public void run() {
        IEditorPart part = getActiveEditor();
        if (part != null) {
            getActivePage().closeEditor(part, true);
        }
    }
}
