package de.lukas.davincicode;

import java.util.List;
import java.util.ArrayList;
import de.lukas.davincicode.Token.Color;

public class Game {

    Pool pool = new Pool();

    List<Player> players = new ArrayList<Player>();

    List<String> playerNames = new ArrayList<String>();

    List<Player> finishedPlayers = new ArrayList<Player>();

    /**
	 * The first player in the list begins.
	 * @param players
	 */
    public Game(List<Player> players) {
        this.players = players;
        for (Player player : players) {
            playerNames.add(player.getName());
        }
    }

    public void performTurn(Player currentPlayer) {
        Token poolToken = null;
        if (!pool.isEmpty()) {
            Color poolColor = currentPlayer.choosePoolColor(pool.isAvailable(Color.BLACK), pool.isAvailable(Color.WHITE));
            poolToken = pool.getRandomToken(poolColor);
            currentPlayer.addPoolToken(poolToken);
        }
        List<Player> opponentPlayers = getOpponentPlayers(currentPlayer);
        Player opponentPlayer = chooseOpponent(currentPlayer, opponentPlayers);
        boolean continueSecondStep = true;
        while (continueSecondStep) {
            OpenPosition opponentPosition = opponentPlayer.getOpenPosition();
            Player.AskPlaceFromResult askResult = opponentPositionIndex = currentPlayer.chooseAskPlaceFrom(opponentPosition);
            if (opponentPlayer.isCorrect(askResult)) {
                opponentPlayer.openToken(askResult.getTokenPlace());
                if (opponentPlayer.isFinished()) opponentPlayers.remove(opponentPlayer);
                if (!currentPlayer.wantsToCarryOn()) {
                    currentPlayer.addPoolToken(poolToken);
                    continueSecondStep = false;
                }
            } else {
                poolToken.open();
                currentPlayer.addPoolToken(poolToken);
                continueSecondStep = false;
            }
            if (continueSecondStep) {
                if (opponentPlayers.isEmpty()) continueSecondStep = false;
            }
        }
    }

    private List<Player> getOpponentPlayers(Player currentPlayer) {
        List<Player> result = new ArrayList<Player>();
        for (Player p : players) {
            if (p != currentPlayer && !p.isFinished()) result.add(p);
        }
        return result;
    }

    protected Player chooseOpponent(Player currentPlayer, List<Player> opponentPlayers) {
        if (opponentPlayers.size() == 1) {
            return opponentPlayers.get(0);
        } else throw new RuntimeException("More than two players not yet implemented!");
    }

    public boolean isFinished() {
        return (players.size() - finishedPlayers.size() == 1);
    }

    /**
	 * Returns the players in order of their rank after the game is finished.
	 * @return
	 */
    public List<Player> getPodium() {
        if (!isFinished()) throw new RuntimeException("game not yet over");
        return finishedPlayers;
    }

    public void start() {
        providePlayersWithInitialPositions();
        Player currentPlayer = players.get(0);
        while (!isFinished()) {
            performTurn(currentPlayer);
            int i = players.indexOf(currentPlayer);
            if (++i >= players.size()) i = 0;
            currentPlayer = players.get(i);
        }
        System.out.println("Winners are: " + getPodium());
    }

    private void providePlayersWithInitialPositions() {
    }

    public static void main(String[] args) {
        Player pA = new ComputerPlayer("Player A");
        Player pB = new ComputerPlayer("Player B");
        List<Player> pList = new ArrayList<Player>();
        pList.add(pA);
        pList.add(pB);
        Game g = new Game(pList);
        g.start();
    }
}
