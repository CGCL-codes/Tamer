package PRISM.RobotCtrl.Activities;

import java.util.Vector;
import PRISM.RobotCtrl.Activity;
import PRISM.RobotCtrl.VRWEvent;

public class MonitorStatus extends Activity {

    public MonitorStatus() {
        super("MonitorStatus", "Status monitor");
    }

    public Vector<VRWEvent> process() {
        long status = getImpl().m_sensorData.status;
        long error = getImpl().m_sensorData.error;
        Vector<VRWEvent> ret = new Vector<VRWEvent>();
        if (error > 0) ret.addElement(new VRWEvent("error", new String[] { "" + error }));
        boolean emergencystop = (((int) status & 64) > 0);
        if (emergencystop) ret.addElement(new VRWEvent("robot", new String[] { "emergency_stop" }));
        return ret;
    }
}
