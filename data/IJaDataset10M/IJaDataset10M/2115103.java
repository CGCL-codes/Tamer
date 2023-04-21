package playground.dsantani.launcher;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.matsim.api.core.v01.ScenarioImpl;
import org.matsim.core.config.Config;
import org.matsim.core.network.MatsimNetworkReader;
import org.matsim.core.network.NetworkLayer;
import org.matsim.core.scenario.ScenarioLoaderImpl;
import playground.dsantani.CarStage;
import playground.dsantani.ParkingSearch;
import playground.dsantani.ParkingSearchTime;
import playground.dsantani.PreProcessing;
import playground.dsantani.parking.GarageParkingDataFactory;
import playground.dsantani.parking.StreetParkingDataFactory;
import playground.dsantani.utils.StringUtils;
import playground.scnadine.gpsProcessing.GPSCoordFactory;
import playground.scnadine.gpsProcessing.GPSCoordFactoryPlakanda;
import playground.scnadine.gpsProcessing.GPSFileFilterRAW;
import playground.scnadine.gpsProcessing.GPSTrip;
import playground.scnadine.gpsProcessing.GPSTrips;

public class ParkingSearchTimeLauncher extends AbstractLauncher {

    static File[] allFiles;

    private Config config;

    private String CONFIG_MODULE;

    private NetworkLayer network;

    @Override
    protected void init(Config config, String CONFIG_MODULE) {
        super.init(config, CONFIG_MODULE);
        this.config = config;
        this.CONFIG_MODULE = CONFIG_MODULE;
    }

    @Override
    public void launch() throws IOException, ParseException {
        GPSCoordFactory coordFactory = new GPSCoordFactoryPlakanda();
        GPSFileFilterRAW filter = new GPSFileFilterRAW();
        allFiles = gpsDataDir.listFiles(filter);
        GarageParkingDataFactory garageParkingFactory = new GarageParkingDataFactory();
        garageParkingFactory.createParkingData(garageParkingDataFile);
        StreetParkingDataFactory streetParkingFactory = new StreetParkingDataFactory();
        streetParkingFactory.createParkingData(streetParkingDataFile);
        BufferedWriter out = new BufferedWriter(new FileWriter(new File(targetDir.getPath() + System.getProperty("file.separator") + "car-stages-trips-activities-search-" + garageParkingRadius + ".csv")));
        out.write("PID," + "CTID," + "ACTUAL_DIST," + "SHORTEST_DIST\n");
        for (int index = 0; index < allFiles.length; index++) {
            System.out.println("\nProcessing input file # " + (index + 1));
            List<CarStage> carStages = new ArrayList<CarStage>();
            PreProcessing preProcess = new PreProcessing(config, CONFIG_MODULE);
            coordFactory.createGPSCoords(allFiles[index]);
            preProcess.init(coordFactory);
            GPSTrips trips = preProcess.getTrips();
            for (GPSTrip currentTrip : trips.getTrips()) {
                String currentTripModeChain = currentTrip.getMostProbableModeChain().getName();
                List<Integer> carIndex = StringUtils.indexOfSubstring(currentTripModeChain, "c");
                for (int i = 0; i < carIndex.size(); i++) {
                    int carStageId = carIndex.get(i);
                    CarStage carStage = new CarStage(currentTrip.getStages().getStage(carStageId));
                    ParkingSearch parkingSearch = new ParkingSearch(garageParkingFactory, streetParkingFactory);
                    parkingSearch.searchParking(carStage, searchRadius, garageParkingRadius, streetParkingDistance);
                    carStages.add(carStage);
                }
                ParkingSearchTime parkingSearchTime = new ParkingSearchTime(config, CONFIG_MODULE, this.getNetwork(), carStages, garageParkingFactory, streetParkingFactory);
                out.write(parkingSearchTime.run(parkingSearchRadius));
            }
            coordFactory.initialiseFactory();
            carStages.clear();
        }
        out.close();
    }

    public static void main(String[] args) throws IOException, ParseException {
        final ScenarioImpl scenario = new ScenarioLoaderImpl(args[0]).getScenario();
        final Config config = scenario.getConfig();
        final String CONFIG_MODULE = "parking";
        HashMap<String, NetworkLayer> networksForMapMatching = new HashMap<String, NetworkLayer>();
        NetworkLayer network = scenario.getNetwork();
        new MatsimNetworkReader(scenario).readFile(config.findParam(CONFIG_MODULE, "networkCar"));
        networksForMapMatching.put("Car", network);
        ParkingSearchTimeLauncher launcher = new ParkingSearchTimeLauncher();
        launcher.init(config, CONFIG_MODULE);
        launcher.setNetwork(network);
        launcher.launch();
    }

    public NetworkLayer getNetwork() {
        return network;
    }

    public void setNetwork(NetworkLayer network) {
        this.network = network;
    }
}
