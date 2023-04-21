package playground.dsantani.gpsProcessing.tripAlgorithms;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.matsim.core.config.Config;
import playground.dsantani.gpsProcessing.GPSTrip;
import playground.dsantani.gpsProcessing.GPSTrips;

public class GPSTripWriteOutputJingmin extends GPSTripAlgorithm {

    private File targetdir;

    private boolean append;

    public GPSTripWriteOutputJingmin(boolean append, Config config, String CONFIG_MODULE) {
        super();
        targetdir = new File(config.findParam(CONFIG_MODULE, "targetdir"));
        this.append = append;
    }

    @Override
    public void run(GPSTrips trips) {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(new File(targetdir.getPath() + System.getProperty("file.separator") + "TripStatistics_Jingmin.txt"), this.append));
            if (!this.append) {
                out.write("PersonID\t");
                out.write("TripID\t");
                out.write("NumberOfGPSPoints\t");
                out.write("XStartingPoint\t");
                out.write("YStartingPoint\t");
                out.write("ZStartingPoint\t");
                out.write("StartDate\t");
                out.write("StartTime\t");
                out.write("XEndingPoint\t");
                out.write("YEndingPoint\t");
                out.write("ZEndingPoint\t");
                out.write("EndDate\t");
                out.write("EndTime\t");
                out.newLine();
                out.flush();
            }
            for (GPSTrip currentTrip : trips.getTrips()) {
                out.write(currentTrip.getPersonID() + "\t");
                out.write(currentTrip.getTripID() + "\t");
                out.write(currentTrip.getTripCoords().length + "\t");
                out.write(currentTrip.getTripCoords()[0].getX() + "\t");
                out.write(currentTrip.getTripCoords()[0].getY() + "\t");
                out.write(currentTrip.getTripCoords()[0].getZ() + "\t");
                out.write(currentTrip.getTripCoords()[0].getDate() + "\t");
                out.write(currentTrip.getTripCoords()[0].getTime() + "\t");
                out.write(currentTrip.getTripCoords()[currentTrip.getTripCoords().length - 1].getX() + "\t");
                out.write(currentTrip.getTripCoords()[currentTrip.getTripCoords().length - 1].getY() + "\t");
                out.write(currentTrip.getTripCoords()[currentTrip.getTripCoords().length - 1].getZ() + "\t");
                out.write(currentTrip.getTripCoords()[currentTrip.getTripCoords().length - 1].getDate() + "\t");
                out.write(currentTrip.getTripCoords()[currentTrip.getTripCoords().length - 1].getTime() + "\t");
                out.newLine();
            }
            out.close();
        } catch (IOException e) {
            System.out.println("Error while writing TripStatistics.txt");
        }
    }
}
