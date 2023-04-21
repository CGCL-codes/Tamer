package competition;

import gui.ScrabbleBoardPanel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import scrabblehelper.StaticFields;
import scrabbletools.Board;
import scrabbletools.BoardAnagramUtils;
import scrabbletools.WordPlacement;

/**
 *
 * @author Nick
 */
public class CompetitionExecutor {

    private ScrabbleBoardPanel sbp;

    ArrayList<Player> players = new ArrayList<Player>();

    LetterSupply supply;

    int currentPlayer = 1;

    public CompetitionExecutor(int numberOfPlayers, ScrabbleBoardPanel sbp) {
        this.sbp = sbp;
        supply = new LetterSupply();
        for (int i = 0; i < numberOfPlayers; i++) {
            players.add(new Player(supply));
        }
    }

    public void playWholeGame() {
        bigLoop: while (true) {
            for (int i = 0; i < players.size(); i++) {
                int score = play(players.get(i));
                if (score < 0) {
                    break bigLoop;
                }
            }
        }
        for (int i = 0; i < players.size(); i++) {
            System.out.println("Player " + (i + 1) + "scored:  " + players.get(i));
        }
    }

    public int play(Player player) {
        BoardAnagramUtils bau = new BoardAnagramUtils(new Board(sbp.getCharArray()));
        bau.setDictionary(StaticFields.getDictionary());
        bau.setRackLetters(player.getRackCharArray());
        List<WordPlacement> words = bau.findAllBoardPossibilities();
        Collections.sort(words);
        if (words.size() > 0) {
            WordPlacement topScorer = words.get(0);
            sbp.putWordPlacement(topScorer, false);
            player.removeAndRefillLetters(topScorer.getPlacedLetters(), supply);
            player.addToScore(topScorer.getScore());
            return topScorer.getScore();
        } else {
            return -1;
        }
    }

    public ScrabbleBoardPanel getScrabblePanel() {
        return sbp;
    }

    public void setScrabblePanel(ScrabbleBoardPanel sbp) {
        this.sbp = sbp;
    }
}
