package jsattrak.gui;

import java.util.List;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import jsattrak.utilities.ProgressStatus;
import jsattrak.utilities.TLEDownloader;

/**
 *
 * @author  sgano
 */
public class TleDownloaderPanel extends javax.swing.JPanel implements java.io.Serializable {

    JSatTrak app;

    JInternalFrame iframe;

    /** Creates new form TleDownloaderPanel */
    public TleDownloaderPanel(JSatTrak app, JInternalFrame iframe) {
        initComponents();
        this.app = app;
        this.iframe = iframe;
    }

    private void initComponents() {
        jTabbedPane = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        tleProgressBar = new javax.swing.JProgressBar();
        tleStartDowloadButton = new javax.swing.JButton();
        tleProgressLabel = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        proxyPortTextField = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        proxyCheckBox = new javax.swing.JCheckBox();
        jLabel2 = new javax.swing.JLabel();
        proxyHostTextField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        proxyPortField = new javax.swing.JTextField();
        tleProgressBar.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        tleStartDowloadButton.setText("Start Dowload");
        tleStartDowloadButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tleStartDowloadButtonActionPerformed(evt);
            }
        });
        tleProgressLabel.setFont(new java.awt.Font("Dialog", 2, 12));
        tleProgressLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        tleProgressLabel.setText("progress details");
        jLabel5.setFont(new java.awt.Font("Dialog", 1, 15));
        jLabel5.setForeground(new java.awt.Color(0, 0, 102));
        jLabel5.setText("NORAD Two-Line Element Data Downloader");
        jLabel6.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabel6.setText("Data from: celestrak.com ");
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(jLabel5)).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(jLabel6)).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(tleProgressLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 355, Short.MAX_VALUE)).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(tleProgressBar, javax.swing.GroupLayout.DEFAULT_SIZE, 355, Short.MAX_VALUE)).addGroup(jPanel1Layout.createSequentialGroup().addGap(125, 125, 125).addComponent(tleStartDowloadButton))).addContainerGap()));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(jLabel5).addGap(14, 14, 14).addComponent(jLabel6).addGap(21, 21, 21).addComponent(tleProgressLabel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(tleProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(tleStartDowloadButton).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jTabbedPane.addTab("TLE Downloader", jPanel1);
        jLabel1.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabel1.setText("Note: Only change these settings if downloader is not working correctly.");
        proxyCheckBox.setText("Internet Connection Requires a Proxy Server");
        proxyCheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        proxyCheckBox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jLabel2.setText("Proxy Host: ");
        jLabel3.setText("Proxy Port:");
        proxyPortField.setText("80");
        javax.swing.GroupLayout proxyPortTextFieldLayout = new javax.swing.GroupLayout(proxyPortTextField);
        proxyPortTextField.setLayout(proxyPortTextFieldLayout);
        proxyPortTextFieldLayout.setHorizontalGroup(proxyPortTextFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(proxyPortTextFieldLayout.createSequentialGroup().addContainerGap().addGroup(proxyPortTextFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel1).addComponent(proxyCheckBox).addGroup(proxyPortTextFieldLayout.createSequentialGroup().addGroup(proxyPortTextFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel2).addComponent(jLabel3)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(proxyPortTextFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(proxyPortField, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(proxyHostTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)))).addContainerGap(30, Short.MAX_VALUE)));
        proxyPortTextFieldLayout.setVerticalGroup(proxyPortTextFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(proxyPortTextFieldLayout.createSequentialGroup().addContainerGap().addComponent(jLabel1).addGap(15, 15, 15).addComponent(proxyCheckBox).addGap(23, 23, 23).addGroup(proxyPortTextFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel2).addComponent(proxyHostTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(proxyPortTextFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel3).addComponent(proxyPortField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(44, Short.MAX_VALUE)));
        jTabbedPane.addTab("Internet Settings", proxyPortTextField);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jTabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jTabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 196, Short.MAX_VALUE));
    }

    private void tleStartDowloadButtonActionPerformed(java.awt.event.ActionEvent evt) {
        tleStartDowloadButton.setEnabled(false);
        tleProgressBar.setValue(0);
        tleProgressBar.setStringPainted(true);
        tleProgressBar.repaint();
        app.startStatusAnimation();
        SwingWorker<Boolean, ProgressStatus> worker = new SwingWorker<Boolean, ProgressStatus>() {

            boolean result;

            String errorMessage;

            @Override
            public Boolean doInBackground() {
                publish(new ProgressStatus(0, ""));
                TLEDownloader tleDownloader = new TLEDownloader();
                if (proxyCheckBox.isSelected()) {
                    tleDownloader.setUsingProxy(true);
                    tleDownloader.setProxyHost(proxyHostTextField.getText());
                    tleDownloader.setProxyPort(proxyPortField.getText());
                }
                result = tleDownloader.startTLEDownload();
                if (result) {
                    while (tleDownloader.hasMoreToDownload() && result) {
                        publish(new ProgressStatus(tleDownloader.getPercentComplete(), tleDownloader.getNextFileName()));
                        result = tleDownloader.downloadNextTLE();
                    }
                }
                publish(new ProgressStatus(100, ""));
                if (!result) {
                    errorMessage = "ERROR Updating TLE Data: " + tleDownloader.getErrorText();
                }
                return new Boolean(result);
            }

            @Override
            protected void process(List<ProgressStatus> chunks) {
                ProgressStatus ps = chunks.get(chunks.size() - 1);
                tleProgressBar.setValue(ps.getPercentComplete());
                tleProgressBar.repaint();
                tleProgressLabel.setText("Downloading File: " + ps.getStatusText());
            }

            @Override
            protected void done() {
                app.stopStatusAnimation();
                tleProgressBar.setValue(0);
                tleProgressBar.setStringPainted(false);
                tleProgressLabel.setText("");
                tleStartDowloadButton.setEnabled(true);
                if (result) {
                    app.updateTleDataInCurrentList();
                    String message = "Satellite TLE data was successfully updated!";
                    JOptionPane.showMessageDialog(app, message, "Update Success", JOptionPane.INFORMATION_MESSAGE);
                    app.setStatusMessage(message);
                    iframe.setVisible(false);
                } else {
                    JOptionPane.showMessageDialog(app, errorMessage, "ERROR", JOptionPane.ERROR_MESSAGE);
                    app.setStatusMessage(errorMessage);
                }
            }
        };
        worker.execute();
    }

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JTabbedPane jTabbedPane;

    private javax.swing.JCheckBox proxyCheckBox;

    private javax.swing.JTextField proxyHostTextField;

    private javax.swing.JTextField proxyPortField;

    private javax.swing.JPanel proxyPortTextField;

    private javax.swing.JProgressBar tleProgressBar;

    private javax.swing.JLabel tleProgressLabel;

    private javax.swing.JButton tleStartDowloadButton;
}
