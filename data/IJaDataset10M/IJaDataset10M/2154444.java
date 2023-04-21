package org.gromurph.javascore.model.ratings;

import java.text.MessageFormat;
import org.gromurph.javascore.SailTime;
import org.gromurph.javascore.model.Division;
import org.gromurph.javascore.model.Finish;
import org.gromurph.javascore.model.Race;
import org.gromurph.util.WarningList;

/**
 * Covering classes for ratings where the corrected time is a simple coefficient
 * of the elapsed time by the rating value.  Typical of time-on-time formulas
 * 
 *
**/
public abstract class RatingCoefficient extends RatingDouble {

    public RatingCoefficient(String sys, double inV) {
        super(sys, inV);
    }

    public int getDecs() {
        return 3;
    }

    protected double getCoefficient() {
        return getPrimaryValue();
    }

    /**
     * Time on time formula
     * @param inFinish Finish object for which corrected time is to be calculated
     * @return corrected time in milliseconds
     */
    public long getCorrectedTime(Finish inFinish) {
        long elapMilli = inFinish.getElapsedTime();
        if (elapMilli != SailTime.NOTIME) return (long) (elapMilli * getCoefficient()); else return SailTime.NOTIME;
    }

    public void validateRace(Race race, Division div, WarningList warnings) {
        long starttime = race.getStartTimeRaw(div);
        if (starttime == SailTime.NOTIME) {
            warnings.add(MessageFormat.format(res.getString("WarningRaceNeedsStartTime"), new Object[] { race.toString(), div.toString() }));
        }
        validateFinishTimesAfterStartTimes(race, div, warnings);
        validateValidFinishTime(race, div, warnings);
    }
}
