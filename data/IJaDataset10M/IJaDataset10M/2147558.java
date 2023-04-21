package playground.yu.tests;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.apache.log4j.Logger;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.population.Leg;
import org.matsim.api.core.v01.population.Plan;
import org.matsim.api.core.v01.population.PlanElement;
import org.matsim.core.api.experimental.facilities.ActivityFacilities;
import org.matsim.core.config.Config;
import org.matsim.core.config.groups.PlanomatConfigGroup;
import org.matsim.core.gbl.MatsimRandom;
import org.matsim.core.replanning.modules.AbstractMultithreadedModule;
import org.matsim.core.utils.misc.StringUtils;
import org.matsim.population.algorithms.PlanAlgorithm;
import playground.meisterk.org.matsim.population.algorithms.PlanAnalyzeTourModeChoiceSet;

/**
 * uses {@code PlanAnalyzeTourModeChoiceSet} to realize "subtour mode choice"
 *
 * @author yu
 *
 */
public class SubTourModeChoice extends AbstractMultithreadedModule {

    private Config config;

    private Set<String> availableModes;

    private Set<String> chainBasedModes;

    private ActivityFacilities facilities;

    private Network network;

    private Random random = MatsimRandom.getLocalInstance();

    public SubTourModeChoice(final Config config, ActivityFacilities facilities, Network network) {
        super(config.global().getNumberOfThreads());
        this.availableModes = new HashSet<String>();
        this.availableModes.add(TransportMode.car);
        this.availableModes.add(TransportMode.pt);
        this.chainBasedModes = new HashSet<String>();
        this.chainBasedModes.add(TransportMode.car);
        this.config = config;
        this.facilities = facilities;
        this.network = network;
        String modes = config.vspExperimental().getModesForSubTourModeChoice();
        if (modes != null) {
            String[] parts = StringUtils.explode(modes, ',');
            this.availableModes = new HashSet<String>();
            for (int i = 0, n = parts.length; i < n; i++) {
                this.availableModes.add(parts[i].trim().intern());
            }
        }
        String chainModes = config.vspExperimental().getChainBasedModes();
        if (chainModes != null) {
            String[] parts = StringUtils.explode(chainModes, ',');
            this.chainBasedModes = new HashSet<String>();
            for (int i = 0, n = parts.length; i < n; i++) {
                this.chainBasedModes.add(parts[i].trim().intern());
            }
        }
    }

    @Override
    public PlanAlgorithm getPlanAlgoInstance() {
        return new ChooseRandomTourModeSet(chainBasedModes, this.config.planomat().getTripStructureAnalysisLayer(), facilities, network);
    }

    private static int wrnCnt = 0;

    public class ChooseRandomTourModeSet implements PlanAlgorithm {

        private PlanAnalyzeTourModeChoiceSet patmcs;

        public ChooseRandomTourModeSet(final Set<String> chainBasedModes, final PlanomatConfigGroup.TripStructureAnalysisLayerOption tripStructureAnalysisLayer, final ActivityFacilities facilities, final Network network) {
            patmcs = new PlanAnalyzeTourModeChoiceSet(chainBasedModes, tripStructureAnalysisLayer, facilities, network);
            patmcs.setModeSet(availableModes);
        }

        @Override
        public void run(Plan plan) {
            if (wrnCnt < 1) {
                wrnCnt++;
                Logger.getLogger(this.getClass()).warn("This method assumes act/leg alternation, which is " + "deprecated.  For the time being, this is probably still ok, but needs to be fixed eventually.  " + "kai, mar'11");
            }
            this.patmcs.run(plan);
            ArrayList<String[]> choiceSet = this.patmcs.getChoiceSet();
            List<PlanElement> pes = plan.getPlanElements();
            int legsSize = (pes.size() - 1) / 2;
            String[] currentModes = new String[legsSize];
            for (int i = 0; i < legsSize; i++) currentModes[i] = ((Leg) pes.get(2 * i + 1)).getMode();
            String[] randomModes = currentModes;
            do {
                randomModes = choiceSet.get(random.nextInt(choiceSet.size()));
            } while (currentModes.equals(randomModes));
            for (int i = 0; i < legsSize; i++) ((Leg) pes.get(2 * i + 1)).setMode(randomModes[i]);
        }
    }
}
