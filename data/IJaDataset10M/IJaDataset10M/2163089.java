package playground.christoph.mobsim;

import org.apache.log4j.Logger;
import org.matsim.core.api.network.Node;
import org.matsim.core.controler.Controler;
import org.matsim.core.mobsim.queuesim.QueueLane;
import org.matsim.core.mobsim.queuesim.QueueNetwork;
import org.matsim.core.mobsim.queuesim.QueueNode;
import org.matsim.core.mobsim.queuesim.QueueVehicle;

public class MyQueueNode extends QueueNode {

    private static final Logger log = Logger.getLogger(MyQueueNode.class);

    public MyQueueNode(Node n, QueueNetwork queueNetwork) {
        super(n, queueNetwork);
    }

    @Override
    public boolean moveVehicleOverNode(final QueueVehicle veh, QueueLane lane, final double now) {
        return super.moveVehicleOverNode(veh, lane, now);
    }

    protected Controler getControler() {
        if (this.queueNetwork instanceof MyQueueNetwork) {
            return ((MyQueueNetwork) this.queueNetwork).getControler();
        } else log.error("Could not return a Controler!");
        return null;
    }
}
