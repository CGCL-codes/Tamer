package de.ios.kontor.sv.address.co;

import java.rmi.*;
import java.util.*;
import de.ios.framework.basic.*;
import de.ios.kontor.utils.*;
import de.ios.kontor.sv.basic.co.*;

/**
 * Contact Types
 */
public interface ContactType extends Basic {

    /**
   * get the contact type
   * @return the contact type
   */
    public String getType() throws RemoteException, KontorException;

    /**
   * set the contact type
   * @param kind - the contact type
   */
    public void setType(String _type) throws RemoteException, KontorException;
}
