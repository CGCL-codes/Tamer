package constants;

import java.util.Random;

/**
 * Class represents diferent game results.
 * @author Fishman Yevgeni
 */
public final class GameResults {

    private String _name;

    private int _value;

    private int _score;

    private GameResults(String name, int value, int score) {
        _name = name;
        _value = value;
        _score = score;
    }

    public static final GameResults REWARD = new GameResults("Reward", 0, 3);

    public static final GameResults SUCKERS_PLAYOFF = new GameResults("Sucker's playoff", 2, 0);

    public static final GameResults TEMPTATION = new GameResults("Temptation", 1, 5);

    public static final GameResults PUNISHMENT = new GameResults("Punishment", 3, 1);

    @Override
    public String toString() {
        return _name;
    }

    public int getValue() {
        return _value;
    }

    public int getScore() {
        return _score;
    }

    /**
     * Static Method to get random game result.
     * @return - GemeResults - Random game result.
     */
    public static GameResults randomizeGameResults() {
        Random rnd = new Random();
        switch(rnd.nextInt(5)) {
            case 0:
                return GameResults.SUCKERS_PLAYOFF;
            case 1:
                return GameResults.PUNISHMENT;
            case 2:
                return GameResults.REWARD;
            case 3:
                return GameResults.TEMPTATION;
            default:
                return GameResults.SUCKERS_PLAYOFF;
        }
    }
}
