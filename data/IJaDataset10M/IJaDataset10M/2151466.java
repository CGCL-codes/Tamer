package de.ios.kontor.sv.address.impl;

import java.rmi.*;
import java.util.*;
import de.ios.framework.basic.*;
import de.ios.framework.db2.*;
import de.ios.kontor.utils.*;
import de.ios.kontor.sv.address.co.*;
import de.ios.kontor.sv.basic.impl.*;

/**
 * Kinds of Addresses
 *
 * @author js (Joachim Schaaf)
 * @version $Id: KindOfAddressImpl.java,v 1.1.1.1 2004/03/24 23:02:47 nanneb Exp $
 */
public class KindOfAddressImpl extends BasicImpl implements KindOfAddress {

    /** The internal DBObject */
    protected KindOfAddressDBO dbo;

    /** 
   * Constructor 
   * @param _db The DBObjectServer
   * @param _sc The SessionCarrier
   * @param _dbo The DBObject
   * @exception java.rmi.RemoteException if the connection to the Server failed.
   */
    public KindOfAddressImpl(DBObjectServer _db, SessionCarrier _sc, KindOfAddressDBO _dbo) throws java.rmi.RemoteException {
        super(_db, _sc, _dbo);
        dbo = _dbo;
    }

    /**
   * Return the Kind
   * @return the address kind
   */
    public String getKind() throws RemoteException, KontorException {
        return dbo.kind.get();
    }

    /**
   * Return the KindOfAddressContollerImpl
   * @return the ControllerImpl for a KindOfAddress
   * @exception de.ios.kontor.utils.KontorException if the Operation failed due to a Server error.
   */
    public BasicControllerImpl getBasicControllerImpl() throws KontorException {
        throw new KontorException("Not yet implemented (not used) !");
    }
}
