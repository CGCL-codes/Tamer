package org.personalsmartspace.psm.rs.impl.strategies;

import java.util.LinkedList;
import org.personalsmartspace.sre.api.pss3p.IDigitalPersonalIdentifier;

public class FIFOStrategy {

    public static IDigitalPersonalIdentifier calculateControllerDPI(LinkedList<IDigitalPersonalIdentifier> requestorDPIs) {
        IDigitalPersonalIdentifier nextController = null;
        if (requestorDPIs.size() > 0) {
            nextController = requestorDPIs.get(0);
        }
        return nextController;
    }
}
