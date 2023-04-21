package playground.dsantani.postprocessChoiceSets;

import java.io.File;
import org.matsim.core.config.Config;
import org.matsim.core.gbl.Gbl;
import org.matsim.core.network.MatsimNetworkReader;
import org.matsim.core.network.NetworkLayer;
import playground.dsantani.postprocessChoiceSets.bikeAlgorithms.BikeChoiceSetStagesCalcRouteCharacteristics;
import playground.dsantani.postprocessChoiceSets.bikeAlgorithms.BikeChoiceSetStagesWriteDatFile;

public class AttributeCalculationForBikeChoiceSets {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        System.out.println("Start attribute calculation bike ...");
        Gbl.startMeasurement();
        final String CONFIG_MODULE = "PostprocessChoiceSets";
        final Config config = Gbl.createConfig(args);
        System.out.println("  reading the network xml file...");
        NetworkLayer network = new NetworkLayer();
        new MatsimNetworkReader(network).readFile(config.findParam(CONFIG_MODULE, "network"));
        System.out.println("  done.");
        System.out.println("====");
        System.out.println();
        File[] sourceDirChoiceSets = new File(config.findParam(CONFIG_MODULE, "sourceDirChoiceSets")).listFiles();
        System.out.println("Number of choice set files: " + sourceDirChoiceSets.length);
        System.out.println();
        System.out.println(" initialise attribute calculator ...");
        BikeChoiceSetStagesCalcRouteCharacteristics calcBikeRouteCharacteristics = new BikeChoiceSetStagesCalcRouteCharacteristics(config, CONFIG_MODULE, network);
        System.out.println("  done.");
        System.out.println("====");
        System.out.println();
        boolean firstFile = true;
        int maxChoiceSetSize = Integer.parseInt(config.findParam(CONFIG_MODULE, "maxChoiceSetSize"));
        System.out.println("maxChoiceSetSize: " + maxChoiceSetSize);
        for (File sourceFileChoiceSet : sourceDirChoiceSets) {
            System.out.println("calculating attributes for " + sourceFileChoiceSet.getName() + " ...");
            System.out.println("creating choice set stages...");
            BikeChoiceSetStages choiceSetStages = new BikeChoiceSetStages(network);
            choiceSetStages.createChoiceSetStages(sourceFileChoiceSet, network);
            System.out.println(" done.");
            System.out.println("Number of ChoiceSetStages created: " + choiceSetStages.getChoiceSetStages().size());
            Gbl.printElapsedTime();
            System.out.println("====");
            System.out.println();
            System.out.println("calc route attributes...");
            calcBikeRouteCharacteristics.run(choiceSetStages);
            System.out.println(" done.");
            Gbl.printElapsedTime();
            System.out.println("====");
            System.out.println();
            System.out.println("writing .dat file...");
            BikeChoiceSetStagesWriteDatFile writeDatFile = new BikeChoiceSetStagesWriteDatFile(!firstFile, config, CONFIG_MODULE, maxChoiceSetSize);
            writeDatFile.run(choiceSetStages);
            System.out.println(" done.");
            Gbl.printElapsedTime();
            System.out.println("====");
            System.out.println();
            firstFile = false;
        }
        System.out.println("Calculation of bike route attributes completed.");
        Gbl.printElapsedTime();
    }
}
