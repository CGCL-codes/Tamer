package playground.scnadine.gpsCompareWithFlammData.stopPointStagesAlgorithms;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.matsim.core.config.Config;
import playground.scnadine.gpsCompareWithFlammData.FlammStopPointStage;
import playground.scnadine.gpsCompareWithFlammData.FlammStopPointStages;

public class FlammStopPointStageWriter {

    private File targetdir;

    private boolean append;

    public FlammStopPointStageWriter(boolean append, Config config, String CONFIG_MODULE) {
        targetdir = new File(config.findParam(CONFIG_MODULE, "targetdir"));
        this.append = append;
    }

    public void run(FlammStopPointStages stages) {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(new File(targetdir.getPath() + System.getProperty("file.separator") + "FlammStopPointStages.txt"), this.append));
            if (!this.append) {
                out.write("PersonID\t");
                out.write("StageID\t");
                out.write("NumberOfCoords\t");
                out.write("IdStartingPoint\t");
                out.write("XStartingPoint\t");
                out.write("YStartingPoint\t");
                out.write("ZStartingPoint\t");
                out.write("StartingDate\t");
                out.write("StartingTime\t");
                out.write("IDEndingPoint\t");
                out.write("XEndingPoint\t");
                out.write("YEndingPoint\t");
                out.write("ZEndingPoint\t");
                out.write("EndingDate\t");
                out.write("EndingTime\t");
                out.write("Distanz\t");
                out.write("Duration\t");
                out.write("AverageSpeed\t");
                out.write("MedianSpeed\t");
                out.write("95PercentileSpeed\t");
                out.write("MedianAcc\t");
                out.write("95PercentileAcc\t");
                out.write("Mode");
                out.newLine();
                out.flush();
            }
            for (FlammStopPointStage stage : stages.getStages()) {
                out.write(stage.getPersonId() + "\t ");
                out.write(stage.getId() + "\t ");
                out.write(stage.getCoords().length + "\t ");
                out.write(stage.getCoords()[0].getId() + "\t");
                out.write(stage.getCoords()[0].getX() + "\t");
                out.write(stage.getCoords()[0].getY() + "\t");
                out.write(stage.getCoords()[0].getZ() + "\t");
                out.write(stage.getCoords()[0].getDate() + "\t");
                out.write(stage.getCoords()[0].getTime() + "\t");
                out.write(stage.getCoords()[stage.getCoords().length - 1].getId() + "\t");
                out.write(stage.getCoords()[stage.getCoords().length - 1].getX() + "\t");
                out.write(stage.getCoords()[stage.getCoords().length - 1].getY() + "\t");
                out.write(stage.getCoords()[stage.getCoords().length - 1].getZ() + "\t");
                out.write(stage.getCoords()[stage.getCoords().length - 1].getDate() + "\t");
                out.write(stage.getCoords()[stage.getCoords().length - 1].getTime() + "\t");
                out.write(stage.getDistance() + "\t");
                out.write(stage.getDuration() + "\t");
                out.write(stage.getAverageSpeed() + "\t");
                out.write(stage.getMedianSpeed() + "\t");
                out.write(stage.getNinetyFivePercSpeed() + "\t");
                out.write(stage.getMedianAcceleration() + "\t");
                out.write(stage.getNinetyFivePercAcceleration() + "\t");
                out.write(stage.getActualMode());
                out.newLine();
                out.flush();
            }
            out.close();
        } catch (IOException e) {
            System.out.println("Error while writing FlammStopPointStages.txt");
        }
    }
}
