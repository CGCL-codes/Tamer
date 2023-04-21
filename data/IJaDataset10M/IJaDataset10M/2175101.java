package bbalc.examples;

import bbalc.control.*;
import bbalc.core.*;
import bbalc.core.codec.*;

/**
 * @author irina
 */
public class TestTurnSyntesizer {

    static ControlHandler ch = ControlHandler.getInstance();

    static final boolean NO_EXCEPTION = true;

    static void setupTeamHome() {
        ch.setPlayerField(12, 7, 1, true);
        ch.setPlayerField(12, 8, 2, true);
        ch.setPlayerField(12, 9, 3, true);
        ch.setPlayerField(12, 6, 5, true);
        ch.setPlayerField(12, 5, 7, true);
    }

    static void setupTeamGuest() {
        ch.setPlayerField(13, 7, 1, false);
        ch.setPlayerField(13, 8, 2, false);
        ch.setPlayerField(13, 9, 3, false);
        ch.setPlayerField(13, 6, 4, false);
        ch.setPlayerField(13, 5, 5, false);
    }

    static void initGame() {
        ch.setGameStatus(IGameState.STATE_TURN);
        ch.setHalf(1);
        ch.setScore(0, ICodec.TEAM_HOME);
        ch.setScore(0, ICodec.TEAM_GUEST);
        ch.setTurn(0, ICodec.TEAM_HOME);
        ch.setTurn(0, ICodec.TEAM_GUEST);
    }

    public static void main(String[] args) {
        ch.setTeam(new HTMLTeam("./Teams/Warpstone+Nibblers.html"), true);
        ch.setTeam(new HTMLTeam("./Teams/Da+Red+Fighterz.html"), false);
        ch.initGame(0);
        initGame();
        setupTeamHome();
        setupTeamGuest();
        System.out.println("searching moves for team home");
        System.out.println("getNextMove returned: " + ch.getNextMove(true));
    }
}
