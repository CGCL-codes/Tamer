package org.bcholmes.jmicro.cif.ui.main;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;

public class OkCancelButtonPanel extends JPanel {

    class OkAction extends AbstractAction {

        private static final long serialVersionUID = 6029176766969838633L;

        OkAction() {
            super(Label.OK.getDescription());
        }

        public void actionPerformed(ActionEvent event) {
            OkCancelButtonPanel.this.handler.doOk();
        }
    }

    class CancelAction extends AbstractAction {

        private static final long serialVersionUID = 6029176766969838633L;

        CancelAction() {
            super(Label.CANCEL.getDescription());
        }

        public void actionPerformed(ActionEvent event) {
            OkCancelButtonPanel.this.handler.doCancel();
        }
    }

    private static final long serialVersionUID = 2563009368073757560L;

    private JButton okButton;

    private JButton cancelButton;

    private final OkCancelHandler handler;

    public OkCancelButtonPanel(OkCancelHandler handler) {
        this.handler = handler;
        initializeGui();
    }

    private void initializeGui() {
        setLayout(new FlowLayout());
        this.okButton = new JButton(new OkAction());
        this.cancelButton = new JButton(new CancelAction());
        add(this.okButton);
        add(this.cancelButton);
    }

    public boolean isOkEnabled() {
        return this.okButton.isEnabled();
    }

    public void setOkEnabled(boolean okEnabled) {
        this.okButton.setEnabled(okEnabled);
    }
}
