package com.vlee.ejb.employee;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;
import java.util.*;
import java.sql.Timestamp;

public interface EmpDesignation extends EJBObject {

    public Integer getPkid() throws RemoteException;

    public void setPkid(Integer pkid) throws RemoteException;

    public Integer getEmpPkid() throws RemoteException;

    public void setEmpPkid(Integer emp_pkid) throws RemoteException;

    public String getName() throws RemoteException;

    public void setName(String name) throws RemoteException;

    public String getDescription() throws RemoteException;

    public void setDescription(String description) throws RemoteException;

    public Integer getRankPkid() throws RemoteException;

    public void setRankPkid(Integer rank_pkid) throws RemoteException;

    public Integer getDeptPkid() throws RemoteException;

    public void setDeptPkid(Integer dept_pkid) throws RemoteException;

    public Integer getBossPkid() throws RemoteException;

    public void setBossPkid(Integer boss_pkid) throws RemoteException;

    public String getStatus() throws RemoteException;

    public void setStatus(String status) throws RemoteException;

    public Timestamp getTimeEffective() throws RemoteException;

    public void setTimeEffective(Timestamp time_effective) throws RemoteException;

    public Timestamp getLastUpdate() throws RemoteException;

    public void setLastUpdate(Timestamp lastUpdate) throws RemoteException;

    public Integer getUserIdUpdate() throws RemoteException;

    public void setUserIdUpdate(Integer userIdUpdate) throws RemoteException;
}
