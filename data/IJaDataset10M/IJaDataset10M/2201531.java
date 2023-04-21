package com.vlee.ejb.accounting;

import java.rmi.RemoteException;
import javax.ejb.EJBObject;

public interface CreditTermsRuleset extends EJBObject {

    public CreditTermsRulesetObject getObject() throws RemoteException;

    public void setObject(CreditTermsRulesetObject ctObj) throws RemoteException;

    public Integer getPkid() throws RemoteException;
}
