package es.gva.cit.catalog.ui.showtree;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JDialog;
import es.gva.cit.catalog.metadataxml.XMLNode;
import es.gva.cit.catalog.utils.Frames;

/**
 * 
 * 
 * 
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */
public class ShowTreeDialog extends JDialog implements WindowListener {

    /**
 * 
 * 
 */
    int currentNode;

    /**
 * 
 * 
 * 
 * @param node 
 */
    public ShowTreeDialog(XMLNode node) {
        super();
        initialize(node);
    }

    /**
 * This method initializes jDialog
 * 
 * 
 * @param node 
 */
    private void initialize(es.gva.cit.catalog.metadataxml.XMLNode node) {
        Frames.centerFrame(this, 800, 675);
        this.setTitle("Cliente de Cat�logo");
        ShowTreeDialogPanel panel = new ShowTreeDialogPanel(node);
        panel.setParent(this);
        getContentPane().add(panel);
        this.setVisible(true);
    }

    /**
 * 
 * 
 * 
 * @param e 
 */
    public void windowActivated(WindowEvent e) {
    }

    /**
 * 
 * 
 * 
 * @param e 
 */
    public void windowClosed(WindowEvent e) {
    }

    /**
 * 
 * 
 * 
 * @param e 
 */
    public void windowClosing(WindowEvent e) {
    }

    /**
 * 
 * 
 * 
 * @param e 
 */
    public void windowDeactivated(WindowEvent e) {
    }

    /**
 * 
 * 
 * 
 * @param e 
 */
    public void windowDeiconified(WindowEvent e) {
    }

    /**
 * 
 * 
 * 
 * @param e 
 */
    public void windowIconified(WindowEvent e) {
    }

    /**
 * 
 * 
 * 
 * @param e 
 */
    public void windowOpened(WindowEvent e) {
    }
}
