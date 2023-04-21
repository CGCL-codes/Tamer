package playground.mfeil;

import org.matsim.planomat.costestimators.DepartureDelayAverageCalculator;
import org.matsim.population.algorithms.PlanAlgorithm;
import org.matsim.core.controler.Controler;
import org.matsim.core.replanning.modules.*;

/**
 * @author Matthias Feil
 * Initialiser for TimeOptimizer module.
 */
public class TmcInitialiser extends AbstractMultithreadedModule {

    private final Controler controler;

    private final DepartureDelayAverageCalculator tDepDelayCalc;

    public TmcInitialiser(Controler controler) {
        this.controler = controler;
        this.tDepDelayCalc = new DepartureDelayAverageCalculator(controler.getNetwork(), controler.getConfig().travelTimeCalculator().getTraveltimeBinSize());
        this.controler.getEvents().addHandler(tDepDelayCalc);
    }

    @Override
    public PlanAlgorithm getPlanAlgoInstance() {
        PlanAlgorithm timeOptAlgorithm = new TimeModeChoicer1(this.controler, this.tDepDelayCalc);
        return timeOptAlgorithm;
    }
}
