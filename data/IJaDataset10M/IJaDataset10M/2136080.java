package de.shandschuh.jaolt.gui.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import de.shandschuh.jaolt.core.Language;
import de.shandschuh.jaolt.gui.ViewAuctionJDialog;

public class RefreshAuctionFromViewAction extends AbstractAction {

    private static final long serialVersionUID = 1L;

    private ViewAuctionJDialog viewAuctionJDialog;

    public RefreshAuctionFromViewAction(ViewAuctionJDialog viewAuctionJDialog) {
        super(Language.translateStatic("BUTTON_REFRESH_TEXT"));
        this.viewAuctionJDialog = viewAuctionJDialog;
    }

    public void actionPerformed(ActionEvent e) {
        viewAuctionJDialog.syncAuction();
    }
}
