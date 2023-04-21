package glaceo.data.impl.goals;

import glaceo.conf.GRulesConstants;
import glaceo.data.dao.club.GClub;
import glaceo.data.dao.contest.GContestElement;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Goals distributor for the Champions League.
 *
 * @version $Id$
 * @author jjanke
 */
public class GCLGoalsDistributor implements IGGoalsDistributor {

    private BigDecimal d_decGoals;

    public void initialize(GContestElement element) {
        switch(element.getContestRound().getRoundType()) {
            case QUALIFICATION_1:
                {
                    d_decGoals = BigDecimal.valueOf((2.5 + 6 + 7.5) * GRulesConstants.GOALS_PER_MATCH_AVG);
                    break;
                }
            case ROUND_MAIN:
                {
                    d_decGoals = BigDecimal.valueOf((6 + 7.5) * GRulesConstants.GOALS_PER_MATCH_AVG);
                    break;
                }
            case QUARTER_FINAL:
                {
                    d_decGoals = BigDecimal.valueOf(7.5 * GRulesConstants.GOALS_PER_MATCH_AVG);
                    break;
                }
            case SEMI_FINAL:
                {
                    d_decGoals = BigDecimal.valueOf(5.0 * GRulesConstants.GOALS_PER_MATCH_AVG);
                    break;
                }
            case FINAL:
                {
                    d_decGoals = BigDecimal.valueOf(2.5 * GRulesConstants.GOALS_PER_MATCH_AVG);
                    break;
                }
            default:
                throw new IllegalStateException("Unexpected CL round type " + element.getContestRound().getRoundType().name());
        }
        d_decGoals = d_decGoals.setScale(1, RoundingMode.UP);
    }

    /**
   * Returns the same amount of goals for every club.
   *
   * @see glaceo.data.impl.goals.IGGoalsDistributor#getGoals(int)
   */
    public BigDecimal getGoals(int nSeeding) {
        return d_decGoals;
    }

    /**
   * Returns the amount of goals to be used in CL KO rounds. The club argument is ignored
   * as all teams benefit from the same amount of goals.
   */
    public BigDecimal getGoals(GClub club) {
        return d_decGoals;
    }
}
