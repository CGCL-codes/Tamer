package edu.uncw.jxpl;

import java.rmi.RemoteException;
import org.globus.wsrf.ResourceContext;
import org.globus.wsrf.ResourceKey;
import org.globus.wsrf.utils.AddressingUtils;
import javax.naming.Context;
import javax.naming.InitialContext;
import org.globus.wsrf.Constants;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.jxpl.*;
import org.jxpl.exception.*;
import org.w3c.dom.*;

/** 
*   This is the code for the JXPL webservice.
*/
public class JXPLService {

    public JxplType evaluate(JxplType input) throws RemoteException {
        JXPL resource = (JXPL) ResourceContext.getResourceContext().getResource();
        try {
            resource.setInput(input);
            JxplType result = resource.getResult();
            return result;
        } catch (JxplException je) {
            throw new RemoteException("JXPL Exception", je);
        }
    }

    public boolean isReady(IsReadyRequest params) throws RemoteException {
        JXPL resource = (JXPL) ResourceContext.getResourceContext().getResource();
        boolean ready = resource.isReady();
        return ready;
    }

    public JxplType getResult(GetResultRequest params) throws RemoteException {
        JXPL resource = (JXPL) ResourceContext.getResourceContext().getResource();
        JxplType result = (JxplType) resource.getResult();
        return result;
    }

    public EndpointReferenceType create(CreateRequest c) throws RemoteException {
        ResourceContext ctx = ResourceContext.getResourceContext();
        ManyJXPLHome home = (ManyJXPLHome) ctx.getResourceHome();
        ResourceKey key = home.create();
        EndpointReferenceType epr;
        try {
            epr = AddressingUtils.createEndpointReference(ctx, key);
        } catch (Exception e) {
            throw new RemoteException("Could not form an EPR to new counter: " + e);
        }
        return epr;
    }
}
