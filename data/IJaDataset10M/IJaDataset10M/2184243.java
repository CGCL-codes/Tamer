package org.jskat.gui.table;

import java.awt.Color;
import java.util.Collection;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import net.miginfocom.swing.MigLayout;
import org.jskat.data.JSkatOptions;
import org.jskat.gui.img.JSkatGraphicRepository;
import org.jskat.util.Card;
import org.jskat.util.GameType;
import org.jskat.util.JSkatResourceBundle;
import org.jskat.util.Player;

/**
 * Abstract class for a panel representing a players hand
 */
abstract class HandPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    /**
	 * Player position
	 */
    Player position;

    /**
	 * Card images
	 */
    JSkatGraphicRepository bitmaps;

    /**
	 * i18n strings
	 */
    JSkatResourceBundle strings;

    /**
	 * Header panel
	 */
    JPanel header;

    /**
	 * Header label
	 */
    JLabel headerLabel;

    /**
	 * Icon panel
	 */
    IconPanel iconPanel;

    /**
	 * Clock panel
	 */
    ClockPanel clockPanel;

    /**
	 * Player name
	 */
    String playerName;

    /**
	 * Player bid
	 */
    int bidValue;

    boolean showIssWidgets;

    CardPanel cardPanel;

    /**
	 * Maximum card count
	 */
    int maxCardCount = 0;

    boolean activePlayer = false;

    boolean playerPassed = false;

    boolean declarer = false;

    /**
	 * Constructor
	 * 
	 * @param actions
	 *            Action map
	 * @param jskatBitmaps
	 *            Card images
	 * @param showIssWidgets
	 *            TRUE, if ISS widgets should be shown
	 */
    HandPanel(ActionMap actions, int maxCards, boolean showIssWidgets) {
        setActionMap(actions);
        bitmaps = JSkatGraphicRepository.instance();
        strings = JSkatResourceBundle.instance();
        maxCardCount = maxCards;
        this.showIssWidgets = showIssWidgets;
        setOpaque(false);
        headerLabel = new JLabel(" ");
        iconPanel = new IconPanel();
        clockPanel = new ClockPanel();
        initPanel();
    }

    /**
	 * Initializes the panel
	 */
    void initPanel() {
        setLayout(new MigLayout("fill", "fill", "[shrink][grow]"));
        setBorder(getPanelBorder());
        header = new JPanel(new MigLayout("fill", "[shrink][grow][shrink]", "fill"));
        header.add(headerLabel);
        header.add(new JPanel());
        if (showIssWidgets) {
            header.add(iconPanel);
            header.add(clockPanel);
        }
        add(header, "shrinky, wrap");
        cardPanel = new CardPanel(this, 1.0, true);
        add(cardPanel, "growy");
        if (JSkatOptions.instance().isShowCards()) {
            showCards();
        }
    }

    private Border getPanelBorder() {
        Border result = null;
        if (activePlayer) {
            result = BorderFactory.createLineBorder(Color.yellow, 2);
        } else {
            result = BorderFactory.createLineBorder(Color.black, 2);
        }
        return result;
    }

    /**
	 * Sets the player position
	 * 
	 * @param newPosition
	 *            Position
	 */
    void setPosition(Player newPosition) {
        position = newPosition;
        refreshHeaderText();
    }

    void setBidValue(int newBidValue) {
        bidValue = newBidValue;
        refreshHeaderText();
    }

    /**
	 * Gets the player position
	 * 
	 * @return Player position
	 */
    Player getPosition() {
        return position;
    }

    private void refreshHeaderText() {
        StringBuffer headerText = new StringBuffer();
        headerText.append(playerName).append(": ");
        if (position != null) {
            switch(position) {
                case FOREHAND:
                    headerText.append(strings.getString("forehand"));
                    break;
                case MIDDLEHAND:
                    headerText.append(strings.getString("middlehand"));
                    break;
                case REARHAND:
                    headerText.append(strings.getString("rearhand"));
                    break;
            }
            headerText.append(" " + strings.getString("bid") + ": ");
            headerText.append(bidValue);
            if (playerPassed) {
                headerText.append(" (" + strings.getString("passed") + ")");
            }
            if (declarer) {
                headerText.append(" (" + strings.getString("declarer") + ")");
            }
        }
        headerLabel.setText(headerText.toString());
    }

    /**
	 * Adds a card to the panel
	 * 
	 * @param newCard
	 *            Card
	 */
    void addCard(Card newCard) {
        cardPanel.addCard(newCard);
    }

    /**
	 * Adds a card to the panel
	 * 
	 * @param newCard
	 *            Card
	 */
    void addCards(Collection<Card> newCards) {
        cardPanel.addCards(newCards);
    }

    /**
	 * Removes a card from the panel
	 * 
	 * @param cardToRemove
	 *            Card
	 */
    void removeCard(Card cardToRemove) {
        cardPanel.removeCard(cardToRemove);
    }

    /**
	 * Removes all cards from the panel
	 */
    void removeAllCards() {
        cardPanel.clearCards();
    }

    /**
	 * Removes all cards from the panel and resets other values
	 */
    void clearHandPanel() {
        cardPanel.clearCards();
        bidValue = 0;
        playerPassed = false;
        declarer = false;
        refreshHeaderText();
        setActivePlayer(false);
    }

    /**
	 * Hides all cards on the panel
	 */
    void hideCards() {
        cardPanel.hideCards();
    }

    /**
	 * Shows all cards on the panel
	 */
    void showCards() {
        cardPanel.showCards();
    }

    void setSortGameType(GameType newGameType) {
        cardPanel.setSortType(newGameType);
    }

    boolean isHandFull() {
        return cardPanel.getCardCount() == maxCardCount;
    }

    public void setPlayerName(String newName) {
        playerName = newName;
        refreshHeaderText();
    }

    void setPlayerTime(double newTime) {
        clockPanel.setPlayerTime(newTime);
    }

    void setChatEnabled(boolean isChatEnabled) {
        iconPanel.setChatEnabled(isChatEnabled);
    }

    void setReadyToPlay(boolean isReadyToPlay) {
        iconPanel.setReadyToPlay(isReadyToPlay);
    }

    boolean isActivePlayer() {
        return activePlayer;
    }

    void setActivePlayer(boolean isActivePlayer) {
        activePlayer = isActivePlayer;
        setBorder(getPanelBorder());
    }

    void setPass(boolean isPassed) {
        playerPassed = isPassed;
        refreshHeaderText();
    }

    void setDeclarer(boolean isDeclarer) {
        declarer = isDeclarer;
        refreshHeaderText();
    }

    public String getPlayerName() {
        return playerName;
    }
}
