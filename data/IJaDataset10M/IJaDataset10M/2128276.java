package titancommon.node.tasks;

import titancommon.node.TitanTask;
import titancommon.node.DataPacket;
import titancommon.tasks.Sink;

/**
 *
 * @author Jeremy Constantin <jeremyc@student.ethz.ch>
 */
public class ESink extends Sink implements ExecutableTitanTask {

    private TitanTask tTask;

    public void setTitanTask(TitanTask tsk) {
        tTask = tsk;
    }

    private static final boolean bCheckOrder = true;

    private long packetCount[];

    public boolean setExecParameters(short[] param) {
        return true;
    }

    public void init() {
        if (bCheckOrder) {
            packetCount = new long[tTask.getPortsInNum()];
        }
    }

    public void inDataHandler(int port, DataPacket data) {
        if (bCheckOrder) {
            long max = ++packetCount[port];
            for (int i = 0; i < tTask.getPortsInNum(); i++) {
                if ((max - packetCount[i]) > 1) {
                    System.err.println("packet count difference > 1 between port " + port + " and " + i);
                }
            }
        }
    }
}
