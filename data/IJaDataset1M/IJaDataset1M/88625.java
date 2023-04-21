package sim.app.ubik;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import sim.app.ubik.Ubik;
import sim.app.ubik.building.BuildingConfigurationGui;
import sim.app.ubik.building.OfficeFloor;

/**
 *
 * @author fox
 */
public class Batch {

    public static final int CASES = 1;

    public static void main(String[] args) throws FileNotFoundException, IOException {
        ArrayList buildingParameters = new ArrayList();
        buildingParameters.add("3");
        buildingParameters.add("100");
        buildingParameters.add("100");
        buildingParameters.add("3");
        buildingParameters.add("1");
        buildingParameters.add("4");
        buildingParameters.add("2");
        buildingParameters.add("2");
        buildingParameters.add("2");
        buildingParameters.add("10");
        buildingParameters.add(new Boolean("true"));
        buildingParameters.add(new Boolean("true"));
        if (!buildingParameters.isEmpty()) {
            executeNUbiks(buildingParameters, 2, 300);
            executeNUbiks(buildingParameters, 0, 300);
            executeNUbiks(buildingParameters, 1, 300);
            LoggerMixer.main(null);
        }
    }

    public static ArrayList obtainBuildingParameters() {
        ArrayList buildingParameters = new ArrayList();
        BuildingConfigurationGui bcg = new BuildingConfigurationGui(buildingParameters);
        bcg.setVisible(true);
        return buildingParameters;
    }

    public static void executeNUbiks(ArrayList buildingParameters, int amiApplication, int nAgents) {
        for (int i = 0; i < CASES; i++) {
            long seed = System.currentTimeMillis();
            Ubik u = new Ubik(seed, buildingParameters);
            u.start();
            u.setAmIApplication(amiApplication);
            u.numWorkers = nAgents;
            LoggerAgent la = new LoggerAgent(seed, buildingParameters, u.obtainPositionForFire());
            LoggerAgent.fixSkipSteps(49);
            u.fixLogger(la);
            executeAnUbik(u);
            System.out.println("Execution " + i + " for the AmIApplication " + u.amiApplications[amiApplication] + " " + getDate());
        }
    }

    public static void executeAnUbik(Ubik u) {
        u.start();
        while (true) {
            if (!u.schedule.step(u)) break;
        }
        u.finish();
    }

    private static String getDate() {
        Calendar calendario = Calendar.getInstance();
        String r = calendario.getTime().toString();
        return r;
    }
}
