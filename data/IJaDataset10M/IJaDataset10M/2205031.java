package com.ibm.aglets.tahiti;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import net.sourceforge.aglets.util.gui.GUICommandStrings;
import net.sourceforge.aglets.util.gui.JComponentBuilder;
import com.ibm.atp.auth.SharedSecret;
import com.ibm.atp.auth.SharedSecrets;
import com.ibm.awb.misc.FileUtils;

public class ExportSharedSecretDialog extends TahitiDialog {

    /**
     * 
     */
    private static final long serialVersionUID = 3129746473663734302L;

    /**
     * The text field where the user can specify the file to export to.
     */
    private JTextField fileTextField = null;

    /**
     * The list of available domains.
     */
    private AgletListPanel<String> domainList = null;

    /**
     * A cached file chooser (thus it stores always the latest directory where
     * the user worked).
     */
    private static JFileChooser fileChooser = null;

    public ExportSharedSecretDialog(JFrame parentFrame) {
        super(parentFrame);
        JLabel label = JComponentBuilder.createJLabel(this.baseKey + ".infoLabel");
        this.contentPanel.add(label, BorderLayout.NORTH);
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new FlowLayout());
        label = JComponentBuilder.createJLabel(this.baseKey + ".domainLabel");
        centerPanel.add(label);
        this.domainList = new AgletListPanel<String>();
        this.domainList.setTitleBorder(this.translator.translate(this.baseKey + ".domainLabel"));
        centerPanel.add(this.domainList);
        this.fillDomainList();
        this.contentPanel.add(centerPanel, BorderLayout.CENTER);
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new FlowLayout());
        label = JComponentBuilder.createJLabel(this.baseKey + ".fileLabel");
        this.fileTextField = JComponentBuilder.createJTextField(20, null, this.baseKey + ".fileName");
        JButton browseButton = JComponentBuilder.createJButton(this.baseKey + ".browseButton", GUICommandStrings.BROWSE_FILESYSTEM_COMMAND, this);
        southPanel.add(label);
        southPanel.add(this.fileTextField);
        southPanel.add(browseButton);
        this.contentPanel.add(southPanel, BorderLayout.SOUTH);
        this.pack();
    }

    /**
     * Iterates on the domain list and adds each domain name (as a string) to
     * the list.
     * 
     */
    private void fillDomainList() {
        SharedSecrets allSecrets = SharedSecrets.getSharedSecrets();
        if (allSecrets == null) return;
        for (Enumeration enumer = allSecrets.getDomainNames(); (enumer != null) && enumer.hasMoreElements(); ) {
            String currentDomain = (String) enumer.nextElement();
            this.domainList.addItem(currentDomain);
        }
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event == null) return;
        String command = event.getActionCommand();
        if (GUICommandStrings.BROWSE_FILESYSTEM_COMMAND.equals(command)) {
            try {
                if (fileChooser == null) {
                    fileChooser = new JFileChooser();
                    fileChooser.setMultiSelectionEnabled(false);
                    fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                    String workingDir = FileUtils.getWorkDirectory();
                    fileChooser.setCurrentDirectory(new File(workingDir));
                }
                fileChooser.showOpenDialog(this);
                this.fileTextField.setText((fileChooser.getSelectedFile()).getCanonicalPath());
            } catch (IOException e) {
                this.logger.error("Exception caught while trying to get a certificate from the filesystem (browsing)", e);
                JOptionPane.showMessageDialog(this, this.translator.translate(this.baseKey + ".fileError"), this.translator.translate(this.baseKey + ".fileError.title"), JOptionPane.ERROR_MESSAGE);
            }
        } else if (GUICommandStrings.OK_COMMAND.equals(command)) {
            try {
                String file = this.fileTextField.getText();
                String domain = this.domainList.getSelectedItem();
                SharedSecret secret = SharedSecrets.getSharedSecrets().getSharedSecret(domain);
                if (secret == null) JOptionPane.showMessageDialog(this, this.translator.translate(this.baseKey + ".sharedSecretNotExists"), this.translator.translate(this.baseKey + ".sharedSecretNotExists.title"), JOptionPane.ERROR_MESSAGE); else secret.save(file);
            } catch (FileNotFoundException e) {
                this.logger.error("Shared secret file not found, cannot import", e);
                JOptionPane.showMessageDialog(this, this.translator.translate(this.baseKey + ".fileError2"), this.translator.translate(this.baseKey + ".fileError2.title"), JOptionPane.ERROR_MESSAGE);
            } catch (IOException e) {
                this.logger.error("Exception caught while trying to access the shared secret file", e);
                JOptionPane.showMessageDialog(this, this.translator.translate(this.baseKey + ".fileError2"), this.translator.translate(this.baseKey + ".fileError2.title"), JOptionPane.ERROR_MESSAGE);
            }
        }
        super.actionPerformed(event);
    }
}
