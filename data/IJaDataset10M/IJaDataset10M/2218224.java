package com.tensegrity.palo.admin;

import java.util.HashMap;
import java.util.Iterator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveListener3;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.palo.api.Connection;
import org.palo.api.Database;
import com.tensegrity.palo.admin.editors.AccountEditorInput;
import com.tensegrity.palo.admin.editors.AccountsEditor;
import com.tensegrity.palobrowser.tree.TreeNode;
import com.tensegrity.palobrowser.views.DbExplorer;

/**
 * The main plugin class to be used in the desktop.
 */
public class PaloAdminPlugin extends AbstractUIPlugin implements IPerspectiveListener3 {

    private static PaloAdminPlugin plugin;

    private IPerspectiveDescriptor oldPerspective;

    private final HashMap editors = new HashMap();

    /**
	 * The constructor.
	 */
    public PaloAdminPlugin() {
        plugin = this;
    }

    /**
	 * This method is called upon plug-in activation
	 */
    public void start(BundleContext context) throws Exception {
        super.start(context);
        final String[] icons = new String[] { "icons/ckbox/checked.gif", "icons/ckbox/unchecked.gif" };
        ImageRegistry imgReg = getImageRegistry();
        for (int i = 0; i < icons.length; i++) {
            imgReg.put(icons[i], getImageDescriptor(icons[i]));
        }
    }

    /**
	 * This method is called when the plug-in is stopped
	 */
    public void stop(BundleContext context) throws Exception {
        try {
            for (Iterator it = editors.values().iterator(); it.hasNext(); ) {
                AccountsEditor editor = (AccountsEditor) it.next();
                editor.release();
                editor.getEditorSite().getPage().closeEditor(editor, true);
            }
        } finally {
            super.stop(context);
            plugin = null;
        }
    }

    public final void register(AccountsEditor editor) {
        IEditorInput input = editor.getEditorInput();
        if (!editors.containsKey(input)) editors.put(input, editor);
    }

    /**
	 * Returns the shared instance.
	 */
    public static PaloAdminPlugin getDefault() {
        return plugin;
    }

    /**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path.
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
    public static ImageDescriptor getImageDescriptor(String path) {
        return AbstractUIPlugin.imageDescriptorFromPlugin("com.tensegrity.palo.admin", path);
    }

    public void perspectiveActivated(IWorkbenchPage page, IPerspectiveDescriptor perspective) {
        if (perspective.getId().equals(Perspective.class.getName())) {
            String errMsg = null;
            Connection paloConn = getConnection(page);
            if (paloConn == null) errMsg = PaloAdminMessages.getString("PaloAdminPlugin.NoServerConnection"); else if (paloConn.isLegacy()) errMsg = "It is not supported by legacy palo server!"; else if (!paloConn.getUsername().equals("admin")) {
                errMsg = PaloAdminMessages.getString("PaloAdminPlugin.NoAdminRights");
            }
            if (errMsg != null) {
                MessageDialog.openError(getShell(), PaloAdminMessages.getString("PaloAdminPlugin.Error"), errMsg);
                IPerspectiveDescriptor _oldPerspective = oldPerspective;
                page.closePerspective(perspective, false, true);
                page.setPerspective(_oldPerspective);
                return;
            }
            Database[] sysDbs = paloConn.getSystemDatabases();
            if (sysDbs != null && sysDbs.length > 0) {
                AccountEditorInput input = new AccountEditorInput(sysDbs[0]);
                try {
                    page.openEditor(input, AccountsEditor.class.getName());
                } catch (PartInitException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void perspectiveChanged(IWorkbenchPage page, IPerspectiveDescriptor perspective, String changeId) {
    }

    public Connection getConnection(ISelection selection) {
        if (selection != null) {
            if (selection instanceof StructuredSelection) {
                Object el = ((StructuredSelection) selection).getFirstElement();
                if (el instanceof TreeNode) {
                    return getConnection((TreeNode) el);
                }
            }
        }
        return null;
    }

    private final Connection getConnection(IWorkbenchPage page) {
        ISelection selection = page.getSelection(DbExplorer.class.getName());
        if (selection == null) selection = page.getSelection();
        return getConnection(selection);
    }

    private final Shell getShell() {
        Shell shell = Display.getDefault().getActiveShell();
        if (shell == null) {
            Shell[] shells = Display.getCurrent().getShells();
            if (shells.length > 0) return shells[0]; else throw new RuntimeException(PaloAdminMessages.getString("PaloAdminPlugin.NoActiveShell"));
        }
        return shell;
    }

    private final Connection getConnection(TreeNode node) {
        if (node == null) return null;
        Object userObj = node.getUserObject();
        if (userObj instanceof Connection) return (Connection) userObj;
        return getConnection(node.getParent());
    }

    public void perspectiveOpened(IWorkbenchPage page, IPerspectiveDescriptor perspective) {
    }

    public void perspectiveClosed(IWorkbenchPage page, IPerspectiveDescriptor perspective) {
    }

    public void perspectiveDeactivated(IWorkbenchPage page, IPerspectiveDescriptor perspective) {
        oldPerspective = perspective;
    }

    public void perspectiveSavedAs(IWorkbenchPage page, IPerspectiveDescriptor oldPerspective, IPerspectiveDescriptor newPerspective) {
    }

    public void perspectiveChanged(IWorkbenchPage page, IPerspectiveDescriptor perspective, IWorkbenchPartReference partRef, String changeId) {
    }
}
