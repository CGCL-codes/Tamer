package org.personalsmartspace.psm.rs.impl.strategies;

import java.util.LinkedList;
import org.personalsmartspace.sre.api.pss3p.IDigitalPersonalIdentifier;

public class NULLStrategy {

    public static IDigitalPersonalIdentifier calculateControllerDPI(LinkedList<IDigitalPersonalIdentifier> requestorDPIs) {
        IDigitalPersonalIdentifier nextController = null;
        return nextController;
    }
}
