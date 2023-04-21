package playground.thibautd.jointtrips.replanning.modules.reroute;

import org.matsim.core.controler.Controler;
import org.matsim.core.replanning.modules.AbstractMultithreadedModule;
import org.matsim.population.algorithms.PlanAlgorithm;

/**
 * Module returning instances of {@link JointReRouteAlgo}
 * @author thibautd
 */
public class JointReRouteModule extends AbstractMultithreadedModule {

    private final Controler controler;

    public JointReRouteModule(final Controler controler) {
        super(controler.getConfig().global());
        this.controler = controler;
    }

    @Override
    public PlanAlgorithm getPlanAlgoInstance() {
        return new JointReRouteAlgo(controler);
    }
}
