package org.redwood.business.usermanagement.periodicitytype;

import java.rmi.RemoteException;
import java.util.Collection;

public interface PeriodicityType {

    public String getRw_id() throws RemoteException;

    public String getRw_name() throws RemoteException;

    public void setRw_name(String name) throws RemoteException;

    public String getRw_description() throws RemoteException;

    public void setRw_description(String description) throws RemoteException;
}
