package de.shandschuh.jaolt.gui.listener.mainmenu.editmenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import de.shandschuh.jaolt.core.Auction;
import de.shandschuh.jaolt.gui.Lister;
import de.shandschuh.jaolt.gui.dialogs.FindAuctionJDialog;
import de.shandschuh.jaolt.gui.maintabbedpane.ListJPanel;

public class FindAuctionActionListener implements ActionListener {

    @SuppressWarnings("unchecked")
    public void actionPerformed(ActionEvent e) {
        try {
            new FindAuctionJDialog((ListJPanel<Auction>) Lister.getCurrentInstance().getMainJPanel().getCurrentListJPanel());
        } catch (Exception exception) {
        }
    }
}
