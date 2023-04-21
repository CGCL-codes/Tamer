package org.agilercp.ui.editor;

import org.agilercp.ui.IView;
import org.agilercp.ui.ViewDelegate;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.ISaveablePart;

/**
 * Supports implementing an MVP-view if it is not possible to use one of the
 * base classes provided by Agile RCP. Adds support for editor handling.
 * 
 * @author Benedikt Arnold
 */
public class EditorDelegate extends ViewDelegate implements IEditorDelegate {

    private ISaveHandler saveHandler;

    /**
     * @param view
     */
    public EditorDelegate(final IView view) {
        super(view);
    }

    /**
     * Delegate the doSave call to the registered {@link ISaveHandler}.
     * 
     * @param monitor
     * @see ISaveablePart#doSave(IProgressMonitor)
     */
    public void notifySave(final IProgressMonitor monitor) {
        if (saveHandler != null) {
            saveHandler.doSave(monitor);
        }
    }

    /**
     * Delegate the doSaveAs call to the registered {@link ISaveHandler}.
     * 
     * @see ISaveablePart#doSaveAs()
     */
    public void notifySaveAs() {
        if (saveHandler != null) {
            saveHandler.doSaveAs();
        }
    }

    /**
     * @see org.agilercp.ui.editor.IEditorDelegate#setSaveHandler(org.agilercp.ui.editor.ISaveHandler)
     */
    public void setSaveHandler(final ISaveHandler handler) {
        saveHandler = handler;
    }
}
