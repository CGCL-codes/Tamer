package org.jpedal.objects.acroforms.gui.certificates;

import javax.swing.JDialog;
import java.math.BigInteger;

public class CertificateHolder extends javax.swing.JPanel {

    private Details detailsTab;

    private General generalTab;

    private JDialog frame;

    public void setValues(String name, int version, String hashAlgorithm, String subjectFields, String issuerFields, BigInteger serialNumber, String notBefore, String notAfter, String publicKeyDescription, String publicKey, String x509Data, String sha1Digest, String md5Digest) {
        generalTab = new General();
        detailsTab = new Details();
        generalTab.setValues(name, notBefore, notAfter);
        detailsTab.setValues(version, hashAlgorithm, subjectFields, issuerFields, serialNumber, notBefore, notAfter, publicKeyDescription, publicKey, x509Data, sha1Digest, md5Digest);
        jTabbedPane1.addTab("General", generalTab);
        jTabbedPane1.addTab("Details", detailsTab);
    }

    /**
     * Creates new form CertificateHolder
     *
     * @param dialog
     */
    public CertificateHolder(JDialog dialog) {
        initComponents();
        this.frame = dialog;
    }

    private void initComponents() {
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jButton1 = new javax.swing.JButton();
        setLayout(null);
        add(jTabbedPane1);
        jTabbedPane1.setBounds(10, 10, 420, 360);
        jButton1.setText("OK");
        jButton1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                close(evt);
            }
        });
        add(jButton1);
        jButton1.setBounds(350, 390, 73, 23);
    }

    private void close(java.awt.event.ActionEvent evt) {
        frame.setVisible(false);
    }

    private javax.swing.JButton jButton1;

    private javax.swing.JTabbedPane jTabbedPane1;
}
