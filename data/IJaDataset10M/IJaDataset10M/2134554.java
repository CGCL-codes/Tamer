package playground.michalm.vrp.run.offline;

import java.io.IOException;
import java.util.Arrays;
import org.matsim.api.core.v01.Scenario;
import org.matsim.core.controler.Controler;
import pl.poznan.put.vrp.dynamic.data.VrpData;
import pl.poznan.put.vrp.dynamic.data.file.LacknerReader;
import playground.michalm.vrp.data.MatsimVrpData;
import playground.michalm.vrp.data.network.MatsimVertexImpl;
import playground.michalm.vrp.data.network.shortestpath.full.*;

public class SimLauncherWithArcEstimator {

    public static void main(String... args) throws IOException {
        String dirName;
        String cfgFileName;
        String vrpDirName;
        String vrpStaticFileName;
        String vrpArcTimesFileName;
        String vrpArcCostsFileName;
        String vrpArcPathsFileName;
        if (args.length == 1 && args[0].equals("test")) {
            dirName = "D:\\PP-dyplomy\\2010_11-mgr\\burkat_andrzej\\siec1\\";
            cfgFileName = dirName + "config-verB.xml";
            vrpDirName = dirName + "dvrp\\";
            vrpStaticFileName = "A101.txt";
            vrpArcTimesFileName = vrpDirName + "arc_times.txt";
            vrpArcCostsFileName = vrpDirName + "arc_costs.txt";
            vrpArcPathsFileName = vrpDirName + "arc_paths.txt";
        } else if (args.length == 7) {
            dirName = args[0];
            cfgFileName = dirName + args[1];
            vrpDirName = dirName + args[2];
            vrpStaticFileName = args[3];
            vrpArcTimesFileName = vrpDirName + args[4];
            vrpArcCostsFileName = vrpDirName + args[5];
            vrpArcPathsFileName = vrpDirName + args[6];
        } else {
            throw new IllegalArgumentException("Incorrect program arguments: " + Arrays.toString(args));
        }
        Controler controler = new Controler(new String[] { cfgFileName });
        controler.setOverwriteFiles(true);
        controler.run();
        Scenario scenario = controler.getScenario();
        VrpData vrpData = LacknerReader.parseStaticFile(vrpDirName, vrpStaticFileName, MatsimVertexImpl.createFromXYBuilder(scenario));
        MatsimVrpData data = new MatsimVrpData(vrpData, scenario);
        FullShortestPathsFinder spf = new FullShortestPathsFinder(data);
        FullShortestPath[][] shortestPaths = spf.findShortestPaths(controler.getTravelTimeCalculator(), controler.getLeastCostPathCalculatorFactory());
        spf.writeShortestPaths(shortestPaths, vrpArcTimesFileName, vrpArcCostsFileName, vrpArcPathsFileName);
    }
}
