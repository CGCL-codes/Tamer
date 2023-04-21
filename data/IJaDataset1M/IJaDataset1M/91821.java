package Scenario;

import CardServer.Card;
import EventServer.CardEvent;
import EventServer.GameEvent;
import EventServer.PlayerActionConstants;
import GameUI.CommandDialog;
import RulesServer.GameEventServer;
import RulesServer.Phaser;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: shmalikov.sergey
 * Date: 21.06.2007
 * Time: 15:42:09
 */
public class GetKripsFromHandActor extends AbstractSimpleActor {

    int cardOwner;

    public GetKripsFromHandActor(int owner) {
        super();
        cardOwner = owner;
    }

    public Boolean playRole() {
        super.logMe();
        Boolean res;
        int kripsNum;
        Vector<Card> krips, playKrips = new Vector<Card>();
        Director getCardsDir = new Director("($Select cards<krip> in Actor)");
        if (cardOwner == Card.OWNER_PLAYER) {
            kripsNum = GameEventServer.getInstance().getPlayerGetKripsNum();
            krips = GameEventServer.getInstance().getPlayerHand().getCardsOfType(Card.KRIP);
            res = GameEventServer.getInstance().checkPermissions(new GameEvent(PlayerActionConstants.PA_PLAYER_GET_KRIPS_FROM_HAND, null));
        } else {
            kripsNum = GameEventServer.getInstance().getEnemyGetKripsNum();
            krips = GameEventServer.getInstance().getEnemyHand().getCardsOfType(Card.KRIP);
            res = GameEventServer.getInstance().checkPermissions(new GameEvent(PlayerActionConstants.PA_ENEMY_GET_KRIPS_FROM_HAND, null));
        }
        for (Card card : krips) {
            Boolean res2 = GameEventServer.getInstance().checkPermissions(new GameEvent(PlayerActionConstants.PLAY_KRIP, new CardEvent(card, 0)));
            if (card.getOwner() == Card.OWNER_PLAYER) {
                res2 &= GameEventServer.getInstance().checkDynamicPermissions(new GameEvent(PlayerActionConstants.PA_PLAYER_PLAY_KRIP, new CardEvent(card, 0)));
            } else {
                res2 &= GameEventServer.getInstance().checkDynamicPermissions(new GameEvent(PlayerActionConstants.PA_ENEMY_PLAY_KRIP, new CardEvent(card, 0)));
            }
            if (res && res2) {
                playKrips.add(card);
            }
        }
        Phaser.getInstance().setCurrentMode(Phaser.SELECT_MODE);
        if (res) {
            if (cardOwner == Card.OWNER_PLAYER) {
                this.endRole = true;
                getCardsDir.addActor(new GetCardsFromPlayerHandActor(playKrips, 1, false, 0));
            } else {
                this.endRole = true;
                getCardsDir.addActor(new GetCardsFromEnemyHandActor(playKrips, 1, false, 0));
            }
            Phaser.getInstance().getGameField().getCommandDialog().setMode(CommandDialog.END_CHOOSE_MODE);
            getCardsDir.playScenario();
        } else {
            return true;
        }
        return true;
    }
}
