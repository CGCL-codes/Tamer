package FourRowSolitaire;

import java.awt.Point;

/**
 * Class: SingleCell
 *
 * Description: The SingleCell class manages an individual cell that can only hold one card.
 *
 * @author Matt Stephen
 */
public class SingleCell extends CardStack {

    public SingleCell() {
    }

    public Card push(Card card) {
        if (isEmpty()) {
            super.push(card);
            return card;
        }
        return null;
    }

    public Card getCardAtLocation(Point p) {
        return peek();
    }

    public boolean isValidMove(Card card) {
        if (isEmpty()) {
            return true;
        }
        return false;
    }

    public boolean isValidMove(CardStack stack) {
        return false;
    }
}
