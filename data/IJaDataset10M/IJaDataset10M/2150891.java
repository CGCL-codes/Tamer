package de.shandschuh.jaolt.gui.listener.maintabbedpane.popupmenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import de.shandschuh.jaolt.core.Auction;
import de.shandschuh.jaolt.gui.Lister;
import de.shandschuh.jaolt.gui.maintabbedpane.ListJPanel;

public class CutAuctionJMenuItemListener implements ActionListener {

    protected ListJPanel<Auction> editAuctionsListJPanel;

    public CutAuctionJMenuItemListener(ListJPanel<Auction> editAuctionsListJPanel) {
        this.editAuctionsListJPanel = editAuctionsListJPanel;
    }

    public void actionPerformed(ActionEvent actionEvent) {
        Vector<Auction> auctions = editAuctionsListJPanel.getSelectedObjects();
        if (auctions != null && auctions.size() > 0) {
            Vector<String> tags = editAuctionsListJPanel.getCurrentFolder().getTags();
            if (tags != null && tags.size() > 0) {
                for (int n = 0, i = auctions.size(); n < i; n++) {
                    auctions.get(n).removeTags(tags);
                }
            }
            Lister.getCurrentInstance().setClipboard(auctions);
            editAuctionsListJPanel.deleteSelectedObjects();
        }
    }
}
