package playground.yu.integration.cadyts.parameterCalibration.withCarCounts.mnlValidation;

import java.util.List;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.Plan;
import org.matsim.core.config.groups.PlanCalcScoreConfigGroup;
import org.matsim.core.utils.collections.Tuple;
import playground.yu.scoring.Events2ScoreI;
import utilities.math.BasicStatistics;

public interface CadytsChoice extends Events2ScoreI {

    /** save Attr values into Maps */
    @Override
    public void finish();

    public PlanCalcScoreConfigGroup getScoring();

    public void reset(List<Tuple<Id, Plan>> toRemoves);

    public void setPersonAttrs(Person person, BasicStatistics[] statistics);

    public void setPersonScore(Person person);
}
