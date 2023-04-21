package PRISM.RobotCtrl.Behaviors;

import java.util.Vector;
import PRISM.RobotCtrl.Behavior;
import PRISM.RobotCtrl.BehaviorInterface;
import PRISM.RobotCtrl.SensorData;
import PRISM.RobotCtrl.Utility;
import PRISM.RobotCtrl.VRWEvent;

/**
 * 
 * @author Mauro Dragone
 */
public class TurnLeft extends Behavior implements BehaviorInterface {

    int pos[] = null;

    int beginAngle = 0;

    int w = 200;

    int delta = 0;

    boolean bTriggered = false;

    public TurnLeft() {
        super("TurnLeft", "90 degree Turn Left");
        pos = new int[3];
    }

    public void setActive(boolean val) {
        super.setActive(val);
        bTriggered = false;
        if (val) {
            getImpl().get_position(pos);
            beginAngle = pos[2];
        }
    }

    public boolean get_commands(SensorData sensorsData, int cmd[]) {
        getImpl().get_position(pos);
        delta = Utility.deltaAngle(pos[2], beginAngle);
        if (delta < 700) w = 400; else w = (int) Math.floor(900 * (1 - delta / (double) 900));
        cmd[0] = 0;
        cmd[1] = w;
        cmd[2] = 0;
        return true;
    }

    public Vector<VRWEvent> process() {
        if (!isActive()) return null;
        if (delta > 890 && !bTriggered) {
            Vector<VRWEvent> v = new Vector<VRWEvent>();
            bTriggered = true;
            v.addElement(new VRWEvent("robot", new String[] { "turned" }));
            return v;
        }
        return null;
    }
}
