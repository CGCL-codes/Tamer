package net.sf.bluecove;

import org.bluecove.tester.log.Logger;

public class SwitcherTck {

    public static void startTCKAgent() {
        if (Configuration.likedTCKAgent) {
            Switcher.tckRFCOMMThread = new BluetoothTCKAgent.RFCOMMThread("RFCOMMThread");
            if (Switcher.tckRFCOMMThread == null) {
                Logger.info("Due to the License we do not include the TCK agent in distribution");
            } else {
                Switcher.tckRFCOMMThread.start();
                try {
                    String agentMtu = System.getProperty("bluetooth.agent_mtu");
                    String timeout = System.getProperty("timeout");
                    Switcher.tckL2CALthread = new BluetoothTCKAgent.L2CAPThread("L2CAPThread", agentMtu, timeout);
                    if (Switcher.tckL2CALthread != null) {
                        Switcher.tckL2CALthread.start();
                    }
                } catch (Throwable e) {
                    Logger.debug("Fail to start L2CAP", e);
                }
                try {
                    Switcher.tckGOEPThread = new BluetoothTCKAgent.GOEPThread("GOEPThread");
                    if (Switcher.tckGOEPThread != null) {
                        Switcher.tckGOEPThread.start();
                    }
                } catch (Throwable e) {
                    Logger.debug("Fail to start GOEP srv", e);
                }
                try {
                    Switcher.tckOBEXThread = new OBEXTCKAgent.OBEXTCKAgentApp("10", Configuration.testServerOBEX_TCP.booleanValue() ? "tcpobex" : "btgoep");
                    if (Switcher.tckOBEXThread != null) {
                        Switcher.tckOBEXThread.start();
                    }
                } catch (Throwable e) {
                    Logger.debug("Fail to start OBEX srv", e);
                }
            }
        }
    }
}
