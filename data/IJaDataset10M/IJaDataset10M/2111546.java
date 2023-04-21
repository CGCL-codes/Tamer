package playground.scnadine.gpsProcessingV2.stopPointAlgorithms;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.matsim.core.config.Config;
import playground.scnadine.gpsProcessingV2.GPSCoord;
import playground.scnadine.gpsProcessingV2.GPSStopPoint;
import playground.scnadine.gpsProcessingV2.GPSStopPoints;

public class GPSStopPointWriteAccelerometerData extends GPSStopPointAlgorithm {

    private File targetdir;

    public GPSStopPointWriteAccelerometerData(Config config, String CONFIG_MODULE) {
        super();
        targetdir = new File(config.findParam(CONFIG_MODULE, "targetdir"));
    }

    @Override
    public void run(GPSStopPoints stopPoints) {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(new File(targetdir.getPath() + System.getProperty("file.separator") + "StopPointCoordsWithAccelerometer_" + stopPoints.getStopPoint(0).getPersonId() + ".txt")));
            out.write("personId\tstopPointId\tX\tY\tZ\tDate\tTime\tXAcc\tYAcc\tZAcc\tAccLength");
            out.newLine();
            for (GPSStopPoint stopPoint : stopPoints.getStopPoints()) {
                for (GPSCoord coord : stopPoint.getCoords()) {
                    out.write(stopPoint.getPersonId() + "\t");
                    out.write(stopPoint.getId() + "\t");
                    out.write(coord.getX() + "\t");
                    out.write(coord.getY() + "\t");
                    out.write(coord.getZ() + "\t");
                    out.write(coord.getDate() + "\t");
                    out.write(coord.getTime() + "\t");
                    out.write(coord.getAccelerometerVector()[0] + "\t");
                    out.write(coord.getAccelerometerVector()[1] + "\t");
                    out.write(coord.getAccelerometerVector()[2] + "\t");
                    out.write(coord.getAccelerometerVectorLength() + "\n");
                }
            }
            out.close();
        } catch (IOException e) {
            System.out.println("Error while writing StopPointStageCoordsWithAccelerometer_" + stopPoints.getStopPoint(0).getPersonId() + ".txt");
        }
    }
}
