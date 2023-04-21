package cards1;

import java.io.*;
import java.util.*;

public class CardGroup {

    CardGroup() {
        cardsInGroup = new ArrayList<Card>(13);
    }

    CardGroup(int capacity) {
        cardsInGroup = new ArrayList<Card>(capacity);
    }

    CardGroup(Collection<Card> givenCards) {
        cardsInGroup = new ArrayList<Card>(givenCards);
    }

    CardGroup(Collection<Card> givenCards, String givenName) {
        this(givenCards);
        groupName = givenName;
    }

    public void addCard(Card newCard) {
        if (!cardsInGroup.contains(newCard)) cardsInGroup.add(newCard);
    }

    public void addCard(int position, Card newCard) {
        if (!cardsInGroup.contains(newCard)) cardsInGroup.add(position, newCard);
    }

    public boolean containsCard(Card givenCard) {
        return cardsInGroup.contains(givenCard);
    }

    public Card getCard(int j) {
        if (j >= 0 && j < cardsInGroup.size()) return cardsInGroup.get(j); else return null;
    }

    public Iterator<Card> getIterator() {
        return cardsInGroup.iterator();
    }

    public String getName() {
        return groupName;
    }

    public CardAgent1 getAgent() {
        return groupAgent;
    }

    public boolean removeCard(Card givenCard) {
        if (cardsInGroup.contains(givenCard)) {
            cardsInGroup.remove(givenCard);
            return true;
        }
        return false;
    }

    public void setCard(int j, Card newValue) {
        if (j >= 0 && j < cardsInGroup.size()) cardsInGroup.set(j, newValue);
    }

    public void setName(String newName) {
        groupName = newName;
    }

    public void setAgent(CardAgent1 newAgent) {
        groupAgent = newAgent;
    }

    public int size() {
        return cardsInGroup.size();
    }

    public void shuffle() {
        Random generator = new Random();
        int nCards = this.size();
        int n = nCards;
        Card selectedCard = null;
        while (n > 1) {
            int positionSelected = generator.nextInt(n);
            --n;
            if (positionSelected < n) {
                selectedCard = cardsInGroup.get(positionSelected);
                cardsInGroup.set(positionSelected, cardsInGroup.get(n));
                cardsInGroup.set(n, selectedCard);
            }
        }
    }

    ArrayList<Card> cardsInGroup = null;

    private String groupName = null;

    private CardAgent1 groupAgent = null;
}
