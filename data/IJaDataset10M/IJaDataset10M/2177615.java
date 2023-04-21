package de.shandschuh.jaolt.gui.editauction;

import java.awt.ComponentOrientation;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import com.jgoodies.forms.builder.PanelBuilder;
import de.shandschuh.jaolt.core.Auction;
import de.shandschuh.jaolt.core.AuctionPlatform;
import de.shandschuh.jaolt.core.Country;
import de.shandschuh.jaolt.core.Language;
import de.shandschuh.jaolt.core.auction.ShippingService;
import de.shandschuh.jaolt.core.auctionplatform.AuctionPlatformFeature;
import de.shandschuh.jaolt.core.exception.DatabaseInitiationException;
import de.shandschuh.jaolt.gui.ErrorJDialog;
import de.shandschuh.jaolt.gui.FormManager;
import de.shandschuh.jaolt.gui.Lister;
import de.shandschuh.jaolt.gui.core.CountryJCheckBox;
import de.shandschuh.jaolt.tools.array.ArrayHelper;

public class ShippingServicesFormManager extends FormManager {

    private Auction auction;

    private ShippingServiceJComboBox[] nationalShippingServiceJComboBoxes;

    private ShippingServiceJComboBox[] internationalShippingServiceJComboBoxes;

    private CountryJCheckBox[] shipToLocationsCountryJCheckBoxes;

    private JCheckBox shippingDiscountJCheckBox;

    private AuctionPlatform auctionPlatform;

    private boolean multiedit;

    public ShippingServicesFormManager(Auction auction) {
        this.auction = auction;
        reload();
    }

    protected String getColumnLayout() {
        if (auction.getAuctionPlatformAccount() != null && auction.getAuctionPlatformSite() != null && auction.getShippingServices() != null) {
            StringBuffer buffer = new StringBuffer("5dlu, 0dlu:grow, p, 4dlu, left:p,");
            AuctionPlatform auctionPlatform = auction.getAuctionPlatformAccount().getAuctionPlatform();
            if (auctionPlatform.hasFeature(AuctionPlatformFeature.ADDITIONALSHIPPINGFEES)) {
                buffer.append(" 4dlu, left:p, ");
            }
            if (auctionPlatform.hasFeature(AuctionPlatformFeature.MAXIMUMSHIPPINGDURATION)) {
                buffer.append(" 4dlu, left:p, ");
            }
            buffer.append(" fill:4dlu");
            return buffer.toString();
        } else {
            return "p, 4dlu, p";
        }
    }

    protected String getRowLayout() {
        if (auction.getAuctionPlatformAccount() != null && auction.getAuctionPlatformSite() != null && auction.getShippingServices() != null) {
            StringBuffer buffer = new StringBuffer();
            AuctionPlatform auctionPlatform = auction.getAuctionPlatformAccount().getAuctionPlatform();
            if (nationalShippingServiceJComboBoxes != null && nationalShippingServiceJComboBoxes.length > 0) {
                buffer.append("p");
                for (int n = 0, i = nationalShippingServiceJComboBoxes.length; n < i; n++) {
                    buffer.append(", 2dlu, p, 1dlu, p");
                }
            }
            if (internationalShippingServiceJComboBoxes != null && internationalShippingServiceJComboBoxes.length > 0) {
                if (buffer.length() > 0) {
                    buffer.append(", 3dlu, p");
                } else {
                    buffer.append("p");
                }
                for (int n = 0, i = internationalShippingServiceJComboBoxes.length; n < i; n++) {
                    buffer.append(", 2dlu, p, 1dlu, p, 1dlu, p");
                }
            }
            if (auctionPlatform.hasFeature(AuctionPlatformFeature.ADDITIONALSHIPPINGLOCATIONS) && shipToLocationsCountryJCheckBoxes != null && shipToLocationsCountryJCheckBoxes.length > 0) {
                if (buffer.length() > 0) {
                    buffer.append(", 3dlu, p, 3dlu, min(p;45dlu)");
                } else {
                    buffer.append("p, 3dlu, min(p;45dlu)");
                }
            }
            if (auctionPlatform.hasFeature(AuctionPlatformFeature.SHIPPINGDISCOUNT)) {
                if (buffer.length() > 0) {
                    buffer.append(", 3dlu, p");
                } else {
                    buffer.append("p");
                }
            }
            return buffer.toString();
        } else {
            return "p";
        }
    }

    protected void addPanelBuilderComponents(PanelBuilder panelBuilder) {
        if (auction.getAuctionPlatformAccount() != null && auction.getAuctionPlatformSite() != null && auction.getShippingServices() != null) {
            AuctionPlatform auctionPlatform = auction.getAuctionPlatformAccount().getAuctionPlatform();
            boolean hasAdditionalShippingFees = auctionPlatform.hasFeature(AuctionPlatformFeature.ADDITIONALSHIPPINGFEES);
            boolean hasMaximumDuration = auctionPlatform.hasFeature(AuctionPlatformFeature.MAXIMUMSHIPPINGDURATION);
            int columnCount = 6 + (hasAdditionalShippingFees ? 2 : 0) + (hasMaximumDuration ? 2 : 0);
            int n = 0;
            if (nationalShippingServiceJComboBoxes != null && nationalShippingServiceJComboBoxes.length > 0) {
                panelBuilder.addSeparator(Language.translateStatic("SHIPPING_NATIONAL"), getCellConstraints(1, 1, columnCount));
                for (int i = nationalShippingServiceJComboBoxes.length; n < i; n++) {
                    panelBuilder.add(nationalShippingServiceJComboBoxes[n], getCellConstraints(2, 3 + n * 4, columnCount - 2));
                    panelBuilder.add(nationalShippingServiceJComboBoxes[n].getCurrencySymbolJLabel(), getCellConstraints(3, 3 + n * 4 + 2));
                    panelBuilder.add(nationalShippingServiceJComboBoxes[n].getPriceJTextField(), getCellConstraints(5, 3 + n * 4 + 2));
                    int l = 3;
                    if (hasAdditionalShippingFees) {
                        panelBuilder.add(nationalShippingServiceJComboBoxes[n].getAddPriceJCheckBoxPriceField(), getCellConstraints((l++) * 2 + 1, 3 + n * 4 + 2));
                    }
                    if (hasMaximumDuration) {
                        panelBuilder.add(nationalShippingServiceJComboBoxes[n].getMaximumDurationJComboBox(), getCellConstraints(l * 2 + 1, 3 + n * 4 + 2));
                    }
                }
                n *= 4;
                n += 2;
            }
            int k = 0;
            if (internationalShippingServiceJComboBoxes != null && internationalShippingServiceJComboBoxes.length > 0) {
                panelBuilder.addSeparator(Language.translateStatic("SHIPPING_INTERNATIONAL"), getCellConstraints(1, n + 1, columnCount));
                for (int i = internationalShippingServiceJComboBoxes != null ? internationalShippingServiceJComboBoxes.length : 0; k < i; k++) {
                    panelBuilder.add(internationalShippingServiceJComboBoxes[k], getCellConstraints(2, 3 + n + k * 6, columnCount - 2));
                    panelBuilder.add(internationalShippingServiceJComboBoxes[k].getCurrencySymbolJLabel(), getCellConstraints(3, 3 + n + k * 6 + 2));
                    panelBuilder.add(internationalShippingServiceJComboBoxes[k].getPriceJTextField(), getCellConstraints(5, 3 + n + k * 6 + 2));
                    int l = 3;
                    if (hasAdditionalShippingFees) {
                        panelBuilder.add(internationalShippingServiceJComboBoxes[k].getAddPriceJCheckBoxPriceField(), getCellConstraints((l++) * 2 + 1, 3 + n + k * 6 + 2));
                    }
                    if (hasMaximumDuration) {
                        panelBuilder.add(internationalShippingServiceJComboBoxes[k].getMaximumDurationJComboBox(), getCellConstraints(l * 2 + 1, 3 + n + k * 6 + 2));
                    }
                    if (internationalShippingServiceJComboBoxes[k].getCountryJCheckBoxesJScrollPane() != null) {
                        panelBuilder.add(internationalShippingServiceJComboBoxes[k].getCountryJCheckBoxesJScrollPane(), getCellConstraints(2, 3 + n + k * 6 + 4, columnCount - 2));
                    }
                }
            }
            int l = 0;
            if (auction.getAuctionPlatformAccount().getAuctionPlatform().hasFeature(AuctionPlatformFeature.ADDITIONALSHIPPINGLOCATIONS) && shipToLocationsCountryJCheckBoxes != null && shipToLocationsCountryJCheckBoxes.length > 0) {
                JPanel shipToLocationsJPanel = new JPanel();
                shipToLocationsJPanel.setLayout(new BoxLayout(shipToLocationsJPanel, BoxLayout.PAGE_AXIS));
                shipToLocationsJPanel.setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 2));
                for (int m = 0, i = shipToLocationsCountryJCheckBoxes != null ? shipToLocationsCountryJCheckBoxes.length : 0; m < i; m++) {
                    shipToLocationsJPanel.add(shipToLocationsCountryJCheckBoxes[m]);
                }
                JScrollPane scrollPane = new JScrollPane(shipToLocationsJPanel);
                scrollPane.getVerticalScrollBar().setUnitIncrement(10);
                panelBuilder.addSeparator(Language.translateStatic("ADDITIONAL_SHIPTOLOCATIONS"), getCellConstraints(1, 3 + n + k * 6, columnCount));
                l = n + k * 6 + 2;
                panelBuilder.add(scrollPane, getCellConstraints(2, 3 + l, columnCount - 2));
            }
            if (shippingDiscountJCheckBox != null && auction.getAuctionPlatformAccount().getAuctionPlatform().hasFeature(AuctionPlatformFeature.SHIPPINGDISCOUNT)) {
                panelBuilder.add(shippingDiscountJCheckBox, getCellConstraints(1, 3 + l + 2, columnCount - 1));
            }
            multiedit = false;
        } else {
            panelBuilder.add(new JLabel(Language.translateStatic("MUILTIEDIT")), getCellConstraints(1, 1));
            multiedit = true;
        }
        if (!isLeftToRight()) {
            getPanel().applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        }
    }

    protected void saveLocal() throws Exception {
        if (auction.getShippingServices() != null) {
            Vector<ShippingService> shippingServices = new Vector<ShippingService>();
            for (int n = 0, i = nationalShippingServiceJComboBoxes != null ? nationalShippingServiceJComboBoxes.length : 0; n < i; n++) {
                if (nationalShippingServiceJComboBoxes[n].isEntered()) {
                    nationalShippingServiceJComboBoxes[n].save();
                    shippingServices.add(nationalShippingServiceJComboBoxes[n].getShippingService().clone());
                }
            }
            for (int n = 0, i = internationalShippingServiceJComboBoxes != null ? internationalShippingServiceJComboBoxes.length : 0; n < i; n++) {
                if (internationalShippingServiceJComboBoxes[n].isEntered()) {
                    internationalShippingServiceJComboBoxes[n].save();
                    shippingServices.add(internationalShippingServiceJComboBoxes[n].getShippingService().clone());
                }
            }
            auction.setShippingServicesDiscount(shippingDiscountJCheckBox != null && shippingDiscountJCheckBox.isSelected());
            auction.setShippingServices(shippingServices.toArray(new ShippingService[0]));
            if (defaultCheckBox != null && defaultCheckBox.isSelected()) {
                Lister.getCurrentInstance().getMember().getDefaultValues(auctionPlatform, true).setShippingServices(auction.getShippingServices());
                Lister.getCurrentInstance().getMember().getDefaultValues(auctionPlatform, true).setShippingDiscoutAvailable(auction.isShippingServicesDiscount());
            }
        }
        if (auction.getShipToLocations() != null) {
            Vector<Country> shipToLocations = new Vector<Country>();
            for (int n = 0, i = shipToLocationsCountryJCheckBoxes != null ? shipToLocationsCountryJCheckBoxes.length : 0; n < i; n++) {
                if (shipToLocationsCountryJCheckBoxes[n] != null && shipToLocationsCountryJCheckBoxes[n].isEnabled() && shipToLocationsCountryJCheckBoxes[n].isSelected()) {
                    shipToLocations.add(shipToLocationsCountryJCheckBoxes[n].getCountry());
                }
            }
            auction.setShipToLocations(shipToLocations.toArray(new Country[0]));
        }
    }

    protected void reloadLocal(boolean rebuild) {
        auctionPlatform = auction.getAuctionPlatform();
        if (auctionPlatform != null && auction.getAuctionPlatformSite() != null && auction.getShippingServices() != null) {
            ShippingService[] nationalShippingServices = auction.getShippingServices(false);
            ShippingService[] internationalShippingServices = auction.getShippingServices(true);
            Vector<ShippingService> availableShippingServices;
            try {
                availableShippingServices = auctionPlatform.getShippingServices(auction.getAuctionPlatformSite(), auction.getAuctionPlatformAccount());
            } catch (DatabaseInitiationException dbie) {
                availableShippingServices = new Vector<ShippingService>();
                ErrorJDialog.showErrorDialog(dbie);
                return;
            } catch (Exception e) {
                availableShippingServices = new Vector<ShippingService>();
                nationalShippingServiceJComboBoxes = null;
                internationalShippingServiceJComboBoxes = null;
                shipToLocationsCountryJCheckBoxes = null;
                showError(e);
                return;
            }
            ShippingService[] availableNationalShippingServices = ShippingService.getShippingServices(availableShippingServices, false);
            ShippingService[] availableInternationalShippingServices = ShippingService.getShippingServices(availableShippingServices, true);
            if (rebuild) {
                if (nationalShippingServiceJComboBoxes != null) {
                    nationalShippingServiceJComboBoxes = ArrayHelper.setArraySize(nationalShippingServiceJComboBoxes, auctionPlatform != null ? auctionPlatform.getMaximumNationalShippingServices() : 0);
                } else {
                    nationalShippingServiceJComboBoxes = new ShippingServiceJComboBox[auctionPlatform != null ? auctionPlatform.getMaximumNationalShippingServices() : 0];
                }
                for (int n = 0, i = Math.min(nationalShippingServices.length, nationalShippingServiceJComboBoxes.length); n < i; n++) {
                    if (nationalShippingServiceJComboBoxes[n] != null) {
                        nationalShippingServiceJComboBoxes[n].setShippingService(nationalShippingServices[n]);
                        nationalShippingServiceJComboBoxes[n].setCurrency(auction.getCurrency());
                        nationalShippingServiceJComboBoxes[n].setAvailableShippingSerivces(availableNationalShippingServices);
                    } else {
                        nationalShippingServiceJComboBoxes[n] = new ShippingServiceJComboBox(nationalShippingServices[n], auction.getCurrency(), availableNationalShippingServices);
                    }
                }
                for (int n = nationalShippingServices.length, i = nationalShippingServiceJComboBoxes.length; n < i; n++) {
                    if (nationalShippingServiceJComboBoxes[n] != null) {
                        nationalShippingServiceJComboBoxes[n].setShippingService(new ShippingService());
                        nationalShippingServiceJComboBoxes[n].setCurrency(auction.getCurrency());
                        nationalShippingServiceJComboBoxes[n].setAvailableShippingSerivces(availableNationalShippingServices);
                    } else {
                        nationalShippingServiceJComboBoxes[n] = new ShippingServiceJComboBox(new ShippingService(), auction.getCurrency(), availableNationalShippingServices);
                    }
                }
                Vector<Country> shipToLocations;
                try {
                    shipToLocations = auctionPlatform.getShipToLocations(auction.getAuctionPlatformSite(), auction.getAuctionPlatformAccount());
                } catch (DatabaseInitiationException dbie) {
                    shipToLocations = new Vector<Country>();
                } catch (Exception e) {
                    shipToLocations = new Vector<Country>();
                    showError(e);
                }
                if (auctionPlatform.hasFeature(AuctionPlatformFeature.ADDITIONALSHIPPINGLOCATIONS)) {
                    shipToLocationsCountryJCheckBoxes = CountryJCheckBox.create(shipToLocations, auction.getShipToLocations());
                }
                if (internationalShippingServiceJComboBoxes != null) {
                    internationalShippingServiceJComboBoxes = ArrayHelper.setArraySize(internationalShippingServiceJComboBoxes, auctionPlatform != null ? auctionPlatform.getMaximumInternationalShippingServices() : 0);
                } else {
                    internationalShippingServiceJComboBoxes = new ShippingServiceJComboBox[auctionPlatform != null ? auctionPlatform.getMaximumInternationalShippingServices() : 0];
                }
                boolean hasSingleInternationalShippingLocation = auctionPlatform.hasFeature(AuctionPlatformFeature.SINGLEINTERNATIONALSHIPINGLOCATION);
                for (int n = 0, i = Math.min(internationalShippingServices.length, internationalShippingServiceJComboBoxes.length); n < i; n++) {
                    if (internationalShippingServiceJComboBoxes[n] != null) {
                        internationalShippingServiceJComboBoxes[n].setAdditionalShipToLocationCountryJCheckBoxes(null);
                        internationalShippingServiceJComboBoxes[n].setCurrency(auction.getCurrency());
                        internationalShippingServiceJComboBoxes[n].setAvailableShippingSerivces(availableInternationalShippingServices);
                        internationalShippingServiceJComboBoxes[n].setShipToLocations(shipToLocations, internationalShippingServices[n], hasSingleInternationalShippingLocation);
                    } else {
                        internationalShippingServiceJComboBoxes[n] = new ShippingServiceJComboBox(internationalShippingServices[n], auction.getCurrency(), availableInternationalShippingServices, shipToLocations, hasSingleInternationalShippingLocation);
                    }
                    if (auctionPlatform.hasFeature(AuctionPlatformFeature.ADDITIONALSHIPPINGLOCATIONS)) {
                        CountryJCheckBox.addHooks(shipToLocationsCountryJCheckBoxes, internationalShippingServices[n].getShippingLocations());
                        internationalShippingServiceJComboBoxes[n].setAdditionalShipToLocationCountryJCheckBoxes(shipToLocationsCountryJCheckBoxes);
                    }
                }
                for (int n = internationalShippingServices.length, i = internationalShippingServiceJComboBoxes.length; n < i; n++) {
                    if (internationalShippingServiceJComboBoxes[n] != null) {
                        internationalShippingServiceJComboBoxes[n].setAdditionalShipToLocationCountryJCheckBoxes(null);
                        internationalShippingServiceJComboBoxes[n].setCurrency(auction.getCurrency());
                        internationalShippingServiceJComboBoxes[n].setAvailableShippingSerivces(availableInternationalShippingServices);
                        internationalShippingServiceJComboBoxes[n].setShipToLocations(shipToLocations, new ShippingService(true), hasSingleInternationalShippingLocation);
                    } else {
                        internationalShippingServiceJComboBoxes[n] = new ShippingServiceJComboBox(new ShippingService(true), auction.getCurrency(), availableInternationalShippingServices, shipToLocations, hasSingleInternationalShippingLocation);
                    }
                    if (auctionPlatform.hasFeature(AuctionPlatformFeature.ADDITIONALSHIPPINGLOCATIONS)) {
                        internationalShippingServiceJComboBoxes[n].setAdditionalShipToLocationCountryJCheckBoxes(shipToLocationsCountryJCheckBoxes);
                    }
                }
                if (auctionPlatform != null && auctionPlatform.hasFeature(AuctionPlatformFeature.SHIPPINGDISCOUNT)) {
                    if (shippingDiscountJCheckBox == null) {
                        shippingDiscountJCheckBox = new JCheckBox(Language.translateStatic("SHIPPING_DISCOUNT_AVAILABLE"));
                    }
                    shippingDiscountJCheckBox.setEnabled(true);
                    shippingDiscountJCheckBox.setSelected(auction.isShippingServicesDiscount());
                }
            } else {
                if (auctionPlatform.hasFeature(AuctionPlatformFeature.ADDITIONALSHIPPINGLOCATIONS)) {
                    CountryJCheckBox.resetHooks(shipToLocationsCountryJCheckBoxes);
                }
                if (nationalShippingServiceJComboBoxes != null && nationalShippingServices != null) {
                    for (int n = 0, i = Math.min(nationalShippingServices.length, nationalShippingServiceJComboBoxes.length); n < i; n++) {
                        if (nationalShippingServiceJComboBoxes[n] != null) {
                            nationalShippingServiceJComboBoxes[n].setAvailableShippingSerivces(availableNationalShippingServices);
                            nationalShippingServiceJComboBoxes[n].setShippingService(nationalShippingServices[n]);
                            nationalShippingServiceJComboBoxes[n].setCurrency(auction.getCurrency());
                        }
                    }
                    for (int n = nationalShippingServices.length; n < nationalShippingServiceJComboBoxes.length; n++) {
                        if (nationalShippingServiceJComboBoxes[n] != null) {
                            nationalShippingServiceJComboBoxes[n].setAvailableShippingSerivces(availableNationalShippingServices);
                            nationalShippingServiceJComboBoxes[n].setShippingService(new ShippingService());
                            nationalShippingServiceJComboBoxes[n].setCurrency(auction.getCurrency());
                        }
                    }
                }
                if (internationalShippingServices != null && internationalShippingServiceJComboBoxes != null) {
                    for (int n = 0, i = Math.min(internationalShippingServices.length, internationalShippingServiceJComboBoxes.length); n < i; n++) {
                        if (internationalShippingServiceJComboBoxes[n] != null) {
                            internationalShippingServiceJComboBoxes[n].setAdditionalShipToLocationCountryJCheckBoxes(null);
                            internationalShippingServiceJComboBoxes[n].setAvailableShippingSerivces(availableInternationalShippingServices);
                            internationalShippingServiceJComboBoxes[n].setCurrency(auction.getCurrency());
                            internationalShippingServiceJComboBoxes[n].setShippingService(internationalShippingServices[n]);
                            if (auctionPlatform.hasFeature(AuctionPlatformFeature.ADDITIONALSHIPPINGLOCATIONS)) {
                                internationalShippingServiceJComboBoxes[n].setAdditionalShipToLocationCountryJCheckBoxes(shipToLocationsCountryJCheckBoxes);
                                CountryJCheckBox.addHooks(shipToLocationsCountryJCheckBoxes, internationalShippingServices[n].getShippingLocations());
                            }
                        }
                    }
                    for (int n = internationalShippingServices.length; n < internationalShippingServiceJComboBoxes.length; n++) {
                        if (internationalShippingServiceJComboBoxes[n] != null) {
                            internationalShippingServiceJComboBoxes[n].setAdditionalShipToLocationCountryJCheckBoxes(null);
                            internationalShippingServiceJComboBoxes[n].setAvailableShippingSerivces(availableInternationalShippingServices);
                            internationalShippingServiceJComboBoxes[n].setCurrency(auction.getCurrency());
                            internationalShippingServiceJComboBoxes[n].setShippingService(new ShippingService(true));
                            if (auctionPlatform.hasFeature(AuctionPlatformFeature.ADDITIONALSHIPPINGLOCATIONS)) {
                                internationalShippingServiceJComboBoxes[n].setAdditionalShipToLocationCountryJCheckBoxes(shipToLocationsCountryJCheckBoxes);
                            }
                        }
                    }
                }
                if (auctionPlatform.hasFeature(AuctionPlatformFeature.ADDITIONALSHIPPINGLOCATIONS)) {
                    CountryJCheckBox.updateSelection(shipToLocationsCountryJCheckBoxes, auction.getShipToLocations());
                }
            }
        } else {
            nationalShippingServiceJComboBoxes = null;
            internationalShippingServiceJComboBoxes = null;
        }
    }

    protected void validateLocal() throws Exception {
    }

    public String getName() {
        return Language.translateStatic("AUCTION_SHIPPINGFEES");
    }

    @Override
    public String[][] getSummary() {
        return new String[][] { new String[] { "" } };
    }

    @Override
    public boolean isDefaultable() {
        return true;
    }

    public void setAuction(Auction auction) {
        this.auction = auction;
    }

    @Override
    public boolean rebuildNeeded() {
        AuctionPlatform auctionPlatform = auction.getAuctionPlatformAccount() != null ? auction.getAuctionPlatformAccount().getAuctionPlatform() : null;
        if (multiedit && auction.getShippingServices() != null || !multiedit && auction.getShippingServices() == null) {
            return true;
        } else if (auctionPlatform == this.auctionPlatform) {
            return false;
        } else {
            if (auctionPlatform == null) {
                return this.auctionPlatform.getMaximumInternationalShippingServices() > 0 || this.auctionPlatform.getMaximumNationalShippingServices() > 0;
            } else if (this.auctionPlatform == null) {
                return auctionPlatform.getMaximumInternationalShippingServices() > 0 || auctionPlatform.getMaximumNationalShippingServices() > 0;
            } else {
                return auctionPlatform.getMaximumInternationalShippingServices() != this.auctionPlatform.getMaximumInternationalShippingServices() || auctionPlatform.getMaximumNationalShippingServices() != this.auctionPlatform.getMaximumNationalShippingServices() || auctionPlatform.hasFeature(AuctionPlatformFeature.ADDITIONALSHIPPINGLOCATIONS) != this.auctionPlatform.hasFeature(AuctionPlatformFeature.ADDITIONALSHIPPINGLOCATIONS) || auctionPlatform.hasFeature(AuctionPlatformFeature.SHIPPINGDISCOUNT) != this.auctionPlatform.hasFeature(AuctionPlatformFeature.SHIPPINGDISCOUNT) || auctionPlatform.hasFeature(AuctionPlatformFeature.ADDITIONALSHIPPINGFEES) != this.auctionPlatform.hasFeature(AuctionPlatformFeature.ADDITIONALSHIPPINGFEES) || auctionPlatform.hasFeature(AuctionPlatformFeature.MAXIMUMSHIPPINGDURATION) != this.auctionPlatform.hasFeature(AuctionPlatformFeature.MAXIMUMSHIPPINGDURATION);
            }
        }
    }
}
