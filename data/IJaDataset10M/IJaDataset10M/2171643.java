package es.gva.cit.catalog.ui.showresults;

import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JDialog;
import es.gva.cit.catalog.CatalogClient;
import es.gva.cit.catalog.drivers.GetRecordsReply;
import es.gva.cit.catalog.utils.Frames;

/**
 * DOCUMENT ME!
 * 
 * 
 * @author luisw
 */
public class ShowResultsDialog extends JDialog implements WindowListener {

    int currentRecord;

    public es.gva.cit.catalog.ui.showresults.ShowResultsDialogPanel showResultsDialogPanel;

    /**
 * Crea un nuevo ShowResultsDialog.
 * @param client DOCUMENT ME!
 * @param nodes DOCUMENT ME!
 * @param currentRecord DOCUMENT ME!
 */
    public ShowResultsDialog(CatalogClient client, GetRecordsReply recordsReply, int currentRecord) {
        super();
        this.currentRecord = currentRecord;
        initialize(client, recordsReply);
    }

    /**
 * This method initializes jDialog
 * 
 * 
 * @param client 
 * @param nodes 
 */
    private void initialize(CatalogClient client, GetRecordsReply recordsReply) {
        Frames.centerFrame(this, 625, 425);
        this.setSize(new Dimension(625, 425));
        this.setTitle("Cliente de Cat�logo");
        ShowResultsDialogPanel panel = new ShowResultsDialogPanel(client, recordsReply, currentRecord);
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
