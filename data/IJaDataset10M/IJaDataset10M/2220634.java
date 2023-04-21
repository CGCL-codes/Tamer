package org.plazmaforge.studio.dbdesigner.actions;

import java.io.File;
import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.internal.filesystem.local.LocalFile;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.FileEditorInput;
import org.plazmaforge.studio.dbdesigner.ERDConstants;
import org.plazmaforge.studio.dbdesigner.editor.ERDFileEditorInput;
import org.plazmaforge.studio.dbdesigner.editor.ERDJavaEditorInput;
import org.plazmaforge.studio.dbdesigner.editor.ERDesignerEditor;
import org.plazmaforge.studio.dbdesigner.model.DBProject;
import org.plazmaforge.studio.dbdesigner.model.ERColumn;
import org.plazmaforge.studio.dbdesigner.model.ERDiagram;
import org.plazmaforge.studio.dbdesigner.model.ERTableDetail;
import org.plazmaforge.studio.dbdesigner.model.ERTableNode;

/** 
 * @author Oleh Hapon
 * $Id: NewERD.java,v 1.8 2010/04/28 06:41:32 ohapon Exp $
 */
public class NewERD implements IWorkbenchWindowActionDelegate {

    private IWorkbenchWindow window;

    private class CreateERDJob extends Job {

        public CreateERDJob(String name) {
            super(name);
        }

        protected IStatus run(IProgressMonitor iprogressmonitor) {
            ERDiagram erdiagram = DBProject.createStandaloneDiagram();
            DBProject.createStandaloneDBModel(erdiagram);
            IStatus status = populateDiagram(iprogressmonitor, erdiagram);
            if (Status.CANCEL_STATUS == status) {
                return status;
            }
            final Object editorInput;
            if (opFile instanceof IFile) {
                editorInput = new ERDFileEditorInput(erdiagram, (IFile) opFile);
            } else {
                EFS.getLocalFileSystem();
                editorInput = new ERDJavaEditorInput(erdiagram, new LocalFile((File) opFile));
            }
            Display display = page.getWorkbenchWindow().getShell().getDisplay();
            display.asyncExec(new Runnable() {

                public void run() {
                    try {
                        ERDesignerEditor erdesignereditor = (ERDesignerEditor) page.openEditor((IEditorInput) editorInput, ERDesignerEditor.ID);
                        erdesignereditor.setAutoLayout();
                        erdesignereditor.doSave(null);
                        erdesignereditor.enableSanityChecking(true);
                    } catch (PartInitException partinitexception) {
                        partinitexception.printStackTrace();
                    }
                }
            });
            return Status.OK_STATUS;
        }
    }

    public IStatus populateDiagram(IProgressMonitor iprogressmonitor, ERDiagram erdiagram) {
        for (int i = 0; i < 2; i++) {
            if (iprogressmonitor.isCanceled()) {
                return Status.CANCEL_STATUS;
            }
            String tableName = "Table" + (i + 1);
            iprogressmonitor.subTask("Create table " + tableName);
            ;
            try {
                ERTableNode ertablenode = new ERTableNode(erdiagram, tableName);
                ERTableDetail tableDetail = ertablenode.getTableDetail();
                ERColumn c = new ERColumn(tableDetail);
                c.setColumnName("id");
                c.setTypeName("INTEGER");
                c.setPrimaryKey(true);
                c.setRequired(true);
                ertablenode.getTableDetail().addChild(c);
                c = new ERColumn(tableDetail);
                c.setColumnName("code");
                c.setTypeName("VARCHAR");
                c.setForeignKey(true);
                c.setRequired(true);
                ertablenode.getTableDetail().addChild(c);
                c = new ERColumn(tableDetail);
                c.setColumnName("name");
                c.setTypeName("VARCHAR");
                tableDetail.addChild(c);
                erdiagram.addChild(ertablenode);
                iprogressmonitor.worked(1);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        return Status.OK_STATUS;
    }

    public IWorkbenchWindow getWindow() {
        return window;
    }

    /**
	 * The constructor.
	 */
    public NewERD() {
    }

    /**
	 * The action has been activated. The argument of the
	 * method represents the 'real' action sitting
	 * in the workbench UI.
	 * @see IWorkbenchWindowActionDelegate#run
	 */
    public void run(IAction action) {
        String diagramName = "Diagram";
        Shell shell = getWindow().getShell();
        IWorkbenchWindow workbenchWindow = getWindow();
        IWorkbench workbench = workbenchWindow.getWorkbench();
        String fileName = ERDUtils.openERDFileDialog(shell, diagramName);
        if (fileName == null) {
            return;
        }
        Object fileObj = new Path(fileName);
        IPath filePath = (IPath) fileObj;
        if (filePath.getFileExtension() == null || !filePath.getFileExtension().equalsIgnoreCase(ERDConstants.FILE_EXT)) {
            fileObj = filePath.addFileExtension(ERDConstants.FILE_EXT);
            filePath = (IPath) fileObj;
        }
        page = workbench.getActiveWorkbenchWindow().getActivePage();
        opFile = filePath.toFile();
        CreateERDJob createerdjob = new CreateERDJob("Creating..");
        createerdjob.schedule();
    }

    private IWorkbenchPage page;

    private Object opFile;

    private boolean isRCP() {
        return false;
    }

    /**
	 * Selection in the workbench has been changed. We 
	 * can change the state of the 'real' action here
	 * if we want, but this can only happen after 
	 * the delegate has been created.
	 * @see IWorkbenchWindowActionDelegate#selectionChanged
	 */
    public void selectionChanged(IAction action, ISelection selection) {
    }

    /**
	 * We can use this method to dispose of any system
	 * resources we previously allocated.
	 * @see IWorkbenchWindowActionDelegate#dispose
	 */
    public void dispose() {
    }

    /**
	 * We will cache window object in order to
	 * be able to provide parent shell for the message dialog.
	 * @see IWorkbenchWindowActionDelegate#init
	 */
    public void init(IWorkbenchWindow window) {
        this.window = window;
    }
}
