package net.sourceforge.epoint.swing;

import net.sourceforge.epoint.pgp.*;
import net.sourceforge.epoint.io.*;
import net.sourceforge.epoint.util.*;
import java.io.*;
import java.util.*;
import java.net.*;

/**
 *
 * @author  nagydani
 */
public class CertifyUID extends javax.swing.JApplet {

    private KeyBlock key;

    private Thread auth = null;

    private KeyServer pks;

    private static class Auth implements Runnable {

        private CertifyUID parent;

        public Auth(CertifyUID sr) {
            parent = sr;
        }

        public void run() {
            parent.certify.setEnabled(false);
            try {
                PasswordAuthentication pa = Authenticator.requestPasswordAuthentication(parent.pks.getHost(), null, 11371, "http", "0x" + Base16.encode(parent.key.getID()), "RSA");
                if (pa != null) {
                    int i, sigt = SIGNATUREPacket.GENERICUID;
                    Object[] selection = parent.uids.getSelectedValues();
                    String uid;
                    KeyRing kr = parent.pks.getKeyRing(pa.getUserName());
                    SeededKeyPair kp = new SeededRSAKeyPair(kr, SeededKeyPair.getSeed(pa));
                    StringWriter sw = new StringWriter();
                    OutputStream os = new ArmoredOutputStream(new PrintWriter(sw), Armor.PUBKEY);
                    Date now = new Date();
                    java.security.PrivateKey sk = java.security.KeyFactory.getInstance("RSA").generatePrivate(kp.getPrivateSpec());
                    parent.key.getKey().write(os);
                    if (parent.persona.isSelected()) sigt = SIGNATUREPacket.PERSONAUID; else if (parent.casual.isSelected()) sigt = SIGNATUREPacket.CASUALUID; else if (parent.careful.isSelected()) sigt = SIGNATUREPacket.POSITIVEUID; else if (parent.reject.isSelected()) sigt = SIGNATUREPacket.CERTREVOCATION;
                    for (i = 0; i < selection.length; i++) {
                        uid = (String) selection[i];
                        PGPUserID.write(uid, os);
                        parent.key.writeSelfCert(uid, os);
                        byte[] uidB = uid.getBytes("UTF-8");
                        new PGPSignature(sk, kr.getKEYPacket().getKeyID(), "SHA1").update(parent.key.getKey().toByteArrayPUBKEY()).update(PGPUserID.cHead(uidB)).update(uidB).write(sigt, now, os);
                    }
                    os.close();
                    parent.pks.put(sw.toString(), System.out);
                    parent.getAppletContext().showDocument(new URL(parent.getParameter("next")));
                }
            } catch (Exception e) {
                e.printStackTrace(System.err);
            }
            parent.auth = null;
            parent.certify.setEnabled(true);
        }
    }

    /** Initializes the applet CertifyUID */
    public void init() {
        try {
            Authenticator.setDefault(AuthenticatorFrame.getAuthenticator());
            String bg = getParameter("bgColor");
            if (bg != null) getContentPane().setBackground(java.awt.Color.decode(bg));
            key = new KeyBlock(new ArmoredInputStream(new FastReader(new URL(getParameter("key")).openStream()), true));
            pks = new KeyServer(getParameter("pks"));
            initComponents();
            fingerprint.setText(key.getKey().getFingerprintText());
            uids.setListData(key.getValidUIDs());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void initComponents() {
        certLevels = new javax.swing.ButtonGroup();
        fingerprint = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        uids = new javax.swing.JList();
        jPanel1 = new javax.swing.JPanel();
        reject = new javax.swing.JRadioButton();
        generic = new javax.swing.JRadioButton();
        persona = new javax.swing.JRadioButton();
        casual = new javax.swing.JRadioButton();
        careful = new javax.swing.JRadioButton();
        certify = new javax.swing.JButton();
        fingerprint.setEditable(false);
        fingerprint.setFont(new java.awt.Font("Monospaced", 0, 12));
        getContentPane().add(fingerprint, java.awt.BorderLayout.NORTH);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane1.setViewportView(uids);
        getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);
        jPanel1.setLayout(new java.awt.GridLayout(0, 1));
        jPanel1.setBackground(getBackground());
        reject.setBackground(getBackground());
        certLevels.add(reject);
        reject.setForeground(java.awt.Color.red);
        reject.setText("Idenity rejected");
        jPanel1.add(reject);
        generic.setBackground(getBackground());
        certLevels.add(generic);
        generic.setSelected(true);
        generic.setText("Identity accepted for unspecified reason");
        jPanel1.add(generic);
        persona.setBackground(getBackground());
        certLevels.add(persona);
        persona.setForeground(java.awt.Color.orange);
        persona.setText("Identity accepted without verification");
        jPanel1.add(persona);
        casual.setBackground(getBackground());
        certLevels.add(casual);
        casual.setForeground(java.awt.Color.blue);
        casual.setText("Identity accepted after casual verification");
        jPanel1.add(casual);
        careful.setBackground(getBackground());
        certLevels.add(careful);
        careful.setForeground(java.awt.Color.green);
        careful.setText("Identity accepted after careful verification");
        jPanel1.add(careful);
        certify.setBackground(getBackground());
        certify.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sourceforge/epoint/swing/ok.png")));
        certify.setBorderPainted(false);
        certify.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                certifyActionPerformed(evt);
            }
        });
        jPanel1.add(certify);
        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);
    }

    private void certifyActionPerformed(java.awt.event.ActionEvent evt) {
        if (auth == null) {
            if (uids.isSelectionEmpty()) {
                uids.setSelectionInterval(0, uids.getModel().getSize() - 1);
                return;
            }
            auth = new Thread(new Auth(this));
            auth.start();
        }
    }

    private javax.swing.JRadioButton careful;

    private javax.swing.JRadioButton casual;

    private javax.swing.ButtonGroup certLevels;

    private javax.swing.JButton certify;

    private javax.swing.JTextField fingerprint;

    private javax.swing.JRadioButton generic;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JRadioButton persona;

    private javax.swing.JRadioButton reject;

    private javax.swing.JList uids;
}
