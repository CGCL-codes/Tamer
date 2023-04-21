package org.jmule.core.protocol.donkey;

import java.util.logging.*;
import org.jmule.core.AbstractTransferRateLimiter;

/** Provides upload speed limitation for all DonkeyConnections.
 * @author emarant 
 * @version $Revision: 1.1.1.1 $
 * <br>Last changed by $Author: jmartinc $ on $Date: 2005/04/22 21:44:53 $
 */
public final class DonkeyUpLoadLimiter extends AbstractTransferRateLimiter {

    public static Logger log = Logger.getLogger(DonkeyUpLoadLimiter.class.getName());

    private static DonkeyProtocol context = DonkeyProtocol.getInstance();

    public static DonkeyUpLoadLimiter Limiter = new DonkeyUpLoadLimiter();

    private DonkeyUpLoadLimiter() {
    }

    /**
     * Calculate the transfer rate limit for Upload.
     * @return rate in bytes per second
     */
    protected float limit() {
        return context.getMaxUploadSpeed() * 1024.0f;
    }

    private int connections = 0;

    private boolean calc = true;

    /**
    * Count the connection for next eta period up.
    */
    public void incConnections() {
        connections++;
        calc = true;
    }

    /**
    * Count the connection for current eta period down.
    */
    public void decConnections() {
        if (calc) {
            caluclateBytesPerConnection(connections);
            calc = false;
        }
        connections--;
        assert connections >= 0;
    }
}
