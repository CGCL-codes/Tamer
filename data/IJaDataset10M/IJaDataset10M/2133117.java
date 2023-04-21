package com.sebulli.fakturama.actions;

import static com.sebulli.fakturama.Translate._;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.WorkbenchWindow;
import com.sebulli.fakturama.Workspace;
import com.sebulli.fakturama.data.DataBaseConnectionState;
import com.sebulli.fakturama.office.FileOrganizer;

/**
 * This action creates a new contact in an editor.
 * 
 * @author Gerd Bartelt
 */
@SuppressWarnings("restriction")
public class ReorganizeDocumentsAction extends NewEditorAction {

    public static final String ACTIONTEXT = _("Reoganize documents");

    /**
	 * Constructor
	 * 
	 */
    public ReorganizeDocumentsAction() {
        super(ACTIONTEXT);
        setToolTipText(_("Reoganize documents"));
        setId(ICommandIds.CMD_REOGANIZE_DOCUMENTS);
        setActionDefinitionId(ICommandIds.CMD_REOGANIZE_DOCUMENTS);
        setImageDescriptor(com.sebulli.fakturama.Activator.getImageDescriptor("/icons/16/reorganize_16.png"));
    }

    /**
	 * Run the action
	 * 
	 * Reorganize all documents
	 */
    @Override
    public void run() {
        if (!DataBaseConnectionState.INSTANCE.isConnected()) return;
        if (Workspace.showMessageBox(SWT.YES | SWT.NO, _("Warning"), _("All printed documents will be renamed.\nYou should first backup your workspace.\nDo you want to reorganize all documents now ?")) != SWT.YES) return;
        IWorkbenchWindow workbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        final StatusLineManager slm = ((WorkbenchWindow) workbenchWindow).getStatusLineManager();
        new Thread(new Runnable() {

            public void run() {
                FileOrganizer.reorganizeDocuments(slm);
                Display.getDefault().syncExec(new Runnable() {

                    public void run() {
                        slm.setMessage(_("done !"));
                    }
                });
            }
        }).start();
    }
}
