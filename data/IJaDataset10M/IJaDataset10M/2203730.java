package Testing;

import CardServer.*;
import Logging.ILogger;
import Logging.Logger;
import java.util.HashMap;
import java.util.Map;
import RulesServer.GameEventServer;
import RulesServer.ActionsStack;
import RulesServer.Phaser;
import Network.Randomizer;
import AI.AIPlayer;
import Modifiers.DynamicGameModifiers.AbstractDynamicGameModifier;
import Modifiers.DynamicGameModifiers.DynamicModifierFactory;
import EventServer.GameEvent;
import EventServer.PlayerActionConstants;
import EventServer.CardEvent;

/**
 * Created by IntelliJ IDEA.
 * User: shmalikov.sergey
 * Date: 19.06.2007
 * Time: 15:09:36
 */
public class PlayableCardsTester {

    public static class logg implements ILogger {

        public void writeLog(String inlog) {
            System.out.println(inlog);
        }
    }

    public static Card test;

    public static void main(String args[]) {
        Randomizer.startRandom(555);
        Phaser.getInstance().setFirstTurnInGame(false);
        CardPack pGame = GameEventServer.getInstance().getPlayerGame();
        CardPack eGame = GameEventServer.getInstance().getEnemyGame();
        CardPack pHand = GameEventServer.getInstance().getPlayerHand();
        CardPack eHand = GameEventServer.getInstance().getEnemyHand();
        Phaser.getInstance().setEnemySupportPhase(true);
        Phaser.getInstance().setEnemyKripPhase(true);
        Phaser.getInstance().setEnemyBombPhase(true);
        Phaser.getInstance().setBlasting(false);
        GameEventServer.getInstance().setEnemyHero(new Card("TestRun\\_cards\\Hero_Norman.xml"));
        test = new Card("TestRun\\_cards\\Brakovannaya_postavka.xml");
        eHand.addCard(test, null, CardPack.DECK_PUT_DEFAULT);
        test = new Card("TestRun\\_cards\\Rastvoryaushaya_bomba.xml");
        eGame.addCard(test, null, CardPack.DECK_PUT_DEFAULT);
        eHand.addCard(test, null, CardPack.DECK_PUT_DEFAULT);
        Phaser.getInstance().setPlayerTurn(false);
        System.out.println("Playable enemy cards are  " + Phaser.getInstance().getPlayableCards());
        Logger.setGFLog(new logg());
    }
}
