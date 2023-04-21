package org.verus.ngl.client.technicalprocessing;

import javax.swing.JDialog;
import org.verus.ngl.utilities.marc.NGLMARCRecord;

/**
 *
 * @author  root
 */
public class MarcRecordImportScreenDialog extends javax.swing.JDialog {

    /** Creates new form MarcRecordImportScreenDialog */
    private static ImportBibliographicdataPanel panel = null;

    public static final int RECORD_TYPE_BIBLIO = 1;

    public static final int RECORD_TYPE_AUTHORITY = 2;

    public MarcRecordImportScreenDialog(java.awt.Frame parent, int recordType) {
        super(parent);
        initComponents();
        panel = new ImportBibliographicdataPanel(outSuggPanel, recordType);
        this.jPanel1.add(panel, java.awt.BorderLayout.CENTER);
        this.setSize(800, 600);
    }

    public MarcRecordImportScreenDialog(JDialog parent, int recordType) {
        super(parent);
        initComponents();
        panel = new ImportBibliographicdataPanel(outSuggPanel, recordType);
        this.jPanel1.add(panel, java.awt.BorderLayout.CENTER);
        this.setSize(800, 600);
    }

    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        bnOk = new javax.swing.JButton();
        bnHelp = new javax.swing.JButton();
        bnCancel = new javax.swing.JButton();
        outSuggPanel = new org.verus.ngl.client.guicomponents.OutputSuggestionsPanel();
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.Y_AXIS));
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Marc Record Import Screen Dialog");
        setModal(true);
        jPanel1.setLayout(new java.awt.BorderLayout());
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        getContentPane().add(jPanel1);
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        bnOk.setMnemonic('o');
        bnOk.setText(org.verus.ngl.client.main.NGLResourceBundle.getInstance().getString("Ok"));
        bnOk.setToolTipText(org.verus.ngl.client.main.NGLResourceBundle.getInstance().getString("Ok"));
        bnOk.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bnOkActionPerformed(evt);
            }
        });
        jPanel2.add(bnOk);
        bnHelp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/verus/ngl/client/images/help.gif")));
        bnHelp.setMnemonic('h');
        bnHelp.setToolTipText(org.verus.ngl.client.main.NGLResourceBundle.getInstance().getString("Help"));
        jPanel2.add(bnHelp);
        bnCancel.setMnemonic('c');
        bnCancel.setText(org.verus.ngl.client.main.NGLResourceBundle.getInstance().getString("Cancel"));
        bnCancel.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bnCancelActionPerformed(evt);
            }
        });
        jPanel2.add(bnCancel);
        getContentPane().add(jPanel2);
        outSuggPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        getContentPane().add(outSuggPanel);
        pack();
    }

    private void bnCancelActionPerformed(java.awt.event.ActionEvent evt) {
        this.setVisible(false);
    }

    private void bnOkActionPerformed(java.awt.event.ActionEvent evt) {
        this.mode = this.ONOKCLICK;
        this.cmdRecord = panel.getSelectedDataSource();
        if (this.cmdRecord != null) this.setVisible(false);
    }

    public int getMode() {
        return mode;
    }

    public NGLMARCRecord getCmdRecord() {
        return cmdRecord;
    }

    private javax.swing.JButton bnCancel;

    private javax.swing.JButton bnHelp;

    private javax.swing.JButton bnOk;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private org.verus.ngl.client.guicomponents.OutputSuggestionsPanel outSuggPanel;

    public final int ONOKCLICK = 1;

    public int mode = -1;

    public NGLMARCRecord cmdRecord = null;
}
