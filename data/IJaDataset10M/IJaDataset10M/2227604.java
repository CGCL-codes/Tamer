package plugin.anagram;

/**
 * This class holds the information of the historic
 * for the anagram plug-in
 * @author Marcel Arrufat Arias
 */
public class AnagramHistoricModel {

    private int score = 0;

    public void increaseScore(int num) {
        score += num;
    }

    public void resetStats() {
        score = 0;
    }

    /**
	 * @return Returns the gamesLost.
	 */
    public int getScore() {
        return score;
    }
}
