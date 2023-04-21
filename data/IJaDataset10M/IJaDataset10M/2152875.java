package planspiel;

import admintool.imp03_data.IMP03_Vacancy;

/**
 *
 * @author Administrator
 */
public class GameRound {

    private int roundID = 0;

    private int score = 0;

    private IMP03_Vacancy vacancy = null;

    /**
     * 
     * @param roundID
     */
    public GameRound(int roundID) {
        this.roundID = roundID;
        vacancy = initVacancy();
    }

    private IMP03_Vacancy initVacancy() {
        return IMP03_Vacancy.getSpecificVacancyFromDB(10);
    }

    /**
     * 
     * @return
     */
    public int getRoundID() {
        return roundID;
    }

    /**
     * 
     * @return
     */
    public IMP03_Vacancy getVacancy() {
        return vacancy;
    }

    /**
	 * @return the score as String
	 */
    public String getScoreAsString() {
        return "Budget: " + score + " €";
    }

    /**
	 * @param score the score to set
	 */
    public void setScore(int score) {
        this.score = score;
    }

    /**
         * 
         * @param amount
         */
    public void reduceScore(int amount) {
        this.score -= amount;
    }
}
