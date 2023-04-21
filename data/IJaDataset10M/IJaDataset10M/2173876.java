package eu.mpower.framework.sensor.fsa.adapter.eibnet;

import basys.eib.exceptions.EIBConnectionNotPossibleException;

/**
 * This class is in charge of solve possible problems with the eibnet interface
 * @author ELR
 */
public class EIBReconnection extends Thread {

    private EIBnetConnection eibCon;

    public EIBReconnection(EIBnetConnection eib) {
        eibCon = eib;
    }

    @Override
    public void run() {
        while (!eibCon.getStop()) {
            try {
                if (!eibCon.isConnected()) {
                    if (reconnect()) {
                        eibCon.setConnected(true);
                    }
                }
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                interrupt();
            }
        }
    }

    public boolean reconnect() {
        try {
            eibCon.getTunnel().connect(eibCon.getEIBnetIP());
        } catch (EIBConnectionNotPossibleException e) {
            System.err.println("Error in the connection with the tunnel EIB so many connections or wrong direction = " + eibCon.getEIBnetIP());
            return false;
        }
        eibCon.getTunnel().addEIBFrameListener(new FrameHandler(eibCon));
        eibCon.setConnected(true);
        return true;
    }
}
