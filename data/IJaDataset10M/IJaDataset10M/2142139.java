package Scenario;

import CardServer.Card;
import EventServer.GFEvent;
import EventServer.GameFieldEventConstants;
import EventServer.IGameFieldEventListener;
import GameUI.CommandDialog;
import GameUI.NewGameField;
import Logging.Logger;
import RulesServer.GameEventServer;
import RulesServer.Phaser;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: shmalikov.sergey
 * Date: 21.06.2007
 * Time: 15:24:25
 */
public class GetCardsFromPlayerHandActor extends AbstractSimpleActor implements IGameFieldEventListener {

    private Vector<Card> cards;

    private int num;

    private Boolean mond;

    private Boolean gfQuery = false;

    private int mode;

    private Boolean cardsSelected = false;

    public GetCardsFromPlayerHandActor(Vector<Card> cards, int num, Boolean mond, int mode) {
        super();
        this.cards = cards;
        this.num = num;
        this.mond = mond;
        this.mode = mode;
        this.cardsSelected = false;
    }

    public Boolean playRole() {
        super.logMe();
        Phaser.getInstance().getGameField().setSelectedCards(new Vector<Card>());
        if (!gfQuery) {
            if (Director.dirDebug) Logger.debug(0, "Running GET CARDS FROM PLAYER HAND IN " + actorDirector.toString(), null);
            Phaser.getInstance().getGameField().setSelectMode(mode);
            Phaser.getInstance().getGameField().getCommandDialog().setMode(CommandDialog.UNDO_MODE);
            Phaser.getInstance().getGameField().addGfEventListener(this);
            cardsSelected = false;
            Phaser.getInstance().getGameField().selectFromPlayerHand(cards, num, mond);
            gfQuery = true;
        }
        while (!cardsSelected) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public void processGameFieldEvent(GFEvent event) {
        switch(event.getType()) {
            case GameFieldEventConstants.CARDS_SELECTED:
                {
                    if (Director.dirDebug) Logger.debug(0, "Actor " + this.toString() + " catch CARD_SELECTED " + Phaser.getInstance().getGameField().getSelectedCards() + " event in " + actorDirector, null);
                    Phaser.getInstance().getGameField().getCommandDialog().setMode(CommandDialog.PHASE_MODE);
                    Vector<Card> cards = new Vector<Card>();
                    cards.addAll(Phaser.getInstance().getGameField().getSelectedCards());
                    GameEventServer.getInstance().setLastSelected(cards);
                    if (cards.size() < num && mond) {
                        GameEventServer.getInstance().runConditionalSubaction(false);
                        GameEventServer.getInstance().setActionCanceled(true);
                    }
                    Phaser.getInstance().getGameField().setStateChanged(true);
                    Phaser.getInstance().getGameField().removeGfEventListener(this);
                    Phaser.getInstance().getGameField().setSelectMode(0);
                    cardsSelected = true;
                    break;
                }
            case GameFieldEventConstants.UNDO:
                {
                    Phaser.getInstance().getGameField().getCommandDialog().setMode(CommandDialog.PHASE_MODE);
                    if (GameEventServer.allDebug) {
                        Logger.debug("-GF- Running conditional subaction....", this);
                    }
                    Phaser.getInstance().setCurrentMode(Phaser.SELECT_MODE);
                    Phaser.getInstance().getGameField().removeGfEventListener(this);
                    GameEventServer.getInstance().runConditionalSubaction(false);
                    GameEventServer.getInstance().setActionCanceled(true);
                    Phaser.getInstance().getGameField().setSelectMode(0);
                    Phaser.getInstance().getGameField().setStateChanged(true);
                    Phaser.getInstance().getGameField().setCurrentMode(NewGameField.PHASE_MODE);
                    actorDirector.removeAllActors();
                    cardsSelected = true;
                    break;
                }
        }
    }
}
