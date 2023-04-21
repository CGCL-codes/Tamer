package org.globus.examples.services.core.rl.impl;

import java.rmi.RemoteException;
import org.globus.wsrf.ResourceContext;
import org.globus.examples.stubs.MathService_instance_rl.AddResponse;
import org.globus.examples.stubs.MathService_instance_rl.SubtractResponse;

public class MathService {

    private MathResource getResource() throws RemoteException {
        Object resource = null;
        try {
            resource = ResourceContext.getResourceContext().getResource();
        } catch (Exception e) {
            throw new RemoteException("", e);
        }
        MathResource mathResource = (MathResource) resource;
        return mathResource;
    }

    public AddResponse add(int a) throws RemoteException {
        MathResource mathResource = getResource();
        mathResource.setValue(mathResource.getValue() + a);
        mathResource.setLastOp("ADDITION");
        return new AddResponse();
    }

    public SubtractResponse subtract(int a) throws RemoteException {
        MathResource mathResource = getResource();
        mathResource.setValue(mathResource.getValue() - a);
        mathResource.setLastOp("SUBTRACTION");
        return new SubtractResponse();
    }
}
