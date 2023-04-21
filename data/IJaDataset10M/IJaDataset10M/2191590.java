package com.rlsoftwares.games.card;

import com.rlsoftwares.util.ResourceUtils;
import com.rlsoftwares.virtualdeck.config.VirtualDeckConfig;
import com.rlsoftwares.visual.DragableLabel;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.util.Vector;

/**
 *
 * @author rdcl
 */
public class PlayerCards extends DragableLabel {

    public static Integer V_OFFSET = 25;

    public static Integer H_OFFSET = 25;

    private CardSet cardSet;

    private Vector<Card> cards = new Vector();

    private boolean turnedUp = true;

    private Orientation orientation = Orientation.RIGHT;

    /**
     * Creates a new instance of PlayerCards
     */
    public PlayerCards(CardSet cs) {
        super();
        this.setCardSet(cs);
        this.setSize(cs.getCardWidth(), cs.getCardHeight());
        this.setOpaque(true);
        this.setIcon(cs.getEmptyBackground());
    }

    public Vector<Card> getCards() {
        return cards;
    }

    public void setCards(Vector<Card> cards) {
        this.cards = cards;
    }

    private void fixHandlerLocation(int numCardsAdded) {
        PlayerCardsHandler pch = (PlayerCardsHandler) getParent();
        if (getOrientation().equals(Orientation.LEFT)) {
            pch.setLocation(pch.getX() - numCardsAdded * H_OFFSET, pch.getY());
        } else if (this.getOrientation().equals(Orientation.UP)) {
            pch.setLocation(pch.getX(), pch.getY() - numCardsAdded * V_OFFSET);
        }
    }

    private int getNewIndexPosition(int x, int y, int w, int h) {
        int newIndex = 0;
        PlayerCardsHandler pch = (PlayerCardsHandler) getParent();
        if (getOrientation().equals(Orientation.RIGHT)) {
            if (x > getX()) {
                newIndex = (x - getX()) / H_OFFSET + 1;
            }
        } else if (getOrientation().equals(Orientation.LEFT)) {
            if (x > getX() && x + w < getX() + getWidth()) {
                newIndex = (getX() + getWidth() - x - w) / H_OFFSET + 1;
            } else if (x + w >= getX() + getWidth()) {
                newIndex = 0;
            } else {
                newIndex = getCards().size();
            }
        } else if (this.getOrientation().equals(Orientation.DOWN)) {
            if (y > getY()) {
                newIndex = (y - getY()) / V_OFFSET + 1;
            }
        } else if (this.getOrientation().equals(Orientation.UP)) {
            if (y > getY() && y + h < getY() + getHeight()) {
                newIndex = (getY() + getHeight() - y - h) / V_OFFSET + 1;
            } else if (y + h >= getY() + getHeight()) {
                newIndex = 0;
            } else {
                newIndex = getCards().size();
            }
        }
        return (newIndex > getCards().size()) ? getCards().size() : newIndex;
    }

    public void resize() {
        int newWidth = getCardSet().getCardWidth();
        int newHeight = getCardSet().getCardHeight();
        if (getCards().size() != 0) {
            if (getOrientation().equals(Orientation.RIGHT) || getOrientation().equals(Orientation.LEFT)) {
                newWidth = (getCards().size() - 1) * H_OFFSET + getCardSet().getCardWidth();
            } else if (getOrientation().equals(Orientation.DOWN) || getOrientation().equals(Orientation.UP)) {
                newHeight = (getCards().size() - 1) * V_OFFSET + getCardSet().getCardHeight();
            }
        }
        this.setSize(newWidth, newHeight);
        this.getParent().setSize(newWidth + 2 * Handler.BORDER_WIDTH, newHeight + 2 * Handler.BORDER_WIDTH);
    }

    public void put(PlayerCards ch) {
        int index = 0;
        for (Card c : ch.getCards()) {
            c.getParent().remove(c);
            this.getParent().add(c);
        }
        if (!ch.getCards().isEmpty()) {
            fixHandlerLocation(getCards().isEmpty() ? ch.getCards().size() - 1 : ch.getCards().size());
            this.getCards().addAll(index, ch.getCards());
            ch.getCards().clear();
            this.redrawCards();
        }
    }

    public void put(Card newCard) {
        Container handler = getParent();
        if (this.getCards().isEmpty()) {
            this.getCards().add(newCard);
            newCard.setLocation(this.getX(), this.getY());
        } else {
            int index = this.getNewIndexPosition(newCard.getX() - handler.getX(), newCard.getY() - handler.getY(), getCardSet().getCardWidth(), getCardSet().getCardHeight());
            fixHandlerLocation(1);
            this.getCards().add(index, newCard);
        }
        Container c = newCard.getParent();
        getParent().add(newCard);
        c.repaint();
        redrawCards();
    }

    public void checkViewPermission() {
        boolean insideOpponentArea = getTable().insideOpponentArea(getParent().getX(), getParent().getY(), getParent().getWidth(), getParent().getHeight());
        for (Card card : getCards()) {
            if (insideOpponentArea) {
                card.setTurnedUp(isTurnedUp(), false);
                card.setIcon(card.getBackFace());
            } else {
                card.setTurnedUp(isTurnedUp());
            }
        }
    }

    public void redrawCards() {
        int position = 0;
        bringToFront();
        resize();
        checkViewPermission();
        for (Card card : getCards()) {
            card.bringToFront();
            if (getOrientation().equals(Orientation.RIGHT)) {
                card.setLocation(getX() + (position * H_OFFSET), getY());
            } else if (getOrientation().equals(Orientation.LEFT)) {
                card.setLocation(getX() + getWidth() - card.getWidth() - (position * H_OFFSET), getY());
            } else if (getOrientation().equals(Orientation.UP)) {
                card.setLocation(getX(), getY() + getHeight() - card.getHeight() - (position * V_OFFSET));
            } else if (getOrientation().equals(Orientation.DOWN)) {
                card.setLocation(getX(), getY() + (position * V_OFFSET));
            }
            position++;
        }
        getPlayerCardsHandler().updateButtonsLocation();
        getPlayerCardsHandler().getButtonFreeHandler().setVisible(getCards().isEmpty() && getTable().canShowPrivateButtons(getPlayerCardsHandler()));
    }

    public boolean accept(Component c) {
        return (c instanceof PlayerCards || c instanceof Card);
    }

    public void join(Component c) {
        if (this.accept(c)) {
            if (c instanceof PlayerCards) {
                this.put((PlayerCards) c);
            }
            if (c instanceof Card) {
                this.put((Card) c);
            }
        }
    }

    public boolean isTurnedUp() {
        return turnedUp;
    }

    public void setTurnedUp(boolean turnedUp) {
        this.turnedUp = turnedUp;
        for (Card c : this.getCards()) {
            c.setTurnedUp(this.isTurnedUp());
        }
    }

    public CardSet getCardSet() {
        return cardSet;
    }

    public void setCardSet(CardSet cardSet) {
        this.cardSet = cardSet;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    private boolean canChangeOrientation(Orientation orientation, Point newLocation) {
        Dimension oldSize = new Dimension(getPlayerCardsHandler().getWidth(), getPlayerCardsHandler().getHeight());
        Dimension newSize = new Dimension(oldSize);
        if (((getOrientation().equals(Orientation.DOWN) || getOrientation().equals(Orientation.UP)) && (orientation.equals(Orientation.LEFT) || orientation.equals(Orientation.RIGHT))) || ((getOrientation().equals(Orientation.RIGHT) || getOrientation().equals(Orientation.LEFT)) && (orientation.equals(Orientation.UP) || orientation.equals(Orientation.DOWN)))) {
            newSize.setSize(oldSize.height, oldSize.width);
        }
        if (getTable().isMyTime()) {
            if (getTable().intersectOpponentArea(newLocation.x, newLocation.y, newSize.width, newSize.height)) {
                getTable().getActionListener().showMessage(ResourceUtils.getMessage(VirtualDeckConfig.TEXT_RESOURCE_PATH, "there_is_no_space_to_expand_cards_in_this_direction"), MessageType.WARN);
                return false;
            } else {
                return true;
            }
        } else {
            return getTable().getMyPrivateArea().contains(newLocation.x, newLocation.y, newSize.width, newSize.height);
        }
    }

    private Point getNewLocation(Orientation orientation) {
        Dimension oldSize = new Dimension(getPlayerCardsHandler().getWidth(), getPlayerCardsHandler().getHeight());
        Point oldLocation = new Point(getPlayerCardsHandler().getLocation());
        Point newLocation = new Point(oldLocation);
        int emptySize = getPlayerCardsHandler().BORDER_WIDTH * 2 + getCardSet().getCardWidth();
        if (getOrientation().equals(Orientation.LEFT)) {
            if (orientation.equals(Orientation.RIGHT) || orientation.equals(Orientation.DOWN)) {
                newLocation.setLocation(oldLocation.x + oldSize.width - emptySize, oldLocation.y);
            } else if (orientation.equals(Orientation.UP)) {
                newLocation.setLocation(oldLocation.x + oldSize.width - emptySize, oldLocation.y + emptySize - oldSize.width);
            }
        } else if (getOrientation().equals(Orientation.RIGHT)) {
            if (orientation.equals(Orientation.LEFT)) {
                newLocation.setLocation(oldLocation.x + emptySize - oldSize.width, oldLocation.y);
            } else if (orientation.equals(Orientation.UP)) {
                newLocation.setLocation(oldLocation.x, oldLocation.y + emptySize - oldSize.width);
            } else if (orientation.equals(Orientation.DOWN)) {
                newLocation.setLocation(oldLocation.x, oldLocation.y);
            }
        } else if (getOrientation().equals(Orientation.UP)) {
            if (orientation.equals(Orientation.LEFT)) {
                newLocation.setLocation(oldLocation.x + emptySize - oldSize.height, oldLocation.y + oldSize.height - emptySize);
            } else if (orientation.equals(Orientation.RIGHT) || orientation.equals(Orientation.DOWN)) {
                newLocation.setLocation(oldLocation.x, oldLocation.y + oldSize.height - emptySize);
            }
        } else if (getOrientation().equals(Orientation.DOWN)) {
            if (orientation.equals(Orientation.LEFT)) {
                newLocation.setLocation(oldLocation.x + emptySize - oldSize.height, oldLocation.y);
            } else if (orientation.equals(Orientation.RIGHT)) {
                newLocation.setLocation(oldLocation.x, oldLocation.y);
            } else if (orientation.equals(Orientation.UP)) {
                newLocation.setLocation(oldLocation.x, oldLocation.y + emptySize - oldSize.height);
            }
        }
        return newLocation;
    }

    public void forceChangeOrientation(Orientation orientation) {
        getPlayerCardsHandler().setLocation(getNewLocation(orientation));
        this.orientation = orientation;
        redrawCards();
    }

    public boolean changeOrientation(Orientation orientation) {
        Point location = getNewLocation(orientation);
        if (canChangeOrientation(orientation, location)) {
            getPlayerCardsHandler().setLocation(location);
            this.orientation = orientation;
            redrawCards();
            return true;
        } else {
            return false;
        }
    }

    private PlayerCardsHandler getPlayerCardsHandler() {
        return (PlayerCardsHandler) getParent();
    }

    private Table getTable() {
        return (Table) getParent().getParent();
    }
}
