package org.epo.jbpa.archiveInterface;

import epo.bsi.*;
import org.epo.jbpa.dl.*;
import org.apache.log4j.Logger;

/**
 * This object represents a session instanciated with a JPXI archive
 * @author INFOTEL Conseil
 */
public class JPxiSession extends Session {

    private JPxiNetworkLink networkLink = null;

    /**
 * JPxiSession constructor
 * @param theNetwork org.epo.jbpa.archiveInterface.JPxiNetworkLink
 * @param traceFileName String
 * @param traceLevel int
 * @exception JBpaException
 */
    public JPxiSession(JPxiNetworkLink theNetwork, String traceFileName, int traceLevel) throws JBpaException {
        super(theNetwork.getUserId(), theNetwork.getPassword(), theNetwork.getHostUrl());
        boolean networkProblem = false;
        setNetworkLink(theNetwork);
        try {
            bnsInitialize();
            if (traceLevel >= 10) traceOn(traceFileName + ".dat");
        } catch (BsiException e) {
            networkProblem = (e.getClass().getName()).equals("epo.bsi.BsiCommunicationException");
            if (!networkProblem) {
                throw new JBpaException(JBpaException.ERR_INST_BSI_SESSION, "BSI Rc = " + e.getReturnCode() + " Mess. : " + e.getMessage());
            }
        }
    }

    /**
 * networkLink variable accesser
 * @return JPxiNetworkLink 
 */
    JPxiNetworkLink getNetworkLink() {
        return networkLink;
    }

    /**
 * networkLink variable setter
 * @param newValue JPxiNetworkLink 
 */
    private void setNetworkLink(JPxiNetworkLink newValue) {
        this.networkLink = newValue;
    }
}
