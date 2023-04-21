package esa.herschel.randres.xmind.actions;

import java.io.File;
import java.io.IOException;
import java.util.Vector;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.eclipse.jface.dialogs.MessageDialog;
import org.xmind.core.ITopic;
import org.xmind.core.IWorkbook;
import esa.herschel.randres.xmind.Activator;
import esa.herschel.randres.xmind.preferences.PreferenceConstants;
import esa.herschel.randres.xmind.util.FileUploader;
import esa.herschel.randres.xmind.util.ITopicable;
import esa.herschel.randres.xmind.util.JiraRecover;
import esa.herschel.randres.xmind.util.ConnectionInfo;

/**
 * Our sample action implements workbench action delegate.
 * The action proxy will be created by the workbench and
 * shown in the UI. When the user tries to use the action,
 * this delegate will be created and execution will be 
 * delegated to it.
 * @see IWorkbenchWindowActionDelegate
 */
public class SampleAction implements IWorkbenchWindowActionDelegate {

    private IWorkbenchWindow window;

    private ITopic m_currentTopic;

    /**
	 * The constructor.
	 */
    public SampleAction() {
    }

    /**
	 * The action has been activated. The argument of the
	 * method represents the 'real' action sitting
	 * in the workbench UI.
	 * @see IWorkbenchWindowActionDelegate#run
	 */
    public void run(IAction action) {
        uploadFile();
    }

    private void insertSXR(String id) {
        IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        if (editor != null) {
            IWorkbook workbook = (IWorkbook) editor.getAdapter(IWorkbook.class);
            if (workbook != null) {
                ITopicable itopicable = JiraRecover.recoverXml(id);
                ITopic ntopic = workbook.createTopic();
                itopicable.dump(ntopic);
                ITopic parentTopic = getCurrentTopic();
                parentTopic.add(ntopic);
            }
        }
    }

    private void uploadFile() {
        System.out.println("upload");
        try {
            IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
            if (editor != null) {
                IWorkbook workbook = (IWorkbook) editor.getAdapter(IWorkbook.class);
                if (workbook != null) {
                    String fileName = workbook.getFile();
                    if (fileName != null) {
                        File file = new File(workbook.getFile());
                        System.out.println(workbook.getFile());
                        IPreferenceStore prefs = Activator.getDefault().getPreferenceStore();
                        String host = prefs.getString(PreferenceConstants.P_HOST);
                        int port = prefs.getInt(PreferenceConstants.P_PORT);
                        String uploadAction = prefs.getString(PreferenceConstants.P_UPLOAD_ACTION);
                        System.out.println(host + port + uploadAction);
                        ConnectionInfo up = new ConnectionInfo(uploadAction, host, port);
                        FileUploader.upload(up, file);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
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
        if (selection instanceof IStructuredSelection) {
            IStructuredSelection ss = (IStructuredSelection) selection;
            Object element = ss.getFirstElement();
            if (element instanceof ITopic) {
                m_currentTopic = (ITopic) element;
            }
        }
    }

    public Object getParent(Object element) {
        if (element instanceof ITopic) {
            ITopic topic = (ITopic) element;
            if (topic.isRoot()) return topic.getOwnedWorkbook();
            return topic.getParent();
        }
        return null;
    }

    private ITopic getCurrentTopic() {
        if (m_currentTopic == null) {
            IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
            if (editor != null) {
                IWorkbook workbook = (IWorkbook) editor.getAdapter(IWorkbook.class);
                if (workbook != null) {
                    m_currentTopic = workbook.getPrimarySheet().getRootTopic();
                }
            }
        }
        return m_currentTopic;
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
