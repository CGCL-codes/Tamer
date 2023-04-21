package CardServer.DeckRules;

import CardServer.Card;
import java.util.Vector;
import Logging.Messages;

public class OneHeroRule implements IDeckRule {

    public String checkRule(Vector<Card> deck) {
        int heroCounter = 0;
        String returnMessage;
        for (Card crd : deck) {
            if (crd.getCardType() == Card.HERO) {
                heroCounter++;
            }
        }
        if (heroCounter != 1) {
            returnMessage = Messages.OneHeroMessage;
        } else {
            returnMessage = null;
        }
        return returnMessage;
    }
}
