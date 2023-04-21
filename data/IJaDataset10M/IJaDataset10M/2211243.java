package com.vlee.ejb.user;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;
import java.util.*;
import java.sql.*;
import java.math.*;

public interface UserConfigRegistry extends EJBObject {

    public Integer getPKid() throws RemoteException;

    public void setPKid(Integer pkid) throws RemoteException;

    public Integer getNodeParent() throws RemoteException;

    public void setNodeParent(Integer iNodeParent) throws RemoteException;

    public Integer getUserId() throws RemoteException;

    public void setUserId(Integer userid) throws RemoteException;

    public String getCategory() throws RemoteException;

    public void setCategory(String strCategory) throws RemoteException;

    public String getNamespace() throws RemoteException;

    public void setNamespace(String strNamespace) throws RemoteException;

    public String getValue1() throws RemoteException;

    public void setValue1(String strValue1) throws RemoteException;

    public String getValue2() throws RemoteException;

    public void setValue2(String strValue2) throws RemoteException;

    public String getValue3() throws RemoteException;

    public void setValue3(String strValue3) throws RemoteException;

    public String getValue4() throws RemoteException;

    public void setValue4(String strValue4) throws RemoteException;

    public String getValue5() throws RemoteException;

    public void setValue5(String strValue5) throws RemoteException;

    public String getStatus() throws RemoteException;

    public void setStatus(String stts) throws RemoteException;

    public Integer getUserIdEdit() throws RemoteException;

    public void setUserIdEdit(Integer intUserId) throws RemoteException;

    public Timestamp getTimeEdit() throws RemoteException;

    public void setTimeEdit(Timestamp tsTime) throws RemoteException;

    public Timestamp getTimeEffective() throws RemoteException;

    public void setTimeEffective(Timestamp tsTime) throws RemoteException;
}
