package playground.dsantani.gpsProcessing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import org.matsim.core.config.Config;
import org.matsim.core.gbl.Gbl;
import org.matsim.core.network.MatsimNetworkReader;
import org.matsim.core.network.NetworkLayer;
import playground.dsantani.gpsProcessing.activityAlgorithms.GPSActivityCalcCharacteristics;
import playground.dsantani.gpsProcessing.coordAlgorithms.GPSCoordDetermineActivityBreakPoints;
import playground.dsantani.gpsProcessing.coordAlgorithms.GPSCoordFilteringAndSmoothing;
import playground.dsantani.gpsProcessing.modeChainAlgorithms.GPSModeChainWriteStatistics;
import playground.dsantani.gpsProcessing.modeChainAlgorithms.GPSModeTransitionWriteStatistics;
import playground.dsantani.gpsProcessing.tripAlgorithms.GPSTripCalcMostProbableModes;
import playground.dsantani.gpsProcessing.tripAlgorithms.GPSTripCalcStageCharacteristics;
import playground.dsantani.gpsProcessing.tripAlgorithms.GPSTripCalcTripCharacteristics;
import playground.dsantani.gpsProcessing.tripAlgorithms.GPSTripCorrectModes;
import playground.dsantani.gpsProcessing.tripAlgorithms.GPSTripDetermineModeChangingPoints;
import playground.dsantani.gpsProcessing.tripAlgorithms.GPSTripGenerateStages;
import playground.dsantani.gpsProcessing.tripAlgorithms.GPSTripMapMatching;
import playground.dsantani.gpsProcessing.tripAlgorithms.GPSTripModeDetection;

public class GPSProcessing {

    static File[] allFiles;

    public static void main(String[] args) throws IOException, ParseException {
        System.out.println("Hi ihr alle wie geht es euch so? Ich hoffe gut!");
        Gbl.startMeasurement();
        final Config config = Gbl.createConfig(args);
        final String CONFIG_MODULE = "GPS";
        System.out.println(config.getModule(CONFIG_MODULE));
        File sourcedir = new File(config.findParam(CONFIG_MODULE, "sourcedir"));
        String dataorigin = config.findParam(CONFIG_MODULE, "dataorigin");
        System.out.println("Data origin: " + dataorigin);
        GPSCoordFactory gpsFactory = null;
        GPSPersons persons = new GPSPersons();
        GPSActivities activities = new GPSActivities();
        GPSTrips trips = new GPSTrips(config, CONFIG_MODULE);
        GPSModeChainFactory modeChains = new GPSModeChainFactoryTripStatistics(trips);
        GPSModeChainFactory modeChainsMZ2005 = new GPSModeChainFactoryMZ2005(config, CONFIG_MODULE);
        GPSModeTransitions modeTransitions = new GPSModeTransitions(config, CONFIG_MODULE);
        GPSModeTransitions modeTransitionsMZ2005 = new GPSModeTransitions(config, CONFIG_MODULE);
        HashMap<String, NetworkLayer> networksForMapMatching = new HashMap<String, NetworkLayer>();
        boolean evaluateMZ2005 = Boolean.parseBoolean(config.findParam(CONFIG_MODULE, "evaluateMZ2005"));
        if (evaluateMZ2005) {
            modeChainsMZ2005.createModeChains();
            System.out.println("NumberOfModeChainsInMZ2005: " + modeChainsMZ2005.getModeChains().size());
        }
        boolean gpsWriteModeChainsMZ2005 = Boolean.parseBoolean(config.findParam(CONFIG_MODULE, "gpsWriteModeChainsMZ2005"));
        if (gpsWriteModeChainsMZ2005) {
            GPSModeChainWriteStatistics writeModeChains = new GPSModeChainWriteStatistics("MZ2005", config, CONFIG_MODULE);
            writeModeChains.run(modeChainsMZ2005);
            System.out.println("Done writing ModeChainsMZ2005.");
        }
        boolean correctModeTransitions = Boolean.parseBoolean(config.findParam(CONFIG_MODULE, "correctModeTransitions"));
        if (correctModeTransitions) {
            modeTransitionsMZ2005.createModeTransitions(modeChainsMZ2005);
            System.out.println("Number of mode transitions: " + modeTransitionsMZ2005.getModeTransitions().size());
        }
        boolean gpsWriteModeTransitionsMZ2005 = Boolean.parseBoolean(config.findParam(CONFIG_MODULE, "gpsWriteModeTransitionsMZ2005"));
        if (gpsWriteModeTransitionsMZ2005) {
            GPSModeTransitionWriteStatistics writeModeTransitions = new GPSModeTransitionWriteStatistics("MZ2005", config, CONFIG_MODULE);
            writeModeTransitions.run(modeTransitionsMZ2005);
            System.out.println("Done writing ModeTransitionsMZ2005.");
        }
        boolean runMapMatching = Boolean.parseBoolean(config.findParam(CONFIG_MODULE, "runMapMatching"));
        if (runMapMatching) {
            int numberOfModesForMapMatching = Integer.parseInt(config.findParam(CONFIG_MODULE, "numberOfModesForMapMatching"));
            String[] modes = new String[numberOfModesForMapMatching];
            for (int i = 0; i < numberOfModesForMapMatching; i++) {
                modes[i] = config.findParam(CONFIG_MODULE, "mapMatchingMode" + i);
            }
            for (String mode : modes) {
                NetworkLayer network = new NetworkLayer();
                new MatsimNetworkReader(network).readFile(config.findParam(CONFIG_MODULE, "network" + mode));
                networksForMapMatching.put(mode, network);
            }
        }
        if (dataorigin.equals("plakanda")) {
            gpsFactory = new GPSCoordFactoryPlakanda();
            GPSFileFilterRAW filter = new GPSFileFilterRAW();
            allFiles = sourcedir.listFiles(filter);
        } else if (dataorigin.equals("flamm")) {
            gpsFactory = new GPSCoordFactoryFlamm();
            GPSFileFilterFLAMM filter = new GPSFileFilterFLAMM();
            allFiles = sourcedir.listFiles(filter);
        } else if (dataorigin.equals("cosim")) {
            gpsFactory = new GPSCoordFactoryCosim();
            GPSFileFilterTXT filter = new GPSFileFilterTXT();
            allFiles = sourcedir.listFiles(filter);
        } else if (dataorigin.equals("jingmin")) {
            gpsFactory = new GPSCoordFactoryJingmin();
            GPSFileFilterTXT filter = new GPSFileFilterTXT();
            allFiles = sourcedir.listFiles(filter);
        }
        ArrayList<Integer> filteredPersons = new ArrayList<Integer>();
        boolean filterPersons = Boolean.parseBoolean(config.findParam(CONFIG_MODULE, "filterPersons"));
        if (filterPersons) {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(new File(config.findParam(CONFIG_MODULE, "inputFileFilter")))));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                filteredPersons.add(Integer.parseInt(inputLine));
            }
        }
        for (int i = 0; i < allFiles.length; i++) {
            System.out.println((i + 1) + ". Datei:");
            gpsFactory.createGPSCoords(allFiles[i]);
            System.out.println("Number of GPS coords: " + gpsFactory.getCoords().size());
            if (!filteredPersons.contains(gpsFactory.getPersonID())) {
                GPSCoordFilteringAndSmoothing filteringAndSmoothing = new GPSCoordFilteringAndSmoothing(config, CONFIG_MODULE);
                filteringAndSmoothing.run(gpsFactory);
                GPSCoordDetermineActivityBreakPoints activityBreakPoints = new GPSCoordDetermineActivityBreakPoints(config, CONFIG_MODULE);
                activityBreakPoints.run(gpsFactory);
                trips.createGPSTrips(gpsFactory);
                System.out.println("Number of trips: " + trips.getTrips().size());
                activities.createActivities(gpsFactory);
                System.out.println("Number of activities: " + activities.getActivities().size());
                GPSActivityCalcCharacteristics activityCharacteristicsCalculator = new GPSActivityCalcCharacteristics(config, CONFIG_MODULE);
                activityCharacteristicsCalculator.run(activities);
                System.out.println("Calculation activity characteristics done.");
                GPSTripCalcTripCharacteristics tripCharacteristicsCalculator = new GPSTripCalcTripCharacteristics();
                tripCharacteristicsCalculator.run(trips);
                System.out.println("Calculation trip characteristics done.");
                GPSTripDetermineModeChangingPoints determineMTPs = new GPSTripDetermineModeChangingPoints(config, CONFIG_MODULE);
                determineMTPs.run(trips);
                System.out.println("Determination mode transfer points done.");
                GPSTripGenerateStages generateStages = new GPSTripGenerateStages();
                generateStages.run(trips);
                System.out.println("Stage generation done.");
                GPSTripCalcStageCharacteristics stageCharacteristicsCalculator = new GPSTripCalcStageCharacteristics();
                stageCharacteristicsCalculator.run(trips);
                System.out.println("Calculation stage of characteristics done.");
                GPSTripModeDetection detectModes = new GPSTripModeDetection(config, CONFIG_MODULE);
                detectModes.run(trips);
                System.out.println("Mode detection done.");
                GPSTripCorrectModes correctingModeChains = new GPSTripCorrectModes(modeTransitionsMZ2005, config, CONFIG_MODULE);
                correctingModeChains.run(trips);
                System.out.println("Done correcting mode chains.");
                GPSTripCalcMostProbableModes mostProbableModeChain = new GPSTripCalcMostProbableModes();
                mostProbableModeChain.run(trips);
                System.out.println("Done calculating most probable modes.");
                if (runMapMatching) {
                    for (String mode : networksForMapMatching.keySet()) {
                        GPSTripMapMatching mapMatching = new GPSTripMapMatching(config, CONFIG_MODULE, mode, networksForMapMatching.get(mode));
                        mapMatching.run(trips);
                    }
                }
                GPSPerson currentPerson = persons.createGPSPerson(gpsFactory.getPersonID());
                GPSCalcPersonStatistics calcPersonStatistics = new GPSCalcPersonStatistics(gpsFactory, trips, activities);
                calcPersonStatistics.run(currentPerson);
                persons.addPerson(currentPerson);
                boolean gpsWriteModeChains = Boolean.parseBoolean(config.findParam(CONFIG_MODULE, "gpsWriteModeChains"));
                if (gpsWriteModeChains) {
                    modeChains.createModeChains();
                    System.out.println(modeChains.getModeChains().size() + " different mode chains are now stored.");
                }
                boolean append = false;
                if (i != 0) append = true;
                GPSWriteIndividualOutput output = new GPSWriteIndividualOutput(gpsFactory, trips, activities, append, config, CONFIG_MODULE);
                output.run();
                if (i == allFiles.length - 1) {
                    GPSWriteOverallOutput overallOutput = new GPSWriteOverallOutput(config, CONFIG_MODULE);
                    overallOutput.run(persons);
                    overallOutput.run(modeChains);
                    boolean gpsWriteModeTransitions = Boolean.parseBoolean(config.findParam(CONFIG_MODULE, "gpsWriteModeTransitions"));
                    if (gpsWriteModeTransitions) {
                        modeTransitions.createModeTransitions(modeChains);
                    }
                    overallOutput.run("Trips", modeTransitions);
                }
            }
            gpsFactory.initialiseFactory();
            activities.initialiseActivities();
            trips.initialiseTrips();
        }
        boolean gpsSplitZGPS = Boolean.parseBoolean(config.findParam(CONFIG_MODULE, "gpsSplitZGPS"));
        if (gpsSplitZGPS) {
            int numberOfWriteZGPS = Integer.parseInt(config.findParam(CONFIG_MODULE, "numberOfWriteZGPS"));
            for (int i = 0; i < numberOfWriteZGPS; i++) {
                String mode = config.findParam(CONFIG_MODULE, "modeForWriteZGPS" + i);
                GPSSplitZGPS splitZGPS = new GPSSplitZGPS(config, CONFIG_MODULE, mode);
                splitZGPS.run();
            }
            System.out.println("Done splitting ZGPS.");
        }
        System.out.println("Done");
        Gbl.printElapsedTime();
    }
}
