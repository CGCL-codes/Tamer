package esa.herschel.randres.tm.actions;

import java.util.HashMap;
import java.util.Vector;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import esa.herschel.randres.tm.utils.CommonDialog;
import esa.herschel.randres.tm.utils.RetrieveFilenamesJob;
import scpretriever.RetrieveParameters;
import scpretriever.ScpUtils;

/**
 * Our sample action implements workbench action delegate.
 * The action proxy will be created by the workbench and
 * shown in the UI. When the user tries to use the action,
 * this delegate will be created and execution will be 
 * delegated to it.
 * @see IWorkbenchWindowActionDelegate
 */
public class CatchTMAction implements IWorkbenchWindowActionDelegate {

    private IWorkbenchWindow window;

    /**
	 * The constructor.
	 */
    public CatchTMAction() {
    }

    /**
	 * The action has been activated. The argument of the
	 * method represents the 'real' action sitting
	 * in the workbench UI.
	 * @see IWorkbenchWindowActionDelegate#run
	 */
    public void run(IAction action) {
        Vector<String> fields = new Vector<String>();
        fields.add("Apid");
        fields.add("Start");
        fields.add("End");
        CommonDialog cd = new CommonDialog(window.getShell(), fields);
        if (cd.success()) {
            String host = "herdb02";
            String user = "hsc";
            String password = "esacvilspa";
            String apid = cd.getField("Apid");
            String startDate = cd.getField("Start");
            String endDate = cd.getField("End");
            RetrieveParameters params = new RetrieveParameters(host, user, password, apid, startDate, endDate);
            RetrieveFilenamesJob m = new RetrieveFilenamesJob("Retrieve files", window, params);
            m.schedule();
        }
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
