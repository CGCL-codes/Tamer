package playground.thibautd.analysis.possiblesharedrides;

import org.matsim.api.core.v01.Scenario;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.scenario.ScenarioUtils;

/**
 * Corrects a data file for which distances were got from route
 * @author thibautd
 */
public class CorrectDataFile {

    public static void main(final String[] args) {
        String configFile = args[0];
        String dataFile = args[1];
        String outputFile = args[2];
        CountPossibleSharedRideNew counter = new CountPossibleSharedRideNew(0, 0);
        Scenario scenario = ScenarioUtils.loadScenario(ConfigUtils.loadConfig(configFile));
        counter.correctFileFromPlans(dataFile, scenario.getPopulation(), scenario.getNetwork(), outputFile);
    }
}
