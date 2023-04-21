package org.kalypso.nofdpidss.core.view.map;

import java.util.Map;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.progress.UIJob;
import org.kalypso.nofdpidss.core.i18n.Messages;
import org.kalypso.ui.views.map.MapView;

/**
 * @author Dirk Kuch
 */
public class CommandSetMapName extends MapCommand {

    protected final String m_customName;

    public CommandSetMapName(final String customName) {
        m_customName = customName;
    }

    /**
   * @see org.kalypso.commons.command.ICommand#process()
   */
    public void process() throws Exception {
        final Map<Object, Object> map = getProcessParameter();
        final MapView mapView = (MapView) map.get(MapView.class);
        new UIJob(Messages.CommandSetMapName_0) {

            @Override
            public IStatus runInUIThread(final IProgressMonitor monitor) {
                mapView.setCustomName(m_customName);
                mapView.setStatusBarMessage("");
                return Status.OK_STATUS;
            }
        }.schedule();
        new UIJob(Messages.CommandSetMapName_0) {

            @Override
            public IStatus runInUIThread(final IProgressMonitor monitor) {
                mapView.setCustomName(m_customName);
                mapView.setStatusBarMessage("");
                return Status.OK_STATUS;
            }
        }.schedule(250);
    }
}
