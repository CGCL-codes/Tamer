package playground.benjamin.old.income;

import org.apache.log4j.Logger;
import org.matsim.core.scoring.CharyparNagelScoringParameters;
import org.matsim.core.scoring.interfaces.BasicScoring;
import org.matsim.core.scoring.interfaces.MoneyScoring;

/**
 * This is a re-implementation of the original CharyparNagel function, based on a
 * modular approach.
 * 
 * ATTENTION:
 * This class is only supposed to work as expected if there are NO OTHER money events than those from road pricing!
 * 
 * @see http://www.matsim.org/node/263
 * @author bkick and michaz after rashid_waraich
 */
public class ScoringFromToll implements MoneyScoring, BasicScoring {

    private static final Logger log = Logger.getLogger(ScoringFromToll.class);

    private double score = 0.0;

    private double betaIncomeCar = 4.58;

    private double incomePerDay;

    /** The parameters used for scoring */
    protected final CharyparNagelScoringParameters params;

    public ScoringFromToll(final CharyparNagelScoringParameters params, double householdIncomePerDay) {
        this.params = params;
        this.incomePerDay = householdIncomePerDay;
    }

    public void reset() {
    }

    public void addMoney(final double amount) {
        this.score += (betaIncomeCar / incomePerDay) * amount;
    }

    public void finish() {
    }

    public double getScore() {
        return this.score;
    }
}
