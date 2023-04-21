package jp.ac.keio.ae.comp.yamaguti.doddle.ui;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
import javax.swing.event.*;
import jp.ac.keio.ae.comp.yamaguti.doddle.utils.*;

/**
 * @author takeshi morita
 */
public class VersionInfoDialog extends JDialog implements HyperlinkListener {

    public VersionInfoDialog(Frame root, String title, ImageIcon icon) {
        super(root, title);
        setIconImage(icon.getImage());
        JEditorPane htmlPane = new JEditorPane();
        htmlPane.addHyperlinkListener(this);
        htmlPane.setEditable(false);
        htmlPane.setContentType("text/html; charset=utf-8");
        URL versionInfoURL = Utils.class.getClassLoader().getResource(Translator.RESOURCE_DIR + "version_info.html");
        try {
            htmlPane.setPage(versionInfoURL);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        setLayout(new BorderLayout());
        add(new JScrollPane(htmlPane), BorderLayout.CENTER);
        add(Utils.createEastPanel(okButton), BorderLayout.SOUTH);
        setSize(500, 400);
        setLocationRelativeTo(root);
        setVisible(true);
    }

    public void hyperlinkUpdate(HyperlinkEvent ae) {
        try {
            if (ae.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                Desktop.getDesktop().browse(ae.getURL().toURI());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
