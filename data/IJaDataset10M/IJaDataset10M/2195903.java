package org.xngr.browser.document;

import java.awt.event.ActionEvent;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.xngr.browser.ExchangerDocument;

/**
 * An action that can be used to show the properties dialog 
 * for a document (remote and normal).
 *
 * @version	$Revision: 1.4 $, $Date: 2007/06/28 13:14:18 $
 * @author Edwin Dankert <edankert@cladonia.com>
 */
public class DocumentPropertiesAction extends AbstractAction {

    private static final long serialVersionUID = -539782595965488170L;

    private ExchangerDocument document = null;

    private DocumentPropertiesDialog dialog = null;

    private RemoteDocumentEditorDialog remoteDialog = null;

    private JFrame frame = null;

    /**
 	 * The constructor for the action which shows
 	 * the properties dialog for the document (remote and normal).
 	 */
    public DocumentPropertiesAction(JFrame frame) {
        super("Properties");
        putValue(MNEMONIC_KEY, new Integer('P'));
        putValue(SMALL_ICON, new ImageIcon(getClass().getResource("/org/xngr/browser/icons/DocumentPropertiesIcon.gif")));
        putValue(SHORT_DESCRIPTION, "Document Properties");
        dialog = new DocumentPropertiesDialog(frame);
        remoteDialog = new RemoteDocumentEditorDialog(frame);
        this.frame = frame;
        setEnabled(false);
    }

    /**
 	 * Set the document for the action.
 	 *
 	 * @param the document.
 	 */
    public void setDocument(ExchangerDocument document) {
        this.document = document;
        setEnabled(document != null);
    }

    /**
 	 * The implementation of the document action, called 
 	 * after a user action.
 	 *
 	 * @param the action event.
 	 */
    public void actionPerformed(ActionEvent e) {
        if (document.isRemote()) {
            remoteDialog.setProperties(document.getName(), document.getURL().toExternalForm(), document.getProperties().validate());
            remoteDialog.setLocationRelativeTo(frame);
            remoteDialog.setVisible(true);
            if (!remoteDialog.isCancelled()) {
                try {
                    URL url = new URL(remoteDialog.getURL());
                    if (url.getProtocol().equalsIgnoreCase("http")) {
                        document.getProperties().setValidate(remoteDialog.isValidate());
                        document.setProperties(url, remoteDialog.getName());
                    } else {
                        JOptionPane.showMessageDialog(frame, "The " + url.getProtocol() + " protocol is not supported for a Remote Document!\n", "Document Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (MalformedURLException mue) {
                    JOptionPane.showMessageDialog(frame, "Invalid URL: " + remoteDialog.getURL() + "\n" + mue.getMessage(), "Document Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            dialog.setProperties(document);
            dialog.setLocationRelativeTo(frame);
            dialog.setVisible(true);
        }
    }
}
