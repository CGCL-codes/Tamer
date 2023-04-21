package meraner81.wattn.player;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import meraner81.wattn.core.WattCard;

public class WattPlayer {

    private String name;

    private List<WattCard> myCards;

    public WattPlayer(String name) {
        this.name = name;
        this.myCards = new ArrayList<WattCard>();
    }

    public void receiveCard(WattCard card) {
        this.myCards.add(card);
    }

    public int getNumberOfCards() {
        return this.myCards.size();
    }

    public boolean playCard(WattCard card) {
        return myCards.remove(card);
    }

    public int getCardCount() {
        return myCards.size();
    }

    public WattCard getCardAt(int index) {
        if (index < myCards.size()) {
            return myCards.get(index);
        } else {
            return null;
        }
    }

    public String getName() {
        return this.name;
    }

    public void print(PrintStream out) {
        out.print(name);
        out.print(" has: ");
        WattCard card;
        for (int i = 0; i < myCards.size(); i++) {
            card = myCards.get(i);
            out.print(card.getDerivedName());
            if (i < myCards.size() - 1) {
                out.print(", ");
            }
        }
        out.println(" in hand.");
    }
}
