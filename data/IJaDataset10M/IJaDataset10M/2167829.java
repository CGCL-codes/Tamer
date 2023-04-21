package org.jogre.server;

import java.util.HashMap;
import java.util.StringTokenizer;
import org.jogre.common.IGameOver;
import org.jogre.common.IJogre;

/**
 * JOGRE's implementation of the ELO rating system.  The following is an
 * example of how to use the Elo Rating System.
 * <code>
 * 		EloRatingSystem elo = new EloRatingSystem();
 * 		int userRating = 1600;
 * 		int opponentRating = 1650; 
 * 		int newUserRating = elo.getNewRating(userRating, opponentRating, WIN);
 * 		int newOpponentRating = elo.getNewRating(opponentRating, userRating, LOSS);
 * </code>
 * 
 * @author Garrett Lehman (gman)
 */
public class EloRatingSystem implements IRatingSystem {

    public static final double WIN = 1.0;

    public static final double DRAW = 0.5;

    public static final double LOSS = 0.0;

    private static final int AVERAGE_OPPONENTS = 0;

    private static final int ONE_ON_ONE = 1;

    private static final int TEAMS = 2;

    private static final int POSITIONAL = 3;

    public KFactor[] kFactors = {};

    private int multiPlayerType;

    /**
	 * Constructor for a JOGRE ELO rating system.
	 * 
	 * @param game   Game to do the rating on as games may vary
	 *               in their implementation of ELO.
	 */
    public EloRatingSystem(String game) {
        this(game, ServerProperties.getInstance().getKFactor(game), ServerProperties.getInstance().getMultiPlayerELOType(game));
    }

    /**
	 * Constructor of an ELO rating system that is provided the settings
	 * for kFactors and multi-player type.
	 * 
	 * @param game                 Game to do the rating on as games may vary
	 *                             in their implementation of ELO.
	 * @param kFactorStr           The string that describes the kFactor to use.
	 * @param multiPlayerTypeStr   The string that describes the method to
	 *                             use for games with more than 2 players.
	 */
    public EloRatingSystem(String game, String kFactorStr, String multiPlayerTypeStr) {
        if (kFactorStr != null) {
            StringTokenizer st1 = new StringTokenizer(kFactorStr, ",");
            kFactors = new KFactor[st1.countTokens()];
            int index = 0;
            while (st1.hasMoreTokens()) {
                String kfr = st1.nextToken();
                StringTokenizer st2 = new StringTokenizer(kfr, "=");
                String range = st2.nextToken();
                double value = Double.parseDouble(st2.nextToken());
                st2 = new StringTokenizer(range, "-");
                int startIndex = Integer.parseInt(st2.nextToken());
                int endIndex = Integer.parseInt(st2.nextToken());
                kFactors[index++] = new KFactor(startIndex, endIndex, value);
            }
        }
        multiPlayerType = AVERAGE_OPPONENTS;
        if ("OneOnOne".equals(multiPlayerTypeStr)) {
            multiPlayerType = ONE_ON_ONE;
        } else if ("Teams".equals(multiPlayerTypeStr)) {
            multiPlayerType = TEAMS;
        } else if ("Positional".equals(multiPlayerTypeStr)) {
            multiPlayerType = POSITIONAL;
        }
    }

    /**
	 * Given an array of current ratings and results for a game, return
	 * a new array of updated ratings.
	 * 
	 * @param currentRatings   The current player ratings.
	 * @param results          The array of player results.
	 * @param customData       Additional data used to compute ratings.
	 *                         This is only used for positional scoring for
	 *                         multi-player.
	 * @return newRatings      An array of the new ratings.
	 */
    public int[] getNewRatings(int[] currentRatings, int[] results, Object customData) {
        int numPlayers = currentRatings.length;
        int[] newRatings;
        if (numPlayers == 2) {
            newRatings = new int[numPlayers];
            newRatings[0] = getNewRating(currentRatings[0], currentRatings[1], results[0]);
            newRatings[1] = getNewRating(currentRatings[1], currentRatings[0], results[1]);
        } else if (numPlayers < 2) {
            newRatings = currentRatings;
        } else {
            if (multiPlayerType == ONE_ON_ONE) {
                newRatings = getNewRatingsOneOnOne(currentRatings, results);
            } else if (multiPlayerType == TEAMS) {
                newRatings = getNewRatingsTeams(currentRatings, results);
            } else if ((multiPlayerType == POSITIONAL) && (customData != null)) {
                if (customData instanceof int[]) {
                    int[] positions = (int[]) customData;
                    newRatings = getNewRatingsPositional(currentRatings, positions);
                } else {
                    newRatings = currentRatings;
                }
            } else {
                newRatings = getNewRatingsAverage(currentRatings, results);
            }
        }
        return newRatings;
    }

    private int[] getNewRatingsAverage(int[] currentRatings, int[] results) {
        int numPlayers = currentRatings.length;
        int[] newRatings = new int[numPlayers];
        int totalRating = 0;
        for (int p = 0; p < numPlayers; p++) {
            totalRating += currentRatings[p];
        }
        for (int p = 0; p < numPlayers; p++) {
            int averageOpponentRating = (totalRating - currentRatings[p]) / (numPlayers - 1);
            newRatings[p] = getNewRating(currentRatings[p], averageOpponentRating, results[p]);
        }
        return newRatings;
    }

    private int[] getNewRatingsOneOnOne(int[] currentRatings, int[] results) {
        int numPlayers = currentRatings.length;
        int[] newRatings = new int[numPlayers];
        int[] numberRatings = new int[numPlayers];
        for (int p = 0; p < numPlayers; p++) {
            newRatings[p] = 0;
            numberRatings[p] = 0;
        }
        for (int p = 0; p < numPlayers; p++) {
            for (int o = 0; o < numPlayers; o++) {
                if (results[p] != results[o]) {
                    newRatings[p] += getNewRating(currentRatings[p], currentRatings[o], results[p]);
                    numberRatings[p] += 1;
                }
            }
        }
        for (int p = 0; p < numPlayers; p++) {
            newRatings[p] = (newRatings[p] / numberRatings[p]);
        }
        return newRatings;
    }

    private int[] getNewRatingsTeams(int[] currentRatings, int[] results) {
        int numPlayers = currentRatings.length;
        int[] newRatings = new int[numPlayers];
        if ((numPlayers & 0x01) != 0) {
            return getNewRatingsAverage(currentRatings, results);
        }
        int evenTeamRating = 0;
        int oddTeamRating = 0;
        for (int p = 0; p < numPlayers; p += 2) {
            evenTeamRating += currentRatings[p];
            oddTeamRating += currentRatings[p + 1];
        }
        evenTeamRating = evenTeamRating / (numPlayers >> 1);
        oddTeamRating = oddTeamRating / (numPlayers >> 1);
        int evenTeamRatingChange = getNewRating(evenTeamRating, oddTeamRating, results[0]) - evenTeamRating;
        int oddTeamRatingChange = getNewRating(oddTeamRating, evenTeamRating, results[1]) - oddTeamRating;
        for (int p = 0; p < numPlayers; p += 2) {
            newRatings[p] = currentRatings[p] + evenTeamRatingChange;
            newRatings[p + 1] = currentRatings[p + 1] + oddTeamRatingChange;
        }
        return newRatings;
    }

    private int[] getNewRatingsPositional(int[] currentRatings, int[] positions) {
        int numPlayers = currentRatings.length;
        int[] newRatings = new int[numPlayers];
        for (int p = 0; p < numPlayers; p++) {
            newRatings[p] = 0;
        }
        for (int p = 0; p < numPlayers; p++) {
            for (int o = 0; o < numPlayers; o++) {
                if (p != o) {
                    double score = (positions[p] < positions[o]) ? 1.0 : ((positions[p] > positions[o]) ? 0.0 : 0.5);
                    newRatings[p] += getNewRating(currentRatings[p], currentRatings[o], score);
                }
            }
        }
        for (int p = 0; p < numPlayers; p++) {
            newRatings[p] = (newRatings[p] / (numPlayers - 1));
        }
        return newRatings;
    }

    /**
	 * Convience overloaded version of getNewRating (int, int, double)
	 * which takes a result type and 
	 * 
	 * @param rating
	 * @param opponentRating
	 * @param resultType
	 * @return
	 */
    public int getNewRating(int rating, int opponentRating, int resultType) {
        switch(resultType) {
            case IGameOver.WIN:
                return getNewRating(rating, opponentRating, WIN);
            case IGameOver.LOSE:
                return getNewRating(rating, opponentRating, LOSS);
            case IGameOver.DRAW:
                return getNewRating(rating, opponentRating, DRAW);
        }
        return -1;
    }

    /**
	 * Get new rating.
	 * 
	 * @param rating
	 *            Rating of either the current player or the average of the
	 *            current team.
	 * @param opponentRating
	 *            Rating of either the opponent player or the average of the
	 *            opponent team or teams.
	 * @param score
	 *            Score: 0=Loss 0.5=Draw 1.0=Win
	 * @return the new rating
	 */
    public int getNewRating(int rating, int opponentRating, double score) {
        double kFactor = getKFactor(rating);
        double expectedScore = getExpectedScore(rating, opponentRating);
        int newRating = calculateNewRating(rating, score, expectedScore, kFactor);
        return newRating;
    }

    /**
	 * Calculate the new rating based on the ELO standard formula.
	 * newRating = oldRating + constant * (score - expectedScore)
	 * 
	 * @param oldRating 	Old Rating
	 * @param score			Score
	 * @param expectedScore	Expected Score
	 * @param constant		Constant
	 * @return				the new rating of the player
	 */
    private int calculateNewRating(int oldRating, double score, double expectedScore, double kFactor) {
        return oldRating + (int) (kFactor * (score - expectedScore));
    }

    /**
	 * This is the standard chess constant.  This constant can differ
	 * based on different games.  The higher the constant the faster
	 * the rating will grow.  That is why for this standard chess method,
	 * the constant is higher for weaker players and lower for stronger
	 * players.
	 *  
	 * @param rating		Rating
	 * @return				Constant
	 */
    private double getKFactor(int rating) {
        for (int i = 0; i < kFactors.length; i++) if (rating >= kFactors[i].getStartIndex() && rating <= kFactors[i].getEndIndex()) {
            return kFactors[i].value;
        }
        return IJogre.DEFAULT_ELO_K_FACTOR;
    }

    /**
	 * Get expected score based on two players.  If more than two players
	 * are competing, then opponentRating will be the average of all other
	 * opponent's ratings.  If there is two teams against each other, rating
	 * and opponentRating will be the average of those players.
	 * 
	 * @param rating			Rating
	 * @param opponentRating	Opponent(s) rating
	 * @return					the expected score
	 */
    private double getExpectedScore(int rating, int opponentRating) {
        return 1.0 / (1.0 + Math.pow(10.0, ((double) (opponentRating - rating) / 400.0)));
    }

    /**
	 * Small inner class data structure to describe a KFactor range.
	 */
    public class KFactor {

        private int startIndex, endIndex;

        private double value;

        public KFactor(int startIndex, int endIndex, double value) {
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            this.value = value;
        }

        public int getStartIndex() {
            return startIndex;
        }

        public int getEndIndex() {
            return endIndex;
        }

        public double getValue() {
            return value;
        }

        public String toString() {
            return "kfactor: " + startIndex + " " + endIndex + " " + value;
        }
    }
}
