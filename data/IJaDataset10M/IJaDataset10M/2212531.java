package de.shandschuh.jaolt.gui.editauction;

import java.util.Vector;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import com.jgoodies.forms.builder.PanelBuilder;
import de.shandschuh.jaolt.core.Auction;
import de.shandschuh.jaolt.core.AuctionPlatform;
import de.shandschuh.jaolt.core.AuctionPlatformAccount;
import de.shandschuh.jaolt.core.AuctionPlatformSite;
import de.shandschuh.jaolt.core.Language;
import de.shandschuh.jaolt.gui.FormManager;
import de.shandschuh.jaolt.gui.Lister;
import de.shandschuh.jaolt.gui.listener.editauction.AuctionPlatformChangeListener;
import de.shandschuh.jaolt.gui.listener.editauction.border.AuctionPlatformAccountComboBoxChangeListener;
import de.shandschuh.jaolt.gui.listener.editauction.border.AuctionPlatformSiteComboBoxChangeListener;
import de.shandschuh.jaolt.gui.listener.editauction.border.AuctionTypeComboBoxChangeListener;
import de.shandschuh.jaolt.tools.gui.ComboBoxHelper;

public class AuctionBorderFormManager extends FormManager {

    private Auction auction;

    private JComboBox auctionPlatformAccountsJComboBox;

    private JComboBox auctionTypesJComboBox;

    private JComboBox auctionPlatformSitesJComboBox;

    private AuctionPlatformAccountComboBoxChangeListener auctionPlatformAccountComboBoxChangeListener;

    private AuctionPlatformSiteComboBoxChangeListener auctionPlatformSiteComboBoxChangeListener;

    private AuctionTypeComboBoxChangeListener auctionTypeComboBoxChangeListener;

    private AuctionFundamentalsFormManager auctionFundamentalsFormManager;

    private ArticleAttributesFormManager articleAttributesFormManager;

    private ShippingServicesFormManager shippingServicesFormManager;

    private PaymentMethodsFormManager paymentMethodsFormManager;

    private AuctionDetailsFormManager auctionDetailsFormManager;

    private EnhancementsFormManager enhancementsFormManager;

    private JLabel platformJLabel;

    private Vector<AuctionPlatformChangeListener> auctionPlatformChangeListener;

    public AuctionBorderFormManager(Auction auction, AuctionFundamentalsFormManager auctionFundamentalsFormManager, ArticleAttributesFormManager articleAttributesFormManager, ShippingServicesFormManager shippingServicesFormManager, PaymentMethodsFormManager paymentMethodsFormManager, AuctionDetailsFormManager auctionDetailsFormManager, EnhancementsFormManager enhancementsFormManager) {
        this.auction = auction;
        this.auctionFundamentalsFormManager = auctionFundamentalsFormManager;
        this.articleAttributesFormManager = articleAttributesFormManager;
        this.shippingServicesFormManager = shippingServicesFormManager;
        this.paymentMethodsFormManager = paymentMethodsFormManager;
        this.auctionDetailsFormManager = auctionDetailsFormManager;
        this.enhancementsFormManager = enhancementsFormManager;
        auctionPlatformAccountComboBoxChangeListener = new AuctionPlatformAccountComboBoxChangeListener(auction, this, enhancementsFormManager);
        auctionPlatformAccountsJComboBox = new JComboBox(Lister.getCurrentInstance().getMember().getAuctionPlatformAccounts());
        auctionPlatformSiteComboBoxChangeListener = new AuctionPlatformSiteComboBoxChangeListener(auction, articleAttributesFormManager, shippingServicesFormManager, paymentMethodsFormManager, auctionDetailsFormManager);
        auctionPlatformSitesJComboBox = new JComboBox();
        auctionTypesJComboBox = new JComboBox();
        auctionTypeComboBoxChangeListener = new AuctionTypeComboBoxChangeListener(auction, auctionDetailsFormManager, auctionFundamentalsFormManager);
        platformJLabel = new JLabel();
        auctionPlatformChangeListener = new Vector<AuctionPlatformChangeListener>();
        reload();
        auctionTypesJComboBox.addItemListener(auctionTypeComboBoxChangeListener);
        auctionPlatformSitesJComboBox.addItemListener(auctionPlatformSiteComboBoxChangeListener);
        auctionPlatformAccountsJComboBox.addItemListener(auctionPlatformAccountComboBoxChangeListener);
    }

    protected void addPanelBuilderComponents(PanelBuilder panelBuilder) {
        panelBuilder.add(new JLabel(Language.translateStatic("AUCTIONPLATFORMACCOUNT")), getCellConstraints(1, 1));
        panelBuilder.add(auctionPlatformAccountsJComboBox, getCellConstraints(3, 1));
        panelBuilder.add(platformJLabel, getCellConstraints(1, 3));
        panelBuilder.add(auctionPlatformSitesJComboBox, getCellConstraints(3, 3));
        panelBuilder.add(new JLabel(Language.translateStatic("AUCTION_TYPE")), getCellConstraints(1, 5));
        panelBuilder.add(auctionTypesJComboBox, getCellConstraints(3, 5));
    }

    protected String getColumnLayout() {
        return "p, 4dlu, fill:p:grow";
    }

    public String getName() {
        return Language.translateStatic("AUCTION_BORDER");
    }

    protected String getRowLayout() {
        return "p, 3dlu, p, 3dlu, p";
    }

    protected void reloadLocal(boolean rebuild) {
        auctionPlatformAccountComboBoxChangeListener.setEnabled(false);
        auctionPlatformSiteComboBoxChangeListener.setEnabled(false);
        auctionTypeComboBoxChangeListener.setEnabled(false);
        reloadAuctionPlatform();
        ComboBoxHelper.updateDataModel(auctionPlatformAccountsJComboBox, Lister.getCurrentInstance().getMember().getAuctionPlatformAccounts());
        auctionPlatformAccountComboBoxChangeListener.setEnabled(true);
        auctionPlatformSiteComboBoxChangeListener.setEnabled(true);
        auctionTypeComboBoxChangeListener.setEnabled(true);
    }

    protected void saveLocal() {
        if (auctionPlatformAccountsJComboBox.getSelectedItem() != null) {
            auction.setAuctionPlatformAccount((AuctionPlatformAccount) auctionPlatformAccountsJComboBox.getSelectedItem());
        }
        if (auctionPlatformSitesJComboBox.getSelectedItem() != null) {
            AuctionPlatformSite selectedAuctionPlatformSite = (AuctionPlatformSite) auctionPlatformSitesJComboBox.getSelectedItem();
            AuctionPlatform auctionPlatform = null;
            if (auction.getAuctionPlatformAccount() != null) {
                auctionPlatform = auction.getAuctionPlatformAccount().getAuctionPlatform();
                if (!auction.getAuctionPlatformAccount().hasAuctionPlatformSite(selectedAuctionPlatformSite)) {
                    auction.setAuctionPlatformSite(auction.getAuctionPlatformAccount().getAuctionPlatformSites()[0]);
                } else {
                    auction.setAuctionPlatformSite(selectedAuctionPlatformSite);
                }
                auction.setCurrency(auction.getAuctionPlatformAccount().getAuctionPlatform().getAvailableCurrencies(auction.getAuctionPlatformSite())[0]);
            }
            if (defaultCheckBox != null && defaultCheckBox.isSelected()) {
                Lister.getCurrentInstance().getMember().getDefaultValues(auctionPlatform, true).setAuctionPlatformSite(auction.getAuctionPlatformSite());
                Lister.getCurrentInstance().getMember().getDefaultValues(auctionPlatform, true).setAuctionType(auction.getType());
            }
        }
    }

    protected void validateLocal() throws Exception {
    }

    public void setAuction(Auction auction) {
        this.auction = auction;
        auctionPlatformAccountComboBoxChangeListener.setAuction(auction);
        auctionPlatformSiteComboBoxChangeListener.setAuction(auction);
        auctionTypeComboBoxChangeListener.setAuction(auction);
    }

    @Override
    public boolean rebuildNeeded() {
        return false;
    }

    public void reloadAuctionPlatform() {
        if (auction.getAuctionPlatformAccount() != null) {
            boolean enabled = auctionPlatformSiteComboBoxChangeListener.isEnabled();
            auctionPlatformSiteComboBoxChangeListener.setEnabled(false);
            ComboBoxHelper.updateDataModel(auctionPlatformSitesJComboBox, auction.getAuctionPlatformAccount().getAuctionPlatformSites());
            auctionPlatformSitesJComboBox.setEnabled(auction.getAuctionPlatformSite() != null);
            auctionPlatformSiteComboBoxChangeListener.setEnabled(enabled);
            AuctionPlatformSite auctionPlatformSite = auction.getAuctionPlatformAccount().getAuctionPlatformSite(auction.getAuctionPlatformSite());
            auctionPlatformSitesJComboBox.setSelectedItem(auctionPlatformSite);
            enabled = auctionTypeComboBoxChangeListener.isEnabled();
            auctionTypeComboBoxChangeListener.setEnabled(false);
            ComboBoxHelper.updateDataModel(auctionTypesJComboBox, auction.getAuctionPlatformAccount().getAuctionPlatform().getAuctionTypes());
            auctionTypesJComboBox.setEnabled(auction.getType() != null);
            auctionTypeComboBoxChangeListener.setEnabled(enabled);
            auctionTypesJComboBox.setSelectedItem(auction.getType());
            enabled = auctionPlatformAccountComboBoxChangeListener.isEnabled();
            auctionPlatformAccountComboBoxChangeListener.setEnabled(false);
            auctionPlatformAccountsJComboBox.setSelectedItem(auction.getAuctionPlatformAccount());
            auctionPlatformAccountsJComboBox.setEnabled(true);
            auctionPlatformAccountComboBoxChangeListener.setEnabled(enabled);
            platformJLabel.setText(auction.getAuctionPlatformAccount().getAuctionPlatform().getName() + Language.translateStatic("-SITE"));
            auctionFundamentalsFormManager.reload();
            articleAttributesFormManager.reload();
            shippingServicesFormManager.reload();
            paymentMethodsFormManager.reload();
            auctionDetailsFormManager.reload();
            enhancementsFormManager.reload();
        } else {
            auctionPlatformAccountsJComboBox.setEnabled(false);
            auctionTypesJComboBox.setEnabled(false);
            if (auction.getType() != null) {
                auctionTypesJComboBox.addItem(auction.getType());
                auctionTypesJComboBox.setSelectedIndex(0);
            }
            auctionPlatformSitesJComboBox.removeAllItems();
            auctionPlatformSitesJComboBox.setEnabled(false);
            if (auction.getAuctionPlatformSite() != null) {
                auctionPlatformSitesJComboBox.addItem(auction.getAuctionPlatformSite());
                auctionPlatformSitesJComboBox.setSelectedIndex(0);
            }
            platformJLabel.setText(Language.translateStatic("AUCTIONPLATFORMSITE"));
        }
        for (int n = 0, i = auctionPlatformChangeListener.size(); n < i; n++) {
            auctionPlatformChangeListener.get(n).auctionPlatformChanged();
        }
    }

    public void addAuctionPlatformChangeListener(AuctionPlatformChangeListener auctionPlatformChangeListener) {
        this.auctionPlatformChangeListener.add(auctionPlatformChangeListener);
    }

    @Override
    public boolean isDefaultable() {
        return true;
    }
}
