package de.shandschuh.jaolt.gui.core;

import java.awt.BorderLayout;
import javax.swing.JDialog;
import de.shandschuh.jaolt.core.AuctionPlatformAccount;
import de.shandschuh.jaolt.core.Language;
import de.shandschuh.jaolt.core.exception.NoAuctionPlatformFoundException;
import de.shandschuh.jaolt.gui.forms.AuctionPlatformAccountFormManager;

public class AuctionPlatformAccountJDialog extends SaveableJDialog {

    /** Default serial version uid */
    private static final long serialVersionUID = 1L;

    private AuctionPlatformAccount auctionPlatformAccount;

    private AuctionPlatformAccountFormManager auctionPlatformAccountFormManager;

    private boolean saved;

    public AuctionPlatformAccountJDialog(AuctionPlatformAccount auctionPlatformAccount, JDialog parent) {
        super(parent, Language.translateStatic("DIALOG_NEWAUCTIONPLATFORMACCOUNT_TITLE"), true);
        this.auctionPlatformAccount = auctionPlatformAccount;
        auctionPlatformAccountFormManager = new AuctionPlatformAccountFormManager(auctionPlatformAccount, this);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(auctionPlatformAccountFormManager.getJComponent(false));
        getContentPane().add(ButtonPanelFactory.create(this), BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    public AuctionPlatformAccountJDialog(JDialog parent) throws NoAuctionPlatformFoundException {
        this(new AuctionPlatformAccount(), parent);
    }

    public boolean isSaved() {
        return saved;
    }

    public AuctionPlatformAccount getAuctionPlatformAccount() {
        return auctionPlatformAccount;
    }

    public void save() throws Exception {
        auctionPlatformAccountFormManager.save();
        saved = true;
        dispose();
    }
}
