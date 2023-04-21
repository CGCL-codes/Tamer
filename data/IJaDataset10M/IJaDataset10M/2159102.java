package playground.dsantani.launcher;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import org.matsim.api.core.v01.ScenarioImpl;
import org.matsim.core.config.Config;
import org.matsim.core.scenario.ScenarioLoaderImpl;
import playground.dsantani.PreProcessing;
import playground.dsantani.parking.StreetParkingDataFactory;
import playground.dsantani.utils.StringUtils;
import playground.dsantani.write.WriteActivities;
import playground.dsantani.write.WriteTrips;
import playground.scnadine.gpsProcessing.GPSCoord;
import playground.scnadine.gpsProcessing.GPSCoordFactory;
import playground.scnadine.gpsProcessing.GPSCoordFactoryPlakanda;
import playground.scnadine.gpsProcessing.GPSFileFilterRAW;

public class GenericLauncher {

    static File[] allFiles;

    public static void main(String[] args) throws IOException, ParseException {
        final ScenarioImpl scenario = new ScenarioLoaderImpl(args[0]).getScenario();
        final Config config = scenario.getConfig();
        final String CONFIG_MODULE = "parking";
        System.out.println(config.getModule(CONFIG_MODULE));
        File gpsDataDir = new File(config.findParam(CONFIG_MODULE, "sourcedir"));
        File targetDir = new File(config.findParam(CONFIG_MODULE, "targetdir"));
        File parkingDataFile = new File(config.findParam(CONFIG_MODULE, "garageParkingData"));
        File streetParkingDataFile = new File(config.findParam(CONFIG_MODULE, "streetParkingData"));
        StreetParkingDataFactory streetParkingFactory = new StreetParkingDataFactory();
        streetParkingFactory.createParkingData(streetParkingDataFile);
        GPSCoordFactory coordFactory = new GPSCoordFactoryPlakanda();
        GPSFileFilterRAW filter = new GPSFileFilterRAW();
        allFiles = gpsDataDir.listFiles(filter);
        for (int index = 0; index < allFiles.length; index++) {
            System.out.println("\nProcessing input file # " + (index + 1));
            coordFactory.createGPSCoords(allFiles[index]);
            PreProcessing preProcess = new PreProcessing(config, CONFIG_MODULE);
            preProcess.init(coordFactory);
            BufferedWriter out = new BufferedWriter(new FileWriter(new File(targetDir.getPath() + System.getProperty("file.separator") + coordFactory.getPersonId() + ".csv")));
            out.write("ID," + "DATE," + "X," + "Y\n");
            for (GPSCoord coord : coordFactory.getCoords()) {
                out.write(coord.getId() + "," + StringUtils.getRTimeStamp(coord.getTimestamp()) + "," + StringUtils.roundDecimalPlaces(coord.getX()) + "," + StringUtils.roundDecimalPlaces(coord.getY()) + "\n");
            }
            WriteActivities writeActivities = new WriteActivities();
            WriteTrips writeTrips = new WriteTrips();
            out = new BufferedWriter(new FileWriter(new File(targetDir.getPath() + System.getProperty("file.separator") + "activities-" + coordFactory.getPersonId() + ".csv")));
            out.write("PID," + "ACT_ID," + "START_DATE," + "S_X," + "S_Y," + "END_DATE," + "DIST," + "ACT_TYPE\n");
            out.write(writeActivities.run(preProcess.getActivities()));
            out.close();
            out = new BufferedWriter(new FileWriter(new File(targetDir.getPath() + System.getProperty("file.separator") + "trips-" + coordFactory.getPersonId() + ".csv")));
            out.write("PID," + "TRIP_ID," + "START_DATE," + "S_X," + "S_Y," + "END_DATE," + "T_STAGES," + "DIST," + "MODE_CHAIN\n");
            out.write(writeTrips.run(preProcess.getTrips()));
            out.close();
        }
    }
}
