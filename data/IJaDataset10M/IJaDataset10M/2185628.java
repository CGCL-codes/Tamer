package de.ios.kontor.sv.fa.co;

import java.rmi.*;
import de.ios.framework.basic.*;
import de.ios.kontor.utils.*;
import de.ios.kontor.sv.basic.co.*;

public interface FAAccountController extends BasicController {

    public FAAccount getFAAccountByOId(long oid) throws java.rmi.RemoteException, KontorException;

    public Iterator getFAAccounts(FAAccount o) throws java.rmi.RemoteException, KontorException;

    public FAAccount createFAAccount() throws java.rmi.RemoteException, KontorException;

    public Iterator getFAAccountDC() throws java.rmi.RemoteException, KontorException;
}

;
