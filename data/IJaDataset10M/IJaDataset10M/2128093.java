package bo.solitario.locator;

import java.util.Vector;
import bo.solitario.card.Card;
import bo.solitario.card.Set;
import bo.solitario.position.CardPosition;

public class GameLocator implements AbstractLocator {

    private Set set;

    public GameLocator() {
        set = new Set();
    }

    public boolean remove(CardPosition position, Card card) {
        return false;
    }

    public boolean allocate(CardPosition position, Card card) {
        Vector vector = position.getVectorCard();
        if (vector.isEmpty() && card.getName().equals(Set.card_K)) {
            vector.addElement(card);
            return true;
        }
        Card previo = null;
        if (!vector.isEmpty()) {
            previo = (Card) vector.lastElement();
        }
        if (previo == null || !previo.isOpen()) {
            return false;
        }
        String palo = previo.getPalo();
        String opt = "";
        if (palo.equals(Set.SPADE_CARD) || palo.equals(Set.TREBOL_CARD)) {
            opt = "BLACK";
        } else if (palo.equals(Set.HEART_CARD) || palo.equals(Set.DIAMOND_CARD)) {
            opt = "RED";
        }
        String paloCard = card.getPalo();
        String optCard = "";
        if (paloCard.equals(Set.SPADE_CARD) || paloCard.equals(Set.TREBOL_CARD)) {
            optCard = "RED";
        } else if (paloCard.equals(Set.HEART_CARD) || paloCard.equals(Set.DIAMOND_CARD)) {
            optCard = "BLACK";
        }
        if (!opt.equals(optCard)) {
            return false;
        }
        String prv = getPrevio(previo.getName());
        if (prv != null && prv.equals(card.getName())) {
            vector.addElement(card);
            return true;
        } else {
            return false;
        }
    }

    public String getPrevio(String name) {
        int i = 0;
        String array[] = set.getContent();
        int res = -1;
        boolean key = true;
        while (i < array.length && key) {
            if (array[i].equals(name)) {
                res = i;
                key = false;
            }
            i++;
        }
        String response = "";
        if (res == 0 || key) {
            response = null;
        } else {
            response = array[res - 1];
        }
        return response;
    }

    public void restart() {
        set.reset();
    }
}
