package de.shandschuh.jaolt.gui.editauction;

import java.awt.FlowLayout;
import java.awt.Font;
import java.util.Date;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.jgoodies.forms.builder.PanelBuilder;
import de.shandschuh.jaolt.core.Auction;
import de.shandschuh.jaolt.core.Language;
import de.shandschuh.jaolt.core.auction.Currency;
import de.shandschuh.jaolt.core.auction.MoneyAmount;
import de.shandschuh.jaolt.core.auction.Price;
import de.shandschuh.jaolt.core.auction.ShippingService;
import de.shandschuh.jaolt.gui.FormManager;
import de.shandschuh.jaolt.gui.core.HTMLRenderer;
import de.shandschuh.jaolt.gui.core.JHyperLink;
import de.shandschuh.jaolt.gui.listener.core.urllabel.URLOpenAction;
import de.shandschuh.jaolt.gui.listener.editauction.ShowBidsActionListener;
import de.shandschuh.jaolt.gui.listener.editauction.ShowQuestionsActionListener;
import de.shandschuh.jaolt.gui.worker.HTMLPanelLoadWorker;
import de.shandschuh.jaolt.gui.worker.PictureCountJPanelWorker;

public class ViewAuctionFormManager extends FormManager {

    private static ViewAuctionFormManager currentInstance;

    public static ViewAuctionFormManager create(Auction auction) {
        if (currentInstance == null) {
            currentInstance = new ViewAuctionFormManager(auction);
        } else {
            currentInstance.setAuction(auction);
        }
        return currentInstance;
    }

    private Auction auction;

    private JLabel auctionPlatformAccountSiteJLabel;

    private JLabel titleJLabel;

    private JLabel subtitleJLabel;

    private JHyperLink firstCategoryJHyperLink;

    private JHyperLink secondCategoryJHyperLink;

    private JHyperLink numberJHyperLink;

    private JLabel startDateJLabel;

    private JLabel endDateJLabel;

    private JLabel timeLeftJLabel;

    private HTMLRenderer htmlRenderer;

    private JButton showBidsJButton;

    private JLabel bidCountJLabel;

    private JLabel watchCountJLabel;

    private JLabel hitCountJLabel;

    private JLabel startPriceJLabel;

    private JLabel fixPriceJLabel;

    private JLabel currentPriceJLabel;

    private JPanel picturesJPanel;

    private JLabel questionsJLabel;

    private JButton showQuestionsJButton;

    private JLabel unansweredQuestionsJLabel;

    private JPanel nationalShippingServicesJPanel;

    private JPanel internationalShippingServicesJPanel;

    private ViewAuctionFormManager(Auction auction) {
        auctionPlatformAccountSiteJLabel = new JLabel();
        auctionPlatformAccountSiteJLabel.setFont(auctionPlatformAccountSiteJLabel.getFont().deriveFont(Font.BOLD));
        titleJLabel = new JLabel();
        firstCategoryJHyperLink = new JHyperLink();
        picturesJPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        nationalShippingServicesJPanel = new JPanel();
        nationalShippingServicesJPanel.setLayout(new BoxLayout(nationalShippingServicesJPanel, BoxLayout.Y_AXIS));
        this.auction = auction;
        reloadLocal(true);
    }

    @Override
    protected void addPanelBuilderComponents(PanelBuilder panelBuilder) {
        panelBuilder.add(auctionPlatformAccountSiteJLabel, getCellConstraints(1, 1, 12));
        panelBuilder.addLabel(Language.translateStatic("AUCTION_TITLE"), getCellConstraints(1, 3));
        panelBuilder.add(titleJLabel, getCellConstraints(3, 3, 10));
        if (auction.hasSubtitle()) {
            panelBuilder.addLabel(Language.translateStatic("AUCTION_SUBTITLE"), getCellConstraints(1, 5));
            panelBuilder.add(subtitleJLabel, getCellConstraints(3, 5, 10));
            setRowShift(2);
        }
        if (auction.hasNumber()) {
            panelBuilder.addLabel(Language.translateStatic("AUCTION_NUMBER"), getCellConstraints(1, 5));
            panelBuilder.add(numberJHyperLink, getCellConstraints(3, 5, 10));
            addRowShift(2);
        }
        panelBuilder.addLabel(Language.translateStatic("AUCTION_1STCATEGORY"), getCellConstraints(1, 5));
        panelBuilder.add(firstCategoryJHyperLink, getCellConstraints(3, 5, 10));
        if (auction.hasSecondCategory()) {
            panelBuilder.addLabel(Language.translateStatic("AUCTION_2NDCATEGORY"), getCellConstraints(1, 7));
            panelBuilder.add(secondCategoryJHyperLink, getCellConstraints(3, 7, 10));
            addRowShift(2);
        }
        if (auction.hasStartTime()) {
            panelBuilder.addLabel(Language.translateStatic("AUCTION_STARTTIME"), getCellConstraints(1, 7));
            panelBuilder.add(startDateJLabel, getCellConstraints(3, 7));
            panelBuilder.addLabel(Language.translateStatic("AUCTION_ENDTIME"), getCellConstraints(5, 7));
            panelBuilder.add(endDateJLabel, getCellConstraints(7, 7));
            panelBuilder.addLabel(Language.translateStatic("AUCTION_TIMELEFT"), getCellConstraints(9, 7));
            panelBuilder.add(timeLeftJLabel, getCellConstraints(11, 7));
            addRowShift(2);
        }
        panelBuilder.add(htmlRenderer.getRenderedComponent(), getCellConstraints(1, 9, 12));
        panelBuilder.addLabel(Language.translateStatic("AUCTION_PICTURES"), getCellConstraints(1, 11));
        panelBuilder.add(picturesJPanel, getCellConstraints(3, 11, 10));
        panelBuilder.addLabel(Language.translateStatic("SHIPPING_NATIONAL"), getCellConstraints(1, 13));
        panelBuilder.add(nationalShippingServicesJPanel, getCellConstraints(3, 13, 10));
        setColumnShift(0);
        if (auction.hasInternationalShippingServices()) {
            panelBuilder.addLabel(Language.translateStatic("SHIPPING_INTERNATIONAL"), getCellConstraints(1, 15));
            panelBuilder.add(internationalShippingServicesJPanel, getCellConstraints(3, 15, 10));
            addRowShift(2);
        }
        if (auction.hasPrice(Price.TYPE_STARTPRICE)) {
            panelBuilder.addLabel(Language.translateStatic("AUCTION_STARTPRICE"), getCellConstraints(1, 15));
            panelBuilder.add(startPriceJLabel, getCellConstraints(3, 15));
            setColumnShift(4);
        }
        if (auction.hasPrice(Price.TYPE_DIRECTBUYPRICE)) {
            panelBuilder.addLabel(Language.translateStatic("AUCTION_BINPRICE"), getCellConstraints(1, 15));
            panelBuilder.add(fixPriceJLabel, getCellConstraints(3, 15));
            setColumnShift(4);
        }
        if (auction.hasCurrentPrice()) {
            panelBuilder.addLabel(Language.translateStatic("AUCTION_CURRENTPRICE"), getCellConstraints(1, 15));
            panelBuilder.add(currentPriceJLabel, getCellConstraints(3, 15));
        }
        setColumnShift(0);
        if (auction.hasNumber()) {
            panelBuilder.addLabel(Language.translateStatic("AUCTION_BIDCOUNT"), getCellConstraints(1, 17));
            panelBuilder.add(bidCountJLabel, getCellConstraints(3, 17));
            panelBuilder.add(showBidsJButton, getCellConstraints(3, 17));
            panelBuilder.addLabel(Language.translateStatic("AUCTION_HITCOUNT"), getCellConstraints(5, 17));
            panelBuilder.add(hitCountJLabel, getCellConstraints(7, 17));
            panelBuilder.addLabel(Language.translateStatic("AUCTION_WATCHCOUNT"), getCellConstraints(9, 17));
            panelBuilder.add(watchCountJLabel, getCellConstraints(11, 17));
            panelBuilder.addLabel(Language.translateStatic("AUCTION_QUESTIONS"), getCellConstraints(1, 19));
            panelBuilder.add(questionsJLabel, getCellConstraints(3, 19));
            panelBuilder.add(showQuestionsJButton, getCellConstraints(3, 19));
            panelBuilder.addLabel(Language.translateStatic("AUCTION_UNANSWEREDQUESTIONS"), getCellConstraints(5, 19));
            panelBuilder.add(unansweredQuestionsJLabel, getCellConstraints(7, 19));
            addRowShift(4);
        }
    }

    @Override
    protected String getColumnLayout() {
        return "p, 4dlu, p, 4dlu, p, 4dlu, p, 4dlu, p, 4dlu, p, fill:0dlu:grow";
    }

    @Override
    public String getName() {
        return Language.translateStatic("AUCTION");
    }

    @Override
    protected String getRowLayout() {
        StringBuffer buffer = new StringBuffer("p, 6dlu, p");
        if (auction.hasSubtitle()) {
            buffer.append(", 3dlu, p");
        }
        buffer.append(", 6dlu, p");
        if (auction.hasSecondCategory()) {
            buffer.append(", 3dlu, p");
        }
        if (auction.hasStartTime()) {
            buffer.append(", 3dlu, p");
        }
        if (auction.hasNumber()) {
            buffer.append(", 6dlu, p");
        }
        buffer.append(", 6dlu, p");
        buffer.append(", 6dlu, fill:4dlu:grow");
        buffer.append(", 3dlu, p");
        buffer.append(", 6dlu, p");
        if (auction.hasInternationalShippingServices()) {
            buffer.append(", 3dlu, p");
        }
        buffer.append(", 6dlu, p");
        if (auction.hasNumber()) {
            buffer.append(", 6dlu, p, 3dlu, p");
        }
        return buffer.toString();
    }

    @Override
    public boolean rebuildNeeded() {
        return true;
    }

    @Override
    protected void reloadLocal(boolean rebuild) {
        auctionPlatformAccountSiteJLabel.setText(auction.getAuctionPlatformAccount().getAuctionPlatform() + " " + auction.getAuctionPlatformSite() + " (" + auction.getAuctionPlatformAccount().getUsername() + ")");
        titleJLabel.setText(auction.getTitle());
        firstCategoryJHyperLink.setAction(new URLOpenAction(auction.getFirstCategory().toString(), auction.getAuctionPlatformAccount().getAuctionPlatform().getCategoryURL(auction.getAuctionPlatformSite(), auction.getFirstCategory())));
        setShippingServicesList(auction.getShippingServices(false), nationalShippingServicesJPanel);
        if (rebuild) {
            if (auction.hasSubtitle()) {
                if (subtitleJLabel == null) {
                    subtitleJLabel = new JLabel(auction.getSubtitle());
                } else {
                    subtitleJLabel.setText(auction.getSubtitle());
                }
            }
            if (auction.hasSecondCategory()) {
                if (secondCategoryJHyperLink == null) {
                    secondCategoryJHyperLink = new JHyperLink(new URLOpenAction(auction.getSecondCategory().toString(), auction.getAuctionPlatformAccount().getAuctionPlatform().getCategoryURL(auction.getAuctionPlatformSite(), auction.getSecondCategory())));
                } else {
                    secondCategoryJHyperLink.setAction(new URLOpenAction(auction.getSecondCategory().toString(), auction.getAuctionPlatformAccount().getAuctionPlatform().getCategoryURL(auction.getAuctionPlatformSite(), auction.getSecondCategory())));
                }
            }
            Currency currency = auction.getCurrency();
            Price price = auction.getPrice(Price.TYPE_STARTPRICE);
            if (price != null) {
                if (startPriceJLabel != null) {
                    startPriceJLabel.setText(currency.toString(price.getValue()));
                } else {
                    startPriceJLabel = new JLabel(currency.toString(price.getValue()));
                }
            }
            price = auction.getPrice(Price.TYPE_DIRECTBUYPRICE);
            if (price != null) {
                if (fixPriceJLabel != null) {
                    fixPriceJLabel.setText(currency.toString(price.getValue()));
                } else {
                    fixPriceJLabel = new JLabel(currency.toString(price.getValue()));
                }
            }
            MoneyAmount moneyAmount = auction.getCurrentPrice();
            if (moneyAmount != null) {
                if (currentPriceJLabel != null) {
                    currentPriceJLabel.setText(moneyAmount.toString());
                } else {
                    currentPriceJLabel = new JLabel(moneyAmount.toString());
                }
            }
            if (auction.hasInternationalShippingServices()) {
                if (internationalShippingServicesJPanel == null) {
                    internationalShippingServicesJPanel = new JPanel();
                    internationalShippingServicesJPanel.setLayout(new BoxLayout(internationalShippingServicesJPanel, BoxLayout.Y_AXIS));
                }
                setShippingServicesList(auction.getShippingServices(true), internationalShippingServicesJPanel);
            }
        } else {
            if (auction.hasSubtitle()) {
                subtitleJLabel.setText(auction.getSubtitle());
            }
            if (auction.hasSecondCategory()) {
                secondCategoryJHyperLink.setAction(new URLOpenAction(auction.getSecondCategory().toString(), auction.getAuctionPlatformAccount().getAuctionPlatform().getCategoryURL(auction.getAuctionPlatformSite(), auction.getSecondCategory())));
            }
            Currency currency = auction.getCurrency();
            Price price = auction.getPrice(Price.TYPE_STARTPRICE);
            if (price != null) {
                startPriceJLabel.setText(currency.toString(price.getValue()));
            }
            price = auction.getPrice(Price.TYPE_DIRECTBUYPRICE);
            if (price != null) {
                fixPriceJLabel.setText(currency.toString(price.getValue()));
            }
            MoneyAmount moneyAmount = auction.getCurrentPrice();
            if (moneyAmount != null) {
                currentPriceJLabel.setText(moneyAmount.toString());
            }
            if (auction.hasInternationalShippingServices()) {
                setShippingServicesList(auction.getShippingServices(true), internationalShippingServicesJPanel);
            }
        }
        if (htmlRenderer == null) {
            htmlRenderer = HTMLRenderer.getAvailableInstance();
        }
        if (auction.hasStartTime()) {
            if (startDateJLabel == null) {
                startDateJLabel = new JLabel(Language.formatDateAndTime(auction.getStartTime()));
            } else {
                startDateJLabel.setText(Language.formatDateAndTime(auction.getStartTime()));
            }
            Date endTime = auction.getEndTime();
            if (endDateJLabel == null) {
                endDateJLabel = new JLabel(Language.formatDateAndTime(endTime));
            } else {
                endDateJLabel.setText(Language.formatDateAndTime(endTime));
            }
            if (timeLeftJLabel == null) {
                timeLeftJLabel = new JLabel(Language.formatTimeLeft(new Date(endTime.getTime() - System.currentTimeMillis())));
            } else {
                timeLeftJLabel.setText(Language.formatTimeLeft(new Date(endTime.getTime() - System.currentTimeMillis())));
            }
        }
        if (auction.hasCurrentPrice()) {
            if (currentPriceJLabel == null) {
                currentPriceJLabel = new JLabel(auction.getCurrentPrice().toString());
            } else {
                currentPriceJLabel.setText(auction.getCurrentPrice().toString());
            }
        }
        if (auction.hasNumber()) {
            if (numberJHyperLink == null) {
                numberJHyperLink = new JHyperLink(new URLOpenAction(auction.getNumber(), auction.getAuctionPlatformAccount().getAuctionPlatform().getAuctionURL(auction)));
            } else {
                numberJHyperLink.setAction(new URLOpenAction(auction.getNumber(), auction.getAuctionPlatformAccount().getAuctionPlatform().getAuctionURL(auction)));
            }
            int bidCount = auction.getBidCount();
            if (bidCountJLabel == null) {
                bidCountJLabel = new JLabel(Integer.toString(bidCount));
                showBidsJButton = new JButton(Integer.toString(bidCount));
            } else {
                bidCountJLabel.setText(Integer.toString(bidCount));
                showBidsJButton.setText(Integer.toString(bidCount));
                removeAllActionListeners(showBidsJButton);
            }
            showBidsJButton.addActionListener(new ShowBidsActionListener(auction.getBids()));
            showBidsJButton.setVisible(bidCount > 0);
            bidCountJLabel.setVisible(bidCount == 0);
            if (hitCountJLabel == null) {
                hitCountJLabel = new JLabel(Long.toString(auction.getHitCount()));
            } else {
                hitCountJLabel.setText(Long.toString(auction.getHitCount()));
            }
            if (watchCountJLabel == null) {
                watchCountJLabel = new JLabel(Long.toString(auction.getWatchCount()));
            } else {
                watchCountJLabel.setText(Long.toString(auction.getWatchCount()));
            }
            int questionCount = auction.getQuestionCount();
            if (questionsJLabel == null) {
                questionsJLabel = new JLabel(Integer.toString(questionCount));
                showQuestionsJButton = new JButton(questionsJLabel.getText());
                unansweredQuestionsJLabel = new JLabel(Integer.toString(auction.getUnasweredQuestionCount()));
            } else {
                questionsJLabel.setText(Integer.toString(questionCount));
                showQuestionsJButton.setText(questionsJLabel.getText());
                unansweredQuestionsJLabel.setText(Integer.toString(auction.getUnasweredQuestionCount()));
                removeAllActionListeners(showQuestionsJButton);
            }
            questionsJLabel.setVisible(questionCount == 0);
            showQuestionsJButton.setVisible(questionCount > 0);
            showQuestionsJButton.addActionListener(new ShowQuestionsActionListener(auction.getQuestions()));
        }
        new HTMLPanelLoadWorker(htmlRenderer, auction).start();
        new PictureCountJPanelWorker(picturesJPanel, auction).start();
    }

    private static void removeAllActionListeners(JButton button) {
        for (int n = button.getActionListeners() != null ? button.getActionListeners().length - 1 : -1; n > -1; n--) {
            button.removeActionListener(button.getActionListeners()[n]);
        }
    }

    @Override
    protected void saveLocal() throws Exception {
    }

    @Override
    protected void validateLocal() throws Exception {
    }

    public void setAuction(Auction auction) {
        this.auction = auction;
        reload();
    }

    private static void setShippingServicesList(ShippingService[] shippingServices, JPanel panel) {
        panel.removeAll();
        if (shippingServices != null && shippingServices.length > 0) {
            for (int n = 0, i = shippingServices.length; n < i; n++) {
                panel.add(new JLabel(shippingServices[n].toString() + " - " + shippingServices[n].getFees()));
            }
        } else {
            panel.add(new JLabel(Language.translateStatic("NONE")));
        }
    }
}
