package org.qfirst.batavia.ui;

import javax.swing.*;
import org.qfirst.vfs.*;
import java.awt.*;
import java.io.*;
import org.apache.log4j.*;
import javax.swing.text.BadLocationException;

/**
 *  Description of the Class
 *
 * @author	francisdobi
 * @created    May 24, 2004
 */
public class LicenseDialog extends JDialog {

    private Logger logger = Logger.getLogger(getClass());

    private AbstractFile file = null;

    private JEditorPane textPane;

    public LicenseDialog(JFrame owner) {
        super(owner);
        initComponents();
        load();
        pack();
        setSize(500, 400);
        setTitle("Batavia license");
    }

    private void load() {
        InputStreamReader reader = null;
        char buffer[] = new char[32 * 1024];
        StringBuffer sb = new StringBuffer();
        try {
            reader = new InputStreamReader(getClass().getResourceAsStream("/License.txt"));
            try {
                int c;
                while ((c = reader.read(buffer)) != -1) {
                    sb.append(buffer, 0, c);
                }
            } catch (IOException ioe) {
                logger.error(ioe, ioe);
                return;
            }
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException ex) {
            }
        }
        try {
            textPane.getDocument().insertString(0, sb.toString(), null);
            textPane.setCaretPosition(0);
        } catch (BadLocationException ble) {
        }
    }

    /**
	 *  Description of the Method
	 */
    private void initComponents() {
        Container container = getContentPane();
        container.setLayout(new GridLayout(1, 1));
        textPane = new JEditorPane();
        textPane.setEditable(false);
        container.add(new JScrollPane(textPane));
    }
}
