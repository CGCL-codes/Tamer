package uk.co.caprica.vlcj.radio.view;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.version.LibVlcVersion;
import uk.co.caprica.vlcj.version.Version;
import uk.co.caprica.vlcj.version.VlcjVersion;

/**
 * A simple "about" dialog.
 */
public class AboutDialog extends BaseDialog {

    private static final long serialVersionUID = 1L;

    private JLabel playerVersionLabel;

    private JLabel copyrightLabel;

    private LinkLabel vendorHomePageLabel;

    private JLabel termsLabel;

    private LinkLabel licensePageLabel;

    private JLabel vlcjVersionLabel;

    private JLabel vlcjVersionValueLabel;

    private JLabel libVlcVersionLabel;

    private JLabel libVlcVersionValueLabel;

    private JLabel libVlcChangeSetLabel;

    private JLabel libVlcChangeSetValueLabel;

    private JLabel libVlcCompilerLabel;

    private JLabel libVlcCompilerValueLabel;

    private JButton okButton;

    public AboutDialog(Frame parentFrame) {
        super(parentFrame, "About vlcj radio");
        Version vlcjVersion = VlcjVersion.getVersion();
        Version libVlcVersion = LibVlcVersion.getVersion();
        playerVersionLabel = new JLabel("vlcj radio 1.2.1");
        copyrightLabel = new JLabel("(C)2010, 2011");
        vendorHomePageLabel = new LinkLabel("Caprica Software Limited", "http://www.capricasoftware.co.uk");
        termsLabel = new JLabel("This software is disributed under the terms of the");
        licensePageLabel = new LinkLabel("GPL 3.0 License", "http://www.gnu.org/licenses/gpl-3.0.html");
        vlcjVersionLabel = new JLabel("vlcj version: ");
        vlcjVersionValueLabel = new JLabel(vlcjVersion.version());
        libVlcVersionLabel = new JLabel("libvlc version:");
        libVlcVersionValueLabel = new JLabel(libVlcVersion.version());
        String changeSet = LibVlc.INSTANCE.libvlc_get_changeset();
        libVlcChangeSetLabel = new JLabel("libvlc change-set:");
        libVlcChangeSetValueLabel = new JLabel(changeSet);
        String compiler = LibVlc.INSTANCE.libvlc_get_compiler();
        libVlcCompilerLabel = new JLabel("libvlc compiler:");
        libVlcCompilerValueLabel = new JLabel(compiler);
        okButton = new JButton("OK");
        createUI();
    }

    private void createUI() {
        JPanel cp = new JPanel();
        okButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                dispose();
            }
        });
        cp.setBorder(new EmptyBorder(16, 16, 16, 16));
        cp.setLayout(new MigLayout("wrap 2", "[][100::, grow]", "[][][]20[][][][][]20[]"));
        cp.add(playerVersionLabel, "c, span 2");
        JPanel vendorPanel = new JPanel();
        vendorPanel.add(copyrightLabel);
        vendorPanel.add(vendorHomePageLabel);
        cp.add(vendorPanel, "c, span 2");
        JPanel termsPanel = new JPanel();
        termsPanel.add(termsLabel);
        termsPanel.add(licensePageLabel);
        cp.add(termsPanel, "c, span 2");
        cp.add(new HeaderLabel("Components:"), "span 2, grow");
        cp.add(vlcjVersionLabel);
        cp.add(vlcjVersionValueLabel, "grow");
        cp.add(libVlcVersionLabel);
        cp.add(libVlcVersionValueLabel, "grow");
        cp.add(libVlcChangeSetLabel);
        cp.add(libVlcChangeSetValueLabel, "grow");
        cp.add(libVlcCompilerLabel);
        cp.add(libVlcCompilerValueLabel, "grow");
        cp.add(okButton, "r, span 2, tag ok");
        setLayout(new BorderLayout());
        add(cp, BorderLayout.CENTER);
        LinkListener linkListener = new LinkListener();
        vendorHomePageLabel.addActionListener(linkListener);
        licensePageLabel.addActionListener(linkListener);
        pack();
        okButton.requestFocusInWindow();
    }

    private class LinkListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent evt) {
            if (Desktop.isDesktopSupported()) {
                try {
                    URI uri = new URI(evt.getActionCommand());
                    Desktop.getDesktop().browse(uri);
                } catch (Exception e) {
                }
            }
        }
    }
}
