package tallyho.model.turn;

import tallyho.model.Team;

/**
 * A mock Turn for unit testing purposes
 */
public class MockTurn implements Turn {

    private Number score;

    private Team team;

    public Team getTeam() {
        if (team == null) {
            throw new IllegalStateException("Team hasn't been set");
        }
        return team;
    }

    public int getScore() {
        if (score == null) {
            throw new IllegalStateException("Score hasn't been set");
        }
        return score.intValue();
    }

    /**
   * Sets up the score of this mock
   * 
   * @param score
   */
    public void setupScore(int score) {
        this.score = new Integer(score);
    }

    /**
   * Sets up the team of this mock
   * 
   * @param team
   */
    public void setupTeam(Team team) {
        this.team = team;
    }
}
