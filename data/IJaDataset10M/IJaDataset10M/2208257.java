package org.globus.examples.services.core.rp.impl;

import java.rmi.RemoteException;
import org.globus.wsrf.Resource;
import org.globus.wsrf.ResourceProperties;
import org.globus.wsrf.ResourceProperty;
import org.globus.wsrf.ResourcePropertySet;
import org.globus.wsrf.impl.ReflectionResourceProperty;
import org.globus.wsrf.impl.SimpleResourcePropertySet;
import org.globus.examples.stubs.MathService_instance_rp.AddResponse;
import org.globus.examples.stubs.MathService_instance_rp.SubtractResponse;

public class MathService implements Resource, ResourceProperties {

    private ResourcePropertySet propSet;

    private int value;

    private String lastOp;

    public MathService() throws RemoteException {
        this.propSet = new SimpleResourcePropertySet(MathQNames.RESOURCE_PROPERTIES);
        try {
            ResourceProperty valueRP = new ReflectionResourceProperty(MathQNames.RP_VALUE, "Value", this);
            this.propSet.add(valueRP);
            setValue(0);
            ResourceProperty lastOpRP = new ReflectionResourceProperty(MathQNames.RP_LASTOP, "LastOp", this);
            this.propSet.add(lastOpRP);
            setLastOp("NONE");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getLastOp() {
        return lastOp;
    }

    public void setLastOp(String lastOp) {
        this.lastOp = lastOp;
    }

    public AddResponse add(int a) throws RemoteException {
        value += a;
        lastOp = "ADDITION";
        return new AddResponse();
    }

    public SubtractResponse subtract(int a) throws RemoteException {
        value -= a;
        lastOp = "SUBTRACTION";
        return new SubtractResponse();
    }

    public ResourcePropertySet getResourcePropertySet() {
        return this.propSet;
    }
}
