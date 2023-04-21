package rallydemogef.actions;

import java.io.File;
import java.rmi.RemoteException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import persister.NotConnectedException;
import persister.distributed.ClientCommunicator;
import persister.factory.PersisterConnectionInfo;
import persister.factory.PersisterFactory;
import rallydemogef.Application;
import rallydemogef.Editor;
import rallydemogef.PathEditorInput;
import rallydemogef.uielements.PersisterConnectDialog;
import rallydemogef.uielements.PersisterConnectDialogChange;
import cards.CardConstants;

public class PersisterSelectAction extends Action implements IWorkbenchAction, ISelectionListener {

    private final IWorkbenchWindow window;

    public static final String ID = "rallydemogef.actions.PersisterSelectAction";

    public PersisterSelectAction(IWorkbenchWindow window) {
        this.window = window;
        setId(ID);
        setText("&Connect to Persister");
        setToolTipText("Connnect to Persister");
        setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(Application.PLUGIN_ID, CardConstants.OpenPlanningDataIcon));
        window.getSelectionService().addSelectionListener(this);
    }

    public void run() {
        if (login()) {
            IWorkbenchPage page;
            page = window.getActivePage();
            page.closeEditor(page.getActiveEditor(), true);
            IEditorInput neweditor = createEditorInput(new File("temp"));
            String editorId = "RallyDemoGEF.Editor";
            try {
                page.openEditor(neweditor, editorId);
                String titleTab;
                titleTab = PersisterConnectionInfo.getPersisterConnectionInfo().getProjectName() + ":" + PersisterConnectionInfo.getPersisterConnectionInfo().getInitial();
                ((Editor) page.getActiveEditor()).updateTitleTab(titleTab);
            } catch (PartInitException e) {
                e.printStackTrace();
            }
        }
    }

    private IEditorInput createEditorInput(File file) {
        IPath path = new Path(file.getAbsolutePath());
        PathEditorInput input = new PathEditorInput(path);
        return input;
    }

    private boolean login() {
        PersisterConnectDialog pcDialog = new PersisterConnectDialog(null);
        if (pcDialog.open() != Window.OK) {
            return false;
        } else {
            return true;
        }
    }

    public void dispose() {
        window.getSelectionService().removeSelectionListener(this);
    }

    public void selectionChanged(IWorkbenchPart part, ISelection selection) {
        if (selection instanceof IStructuredSelection) {
            selection = (IStructuredSelection) selection;
            setEnabled(true);
        } else setEnabled(false);
    }
}
