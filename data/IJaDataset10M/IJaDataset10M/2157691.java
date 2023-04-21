package tractor.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;
import tractor.lib.Card;

public class PlayerHand {

    private List<Card> cards;

    private TreeSet<Card> pairs;

    private TreeSet<Card> triples;

    private TreeSet<Tractor> tractors;

    private TreeSet<Tractor> mixed;

    private List<Card> currentPlay;

    private CardComparator cc;

    public PlayerHand() {
        this.cards = Collections.synchronizedList(new ArrayList<Card>());
        this.currentPlay = Collections.emptyList();
    }

    public PlayerHand(PlayerHand hand) {
        this.cards = Collections.synchronizedList(new ArrayList<Card>(hand.getCards()));
        this.currentPlay = Collections.emptyList();
    }

    @SuppressWarnings("unchecked")
    public void init(TreeSet<Card> pairs, TreeSet<Card> triples, TreeSet<Tractor> tractors, TreeSet<Tractor> mixed) {
        this.pairs = pairs;
        this.triples = triples;
        this.tractors = tractors;
        this.mixed = mixed;
        this.cc = ((CardComparator) this.pairs.comparator());
    }

    public TreeSet<Tractor> getTractors() {
        return this.tractors;
    }

    public TreeSet<Tractor> getMixedTractors() {
        return this.mixed;
    }

    public TreeSet<Card> getPairs() {
        return this.pairs;
    }

    public TreeSet<Card> getTriples() {
        return this.triples;
    }

    public int getNumSuit(int suit, int trumpnumber) {
        int numsuit = 0;
        for (Card card : this.cards) {
            if (card.getSuit() == suit && card.getNumber() != trumpnumber) numsuit++;
        }
        return numsuit;
    }

    public int getNumTrump(int trumpsuit, int trumpnumber) {
        int trump = 0;
        for (Card card : this.cards) {
            if (card.getSuit() == trumpsuit || card.getSuit() == Card.TRUMP || card.getNumber() == trumpnumber) trump++;
        }
        return trump;
    }

    /**It gives the frequency that the card appears in the players hand.
	 * @param card
	 * @return
	 */
    public int frequency(Card card) {
        return Collections.frequency(this.cards, card);
    }

    /** It checks whether the player's hand contains a particular card.
	 * @param card
	 * @return
	 */
    public boolean contains(Card card) {
        return this.cards.contains(card);
    }

    public boolean contains(Collection<Card> card) {
        return this.cards.containsAll(card);
    }

    public Iterator<Card> iterator() {
        return this.cards.iterator();
    }

    public void sort(Comparator<Card> comparator) {
        Collections.sort(this.cards, comparator);
    }

    /** It returns the Player's cards.
	 * @return
	 */
    public List<Card> getCards() {
        return this.cards;
    }

    /** It adds a card to the hand.
	 * @param card
	 */
    public void addCard(Card card) {
        this.cards.add(card);
    }

    /** It removes the card from the players hand.
	 * @param card
	 */
    public void removeCard(Card card) {
        synchronized (this) {
            LinkedList<Tractor> postadd = new LinkedList<Tractor>();
            if (this.triples.contains(card)) {
                this.triples.remove(card);
                this.pairs.add(card);
                for (Iterator<Tractor> i = this.tractors.iterator(); i.hasNext(); ) {
                    Tractor tractor = i.next();
                    int index = tractor.getCards().indexOf(card);
                    if (index > -1) {
                        if (tractor.getType() == 3) {
                            tractor.tripleToPair();
                            if (tractor.getLength() > 2) {
                                Iterator<Card> it = tractor.getCards().iterator();
                                Card current = it.next();
                                if (current == card) current = it.next();
                                while (it.hasNext()) {
                                    Card previous;
                                    List<Card> tractorcards = new LinkedList<Card>();
                                    do {
                                        tractorcards.add(current);
                                        previous = current;
                                        current = it.next();
                                        if (current == card) {
                                            if (it.hasNext()) {
                                                current = it.next();
                                            } else {
                                                current = null;
                                                break;
                                            }
                                        }
                                    } while (it.hasNext() && this.cc.gameCompare(current, previous) == 1);
                                    if (this.cc.gameCompare(current, previous) == 1) tractorcards.add(current);
                                    if (tractorcards.size() >= 2) {
                                        postadd.add(new Tractor(3, tractorcards));
                                    }
                                    tractorcards.clear();
                                }
                            }
                        }
                    }
                }
                for (Iterator<Tractor> i = postadd.iterator(); i.hasNext(); ) {
                    tractors.add(i.next());
                    i.remove();
                }
            } else if (this.pairs.contains(card)) {
                this.pairs.remove(card);
                for (Iterator<Tractor> i = this.tractors.iterator(); i.hasNext(); ) {
                    Tractor tractor = null;
                    try {
                        tractor = i.next();
                    } catch (ConcurrentModificationException e) {
                        System.out.println("shit @PlayerHand.188");
                        e.printStackTrace(System.out);
                        break;
                    }
                    int index = tractor.getCards().indexOf(card);
                    if (index > -1) {
                        if (tractor.getType() == 2) {
                            i.remove();
                            if (tractor.getLength() > 2) {
                                Iterator<Card> it = tractor.getCards().iterator();
                                Card current = it.next();
                                if (current == card) current = it.next();
                                while (it.hasNext()) {
                                    Card previous;
                                    List<Card> tractorcards = new LinkedList<Card>();
                                    do {
                                        tractorcards.add(current);
                                        previous = current;
                                        current = it.next();
                                        if (current == card) {
                                            if (it.hasNext()) {
                                                current = it.next();
                                            } else {
                                                current = null;
                                                break;
                                            }
                                        }
                                    } while (it.hasNext() && this.cc.gameCompare(current, previous) == 1);
                                    if (this.cc.gameCompare(current, previous) == 1) tractorcards.add(current);
                                    if (tractorcards.size() >= 2) {
                                        postadd.add(new Tractor(2, tractorcards));
                                    }
                                    tractorcards.clear();
                                }
                            }
                        }
                    }
                }
                for (Iterator<Tractor> i = postadd.iterator(); i.hasNext(); ) {
                    tractors.add(i.next());
                    i.remove();
                }
                for (Iterator<Tractor> i = this.mixed.iterator(); i.hasNext(); ) {
                    Tractor tractor = i.next();
                    int index = tractor.getCards().indexOf(card);
                    if (index > -1) {
                        if (tractor.getType() == 2) {
                            i.remove();
                            if (tractor.getLength() > 2) {
                                Iterator<Card> it = tractor.getCards().iterator();
                                Card current = it.next();
                                if (current == card) current = it.next();
                                while (it.hasNext()) {
                                    Card previous;
                                    List<Card> tractorcards = new LinkedList<Card>();
                                    do {
                                        tractorcards.add(current);
                                        previous = current;
                                        current = it.next();
                                        if (current == card) {
                                            if (it.hasNext()) {
                                                current = it.next();
                                            } else {
                                                current = null;
                                                break;
                                            }
                                        }
                                    } while (it.hasNext() && this.cc.gameCompare(current, previous) == 1);
                                    if (this.cc.gameCompare(current, previous) == 1) tractorcards.add(current);
                                    if (tractorcards.size() >= 2) {
                                        tractors.add(new Tractor(2, tractorcards));
                                    }
                                    tractorcards.clear();
                                }
                            }
                        }
                    }
                }
            }
            this.cards.remove(card);
        }
    }

    public void removeAllCards(Collection<Card> cards) {
        for (Card card : cards) {
            this.removeCard(card);
        }
    }

    /** It sets the current play.
	 * @param play
	 */
    public void setCurrentPlay(List<Card> play) {
        try {
            this.currentPlay = Collections.synchronizedList(play);
        } catch (NullPointerException e) {
            this.currentPlay = Collections.emptyList();
        }
    }

    /** It gets the current play in the players hand.
	 * @return
	 */
    public List<Card> getCurrentPlay() {
        return this.currentPlay;
    }

    public String toString() {
        return "Cards: " + cards.toString() + "\nPairs: " + this.getPairs().toString() + "\nTriples: " + this.getTriples().toString() + "\nTractors: " + this.getTractors().toString();
    }
}
