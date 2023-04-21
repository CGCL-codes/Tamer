package net.sf.elbe.ui.actions;

import net.sf.elbe.core.jobs.ReadEntryJob;
import net.sf.elbe.core.model.DN;
import net.sf.elbe.core.model.IConnection;
import net.sf.elbe.core.model.IEntry;
import net.sf.elbe.ui.jobs.RunnableContextJobAdapter;
import net.sf.elbe.ui.views.browser.BrowserView;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

public abstract class LocateInDitAction extends ElbeAction {

    public final void run() {
        Object[] connectionAndDn = getConnectionAndDn();
        if (connectionAndDn != null) {
            IConnection connection = (IConnection) connectionAndDn[0];
            DN dn = (DN) connectionAndDn[1];
            IEntry entry = connection.getEntryFromCache(dn);
            if (entry == null) {
                ReadEntryJob job = new ReadEntryJob(connection, dn);
                RunnableContextJobAdapter.execute(job);
                entry = job.getReadEntry();
            }
            if (entry != null) {
                String targetId = BrowserView.getId();
                IViewPart targetView = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(targetId);
                if (targetView == null) {
                    try {
                        targetView = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(targetId, null, IWorkbenchPage.VIEW_ACTIVATE);
                    } catch (PartInitException e) {
                    }
                }
                if (targetView != null && targetView instanceof BrowserView) {
                    ((BrowserView) targetView).select(entry);
                    PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().activate(targetView);
                }
            }
        }
    }

    public String getCommandId() {
        return "net.sf.elbe.action.locateInDit";
    }

    public final boolean isEnabled() {
        return getConnectionAndDn() != null;
    }

    protected abstract Object[] getConnectionAndDn();
}
