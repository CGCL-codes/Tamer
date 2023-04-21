package bs.relay;

import bs.*;
import java.util.LinkedList;

/**
 *
 * @author  cf2
 */
public class RelayBattleStar extends BattleStar {

    public boolean tracerShot = false;

    public int relaycount = 0;

    public boolean oldTracerShot = false;

    public RelayTracerShot tracer = null;

    /** Creates a new instance of RelayBattleStar */
    public RelayBattleStar() {
        shotClass = RelayShot.class;
        painter = RelayBattleStarPainter.class;
    }

    public void setTracerShot(boolean b) {
        tracerShot = b;
        shotClass = b ? RelayTracerShot.class : RelayShot.class;
        sendNetUpdate();
    }

    public void simulationStopped() {
        super.simulationStopped();
        relaycount = 0;
        oldTracerShot = tracerShot;
    }

    ;

    public void sendNetUpdate() {
        if (!controller.isLocallyControlled() && !(controller instanceof AIPlayer && World.instance.isHost())) return;
        LinkedList data = new LinkedList();
        data.add(new Integer(ID));
        data.add(new Double(getAngle()));
        data.add(new Double(getPower()));
        data.add(new Boolean(tracerShot));
        World.instance.broadCast(NetworkMessage.Message.GM_ANGLEPOWERUPDATE, data);
    }
}
