package org.zaproxy.zap.extension.history;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.TreeSelectionListener;
import org.parosproxy.paros.Constant;
import org.parosproxy.paros.control.Control;
import org.parosproxy.paros.core.scanner.Alert;
import org.parosproxy.paros.extension.AbstractDialog;
import org.parosproxy.paros.extension.history.ExtensionHistory;
import org.parosproxy.paros.extension.scanner.AlertViewPanel;
import org.parosproxy.paros.extension.scanner.ExtensionScanner;
import org.parosproxy.paros.model.HistoryReference;

/**
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class AlertAddDialog extends AbstractDialog implements TreeSelectionListener {

    private static final long serialVersionUID = 1L;

    private JPanel jPanel = null;

    private JButton btnOk = null;

    private JButton btnCancel = null;

    private ExtensionHistory extension = null;

    private ExtensionScanner extScan = null;

    private HistoryReference historyRef;

    private JScrollPane jScrollPane = null;

    private AlertViewPanel alertViewPanel = null;

    /**
     * @throws HeadlessException
     */
    public AlertAddDialog() throws HeadlessException {
        super();
        initialize();
    }

    /**
     * @param arg0
     * @param arg1
     * @throws HeadlessException
     */
    public AlertAddDialog(Frame arg0, boolean arg1) throws HeadlessException {
        super(arg0, arg1);
        initialize();
    }

    /**
	 * This method initializes this
	 * 
	 * @return void
	 */
    private void initialize() {
        this.setTitle(Constant.messages.getString("alert.add.title"));
        this.setContentPane(getJPanel());
        this.addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowOpened(java.awt.event.WindowEvent e) {
            }

            public void windowClosing(java.awt.event.WindowEvent e) {
                btnCancel.doClick();
            }
        });
        pack();
    }

    /**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getJPanel() {
        if (jPanel == null) {
            GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
            java.awt.GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
            javax.swing.JLabel jLabel2 = new JLabel();
            java.awt.GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            java.awt.GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            jPanel = new JPanel();
            jPanel.setLayout(new GridBagLayout());
            jPanel.setPreferredSize(new java.awt.Dimension(400, 600));
            jPanel.setMinimumSize(new java.awt.Dimension(400, 600));
            gridBagConstraints2.gridx = 1;
            gridBagConstraints2.gridy = 5;
            gridBagConstraints2.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints2.anchor = java.awt.GridBagConstraints.EAST;
            gridBagConstraints3.gridx = 2;
            gridBagConstraints3.gridy = 5;
            gridBagConstraints3.insets = new java.awt.Insets(2, 2, 2, 10);
            gridBagConstraints3.anchor = java.awt.GridBagConstraints.EAST;
            gridBagConstraints13.gridx = 0;
            gridBagConstraints13.gridy = 5;
            gridBagConstraints13.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints13.weightx = 1.0D;
            gridBagConstraints13.insets = new java.awt.Insets(2, 10, 2, 5);
            gridBagConstraints15.weightx = 1.0D;
            gridBagConstraints15.weighty = 1.0D;
            gridBagConstraints15.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints15.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints15.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints15.gridwidth = 3;
            gridBagConstraints15.gridx = 0;
            gridBagConstraints15.gridy = 2;
            gridBagConstraints15.anchor = java.awt.GridBagConstraints.NORTHWEST;
            gridBagConstraints15.ipadx = 0;
            gridBagConstraints15.ipady = 10;
            jPanel.add(getJScrollPane(), gridBagConstraints15);
            jPanel.add(jLabel2, gridBagConstraints13);
            jPanel.add(getBtnCancel(), gridBagConstraints2);
            jPanel.add(getBtnOk(), gridBagConstraints3);
        }
        return jPanel;
    }

    /**
	 * This method initializes btnStart	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getBtnOk() {
        if (btnOk == null) {
            btnOk = new JButton();
            btnOk.setText(Constant.messages.getString("alert.add.button.save"));
            btnOk.setMinimumSize(new java.awt.Dimension(75, 30));
            btnOk.setPreferredSize(new java.awt.Dimension(75, 30));
            btnOk.setMaximumSize(new java.awt.Dimension(100, 40));
            btnOk.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    Alert alert = alertViewPanel.getAlert();
                    try {
                        if (alert.getAlertId() >= 0) {
                            if (extScan == null) {
                                extScan = (ExtensionScanner) Control.getSingleton().getExtensionLoader().getExtension("ExtensionScanner");
                            }
                            extScan.updateAlertInDB(alert);
                            extScan.displayAlert(alert);
                            extScan.updateAlertInTree(alertViewPanel.getOriginalAlert(), alert);
                            if (historyRef != null) {
                                historyRef.updateAlert(alert);
                                extension.getHistoryList().notifyItemChanged(historyRef);
                            }
                        } else {
                            historyRef.addAlert(alert);
                            extension.getHistoryList().notifyItemChanged(historyRef);
                            if (extScan == null) {
                                extScan = (ExtensionScanner) Control.getSingleton().getExtensionLoader().getExtension("ExtensionScanner");
                            }
                            extScan.alertFound(alert);
                        }
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    extension.hideAlertAddDialog();
                }
            });
        }
        return btnOk;
    }

    /**
	 * This method initializes btnStop	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getBtnCancel() {
        if (btnCancel == null) {
            btnCancel = new JButton();
            btnCancel.setText(Constant.messages.getString("alert.add.button.cancel"));
            btnCancel.setMaximumSize(new java.awt.Dimension(100, 40));
            btnCancel.setMinimumSize(new java.awt.Dimension(70, 30));
            btnCancel.setPreferredSize(new java.awt.Dimension(70, 30));
            btnCancel.setEnabled(true);
            btnCancel.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    extension.hideAlertAddDialog();
                }
            });
        }
        return btnCancel;
    }

    public void setPlugin(ExtensionHistory plugin) {
        this.extension = plugin;
        plugin.getView().getSiteTreePanel().getTreeSite().addTreeSelectionListener(this);
    }

    public void valueChanged(javax.swing.event.TreeSelectionEvent e) {
    }

    private AlertViewPanel getAlertViewPanel() {
        if (alertViewPanel == null) {
            alertViewPanel = new AlertViewPanel(true);
        }
        return this.alertViewPanel;
    }

    public void setAlert(Alert alert) {
        this.getAlertViewPanel().displayAlert(alert);
        this.setHistoryRef(alert.getMessage().getHistoryRef());
    }

    /**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
    private JScrollPane getJScrollPane() {
        if (jScrollPane == null) {
            jScrollPane = new JScrollPane();
            jScrollPane.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            jScrollPane.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            jScrollPane.setViewportView(getAlertViewPanel());
        }
        return jScrollPane;
    }

    public HistoryReference getHistoryRef() {
        return historyRef;
    }

    public void setHistoryRef(HistoryReference historyRef) {
        this.historyRef = historyRef;
        alertViewPanel.setHistoryRef(historyRef);
    }
}
