package de.qnerd.rpgBot.renewed;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import de.qnerd.rpgBot.gameobjects.Player;
import de.qnerd.rpgBot.gameobjects.Team;

/**
 * @author Sven Kuhnert
 *
 * 
 * 
 */
public class RoundController {

    private GameController gameController;

    private Player[] player;

    private int aktualPlayerId = 0;

    public RoundController(GameController controller) {
        this.gameController = controller;
    }

    public void init() {
        loadPlayerArray();
        player[aktualPlayerId].setActivePlayer(true);
        gameController.sendToAll(player[aktualPlayerId].getName() + " beginnt den Kampf.");
    }

    public void nextPlayer() {
        player[aktualPlayerId].setActivePlayer(false);
        aktualPlayerId++;
        if (aktualPlayerId == player.length) newRound();
        while (player[aktualPlayerId].isKilled()) {
            aktualPlayerId++;
            if (aktualPlayerId == player.length) newRound();
        }
        if (gameController.teamWiped(GameController.TEAM_BLAU) || gameController.teamWiped(GameController.TEAM_ROT)) {
            gameController.stopGame(player[aktualPlayerId]);
        } else {
            player[aktualPlayerId].setActivePlayer(true);
            gameController.sendToAll(player[aktualPlayerId].getName() + " ist nun an der Reihe.");
        }
    }

    private void newRound() {
        gameController.sendToAll("Eine Runde ist vorueber.");
        aktualPlayerId = 0;
    }

    private void loadPlayerArray() {
        int count = 0;
        player = new Player[gameController.getAllPlayerSize()];
        for (int i = 0; i < gameController.teams.length; i++) {
            Team team = gameController.teams[i];
            HashMap playerMap = team.getPlayerMap();
            Iterator it = playerMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                Player aktualPlayer = (Player) playerMap.get(entry.getKey());
                player[count++] = aktualPlayer;
            }
        }
        sortPlayerArray();
    }

    /**
     * Simple Bubble-Sort
     *
     */
    private void sortPlayerArray() {
        Player temp;
        int n = player.length;
        for (int i = 0; i < n - 1; i++) for (int j = n - 1; j > i; j--) if (player[j - 1].getSpeed() < player[j].getSpeed()) {
            temp = player[j - 1];
            player[j - 1] = player[j];
            player[j] = temp;
        }
    }
}
