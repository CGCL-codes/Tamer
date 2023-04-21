package org.jogre.spades.client;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import org.jogre.client.awt.JogreComponent;
import org.jogre.common.games.Card;

/**
 * @author Garrett Lehman (Gman)
 * @version Alpha 0.2.3
 *
 * Spades board component.
 */
public class SpadesBoardComponent extends JogreComponent {

    public static final int BOTTOM = 0;

    public static final int LEFT = 1;

    public static final int TOP = 2;

    public static final int RIGHT = 3;

    private static final int SPACING = 5;

    private SpadesModel model = null;

    /**
	 * Default constructor which requires a spades model.
	 *
	 * @param model
	 *            Spades model
	 */
    public SpadesBoardComponent(SpadesModel model) {
        this.model = model;
        int cardHeight = Card.CARD_PIXEL_HEIGHT;
        int cardWidth = Card.CARD_PIXEL_WIDTH;
        int width = (SPACING * 4) + (cardHeight * 2) + cardWidth;
        int height = (SPACING * 3) + (cardHeight * 2);
        setPreferredSize(new Dimension(width, height));
        repaint();
    }

    /**
	 * Refresh the component.
	 *
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
    public void paintComponent(Graphics g) {
        g.setColor(SpadesLookAndFeel.TABLE_BG_COLOUR);
        g.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
        g.setColor(SpadesLookAndFeel.BORDER_COLOUR);
        g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        int cardHeight = Card.CARD_PIXEL_HEIGHT;
        int cardWidth = Card.CARD_PIXEL_WIDTH;
        Image img = null;
        int x = 0;
        int y = 0;
        Card[] cards = this.model.getTableCards();
        if (cards[TOP] != null) {
            x = (getWidth() / 2) - (cardWidth / 2);
            y = SPACING;
            cards[TOP].paintComponent(g, x, y);
        }
        if (cards[BOTTOM] != null) {
            x = (getWidth() / 2) - (cardWidth / 2);
            y = (cardHeight) + (SPACING * 2);
            cards[BOTTOM].paintComponent(g, x, y);
        }
        if (cards[LEFT] != null) {
            x = SPACING;
            y = (getHeight() / 2) - (cardWidth / 2);
            cards[LEFT].paintComponent(g, x, y, true);
        }
        if (cards[RIGHT] != null) {
            x = (cardHeight) + (cardWidth) + (SPACING * 3);
            y = (getHeight() / 2) - (cardWidth / 2);
            cards[RIGHT].paintComponent(g, x, y, true);
        }
    }
}
