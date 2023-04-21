package playground.johannes.mz2005.validate;

import org.matsim.api.core.v01.population.Plan;

/**
 * @author illenberger
 *
 */
public interface PlanValidator {

    public boolean validate(Plan plan);
}
