package playground.scnadine.gpsProcessingV2.stopPointStageAlgorithms;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Link;
import org.matsim.core.config.Config;
import playground.scnadine.gpsProcessingV2.GPSStopPointStage;
import playground.scnadine.gpsProcessingV2.GPSStopPointStages;

public class GPSStopPointStageWriteChosenRoutesForGIS extends GPSStopPointStageAlgorithm {

    private File targetdir;

    private String[] modes;

    public GPSStopPointStageWriteChosenRoutesForGIS(Config config, String CONFIG_MODULE) {
        this.targetdir = new File(config.findParam(CONFIG_MODULE, "targetdir"));
        int numberOfModesForMapMatching = Integer.parseInt(config.findParam(CONFIG_MODULE, "numberOfModesForMapMatching"));
        this.modes = new String[numberOfModesForMapMatching];
        for (int i = 0; i < numberOfModesForMapMatching; i++) {
            modes[i] = config.findParam(CONFIG_MODULE, "mapMatchingMode" + i);
        }
    }

    @Override
    public void run(GPSStopPointStages stages) {
        if (!stages.getStages().isEmpty()) {
            for (String mode : modes) {
                try {
                    BufferedWriter out = new BufferedWriter(new FileWriter(new File(targetdir.getPath() + System.getProperty("file.separator") + "ChosenRoutesGIS_" + stages.getStage(0).getPersonId() + "_" + mode + ".txt")));
                    out.write("personId\tstageId\tlinkId\tX1\tY1\tX2\tY2\n");
                    out.flush();
                    for (GPSStopPointStage stage : stages.getStages()) {
                        if (stage.getMostProbableMode().equals(mode.toLowerCase()) && !stage.isFilteredForBadMapMatching() && stage.getChosenRoute() != null && stage.getChosenRoute().getLinkIds().size() >= 3) {
                            for (Id linkId : stage.getChosenRoute().getLinkIds()) {
                                Link link = stage.getChosenRouteNetwork().getLinks().get(linkId);
                                out.write(stage.getPersonId() + "\t");
                                out.write(stage.getId() + "\t");
                                out.write(link.getId() + "\t");
                                out.write(link.getFromNode().getCoord().getX() + "\t");
                                out.write(link.getFromNode().getCoord().getY() + "\t");
                                out.write(link.getToNode().getCoord().getX() + "\t");
                                out.write(link.getToNode().getCoord().getY() + "");
                                out.newLine();
                                out.write(link.getFromNode().getCoord().getX() + "\t");
                                out.write(link.getFromNode().getCoord().getY() + "");
                                out.newLine();
                                out.write(link.getToNode().getCoord().getX() + "\t");
                                out.write(link.getToNode().getCoord().getY() + "");
                                out.newLine();
                                out.write("END");
                                out.newLine();
                            }
                        }
                    }
                    out.close();
                } catch (IOException e) {
                    System.out.println("Error while writing ChosenRoutesForGIS.");
                }
            }
        }
    }
}
