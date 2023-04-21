package net.sf.smbt.comm.utils.rxtx;

import net.sf.xqz.model.engine.Port;

public interface IRXTXSerialUtils {

    public abstract ISerialCommunicator openPort(Port port, String portID, int speed);
}
