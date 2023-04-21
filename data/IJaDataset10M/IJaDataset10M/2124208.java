package playground.jjoubert.CommercialDemand.WithinTraffic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;
import org.apache.log4j.Logger;

public class MyWithinTrafficDemandGenerator01 {

    private final Logger log;

    private final String studyArea;

    private final String root;

    public MyWithinTrafficDemandGenerator01(String root, String studyArea) {
        log = Logger.getLogger(MyWithinTrafficDemandGenerator01.class);
        this.studyArea = studyArea;
        this.root = root;
        final int dimensionStart = 24;
        final int dimensionActivities = 21;
        final int dimensionDuration = 49;
    }

    private Collection<Integer> findVehicleList() {
        Collection<Integer> vehicleList = new ArrayList<Integer>();
        String vehicleStatsFilename = root + studyArea + "/Activities/" + studyArea + "VehicleStats.txt";
        try {
            Scanner input = new Scanner(new BufferedReader(new FileReader(new File(vehicleStatsFilename))));
            input.nextLine();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return vehicleList;
    }

    /**
	 * TODO Build cumulative distribution function.
	 */
    public void createWithinDemand(Integer populationSize, Integer firstIndex) {
    }
}
