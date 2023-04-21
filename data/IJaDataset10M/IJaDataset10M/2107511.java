package ee.fctwister.poker.matrix;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import ee.fctwister.poker.classifiers.Player;
import ee.fctwister.poker.classifiers.Round;
import ee.fctwister.poker.db.AbstractDAO;
import ee.fctwister.poker.db.PlayerData;
import ee.fctwister.poker.db.RoundData;

public class PlayerMatrix extends AbstractDAO {

    private static Map<Player, Integer> playerMap;

    private static ArrayList<Player> players;

    private static int[][] getPlayerRoundsMatrix(int leagueId) {
        int[][] playerRoundsMatrix = null;
        Connection conn = null;
        try {
            conn = openConnection();
            playerMap = new HashMap<Player, Integer>();
            players = PlayerData.getLeaguePlayers(leagueId, conn);
            int playerMapIndex = 0;
            for (Player player : players) {
                playerMap.put(player, playerMapIndex++);
            }
            ArrayList<Round> rounds = RoundData.getLeagueRounds(leagueId, conn);
            int roundsLength = rounds.toArray().length;
            playerRoundsMatrix = new int[roundsLength][roundsLength];
            for (Round round : rounds) {
                ArrayList<Player> roundPlayers = round.getPlayers();
                System.out.println(round.getNr() + ". voor");
                System.out.println("");
                for (Player player : roundPlayers) {
                    for (int i = 0; i < 6; i++) {
                        playerRoundsMatrix[playerMap.get(player)][playerMap.get(roundPlayers.get(i))]++;
                    }
                    System.out.println(player);
                }
                System.out.println("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            endDbCommunication(null, null, conn);
        }
        return playerRoundsMatrix;
    }

    public static void printPlayerRoundsMatrix(int leagueId) {
        int[][] playerRoundsMatrix = getPlayerRoundsMatrix(leagueId);
        for (int i = 0; i < playerRoundsMatrix.length; i++) {
            System.out.print(players.get(i));
            int sum = 0;
            for (int j = 0; j < playerRoundsMatrix[i].length; j++) {
                if (i == j) System.out.print("\tX"); else {
                    System.out.print("\t" + playerRoundsMatrix[i][j]);
                    sum += playerRoundsMatrix[i][j];
                }
            }
            System.out.println("\t" + sum);
        }
    }
}
