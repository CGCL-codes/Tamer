package com.sshtools.j2ssh.authentication;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.io.File;
import java.io.FileInputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.globus.gsi.GlobusCredential;
import com.sshtools.common.authentication.PassphraseDialog;

public class PKCS12Dialog {

    private Component parent;

    public PKCS12Dialog(Component parent) {
        this.parent = parent;
    }

    public GlobusCredential showPrompt() throws AuthenticationProtocolException {
        Security.addProvider(new BouncyCastleProvider());
        File keyfile = null;
        String passphrase = null;
        if (keyfile == null || !keyfile.exists()) {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileHidingEnabled(false);
            chooser.setDialogTitle("Select Certificate File For Authentication");
            if (chooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
                keyfile = chooser.getSelectedFile();
            } else {
                return null;
            }
        }
        Window w = (Window) SwingUtilities.getAncestorOfClass(Window.class, parent);
        PassphraseDialog dialog = null;
        if (w instanceof Frame) {
            dialog = new PassphraseDialog((Frame) w);
        } else if (w instanceof Dialog) {
            dialog = new PassphraseDialog((Dialog) w);
        } else {
            dialog = new PassphraseDialog();
        }
        KeyStore store = null;
        do {
            dialog.setVisible(true);
            if (dialog.isCancelled()) {
                return null;
            }
            passphrase = new String(dialog.getPassphrase());
            try {
                store = KeyStore.getInstance("PKCS12", "BC");
                FileInputStream in = new FileInputStream(keyfile);
                store.load(in, passphrase.toCharArray());
                break;
            } catch (Exception ihke) {
                dialog.setMessage("Had a problem: " + ihke);
                dialog.setMessageForeground(Color.red);
            }
        } while (true);
        try {
            Enumeration e = store.aliases();
            if (!e.hasMoreElements()) return null;
            String alias = (String) e.nextElement();
            java.security.cert.Certificate cert = store.getCertificate(alias);
            Key key = store.getKey(alias, passphrase.toCharArray());
            if (!(cert instanceof X509Certificate)) return null;
            if (!(key instanceof PrivateKey)) return null;
            return new GlobusCredential((PrivateKey) key, new X509Certificate[] { (X509Certificate) cert });
        } catch (Exception ihke) {
            throw new AuthenticationProtocolException("Had a problem: " + ihke);
        }
    }
}
