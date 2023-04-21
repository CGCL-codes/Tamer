package playground.andreas.P2.replanning.modules.deprecated;

import java.util.ArrayList;
import org.apache.log4j.Logger;
import org.matsim.core.basic.v01.IdImpl;
import playground.andreas.P2.pbox.Cooperative;
import playground.andreas.P2.plan.PPlan;
import playground.andreas.P2.replanning.PPlanStrategy;
import playground.andreas.P2.replanning.PStrategy;

/**
 * 
 * Clones a given plan, but resets the number of vehicles to one.
 * 
 * @author aneumann
 *
 */
public class RemoveAllVehiclesButOne extends PStrategy implements PPlanStrategy {

    private static final Logger log = Logger.getLogger(RemoveAllVehiclesButOne.class);

    public static final String STRATEGY_NAME = "RemoveAllVehiclesButOne";

    public RemoveAllVehiclesButOne(ArrayList<String> parameter) {
        super(parameter);
        if (parameter.size() != 0) {
            log.error("Too many parameter. Will ignore: " + parameter);
        }
    }

    @Override
    public PPlan run(Cooperative cooperative) {
        PPlan newPlan = new PPlan(new IdImpl(cooperative.getCurrentIteration()), cooperative.getBestPlan());
        newPlan.setLine(cooperative.getRouteProvider().createTransitLine(cooperative.getId(), newPlan.getStartTime(), newPlan.getEndTime(), 1, newPlan.getStopsToBeServed(), new IdImpl(cooperative.getCurrentIteration())));
        return newPlan;
    }

    @Override
    public String getName() {
        return RemoveAllVehiclesButOne.STRATEGY_NAME;
    }
}
