package com.intel.gpe.gridbeans.breakpoint.plugin;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.xml.namespace.QName;
import com.intel.gpe.client2.Client;
import com.intel.gpe.client2.WorkflowClient;
import com.intel.gpe.client2.defaults.preferences.INode;
import com.intel.gpe.clients.api.workflow.WorkflowJobClient;
import com.intel.gpe.gridbeans.plugins.GridBeanPanel;

/**
 * 
 * @author Valery Shorin
 * @version $Id: BreakpointInputPanel.java,v 1.11 2007/02/22 14:40:18 dizhigul Exp $
 */
public class BreakpointInputPanel extends GridBeanPanel {

    JButton resumeButton;

    private WorkflowClient wfClient;

    public BreakpointInputPanel(Client client, INode node) {
        super(client, "BreakPoint", node);
        if (client instanceof WorkflowClient) {
            wfClient = (WorkflowClient) client;
        }
        buildComponents();
    }

    private void buildComponents() {
        ButtonListener buttonListener = new ButtonListener();
        resumeButton = new JButton("Resume");
        add(resumeButton);
        resumeButton.addActionListener(buttonListener);
        if (wfClient == null) {
            parent2.getMessageAdapter().showMessage("This gridbean may be used only in workflow");
            resumeButton.setEnabled(false);
        }
    }

    private class ButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent event) {
            Object source = event.getSource();
            WorkflowJobClient wjc;
            if (source == resumeButton) {
                wjc = wfClient.getJobClient();
                if (wjc != null) {
                    QName resume = new QName("http://unigrids.org/2005/06/services/jms", "Resume");
                    try {
                        wjc.notify(resume, null);
                    } catch (Exception e) {
                        parent2.getMessageAdapter().showException("Exception occured while resuming workflow", e);
                    }
                }
            }
        }
    }
}
