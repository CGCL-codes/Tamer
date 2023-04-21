package org.rjam.gui.admin;

import javax.swing.ProgressMonitor;
import javax.swing.SwingUtilities;
import org.jfree.ui.RefineryUtilities;
import org.rjam.gui.api.IDataManager;
import org.rjam.gui.base.Constants;

/**
 *
 * @author  Tony Bringardner
 */
public class ApplicationEditDialog extends javax.swing.JDialog {

    private static final long serialVersionUID = 1L;

    private boolean canceled;

    private Application app;

    private IDataManager dm;

    /** Creates new form ApplicationEditDialog */
    public ApplicationEditDialog() {
        super();
        setIconImage(new javax.swing.ImageIcon(getClass().getResource(Constants.IMAGE_TITLE)).getImage());
        setModal(true);
        initComponents();
    }

    public boolean isCanceled() {
        return canceled;
    }

    public boolean showDialog(IDataManager dm, Application app, boolean exists) {
        this.app = app;
        this.dm = dm;
        int id = app.getAppId();
        if (id != 0) {
            appId.setText("" + app.getAppId());
        }
        String tmp = app.getAppName();
        if (tmp != null) {
            appName.setText(tmp);
        }
        if (exists) {
            appName.setEditable(false);
        }
        id = app.getExternId();
        if (id > 0) {
            externalId.setText("" + id);
        }
        tmp = app.getDescription();
        if (tmp != null) {
            description.setText(tmp);
        }
        updateView();
        RefineryUtilities.centerFrameOnScreen(this);
        setVisible(true);
        if (!canceled) {
            if (!exists) {
                app.setAppName(appName.getText());
            }
            int exid = 0;
            try {
                exid = Integer.parseInt(externalId.getText());
            } catch (Exception e) {
            }
            app.setExternId(exid);
            app.setDescription(description.getText());
        }
        return canceled;
    }

    private void updateView() {
        firstDateFld.setText("");
        lastDateFld.setText("");
        long date = app.getMinDate();
        if (date > 0) {
            firstDateFld.setText(app.formatDate(date));
        }
        date = app.getMaxDate();
        if (date > 0) {
            lastDateFld.setText(app.formatDate(date));
        }
    }

    private void initComponents() {
        centerPanel = new javax.swing.JPanel();
        appId = new javax.swing.JTextField();
        appName = new javax.swing.JTextField();
        externalId = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        description = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        firstDateFld = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        lastDateFld = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        southPanel = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        centerPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        appId.setEditable(false);
        appId.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        centerPanel.add(appId, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 20, 50, -1));
        centerPanel.add(appName, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 20, 90, -1));
        externalId.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        centerPanel.add(externalId, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 20, 80, -1));
        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder("Description"));
        description.setColumns(20);
        description.setRows(5);
        jScrollPane1.setViewportView(description);
        centerPanel.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 120, 530, 90));
        jLabel1.setText("App Id:");
        centerPanel.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, -1, 20));
        jLabel2.setText("Name:");
        centerPanel.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 20, -1, 20));
        jLabel3.setText("External Id:");
        centerPanel.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 20, -1, 20));
        firstDateFld.setEditable(false);
        centerPanel.add(firstDateFld, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 70, 130, -1));
        jLabel4.setText("First Data");
        centerPanel.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 70, 50, 20));
        jLabel5.setText("Last Data");
        centerPanel.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 70, 50, 20));
        lastDateFld.setEditable(false);
        centerPanel.add(lastDateFld, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 70, 130, -1));
        jButton1.setText("Find Dates");
        jButton1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        centerPanel.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 70, -1, -1));
        getContentPane().add(centerPanel, java.awt.BorderLayout.CENTER);
        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });
        southPanel.add(okButton);
        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        southPanel.add(cancelButton);
        getContentPane().add(southPanel, java.awt.BorderLayout.SOUTH);
        pack();
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        final int max = 20;
        final ProgressMonitor bar = new ProgressMonitor(this, "Searching for data (This may take a few of minutes).", "Note", 0, max);
        new Thread(new Runnable() {

            public void run() {
                try {
                    app.getMaxDate(dm.getGlobalConnection());
                    updateView();
                } catch (Exception e) {
                    bar.close();
                }
            }
        }).start();
        new Thread(new Runnable() {

            public void run() {
                int seconds = 0;
                try {
                    while (app.getMaxDate() <= 0 && !bar.isCanceled()) {
                        Thread.sleep(250);
                        final int status = ++seconds;
                        SwingUtilities.invokeLater(new Runnable() {

                            public void run() {
                                bar.setProgress(status % max);
                                bar.setNote("Elapsed Time=" + (status / 4));
                            }
                        });
                    }
                    bar.close();
                } catch (Exception e) {
                } finally {
                    bar.close();
                }
            }
        }).start();
    }

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {
        this.canceled = true;
        dispose();
    }

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {
        dispose();
    }

    /**
	 * @param args the command line arguments
	 */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                ApplicationEditDialog dialog = new ApplicationEditDialog();
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {

                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                Application app = new Application();
                dialog.showDialog(null, app, false);
            }
        });
    }

    private javax.swing.JTextField appId;

    private javax.swing.JTextField appName;

    private javax.swing.JButton cancelButton;

    private javax.swing.JPanel centerPanel;

    private javax.swing.JTextArea description;

    private javax.swing.JTextField externalId;

    private javax.swing.JTextField firstDateFld;

    private javax.swing.JButton jButton1;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JTextField lastDateFld;

    private javax.swing.JButton okButton;

    private javax.swing.JPanel southPanel;
}
