package edu.indiana.extreme.xbaya.gpel.gui;

import edu.indiana.extreme.xbaya.XBayaEngine;
import edu.indiana.extreme.xbaya.gpel.script.BPELScript;
import edu.indiana.extreme.xbaya.gui.*;
import edu.indiana.extreme.xbaya.myproxy.MyProxyClient;
import edu.indiana.extreme.xbaya.myproxy.gui.MyProxyChecker;
import edu.indiana.extreme.xbaya.security.UserX509Credential;
import edu.indiana.extreme.xbaya.security.XBayaSecurity;
import edu.indiana.extreme.xbaya.wf.Workflow;
import edu.indiana.extreme.xbaya.workflow.WorkflowClient;
import edu.indiana.extreme.xbaya.workflow.WorkflowEngineException;
import org.ietf.jgss.GSSCredential;
import xsul5.MLogger;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

/**
 * @author Satoshi Shirasuna
 */
public class GPELDeployWindow {

    private static final MLogger logger = MLogger.getLogger();

    protected XBayaEngine engine;

    private MyProxyChecker myProxyChecker;

    protected XBayaDialog dialog;

    private JButton newButton;

    private JButton redeployButton;

    protected XBayaTextField nameTextField;

    protected XBayaTextArea descriptionTextArea;

    private GPELDeployer deployer;

    private Workflow workflow;

    /**
     * Constructs a GPELDeployWindow.
     * 
     * @param engine
     */
    public GPELDeployWindow(XBayaEngine engine) {
        this.engine = engine;
        this.deployer = new GPELDeployer(engine);
        this.myProxyChecker = new MyProxyChecker(this.engine);
        initGUI();
    }

    /**
     * Shows the window.
     */
    public void show() {
        WorkflowClient workflowClient = this.engine.getWorkflowClient();
        if (workflowClient.isSecure()) {
            boolean loaded = this.myProxyChecker.loadIfNecessary();
            if (!loaded) {
                return;
            }
            MyProxyClient myProxyClient = this.engine.getMyProxyClient();
            GSSCredential proxy = myProxyClient.getProxy();
            UserX509Credential credential = new UserX509Credential(proxy, XBayaSecurity.getTrustedCertificates());
            try {
                workflowClient.setUserX509Credential(credential);
            } catch (WorkflowEngineException e) {
                this.engine.getErrorWindow().error(ErrorMessages.GPEL_ERROR, e);
                return;
            }
        }
        this.workflow = this.engine.getWorkflow();
        BPELScript bpel = new BPELScript(this.workflow);
        ArrayList<String> warnings = new ArrayList<String>();
        if (!bpel.validate(warnings)) {
            StringBuilder buf = new StringBuilder();
            for (String warning : warnings) {
                buf.append("- ");
                buf.append(warning);
                buf.append("\n");
            }
            this.engine.getErrorWindow().warning(buf.toString());
            return;
        }
        String name = this.workflow.getName();
        String description = this.workflow.getDescription();
        this.nameTextField.setText(name);
        this.descriptionTextArea.setText(description);
        if (this.workflow.getGPELTemplateID() == null) {
            this.redeployButton.setEnabled(false);
        } else {
            this.redeployButton.setEnabled(true);
        }
        logger.finest("before show");
        this.dialog.show();
    }

    /**
     * Hides the window.
     */
    public void hide() {
        this.dialog.hide();
    }

    protected void deploy(boolean redeploy) {
        String name = this.nameTextField.getText();
        String description = this.descriptionTextArea.getText();
        if (name.trim().length() == 0) {
            this.engine.getErrorWindow().warning("You need to set the name of the workflow.");
        } else {
            this.engine.getGUI().getGraphCanvas().setNameAndDescription(name, description);
            hide();
            this.deployer.deploy(redeploy);
        }
    }

    /**
     * Initializes the GUI
     */
    private void initGUI() {
        this.nameTextField = new XBayaTextField();
        XBayaLabel nameLabel = new XBayaLabel("Name", this.nameTextField);
        this.descriptionTextArea = new XBayaTextArea();
        XBayaLabel descriptionLabel = new XBayaLabel("Description", this.descriptionTextArea);
        GridPanel mainPanel = new GridPanel();
        mainPanel.add(nameLabel);
        mainPanel.add(this.nameTextField);
        mainPanel.add(descriptionLabel);
        mainPanel.add(this.descriptionTextArea);
        mainPanel.layout(2, 2, 1, 1);
        this.newButton = new JButton("Deploy New");
        this.newButton.setDefaultCapable(true);
        this.newButton.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                deploy(false);
            }
        });
        this.redeployButton = new JButton("Redeploy");
        this.redeployButton.setDefaultCapable(true);
        this.redeployButton.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                deploy(true);
            }
        });
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                hide();
            }
        });
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(this.newButton);
        buttonPanel.add(this.redeployButton);
        buttonPanel.add(cancelButton);
        this.dialog = new XBayaDialog(this.engine, "Deploy the Workflow to the BPEL Engine", mainPanel, buttonPanel);
    }
}
