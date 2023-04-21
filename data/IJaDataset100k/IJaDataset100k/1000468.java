package org.dicom4j.apps.commons.network.verificationscu.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.dicom4j.apps.commons.network.ui.panel.AbstractSCUPanel;
import org.dicom4j.apps.commons.ui.UIController;
import org.dicom4j.apps.commons.ui.UserMessages;
import org.dicom4j.apps.commons.ui.panel.AssociationEventsPanel;
import org.dicom4j.dicom.uniqueidentifiers.SOPClass;
import org.dicom4j.network.association.Association;
import org.dicom4j.network.association.associate.AssociateRequest;
import org.dicom4j.network.association.associate.AssociateSession;
import org.dicom4j.network.association.listeners.AssociationListener;
import org.dicom4j.network.association.listeners.DefaultAssociationListener;
import org.dicom4j.network.dimse.messages.CEchoRequestMessage;
import org.dicom4j.network.dimse.messages.CEchoResponseMessage;
import org.dicom4j.network.dimse.messages.DimseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.dicom4j.apps.commons.application.I18n;

/**
 * Panel to performs Verification (C-Echo) in GUI or no-GUI mode.
 *
 * @since 0.1
 * @author <a href="mailto:straahd@users.sourceforge.net">Laurent Lecomte 
 *
 */
public class VerificationPanel extends AbstractSCUPanel {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * the logger
	 */
    private static final Logger logger = LoggerFactory.getLogger(VerificationPanel.class);

    private AssociationEventsPanel statusPanel = new AssociationEventsPanel();

    private EchoQueryRunnerListener listener = new EchoQueryRunnerListener();

    public VerificationPanel(UIController controller) {
        super(controller);
        setRemoteDevicePanelVisible(true);
        getRemoteDevicePanel().connectButton.setVisible(false);
        getRemoteDevicePanel().disconnectButton.setVisible(false);
        setEchoActionListener(new EchoActionListener());
        add(getTopPanel(), BorderLayout.NORTH);
        getConnector().getConfiguration().setMonitor(statusPanel);
        add(statusPanel, BorderLayout.CENTER);
    }

    private class EchoActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            try {
                if (checkConnectSettings()) {
                    getController().startLongPerform(true, "");
                    statusPanel.addInformation(I18n.getTrans(I18n.getTrans("performingCEcho")));
                    openAssocation(createAssociateRequest(), getAssociationListener());
                }
            } catch (Exception ex) {
                getController().stopLongPerform();
                statusPanel.addError(ex.getMessage());
            }
        }
    }

    private class EchoQueryRunnerListener extends DefaultAssociationListener {

        @Override
        public void associationOpened(Association association, AssociateSession associateSession) throws Exception {
            super.associationOpened(association, associateSession);
            CEchoRequestMessage message = association.getMessageFactory().newCEchoRequest();
            logger.info("CEchoRequestMessage: \n " + message.toString());
            byte presID = associateSession.getSuitablePresentationContextID(SOPClass.Verification);
            association.sendMessage(presID, message);
        }

        @Override
        public void messageReceived(Association association, byte presentationContextID, DimseMessage message) throws Exception {
            super.messageReceived(association, presentationContextID, message);
            if (message instanceof CEchoResponseMessage) {
                if (((CEchoResponseMessage) message).isSuccess()) {
                    statusPanel.addInformation(I18n.getTrans("echoSucceeded"));
                }
            } else {
                statusPanel.addError(I18n.getTrans("echoFailed"));
            }
            getController().stopLongPerform();
        }

        public void exceptionCaught(Association association, Throwable cause) {
            statusPanel.addError(UserMessages.getNetworkError(I18n.getTrans("echo")));
            getController().stopLongPerform();
        }
    }

    @Override
    protected AssociateRequest createAssociateRequest() throws Exception {
        return getConnector().getAssociatePrimitiveFactory().newVerificationQuery();
    }

    @Override
    protected AssociationListener getAssociationListener() {
        return listener;
    }
}
