package PRISM.RobotCtrl.Activities;

import java.util.Enumeration;
import java.util.Vector;
import PRISM.RobotCtrl.Activity;
import PRISM.RobotCtrl.DataSet;
import PRISM.RobotCtrl.SensorData;
import PRISM.RobotCtrl.VRWEvent;

public class UtilityDumpSonar extends Activity {

    int rPos[] = new int[3];

    int rVel[] = new int[2];

    public UtilityDumpSonar() {
        super("UtilityDumpSonar", "Dump Sonar Data");
    }

    public Vector<VRWEvent> process() {
        getImpl().get_position(rPos);
        getImpl().get_velocity(rVel);
        SensorData sd = getImpl().get_sensor_data();
        if (sd == null) return null;
        Enumeration eSets = sd.getDataSets();
        if (eSets == null) return null;
        while (eSets.hasMoreElements()) {
            DataSet ds = (DataSet) (eSets.nextElement());
            int[] data = ds.getData();
            String strName = ds.getName();
            if (strName.equalsIgnoreCase("Sonar")) {
                System.out.print(rPos[0] + " " + rPos[1] + " " + rPos[2] + " " + rVel[0] + " " + rVel[1] + " ");
                for (int i = 0; i < ds.getLength(); i++) {
                    System.out.print(data[i] + " ");
                }
                System.out.println(data[ds.getLength() - 1]);
            }
        }
        return null;
    }
}
