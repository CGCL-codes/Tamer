package com.navigator.shared.service.unit;

import java.rmi.RemoteException;
import java.util.List;
import com.navigator.shared.data.Unit;

/**
 * @author Derek Knuese
 * 
 * Created on Apr 27, 2006
 */
public interface UnitService {

    public List getAllUnits() throws RemoteException;

    public List getUnits(int setNumber) throws RemoteException;

    public Unit getUnit(int setNumber, int unitNumber) throws RemoteException;
}
