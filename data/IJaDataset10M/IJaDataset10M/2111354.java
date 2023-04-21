package routingApp.dymo.sim;

import routingApp.dymo.ReceivePackets;
import takatuka.drivers.*;
import takatuka.sim.*;

/**
 * <p>Title: </p>
 * <p>Description:
 * 
 *  An sample application for Dymo Routing Protocol (Dymo) routing algorithm.
 *
 * </p>
 * @author Faisal Aslam
 * @version 1.0
 */
public class Intermediate extends ReceivePackets {

    /**
     * Factory method to get the factory :)
     * We need it so that subclasses can use different factories.
     * @return 
     */
    protected IDriversFactory createDriversFactory() {
        return SimDriversFactory.getInstanceOf();
    }

    public void execute() {
        super.execute();
    }

    public static void main(String args[]) {
        new SimReceivePackets().execute();
    }
}
