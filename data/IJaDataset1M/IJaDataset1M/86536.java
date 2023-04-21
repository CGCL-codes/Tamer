package org.xaware.designer.datatools.action;

import java.util.ArrayList;
import org.eclipse.core.resources.IFile;
import org.eclipse.datatools.connectivity.IConnectionProfile;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.xaware.designer.datatools.connectivity.XADataSource;
import org.xaware.designer.datatools.connectivity.XARefreshManager;
import org.xaware.designer.datatools.connectivity.connectionprofile.ProfileUtils;
import org.xaware.designer.datatools.connectivity.ui.action.NewBizCompAction;

/**
 * Disconnect action for the Connection Profile.
 * 
 * @author blueAlly
 * 
 */
public class DisconnectAction extends org.eclipse.datatools.connectivity.ui.actions.DisconnectAction {

    /**
     * (non-Javadoc)
     * 
     * @see org.eclipse.datatools.connectivity.ui.actions.DisconnectAction#selectionChanged(org.eclipse.jface.action.IAction,
     *      org.eclipse.jface.viewers.ISelection)
     */
    @Override
    public void selectionChanged(IAction action, ISelection selection) {
        Object[] currentSelection = ((IStructuredSelection) selection).toArray();
        for (int i = 0; i < currentSelection.length; i++) {
            Object object = currentSelection[i];
            if (object instanceof IConnectionProfile) {
                IFile bizDriver = NewBizCompAction.getBizDriverFile((IStructuredSelection) selection);
                if (bizDriver == null) {
                    ArrayList<XADataSource> dataSources = XARefreshManager.getRefreshManager().getBizDrivers((IConnectionProfile) object);
                    bizDriver = (dataSources.size() > 0) ? dataSources.get(0).getIFile() : null;
                }
                IConnectionProfile profile = ProfileUtils.getProfile((IConnectionProfile) object, bizDriver);
                currentSelection[i] = profile;
            }
        }
        super.selectionChanged(action, new StructuredSelection(currentSelection));
    }
}
