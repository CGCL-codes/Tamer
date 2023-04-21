package Controllers.Main;

import Controllers.Transfer.TransferController;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author nrovinskiy
 */
public class TransferListener implements ActionListener {

    MainWindowController mwcHost;

    public TransferListener(MainWindowController host) {
        mwcHost = host;
    }

    public void actionPerformed(ActionEvent e) {
        TransferController tcTransfer = new TransferController(mwcHost);
        tcTransfer.setVisible(true);
        tcTransfer = null;
    }
}
