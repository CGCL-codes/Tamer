package takatuka.drivers.hwImpl.timer;

import takatuka.drivers.interfaces.IDelay;

/**
 * 
 * Description:
 * <p>
 * </p> 
 * @author Jet Tang
 * @version 1.0
 */
public class Delay implements IDelay {

    private static Delay instance = null;

    private Delay() {
    }

    public static Delay getInstanceOf() {
        if (instance == null) {
            instance = new Delay();
        }
        return instance;
    }

    /**
     * This method calls busy waiting. 
     * During the waiting time the vm is in idle.
     */
    public native void busywait(int delaytime);
}
