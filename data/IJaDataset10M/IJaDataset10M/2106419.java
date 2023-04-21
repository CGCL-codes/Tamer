package org.dcm4chex.archive.web.maverick.xdsi;

import javax.xml.registry.JAXRException;
import javax.xml.registry.infomodel.ExtrinsicObject;

public class XDSStatus {

    /**
     * Get status of document as String.
     * 
     * @return
     * @throws JAXRException
     */
    public static String getStatusAsString(int s) throws JAXRException {
        switch(s) {
            case ExtrinsicObject.STATUS_APPROVED:
                return "Approved";
            case ExtrinsicObject.STATUS_DEPRECATED:
                return "Deprecated";
            case ExtrinsicObject.STATUS_SUBMITTED:
                return "Submitted";
            case ExtrinsicObject.STATUS_WITHDRAWN:
                return "Withdrawn";
            default:
                return "unknown(" + s + ")";
        }
    }

    public static int toStatus(String status) {
        if ("Approved".equals(status)) {
            return ExtrinsicObject.STATUS_APPROVED;
        } else if ("Deprecated".equals(status)) {
            return ExtrinsicObject.STATUS_DEPRECATED;
        } else if ("Submitted".equals(status)) {
            return ExtrinsicObject.STATUS_SUBMITTED;
        } else if ("Withdrawn".equals(status)) {
            return ExtrinsicObject.STATUS_WITHDRAWN;
        }
        throw new IllegalArgumentException("Unknown XDSStatus:" + status);
    }
}
