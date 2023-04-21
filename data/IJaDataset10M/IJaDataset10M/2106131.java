package Modifiers.DynamicGameModifiers;

import EventServer.GameEvent;
import EventServer.PlayerActionConstants;
import RulesServer.Phaser;
import Logging.Logger;
import java.util.Map;
import CardServer.Card;
import CardServer.CardWorker;

public class CancelPlayerCardPriceModifier extends AbstractDynamicGameModifier {

    private int cardType;

    private String cardAttrib;

    public CancelPlayerCardPriceModifier(Boolean oneTurn, Boolean runCreate, Map<String, String> param) {
        super(oneTurn, runCreate, null, null);
        if (param.get("card") == null) {
            param.put("card", "affair");
        }
        if (param.get("attrib") == null) {
            param.put("attrib", "none");
        }
        String type = param.get("card");
        if (type.equals("bomb")) {
            cardType = Card.BOMB;
        } else if (type.equals("krip")) {
            cardType = Card.KRIP;
        } else if (type.equals("affair")) {
            cardType = Card.AFFAIR;
        } else if (type.equals("alteration")) {
            cardType = Card.ALTERATION;
        } else if (type.equals("condition")) {
            cardType = Card.CONDITION;
        }
        cardAttrib = param.get("attrib");
        addTargetEvent(PlayerActionConstants.PA_PLAYER_PAY_PRICE);
    }

    public Boolean testPAction(GameEvent ge) {
        Card testCard = ge.getGameCardEvent().getEventCard();
        return !(testCard.getCardType() == cardType && testCard.haveAttrib(cardAttrib));
    }
}
