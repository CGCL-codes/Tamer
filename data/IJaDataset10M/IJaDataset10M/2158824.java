package jp.go.aist.sot.client.control;

import java.awt.Component;
import jp.go.aist.sot.client.common.StatusManager;
import jp.go.aist.sot.client.gui.SignOnComponent;
import jp.go.aist.sot.client.gui.SignOnController;
import jp.go.aist.sot.client.resources.MessageHandler;
import jp.go.aist.sot.client.gui.panel.ProxyCertOptsPanel;

public class ProxyCertsOptController extends StatusManager implements SignOnComponent {

    private static final String CLASS_NAME = CertsMgrController.class.getName();

    private static final MessageHandler mh = new MessageHandler("proxycerts_controller");

    private SignOnManager mgr;

    private SignOnController controller = null;

    private ProxyCertOptsPanel hostListPanel = null;

    public ProxyCertsOptController(SignOnManager mgr, SignOnController controller) {
        this.mgr = mgr;
        this.controller = controller;
    }

    public String getTitle() {
        return mh.getMessage("panel_title");
    }

    public Component getComponent() {
        if (hostListPanel == null) {
            hostListPanel = new ProxyCertOptsPanel(controller);
        }
        return hostListPanel;
    }
}
