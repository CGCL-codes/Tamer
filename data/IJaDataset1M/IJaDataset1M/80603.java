package com.vlee.ejb.customer;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;
import java.util.*;
import java.sql.Timestamp;

public interface CustCompany extends EJBObject {

    public Integer getPkid() throws RemoteException;

    public void setPkid(Integer pkid) throws RemoteException;

    public String getCode() throws RemoteException;

    public void setCode(String code) throws RemoteException;

    public String getRegNo() throws RemoteException;

    public void setRegNo(String regNo) throws RemoteException;

    public String getName() throws RemoteException;

    public void setName(String name) throws RemoteException;

    public String getDescription() throws RemoteException;

    public void setDescription(String description) throws RemoteException;

    public String getAddr1() throws RemoteException;

    public void setAddr1(String addr1) throws RemoteException;

    public String getAddr2() throws RemoteException;

    public void setAddr2(String addr2) throws RemoteException;

    public String getAddr3() throws RemoteException;

    public void setAddr3(String addr3) throws RemoteException;

    public String getZip() throws RemoteException;

    public void setZip(String zip) throws RemoteException;

    public String getState() throws RemoteException;

    public void setState(String state) throws RemoteException;

    public String getCountryCode() throws RemoteException;

    public void setCountryCode(String countryCode) throws RemoteException;

    public String getWebUrl() throws RemoteException;

    public void setWebUrl(String webUrl) throws RemoteException;

    public Integer getCustAccountId() throws RemoteException;

    public void setCustAccountId(Integer custAccountId) throws RemoteException;

    public String getStatus() throws RemoteException;

    public void setStatus(String status) throws RemoteException;

    public Timestamp getLastUpdate() throws RemoteException;

    public void setLastUpdate(Timestamp lastUpdate) throws RemoteException;

    public Integer getUserIdUpdate() throws RemoteException;

    public void setUserIdUpdate(Integer userIdUpdate) throws RemoteException;
}
