package playground.telaviv.locationchoice.analysis;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.population.Activity;
import org.matsim.api.core.v01.population.Person;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.events.EventsReaderTXTv1;
import org.matsim.core.events.EventsUtils;
import org.matsim.core.network.MatsimNetworkReader;
import org.matsim.core.population.MatsimPopulationReader;
import org.matsim.core.scenario.ScenarioImpl;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.core.trafficmonitoring.TravelTimeCalculator;
import org.matsim.core.utils.collections.Tuple;
import org.matsim.core.utils.geometry.transformations.TransformationFactory;
import playground.telaviv.locationchoice.ExtendedLocationChoicePlanModule;
import playground.telaviv.locationchoice.ExtendedLocationChoiceProbabilityCreator;
import playground.telaviv.zones.ZoneMapping;

public class LegZoneAnalyzer {

    private static final Logger log = Logger.getLogger(LegZoneAnalyzer.class);

    private static String basePath = "../../matsim/mysimulations/telaviv/";

    private static String runPath = "output_JDEQSim_with_location_choice_without_TravelTime/";

    private static String networkFile = basePath + "input/network.xml";

    private static String populationFile = basePath + runPath + "ITERS/it.100/100.plans.xml.gz";

    private String eventsFile = basePath + runPath + "/ITERS/it.100/100.events.txt.gz";

    private String shoppingOutFile = basePath + runPath + "100.shoppingLegsCarProbabilities.txt";

    private String otherOutFile = basePath + runPath + "100.otherLegsCarProbabilities.txt";

    private String workOutFile = basePath + runPath + "100.workLegsCarProbabilities.txt";

    private String educationOutFile = basePath + runPath + "100.educationLegsCarProbabilities.txt";

    private String delimiter = "\t";

    private Charset charset = Charset.forName("UTF-8");

    private Scenario scenario;

    private ZoneMapping zoneMapping;

    private ExtendedLocationChoiceProbabilityCreator extendedLocationChoiceProbabilityCreator;

    private Map<Id, List<Integer>> shoppingActivities;

    private Map<Id, List<Integer>> otherActivities;

    private Map<Id, List<Integer>> workActivities;

    private Map<Id, List<Integer>> educationActivities;

    private List<Double> shoppingProbabilities;

    private List<Double> otherProbabilities;

    private List<Double> workProbabilities;

    private List<Double> educationProbabilities;

    public static void main(String[] args) {
        Scenario scenario = (ScenarioImpl) ScenarioUtils.createScenario(ConfigUtils.createConfig());
        new MatsimNetworkReader(scenario).readFile(networkFile);
        new MatsimPopulationReader(scenario).readFile(populationFile);
        new LegZoneAnalyzer(scenario);
    }

    public LegZoneAnalyzer(Scenario scenario) {
        this.scenario = scenario;
        TravelTimeCalculator travelTime = new TravelTimeCalculator(scenario.getNetwork(), scenario.getConfig().travelTimeCalculator());
        if (eventsFile != null) {
            EventsManager eventsManager = (EventsManager) EventsUtils.createEventsManager();
            eventsManager.addHandler(travelTime);
            log.info("Processing events file to get initial travel times...");
            EventsReaderTXTv1 reader = new EventsReaderTXTv1(eventsManager);
            reader.readFile(eventsFile);
            eventsManager.removeHandler(travelTime);
            eventsManager = null;
        }
        log.info("Identifying shopping activities...");
        ExtendedLocationChoicePlanModule elcpm = new ExtendedLocationChoicePlanModule(scenario, null);
        shoppingActivities = elcpm.getShoppingActivities();
        otherActivities = elcpm.getOtherActivities();
        workActivities = elcpm.getWorkActivities();
        educationActivities = elcpm.getEducationActivities();
        log.info("done.");
        log.info("Creating ZoneMapping...");
        zoneMapping = new ZoneMapping(scenario, TransformationFactory.getCoordinateTransformation("EPSG:2039", "WGS84"));
        log.info("done.");
        log.info("Creating LocationChoiceProbabilityCreator...");
        extendedLocationChoiceProbabilityCreator = new ExtendedLocationChoiceProbabilityCreator(scenario, travelTime);
        extendedLocationChoiceProbabilityCreator.calculateDynamicProbabilities();
        extendedLocationChoiceProbabilityCreator.calculateTotalProbabilities();
        log.info("done.");
        log.info("Get probabilities of selected zones...");
        shoppingProbabilities = getProbabilities(shoppingActivities, 0);
        otherProbabilities = getProbabilities(otherActivities, 1);
        workProbabilities = getProbabilities(workActivities, 2);
        educationProbabilities = getProbabilities(educationActivities, 3);
        log.info("done.");
        log.info("Writing probabilities to file...");
        writeFile(shoppingOutFile, shoppingProbabilities);
        writeFile(otherOutFile, otherProbabilities);
        writeFile(workOutFile, workProbabilities);
        writeFile(educationOutFile, educationProbabilities);
        log.info("done.");
    }

    private List<Double> getProbabilities(Map<Id, List<Integer>> activities, int type) {
        List<Double> probabilities = new ArrayList<Double>();
        for (Person person : scenario.getPopulation().getPersons().values()) {
            List<Integer> activitiesList = activities.get(person.getId());
            if (activitiesList == null) continue;
            Activity homeActivity = (Activity) person.getSelectedPlan().getPlanElements().get(0);
            for (int index : activitiesList) {
                Activity activity = (Activity) person.getSelectedPlan().getPlanElements().get(index);
                double probability = getProbability(homeActivity, activity, type);
                probabilities.add(probability);
            }
        }
        return probabilities;
    }

    private double getProbability(Activity homeActivity, Activity activity, int type) {
        Id homeLinkId = homeActivity.getLinkId();
        Id shoppingLinkId = activity.getLinkId();
        int homeTAZ = zoneMapping.getLinkTAZ(homeLinkId);
        int activityTAZ = zoneMapping.getLinkTAZ(shoppingLinkId);
        Tuple<int[], double[]> tuple = extendedLocationChoiceProbabilityCreator.getFromZoneProbabilities(type, homeTAZ, activity.getEndTime());
        int[] indices = tuple.getFirst();
        double[] probabilities = tuple.getSecond();
        for (int i = 0; i < indices.length; i++) {
            if (indices[i] == activityTAZ) return probabilities[i];
        }
        return 0.0;
    }

    private void writeFile(String outFile, List<Double> probabilities) {
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        BufferedWriter bw = null;
        try {
            fos = new FileOutputStream(outFile);
            osw = new OutputStreamWriter(fos, charset);
            bw = new BufferedWriter(osw);
            bw.write("probability" + "\n");
            for (Double probability : probabilities) {
                bw.write(String.valueOf(probability));
                bw.write("\n");
            }
            bw.close();
            osw.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
