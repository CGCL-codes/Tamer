package com.h9labs.swbem.msvm.networking;

import org.jinterop.dcom.impls.automation.IJIDispatch;
import com.h9labs.swbem.SWbemObjectSet;
import com.h9labs.swbem.SWbemServices;
import com.h9labs.swbem.SWbemSet;
import com.h9labs.swbem.msvm.MsvmObject;

/**
 * Represents a synthetic Ethernet adapter.
 * 
 * @author akutz
 * @remarks This adapter is the preferred network adapter because of its
 *          performance and hot configurability.
 */
public class MsvmSyntheticEthernetPort extends MsvmObject {

    /**
     * Initializes a new instance of the Msvm_SyntheticEthernetPort class.
     * 
     * @param objectDispatcher The underlying dispatch object used to
     *        communicate with the server.
     * @param service The service connection.
     */
    public MsvmSyntheticEthernetPort(IJIDispatch objectDispatcher, SWbemServices service) {
        super(objectDispatcher, service);
    }

    /**
     * Gets the associated MsvmVmLANEndpoint for this synthetic Ethernet port.
     * 
     * @return The associated MsvmVmLANEndpoints for this synthetic Ethernet
     *         port.
     * @throws Exception When an error occurs.
     */
    public SWbemSet<MsvmVmLANEndpoint> getVmLANEndpoints() throws Exception {
        String path = super.getObjectPath().getPath();
        String format = "ASSOCIATORS OF {%s} " + "WHERE AssocClass=Msvm_DeviceSAPImplementation " + "ResultClass=Msvm_VmLANEndpoint " + "ResultRole=Dependent Role=Antecedent";
        String query = String.format(format, path);
        SWbemObjectSet<MsvmVmLANEndpoint> objSetVMLEP = super.getService().execQuery(query, MsvmVmLANEndpoint.class);
        return objSetVMLEP;
    }
}
