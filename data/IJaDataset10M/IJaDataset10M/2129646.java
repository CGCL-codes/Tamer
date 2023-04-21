package com.tensegrity.palobrowser.editors;

import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

/**
 * <code>StatusLineEditorPart</code>
 *
 * @author Stepan Rutz
 * @version $ID$
 */
public abstract class StatusLineEditorPart extends EditorPart implements IPartListener2 {

    private String message;

    private String errorMessage;

    public StatusLineEditorPart() {
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        getEditorSite().getActionBars().getStatusLineManager().setErrorMessage(errorMessage);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
        getEditorSite().getActionBars().getStatusLineManager().setMessage(message);
    }

    protected void rehashStatusLine() {
        getEditorSite().getActionBars().getStatusLineManager().setMessage(message);
        getEditorSite().getActionBars().getStatusLineManager().setErrorMessage(errorMessage);
    }

    public void init(IEditorSite site, IEditorInput input) throws PartInitException {
        site.getPage().addPartListener(this);
    }

    public void dispose() {
        getEditorSite().getPage().removePartListener(this);
        super.dispose();
    }

    public void partActivated(IWorkbenchPartReference partRef) {
        if (partRef.getPart(false) == this) rehashStatusLine();
    }

    public void partBroughtToTop(IWorkbenchPartReference partRef) {
    }

    public void partClosed(IWorkbenchPartReference partRef) {
    }

    public void partDeactivated(IWorkbenchPartReference partRef) {
    }

    public void partHidden(IWorkbenchPartReference partRef) {
    }

    public void partInputChanged(IWorkbenchPartReference partRef) {
    }

    public void partOpened(IWorkbenchPartReference partRef) {
    }

    public void partVisible(IWorkbenchPartReference partRef) {
    }
}
