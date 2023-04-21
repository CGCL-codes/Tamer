package com.intel.gpe.rmitsi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.intel.gpe.services.tss.common.TargetSystemDatabase;
import com.intel.gpe.tsi.common.ITSI;
import com.intel.gpe.tsiclient.ITSIClientFactory;

/**
 * @version $Id: RMITSIClientFactory.java,v 1.2 2007/01/30 11:08:16 dnpetrov Exp $
 * @author Dmitry Petrov
 */
public class RMITSIClientFactory implements ITSIClientFactory {

    private static Logger logger = Logger.getLogger("com.intel.gpe");

    public ITSI createTSIClient(TargetSystemDatabase tsdb) throws Exception {
        String host = tsdb.getTSIClientInitField("Host");
        int port = Integer.parseInt(tsdb.getTSIClientInitField("Port"));
        String object = tsdb.getTSIClientInitField("Object");
        ITSI tsi = null;
        try {
            Registry reg = LocateRegistry.getRegistry(host, port);
            tsi = (ITSI) reg.lookup(object);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to connect to TSI " + object + " at " + host + ":" + port);
            throw e;
        }
        return tsi;
    }
}
