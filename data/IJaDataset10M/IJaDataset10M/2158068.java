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
public class GetCardsFromPlayerGameActor extends AbstractSimpleActor implements IGameFieldEventListener {

    private Vector<Card> cards;

    private int num;

    private Boolean mond;

    private Boolean gfQuery = false;

    private Boolean cardsSelected = false;

    private int dialogMode = 2;

    public GetCardsFromPlayerGameActor(Vector<Card> cards, int num, Boolean mond, int dialogMode) {
        super();
        this.cards = cards;
        this.num = num;
        this.mond = mond;
        this.cardsSelected = false;
        this.dialogMode = dialogMode;
    }

    public Boolean playRole() {
        super.logMe();
        if (cards == null || cards.size() == 0) {
            Phaser.getInstance().getGameField().setSelectedCards(new Vector<Card>());
            return true;
        }
        Phaser.getInstance().getGameField().setSelectedCards(new Vector<Card>());
        Vector<Card> definedTargets = GameEventServer.getInstance().getPredefinedTarget();
        if (definedTargets.size() > 0) {
            cards = definedTargets;
        }
        if (!gfQuery) {
            if (Director.dirDebug) Logger.debug("Running get cards of " + cards, this);
            Phaser.getInstance().getGameField().addGfEventListener(this);
            Phaser.getInstance().getGameField().getCommandDialog().setMode(CommandDialog.UNDO_MODE);
            cardsSelected = false;
            Phaser.getInstance().getGameField().selectFromPlayerGame(cards, num, mond, dialogMode);
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
