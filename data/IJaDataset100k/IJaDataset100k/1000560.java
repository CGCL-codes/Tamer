package org.epo.jbps.bpaInterface;

import epo.bsi.*;
import org.apache.log4j.Logger;
import org.epo.jbps.generic.*;
import org.epoline.jsf.utils.Log4jManager;

/**
 * Session to the BPA System
 * @author INFOTEL Conseil
 */
public class BpaSession extends Session {

    private BpaNetworkLink networkLink = null;

    /**
 * BpaSession constructor
 * @param theNetwork epo.jbps.bpaInterface.BpaNetworkLink
 * @param traceFileName tring
 * @param traceLevel int
 * @param theTrace BpsTrace
 * @exception BpsStartException
 */
    public BpaSession(BpaNetworkLink theNetwork, String traceFileName, int traceLevel) throws BpsStartException {
        super(theNetwork.getUserId(), theNetwork.getPassword(), theNetwork.getHostUrl());
        boolean networkProblem = false;
        setNetworkLink(theNetwork);
        try {
            bnsInitialize();
            if (traceLevel > 0) traceOn(traceFileName + ".Bpa.log", traceLevel);
        } catch (BsiException e) {
            networkProblem = (e.getClass().getName()).equals("epo.bsi.BsiCommunicationException");
            if (!networkProblem) {
                throw new BpsStartException(BpsException.ERR_INST_BSI_SESSION, "BSI Rc = " + e.getReturnCode() + " Mess. : " + e.getMessage());
            } else {
            }
        }
    }

    /**
 * networkLink variable accesser
 * @return org.epo.jbps.bpaInterface.BpaNetworkLink
 */
    public BpaNetworkLink getNetworkLink() {
        return networkLink;
    }

    /**
 * networkLink variable setter.
 * @param newValue org.epo.jbps.bpaInterface.BpaNetworkLink
 */
    private void setNetworkLink(BpaNetworkLink newValue) {
        this.networkLink = newValue;
    }
}
