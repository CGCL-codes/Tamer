package net.alinnistor.nk.service.factory;

import java.net.DatagramSocket;
import java.net.SocketException;
import net.alinnistor.nk.domain.Context;
import net.alinnistor.nk.service.network.NetUtil;

/**
 * @author <a href="mailto:nad7ir@yahoo.com">Alin NISTOR</a>
 */
public class DatagramSkFactory {

    public static DatagramSocket createDatagramSocket(int destPort) throws Exception {
        DatagramSocket dsk = null;
        if (!Context.getParameter(Context.BROADCAST_ADDRESS).toString().equals("localhost")) {
            try {
                dsk = new DatagramSocket(destPort);
            } catch (SocketException e) {
                e.printStackTrace();
            }
        } else {
            try {
                dsk = new DatagramSocket(destPort);
            } catch (SocketException e) {
                for (int i = 1; i < Context.MAX_LOCALLY_NK; i++) {
                    if (NetUtil.isUDPPortAvailable(destPort + i)) {
                        try {
                            dsk = new DatagramSocket(destPort + i);
                            return dsk;
                        } catch (SocketException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
                throw new Exception("All ports for test purpose are bind");
            }
        }
        return dsk;
    }
}
