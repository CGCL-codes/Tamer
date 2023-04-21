package cribbage.data;

import java.util.Comparator;
import com.threerings.parlor.card.data.Card;

public class CardComparator implements Comparator {

    public int compare(Object card1, Object card2) {
        return ((Card) card1).getNumber() - ((Card) card2).getNumber();
    }
}
