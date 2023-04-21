package titancommon.node.tasks;

import titancommon.node.TitanTask;
import titancommon.node.DataPacket;

/**
 *
 * @author Jeremy Constantin <jeremyc@student.ethz.ch>
 */
public abstract interface ExecutableTitanTask {

    public void setTitanTask(TitanTask tsk);

    public abstract boolean setExecParameters(short[] param);

    public abstract void init();

    public abstract void inDataHandler(int port, DataPacket data);
}
