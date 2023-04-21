package net.sf.orcc.simulators;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import net.sf.orcc.ir.Port;
import net.sf.orcc.network.Broadcast;
import net.sf.orcc.runtime.Fifo;

/**
 * This class defines a broadcast that can be interpreted by calling
 * {@link #schedule()}.
 * 
 * @author Ghislain Roquier
 * 
 */
public class BroadcastInterpreter {

    /**
	 * Communication fifos map : key = related I/O port name; value =
	 * Fifo_int/boolean/String
	 */
    private Map<Port, Fifo> fifos;

    private Broadcast broadcast;

    private Fifo input;

    private List<Fifo> outputs;

    public BroadcastInterpreter(Broadcast broadcast) {
        this.broadcast = broadcast;
    }

    public void initialize() {
        input = fifos.get(broadcast.getInput());
        outputs = new LinkedList<Fifo>();
        for (Port port : broadcast.getOutputs().getList()) {
            outputs.add(fifos.get(port));
        }
    }

    public boolean schedule() {
        if (input.hasTokens(1) && outputsHaveRoom()) {
            Object value = input.read();
            for (Fifo output : outputs) {
                output.write(value);
            }
            return true;
        } else {
            return false;
        }
    }

    private final boolean outputsHaveRoom() {
        boolean hasRoom = true;
        for (Fifo output : outputs) {
            hasRoom &= output.hasRoom(1);
        }
        return hasRoom;
    }

    public void setFifos(Map<Port, Fifo> fifos) {
        this.fifos = fifos;
    }
}
