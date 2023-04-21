package org.xebra.scp.util;

import org.apache.log4j.Logger;
import org.dcm4che2.data.UID;
import org.dcm4che2.net.Association;
import org.dcm4che2.net.TransferCapability;
import org.xebra.scp.XebraConstants;

/**
 * Simplifies the selection of a transfer systax to use during
 * an association.
 * 
 * @author Rafael Chargel
 * @version $Revision: 1.1 $
 */
public class TransferSyntaxSelector implements XebraConstants {

    private static Logger _log = Logger.getLogger(TransferCapability.class);

    /**
	 * Gets the preferred transfer syntax of this association.
	 * 
	 * @param as The association.
	 * @param sopClassUID The SOP Class UID.
	 * @return Returns the preferred transfer syntax.
	 */
    public static final String selectTransferSyntax(Association as, String sopClassUID) {
        _log.debug("Trying to find transfer syntax for SOP Class: " + sopClassUID);
        TransferCapability tc = as.getTransferCapabilityAsSCU(sopClassUID);
        if (tc == null) {
            tc = as.getTransferCapabilityAsSCP(sopClassUID);
        }
        if (tc == null) {
            _log.debug("Could not find transfer capability");
            return UID.ImplicitVRLittleEndian;
        }
        String[] tcs = tc.getTransferSyntax();
        return selectTransferSyntax(tcs, DEFAULT_TRANSFER_SYNTAX);
    }

    /**
     * Gets the preferred transfer syntax of this association.
	 * 
	 * @param as The association.
	 * @param sopClassUID The SOP Class UID.
     * @param defaultSyntax The default syntax to use.
     * @return Returns the preferred transfer syntax.
     */
    public static final String selectTransferSyntax(Association as, String sopClassUID, String defaultSyntax) {
        _log.debug("Trying to find transfer syntax for SOP Class: " + sopClassUID);
        TransferCapability tc = as.getTransferCapabilityAsSCU(sopClassUID);
        if (tc == null) {
            tc = as.getTransferCapabilityAsSCP(sopClassUID);
        }
        if (tc == null) {
            _log.debug("Could not find transfer capability");
            return defaultSyntax;
        }
        String[] tcs = tc.getTransferSyntax();
        return selectTransferSyntax(tcs, defaultSyntax);
    }

    private static String selectTransferSyntax(String[] available, String tsuid) {
        if (tsuid.equals(UID.ImplicitVRLittleEndian)) return selectTransferSyntax(available, IVLE_TS);
        if (tsuid.equals(UID.ExplicitVRLittleEndian)) return selectTransferSyntax(available, EVLE_TS);
        if (tsuid.equals(UID.ExplicitVRBigEndian)) return selectTransferSyntax(available, EVBE_TS);
        return tsuid;
    }

    private static String selectTransferSyntax(String[] available, String[] tsuids) {
        for (String tsuid : tsuids) {
            for (String avail : available) {
                if (avail.equals(tsuid)) return avail;
            }
        }
        return null;
    }
}
