package tractorlk.drops;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.awt.image.ImageObserver;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import tractorlk.Drawable;

/**
 * Models the behavior of falling drops of fluid, the cards they fall on, and
 * the fluid catchers.
 * 
 * @author adamd
 */
public class Board extends MouseAdapter implements MouseMotionListener {

    int mode;

    Card southDrew;

    Card eastDrew;

    Card northDrew;

    Card westDrew;

    boolean toggled = false;

    boolean downed = false;

    int northRank;

    int southRank;

    int eastRank;

    int westRank;

    int cardsDeclared;

    int declarer;

    int suitDeclared;

    int declaredID;

    Dimension size;

    List<Card> cards;

    List<Card> cardsPlay;

    Hand selected;

    Image image;

    Image cardBack;

    Deck theDeck;

    CardComparator comparer;

    Hand north;

    Hand south;

    Hand east;

    Hand west;

    Hand northPlay;

    Hand southPlay;

    Hand eastPlay;

    Hand westPlay;

    Hand kitty;

    int singleIndex;

    Hand highCards;

    int rank;

    int suit;

    int turn;

    int lead;

    int winner;

    int plays;

    Card maxCard;

    int handType;

    int suitType;

    boolean sModified;

    boolean debug = true;

    int nsScore;

    int ewScore;

    Card selectedCard;

    public Board(Dimension size) {
        super();
        this.size = size;
        theDeck = new Deck();
        this.rank = 2;
        this.suit = 2;
        this.newGame();
        this.plays = 0;
        sModified = true;
        maxCard = new Card(-1);
        java.net.URL imgURL = getClass().getResource("/deck/redback.png");
        ImageIcon icon = new ImageIcon(imgURL);
        this.cardBack = icon.getImage();
    }

    public void resize(Dimension size) {
        this.size = size;
    }

    public void draw(Graphics g) {
        g.setColor(new Color(13, 136, 2));
        g.clearRect(0, 0, size.width, size.height);
        g.fillRect(0, 0, size.width, size.height);
        g.setColor(Color.BLUE);
        g.drawRect(size.width / 4, size.height / 6, size.width / 2, size.height / 2);
        g.setColor(Color.ORANGE);
        switch(turn) {
            case 1:
                g.fillOval(size.width / 2 - 25, size.height * 6 / 8 - 25, 50, 50);
                break;
            case 2:
                g.fillOval(size.width * 7 / 8 - 25, size.height / 2 - 25, 50, 50);
                break;
            case 3:
                g.fillOval(size.width / 2 - 25, size.height * 1 / 8 - 25, 50, 50);
                break;
            case 4:
                g.fillOval(size.width * 1 / 8 - 25, size.height / 2 - 25, 50, 50);
                break;
        }
        switch(mode) {
            case (0):
                if (singleIndex < 100) g.drawImage(cardBack, size.width / 2 - 34, size.height / 2 - 80, null);
                g.setColor(Color.RED);
                g.fillRect(size.width - 100, 0, 100, 50);
                g.setColor(Color.WHITE);
                g.drawString("Declare", size.width - 80, 25);
                g.setColor(new Color(255, 255, 0));
                g.fillRect(0, 0, 80, 50);
                g.drawImage(image, 0, 0, null);
                g.setColor(new Color(255, 0, 255));
                g.drawString("Pass", 25, 25);
                break;
            case (1):
                break;
            case (2):
                g.setColor(new Color(255, 255, 0));
                g.fillRect(0, 0, 75, 50);
                g.drawImage(image, 0, 0, null);
                g.setColor(new Color(255, 0, 255));
                g.drawString("N/S", 15, 15);
                g.drawString(Integer.toString(nsScore), 15, 40);
                g.drawString("E/W", 40, 15);
                g.drawString(Integer.toString(ewScore), 40, 40);
                break;
        }
        if (sModified) {
            sModified = false;
            selected.clear();
        }
        g.setColor(new Color(255, 0, 255));
        if (toggled) {
            south.updateHand(this.size, g);
            toggled = false;
            southDrew.clicked();
            eastDrew.clicked();
            northDrew.clicked();
            westDrew.clicked();
        }
        if (singleIndex != 0) showHands(g);
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        switch(mode) {
            case (0):
                drawCard(e);
                toggleCard(e);
                declare(e);
                pass(e);
                break;
            case (1):
                break;
            case (2):
                toggleCard(e);
                playCard(e);
                help(e);
                break;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        selectedCard = null;
    }

    public void mouseMoved(MouseEvent e) {
    }

    public Dimension getSize() {
        return size;
    }

    public void setSize(Dimension size) {
        this.size = size;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    private void declare(MouseEvent e) {
        int x = e.getPoint().x;
        int y = e.getPoint().y;
        if (x > size.width - 100 && x < size.width && y > 0 && y < 50) {
            System.out.println("Attempting to declare: " + southDrew);
            if (selected == null || selected.getSize() == 0) {
                if (southDrew.getRank() != southRank) return; else testDeclare(southDrew, null, 1);
            } else {
                if (selected.getSize() > 2) return;
                if (selected.getSize() == 2) {
                    int cardID = selected.getHand().get(0).getID() % 54;
                    if (cardID != selected.getHand().get(1).getID() % 54 || cardID % 13 + 2 != southRank) return; else testDeclare(selected.getHand().get(0), selected.getHand().get(1), 2);
                }
                if (selected.getSize() == 1) {
                    int cardID = selected.getHand().get(0).getID() % 54;
                    if (cardID == southDrew.getID() % 54 && cardID % 13 + 2 == southRank && southDrew.getID() != selected.getHand().get(0).getID()) {
                        testDeclare(southDrew, selected.getHand().get(0), 2);
                        return;
                    }
                    if (cardID % 13 + 2 == southRank) {
                        testDeclare(selected.getHand().get(0), null, 1);
                        return;
                    } else return;
                }
            }
        }
    }

    private void pass(MouseEvent e) {
        int x = e.getPoint().x;
        int y = e.getPoint().y;
        if (x > 0 && x < 80 && y > 0 && y < 50) {
            if (singleIndex >= 100) leaveDraw();
        }
    }

    private void testDeclare(Card c, Card c1, int num) {
        System.out.println("entered testDeclared");
        if (cardsDeclared < num || (c.getID() != declaredID && cardsDeclared == 1 && suitDeclared == c.getSuit())) {
            System.out.println("SUCCESSFUL DECLARE!!: " + c);
            declaredID = c.getID();
            cardsDeclared = cardsDeclared + num;
            if (cardsDeclared > 2) cardsDeclared = 2;
            suitDeclared = c.getSuit();
            System.out.println("cardsDeclared: " + cardsDeclared + " , suitDeclared: " + suitDeclared);
            this.suit = c.getSuit();
            this.rank = c.getRank();
            southDrew.down();
            for (Card d : selected.getHand()) d.down();
            south.play(c, southPlay);
            if (c1 != null) south.play(c1, southPlay);
        }
    }

    private void toggleCard(MouseEvent e) {
        for (Card c : cards) {
            if (c.polygon.contains(e.getPoint())) {
                selectedCard = c;
            }
        }
        if (selectedCard != null) {
            selectedCard.clicked();
            if (selectedCard.getStatus()) selected.add2(selectedCard); else selected.remove(selectedCard);
        }
    }

    private void drawCard(MouseEvent e) {
        int x = e.getPoint().x;
        int y = e.getPoint().y;
        if (x > size.width / 4 && x < size.width * 3 / 4 && y > size.height / 6 && y < size.height * 2 / 3) {
            selected.clear();
            for (Card c : south.getHand()) c.down();
            for (Card c : north.getHand()) c.down();
            for (Card c : west.getHand()) c.down();
            for (Card c : east.getHand()) c.down();
            if (singleIndex < 100) {
                southDrew = theDeck.dealSingle(south, singleIndex++);
                south.sortHand(0, 0);
                eastDrew = theDeck.dealSingle(east, singleIndex++);
                east.sortHand(0, 0);
                northDrew = theDeck.dealSingle(north, singleIndex++);
                north.sortHand(0, 0);
                westDrew = theDeck.dealSingle(west, singleIndex++);
                west.sortHand(0, 0);
                unsetHands();
                toggled = true;
            }
        }
        cards = south.getHand();
    }

    private void leaveDraw() {
        Card temp;
        int playsize = southPlay.getSize();
        for (int i = 0; i < playsize; i++) {
            temp = southPlay.getHand().get(playsize - 1 - i);
            southPlay.play(temp, south);
        }
        this.mode = 2;
        south.unsortHand();
        north.unsortHand();
        west.unsortHand();
        east.unsortHand();
        southPlay.clear();
        northPlay.clear();
        eastPlay.clear();
        westPlay.clear();
        south.sortHand(this.rank, this.suit);
        north.sortHand(this.rank, this.suit);
        east.sortHand(this.rank, this.suit);
        west.sortHand(this.rank, this.suit);
        this.comparer = new CardComparator(this.rank, this.suit);
        for (Card c : selected.getHand()) c.down();
        selected.clear();
        unsetHands();
        cards = south.getHand();
    }

    private void playCard(MouseEvent e) {
        int x = e.getPoint().x;
        int y = e.getPoint().y;
        if (x > size.width / 4 && x < size.width * 3 / 4 && y > size.height / 6 && y < size.height * 2 / 3) {
            if (selected.isEmpty()) return;
            play();
        }
    }

    private void play() {
        if (turn == lead) {
            southPlay = new Hand();
            northPlay = new Hand();
            eastPlay = new Hand();
            westPlay = new Hand();
        }
        if (plays == 0 && !comparer.isLead(selected, highCards)) return;
        int check = 1;
        switch(turn) {
            case (1):
                check = comparer.check(south, selected, handType, suitType, highCards);
                break;
            case (2):
                check = comparer.check(east, selected, handType, suitType, highCards);
                break;
            case (3):
                check = comparer.check(north, selected, handType, suitType, highCards);
                break;
            case (4):
                check = comparer.check(west, selected, handType, suitType, highCards);
                break;
        }
        if (check == -1) return;
        for (Card c : selected.getHand()) {
            switch(turn) {
                case (1):
                    south.play(c, southPlay);
                    break;
                case (2):
                    east.play(c, eastPlay);
                    break;
                case (3):
                    north.play(c, northPlay);
                    break;
                case (4):
                    west.play(c, westPlay);
                    break;
            }
            highCards.remove(c.getID() % 54);
        }
        if (handType == 0) {
            switch(turn) {
                case (1):
                    handType = comparer.handType(southPlay, highCards);
                    suitType = comparer.suitType(southPlay);
                    break;
                case (2):
                    handType = comparer.handType(eastPlay, highCards);
                    suitType = comparer.suitType(eastPlay);
                    break;
                case (3):
                    handType = comparer.handType(northPlay, highCards);
                    suitType = comparer.suitType(northPlay);
                    break;
                case (4):
                    handType = comparer.handType(westPlay, highCards);
                    suitType = comparer.suitType(westPlay);
                    break;
            }
        }
        selected.sortHand(this.rank, this.suit);
        if ((check == 1) && !comparer.winner(maxCard, selected, handType, highCards)) {
            maxCard = new Card(selected.getMaxCard());
            System.out.println("New winning card: " + maxCard);
            winner = turn;
        }
        unsetHands();
        turn++;
        if (turn == 5) turn = 1;
        plays++;
        if (plays == 4) {
            plays = 0;
            lead = winner;
            turn = lead;
            maxCard = new Card(-1);
            int score = comparer.points(northPlay, eastPlay, southPlay, westPlay);
            System.out.println("The trick winner is: " + winner + ", who earned " + score + " points.");
            if (winner == 1 || winner == 3) nsScore = nsScore + score;
            if (winner == 2 || winner == 4) ewScore = ewScore + score;
            handType = 0;
            suitType = 0;
        }
        switch(turn) {
            case (2):
                cards = east.getHand();
                break;
            case (3):
                cards = north.getHand();
                break;
            case (4):
                cards = west.getHand();
                break;
            case (1):
                cards = south.getHand();
                break;
        }
        sModified = true;
    }

    private void unsetHands() {
        south.unset();
        southPlay.unset();
        north.unset();
        northPlay.unset();
        east.unset();
        eastPlay.unset();
        west.unset();
        westPlay.unset();
        highCards.unset();
    }

    private void showHands(Graphics g) {
        switch(turn) {
            case (1):
                south.showHand(this.size, 1, g);
                break;
            case (2):
                east.showHand(this.size, 1, g);
                break;
            case (3):
                north.showHand(this.size, 1, g);
                break;
            case (4):
                west.showHand(this.size, 1, g);
                break;
        }
        southPlay.showHand(this.size, 5, g);
        eastPlay.showHand(this.size, 6, g);
        northPlay.showHand(this.size, 7, g);
        westPlay.showHand(this.size, 8, g);
    }

    private void newGame() {
        this.mode = 0;
        theDeck.shuffle();
        north = new Hand();
        south = new Hand();
        east = new Hand();
        west = new Hand();
        northPlay = new Hand();
        southPlay = new Hand();
        eastPlay = new Hand();
        westPlay = new Hand();
        singleIndex = 0;
        northRank = 2;
        southRank = 2;
        eastRank = 2;
        westRank = 2;
        cardsDeclared = 0;
        declarer = 0;
        suitDeclared = 0;
        declaredID = -1;
        highCards = new Hand();
        highCards.populateHighCards();
        highCards.sortHand(this.rank, this.suit);
        kitty = new Hand(theDeck, 0);
        selected = new Hand();
        selectedCard = null;
        turn = 1;
        lead = 1;
        winner = 1;
        plays = 0;
        handType = 0;
        suitType = 0;
        nsScore = 0;
        ewScore = 0;
        maxCard = new Card(-1);
    }

    private void help(MouseEvent e) {
        int x = e.getPoint().x;
        int y = e.getPoint().y;
        if (x > 50 || y > 50) return;
        System.out.println("****************************");
        System.out.println("Trump rank: " + this.rank + " Trump suit: " + this.suit);
        System.out.println("Turn: " + turn);
        System.out.println("Lead: " + lead);
        System.out.println("Winner: " + winner);
        System.out.println("Max Card: " + maxCard);
        System.out.println("Plays: " + plays);
        System.out.println("Hand Type: " + handType);
        System.out.println("Suit Type: " + suitType);
        System.out.println("N/S Score: " + nsScore);
        System.out.println("E/W Score: " + ewScore);
        System.out.println("****************************");
    }

    public void restart() {
        newGame();
    }
}
