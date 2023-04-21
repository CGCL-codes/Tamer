package gameEngine.baseElement;

import java.io.Serializable;
import java.util.Random;

public class GameItemDeck extends GameComposite implements Serializable {

    public GameItemDeck(String family) {
        super(family);
    }

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public int getDeskSize() {
        if (components == null) return -1;
        return components.size();
    }

    public void shuffle() {
        int deckSize = getDeskSize();
        GameComponent tempItem;
        Random rand = new Random();
        if (deckSize > 1) {
            for (int i = 0; i < deckSize; i++) {
                int swap = Math.abs(rand.nextInt() % deckSize);
                tempItem = components.get(swap);
                components.set(swap, components.get(i));
                components.set(i, tempItem);
            }
        }
    }

    private GameComposite getCards(int howMany, boolean removeCard) {
        GameComposite cardList = new GameComposite(getFamilyName());
        if (howMany <= 0) return null;
        if (howMany > getDeskSize()) howMany = getDeskSize();
        if (removeCard) {
            for (int i = 0; i < howMany; i++) {
                cardList.addComponent(components.get(0));
                components.remove(0);
            }
        } else {
            for (int i = 0; i < howMany; i++) {
                cardList.addComponent(components.get(i));
            }
        }
        return cardList;
    }

    public GameComposite drawOneCard() {
        return getCards(1, true);
    }

    public GameComposite drawCards(int howMany) {
        return getCards(howMany, true);
    }

    public GameComposite listOneCard() {
        return getCards(1, false);
    }

    public GameComposite listCards(int howMany) {
        return getCards(howMany, false);
    }

    public GameComposite listAllCards() {
        return getCards(getDeskSize(), false);
    }
}
