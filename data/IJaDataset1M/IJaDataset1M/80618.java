package herschel.phs.prophandler.tools.missionevolver;

import herschel.phs.prophandler.tools.missionevolver.gui.MissionEvolverFrame;
import javax.swing.SwingUtilities;

public class MissionEvolverMain {

    public static String RESOURCE_BASE_DIR = "/herschel/phs/prophandler/tools/missionevolver/image/";

    public static void main(String argv[]) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                MissionEvolverFrame v = new MissionEvolverFrame();
            }
        });
    }
}
