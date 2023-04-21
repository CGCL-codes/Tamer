package com.gampire.pc.net.broadcast;

public class FindSlaveThread extends Thread {

    SlaveFoundHandler slaveFoundHandler;

    public FindSlaveThread(SlaveFoundHandler slaveFoundHandler) {
        super("FindSlaveThread");
        this.slaveFoundHandler = slaveFoundHandler;
    }

    @Override
    public void run() {
        AddressPort slaveAddressPort = BroadCastUtils.findSlave();
        slaveFoundHandler.onSlaveFound(slaveAddressPort);
    }
}
