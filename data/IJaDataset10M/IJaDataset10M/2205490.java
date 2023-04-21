package com.mudderman.mudutils.ui.card;

import java.awt.CardLayout;
import java.awt.Component;
import java.util.ArrayList;
import javax.swing.GroupLayout;
import javax.swing.JPanel;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import com.mudderman.mudutils.event.EventManager;
import com.mudderman.mudutils.ui.components.Header;
import com.mudderman.mudutils.ui.trailbar.TrailBar;
import com.mudderman.mudutils.ui.trailbar.TrailBarItem;

public class CardContainer extends JPanel {

    private static final long serialVersionUID = 2277431753333812009L;

    private JPanel content;

    private Card prevCard;

    private Card currentCard;

    private Header header;

    private TrailBar trailBar;

    private boolean showHeader = true;

    private boolean showTrail = false;

    /**
	 * Shows the header, hides the trail
	 */
    public CardContainer() {
        this(true, false);
    }

    public CardContainer(boolean showHeader, boolean showTrail) {
        this.showHeader = showHeader;
        this.showTrail = showTrail;
        this.setBorder(null);
        if (showHeader) {
            header = new Header("dummy", "", null);
        }
        content = new JPanel();
        content.setLayout(new CardLayout());
        content.setBorder(null);
        if (showTrail) {
            trailBar = new TrailBar();
        }
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        ParallelGroup hGroup = layout.createParallelGroup();
        SequentialGroup vGroup = layout.createSequentialGroup();
        if (showHeader) {
            hGroup.addComponent(header);
            vGroup.addComponent(header);
        }
        if (showTrail) {
            vGroup.addGap(2);
            hGroup.addComponent(trailBar, 0, 0, Short.MAX_VALUE);
            vGroup.addComponent(trailBar);
            vGroup.addGap(2);
        }
        hGroup.addComponent(content);
        vGroup.addComponent(content);
        layout.setHorizontalGroup(hGroup);
        layout.setVerticalGroup(vGroup);
        this.prevCard = null;
        this.currentCard = null;
    }

    /**
	 * Add a card to the container
	 * @param card The <code>Card</code> that shall be added.
	 */
    public void addCard(Card card) {
        if (card != null) {
            this.content.add(card, card.getIdentifier());
            EventManager.INSTANCE.addObserver(card);
        }
    }

    /**
	 * Show a card
	 * @param cardIdentifier the unique identifier of the card that shall be shown
	 */
    public void showCard(String cardIdentifier) {
        this.prevCard = this.currentCard;
        if (cardIdentifier != null) {
            Card currentCard = getCard(cardIdentifier);
            this.currentCard = currentCard;
            if (currentCard != null) {
                if (prevCard != null) {
                    prevCard.onCardBlur();
                }
                currentCard.onCardFocus();
            }
            CardLayout layout = (CardLayout) this.content.getLayout();
            layout.show(this.content, cardIdentifier);
            if (currentCard != null) {
                if (this.showHeader) {
                    this.header.setTitle(currentCard.getTitle());
                    this.header.setDescription(currentCard.getDescription());
                }
                if (showTrail) {
                    trailBar.getModel().setRootItem(new TrailBarItem(this.currentCard.getTitle(), ""));
                }
            }
        }
    }

    public void showCard(Card card) {
        this.showCard(card.getIdentifier());
    }

    /**
	 * The number of cards in the container
	 */
    public int getCardCount() {
        return this.content.getComponentCount();
    }

    /**
	 * Get a card from the container
	 * @param identifier the unique indentifier of the card that shall be returned
	 * @return Card the wanted card or bust (null) if not found
	 */
    public Card getCard(String identifier) {
        if (identifier != null && !identifier.trim().equals("")) {
            if (this.content.getComponentCount() != 0) {
                for (Component comp : this.content.getComponents()) {
                    Card card = (Card) comp;
                    if (card.getIdentifier().equals(identifier)) {
                        return card;
                    }
                }
            }
        }
        return null;
    }

    public ArrayList<Card> getCards() {
        ArrayList<Card> cards = new ArrayList<Card>();
        for (Component c : this.content.getComponents()) {
            if (c instanceof Card) {
                cards.add((Card) c);
            }
        }
        return cards;
    }

    public Card getCurrentCard() {
        return this.currentCard;
    }

    public Card getPreviousCard() {
        return this.prevCard;
    }

    public boolean isShowHeader() {
        return showHeader;
    }

    public void setShowHeader(boolean showHeader) {
        this.showHeader = showHeader;
    }

    public boolean isShowTrail() {
        return showTrail;
    }

    public void setShowTrail(boolean showTrail) {
        this.showTrail = showTrail;
    }

    public TrailBar getTrailBar() {
        return this.trailBar;
    }
}
