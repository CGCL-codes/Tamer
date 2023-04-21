package de.fu_berlin.inf.dpp.ui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.ImageData;
import org.jivesoftware.smack.Connection;
import org.picocontainer.annotations.Inject;
import de.fu_berlin.inf.dpp.Saros;
import de.fu_berlin.inf.dpp.SarosPluginContext;
import de.fu_berlin.inf.dpp.net.ConnectionState;
import de.fu_berlin.inf.dpp.net.IConnectionListener;
import de.fu_berlin.inf.dpp.ui.ImageManager;
import de.fu_berlin.inf.dpp.ui.Messages;
import de.fu_berlin.inf.dpp.ui.util.WizardUtils;

public class NewContactAction extends Action {

    @Inject
    protected Saros saros;

    public NewContactAction() {
        setToolTipText(Messages.NewContactAction_tooltip);
        setImageDescriptor(new ImageDescriptor() {

            @Override
            public ImageData getImageData() {
                return ImageManager.ELCL_BUDDY_ADD.getImageData();
            }
        });
        SarosPluginContext.initComponent(this);
        saros.getSarosNet().addListener(new IConnectionListener() {

            public void connectionStateChanged(Connection connection, ConnectionState newState) {
                updateEnablement();
            }
        });
        updateEnablement();
    }

    @Override
    public void run() {
        WizardUtils.openAddBuddyWizard();
    }

    protected void updateEnablement() {
        setEnabled(saros.getSarosNet().isConnected());
    }
}
