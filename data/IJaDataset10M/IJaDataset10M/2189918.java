package ag.ion.bion.workbench.office.editor.ui.editors;

import ag.ion.bion.officelayer.desktop.IFrame;
import ag.ion.bion.workbench.office.editor.ui.EditorUIPlugin;
import ag.ion.noa.document.IDocumentProvider;
import ag.ion.noa.frame.IFrameProvider;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

/**
 * Abstract editor for OpenOffice.org documents.
 * 
 * @author Andreas Br�ker
 * @author Markus Kr�ger
 * @version $Revision: 9747 $
 */
public abstract class AbstractOfficeEditor extends EditorPart implements IDocumentProvider, IFrameProvider {

    private static final String ERROR_STORING_DOCUMENT = Messages.AbstractOfficeEditor_message_document_can_not_be_stored;

    private IResourceChangeListener resourceCloseProjectListener = null;

    private IEditorFileSynchronizer officeEditorFileSynchronizer = null;

    /**
   * Internal editor advisor state delegate.
   * 
   * @author Andreas Br�ker
   * @author Markus Kr�ger
   */
    private class EditorAdvisorStateDelegate implements IEditorAdvisorStateDelegate {

        /**
     * Sets dirty state.
     * 
     * @param dirtyState new dirty state to be used
     * 
     * @author Andreas Br�ker
     * @author Markus Kr�ger
     */
        public void setDirtyState(boolean dirtyState) {
            EditorUIPlugin.getDefault().getWorkbench().getDisplay().asyncExec(new Runnable() {

                public void run() {
                    setDirty();
                }
            });
        }
    }

    /**
   * Initializes this editor with the given editor site and input. 
   * 
   * @param editorSite the editor site to be used
   * @param editorInput the editor input to be used
   * 
   * @throws PartInitException if this editor was not initialized successfully
   * 
   * @author Andreas Br�ker
   * @author Markus Kr�ger
   * @date 24.08.2006
   */
    public void init(IEditorSite editorSite, IEditorInput editorInput) throws PartInitException {
        setSite(editorSite);
        setInput(editorInput);
        if (editorInput instanceof IFileEditorInput) {
            IFile file = ((IFileEditorInput) getEditorInput()).getFile();
            IProject project = file.getProject();
            resourceCloseProjectListener = new EditorResourceProjectCloseListener(project, this);
            ResourcesPlugin.getWorkspace().addResourceChangeListener(resourceCloseProjectListener, IResourceChangeEvent.PRE_CLOSE);
            officeEditorFileSynchronizer = new OfficeEditorFileSynchronizer(file, this);
            officeEditorFileSynchronizer.install();
        }
    }

    /**
   * Creates the SWT controls for this workbench part. 
   * 
   * @param parent parent control
   * 
   * @author Andreas Br�ker
   * @author Markus Kr�ger
   */
    public void createPartControl(Composite parent) {
        EditorUIPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getPartService().addPartListener(new IPartListener2() {

            public void partActivated(IWorkbenchPartReference partRef) {
            }

            public void partBroughtToTop(IWorkbenchPartReference partRef) {
            }

            public void partClosed(IWorkbenchPartReference partRef) {
                if (partRef.getPart(false).equals(AbstractOfficeEditor.this)) {
                    getOfficeEditorAdvisor().dispose();
                }
            }

            public void partDeactivated(IWorkbenchPartReference partRef) {
            }

            public void partOpened(IWorkbenchPartReference partRef) {
            }

            public void partHidden(IWorkbenchPartReference partRef) {
            }

            public void partVisible(IWorkbenchPartReference partRef) {
            }

            public void partInputChanged(IWorkbenchPartReference partRef) {
            }
        });
        try {
            getOfficeEditorAdvisor().constructEditorControl(parent, getEditorInput());
            if (getOfficeEditorAdvisor().getLoadedDocument() == null) return;
            getOfficeEditorAdvisor().setStateDelegate(new EditorAdvisorStateDelegate());
            setPartName(getEditorInput().getName());
        } catch (CoreException coreException) {
            ErrorDialog.openError(getSite().getShell(), Messages.AbstractOfficeEditor_dialog_title_error, coreException.getMessage(), coreException.getStatus());
        }
    }

    /**
   * Saves the contents of this part.
   * 
   * @param progressMonitor the progress monitor
   * 
   * @author Andreas Br�ker
   * @author Markus Kr�ger
   */
    public void doSave(IProgressMonitor progressMonitor) {
        try {
            if (officeEditorFileSynchronizer != null) officeEditorFileSynchronizer.uninstall();
            getOfficeEditorAdvisor().doSave(progressMonitor, false);
            if (officeEditorFileSynchronizer != null) officeEditorFileSynchronizer.install();
        } catch (CoreException coreException) {
            if (officeEditorFileSynchronizer != null) officeEditorFileSynchronizer.install();
            ErrorDialog.openError(getSite().getShell(), Messages.AbstractOfficeEditor_dialog_title_error, ERROR_STORING_DOCUMENT, coreException.getStatus());
        }
    }

    /**
   * Saves the contents of this part to another object. 
   * 
   * @author Andreas Br�ker 
   */
    public void doSaveAs() {
        try {
            getOfficeEditorAdvisor().doSaveAs(getSite().getShell());
        } catch (CoreException coreException) {
            ErrorDialog.openError(getSite().getShell(), Messages.AbstractOfficeEditor_dialog_title_error, ERROR_STORING_DOCUMENT, coreException.getStatus());
        }
    }

    /**
   * Returns whether the contents of this part have changed since the last save operation.
   * 
   * @return true if the contents have been modified and need saving, and false if they have 
   * not changed since the last save
   * 
   * @author Andreas Br�ker
   */
    public boolean isDirty() {
        return getOfficeEditorAdvisor().isDocumentDirty();
    }

    /**
   * Asks this part to take focus within the workbench.
   * 
   * @author Andreas Br�ker
   * @author Markus Kr�ger
   */
    public void setFocus() {
        if (getOfficeEditorAdvisor().getLoadedDocument() == null) {
            EditorUIPlugin.getDefault().getWorkbench().getDisplay().asyncExec(new Runnable() {

                public void run() {
                    EditorUIPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage().closeEditor(AbstractOfficeEditor.this, false);
                }
            });
        } else {
            getOfficeEditorAdvisor().getFrame().setFocus();
        }
    }

    /**
   * Returns information whether the "Save As" operation is supported by this part.
   * 
   * @return information whether the "Save As" operation is supported by this part
   * 
   * @author Andreas Br�ker
   */
    public boolean isSaveAsAllowed() {
        return getOfficeEditorAdvisor().isSaveAsAllowed();
    }

    /**
	 * Returns frame. Returns null
	 * if a frame is not available.
	 * 
	 * @return frame or null
	 * if a frame is not available
	 * 
	 * @author Andreas Br�ker
	 * @date 15.07.2006
	 */
    public IFrame getFrame() {
        return getOfficeEditorAdvisor().getFrame();
    }

    /**
   * Disposes of this workbench part
   * 
   * @author Andreas Br�ker
   * @author Markus Kr�ger
   */
    public void dispose() {
        if (resourceCloseProjectListener != null) ResourcesPlugin.getWorkspace().removeResourceChangeListener(resourceCloseProjectListener);
        if (officeEditorFileSynchronizer != null) officeEditorFileSynchronizer.uninstall();
        IOfficeEditorAdvisor officeEditorAdvisor = getOfficeEditorAdvisor();
        if (officeEditorAdvisor != null) {
            officeEditorAdvisor.dispose();
        }
        super.dispose();
    }

    /**
   * Sets editor dirty.
   * 
   * @author Andreas Br�ker
   */
    protected void setDirty() {
        firePropertyChange(PROP_DIRTY);
        getEditorSite().getActionBars().updateActionBars();
        getEditorSite().getActionBars().getMenuManager().update();
        getEditorSite().getActionBars().getToolBarManager().update(true);
    }

    /**
   * Returns office editor advisor. Sublasses must implement their own
   * office editor advisor.
   * 
   * @return office editor advisor
   * 
   * @author Andreas Br�ker
   */
    protected abstract IOfficeEditorAdvisor getOfficeEditorAdvisor();
}
