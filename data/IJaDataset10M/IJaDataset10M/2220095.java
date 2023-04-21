package playground.dgrether.analysis.io;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.population.Activity;
import org.matsim.api.core.v01.population.Plan;
import org.matsim.api.core.v01.population.Population;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.population.MatsimPopulationReader;
import org.matsim.core.population.PlanImpl;
import org.matsim.core.population.PopulationReader;
import org.matsim.core.scenario.ScenarioImpl;
import org.matsim.core.scenario.ScenarioLoaderImpl;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.population.algorithms.PlanCalcType;
import playground.dgrether.analysis.population.DgAnalysisPopulation;
import playground.dgrether.analysis.population.DgPersonData;
import playground.dgrether.analysis.population.DgPlanData;

public class DgAnalysisPopulationReader {

    private static final Logger log = Logger.getLogger(DgAnalysisPopulationReader.class);

    private Map<String, Network> loadedNetworks = new HashMap<String, Network>();

    private List<DgAnalysisReaderFilter> filterList = null;

    public DgAnalysisPopulationReader() {
    }

    public DgAnalysisPopulation readAnalysisPopulation(DgAnalysisPopulation analysisPopulation, final Id runId, final String networkPath, final String firstPlanPath) {
        ScenarioImpl sc = (ScenarioImpl) ScenarioUtils.createScenario(ConfigUtils.createConfig());
        Population population;
        Network net;
        if (this.loadedNetworks.containsKey(networkPath)) {
            net = loadedNetworks.get(networkPath);
            sc.setNetwork(net);
        } else {
            ScenarioLoaderImpl sl = new ScenarioLoaderImpl(sc);
            sc.getConfig().network().setInputFile(networkPath);
            sl.loadNetwork();
            net = sc.getNetwork();
            this.loadedNetworks.put(networkPath, net);
        }
        population = loadPopulationFile(firstPlanPath, sc);
        new PlanCalcType().run(population);
        Plan plan;
        Activity act;
        for (Id id : population.getPersons().keySet()) {
            if (this.filterList != null) {
                boolean doContinue = false;
                for (DgAnalysisReaderFilter f : this.filterList) {
                    if (!f.doAcceptPerson(population.getPersons().get(id))) {
                        doContinue = true;
                    }
                }
                if (doContinue) {
                    continue;
                }
            }
            plan = population.getPersons().get(id).getSelectedPlan();
            act = ((PlanImpl) plan).getFirstActivity();
            DgPersonData personData;
            personData = analysisPopulation.getPersonData().get(id);
            if (personData == null) {
                personData = new DgPersonData();
                analysisPopulation.getPersonData().put(id, personData);
                personData.setFirstActivity(act);
                personData.setPersonId(id);
            }
            DgPlanData pd = new DgPlanData();
            pd.setScore(plan.getScore());
            plan.setPerson(null);
            pd.setPlan(plan);
            personData.getPlanData().put(runId, pd);
        }
        population = null;
        System.gc();
        return analysisPopulation;
    }

    /**
   * Load the plan file with the given path.
   *
   * @param filename
   *          the path to the filename
   * @return the Plans object containing the population
   */
    protected Population loadPopulationFile(final String filename, Scenario sc) {
        Population plans = sc.getPopulation();
        log.info("  reading plans xml file... ");
        PopulationReader plansReader = new MatsimPopulationReader(sc);
        plansReader.readFile(filename);
        log.info("  done");
        return plans;
    }

    public void addFilter(DgAnalysisReaderFilter filter) {
        if (this.filterList == null) {
            this.filterList = new ArrayList<DgAnalysisReaderFilter>();
        }
        this.filterList.add(filter);
    }
}
