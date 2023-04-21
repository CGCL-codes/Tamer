package org.apache.rampart.authorization;

import org.apache.axiom.soap.SOAPFault;
import org.apache.axis2.AxisFault;

public class SpecificMetadataFault extends AxisFault {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public SpecificMetadataFault(SOAPFault soapFault) {
        super(soapFault);
    }
}
