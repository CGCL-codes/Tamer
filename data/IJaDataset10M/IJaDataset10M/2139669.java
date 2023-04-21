package com.vlee.ejb.inventory;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;
import java.util.*;
import java.sql.Timestamp;
import java.math.BigDecimal;

public interface StockOpening extends EJBObject {

    public Integer getPkid() throws RemoteException;

    public void setPkid(Integer pkid) throws RemoteException;

    public Integer getStockId() throws RemoteException;

    public void setStockId(Integer StockId) throws RemoteException;

    public BigDecimal getBalance() throws RemoteException;

    public void setBalance(BigDecimal balance) throws RemoteException;

    public String getRemarks() throws RemoteException;

    public void setRemarks(String remarks) throws RemoteException;

    public Timestamp getOpenTime() throws RemoteException;

    public void setOpenTime(Timestamp openTime) throws RemoteException;

    public BigDecimal getUnitPrice() throws RemoteException;

    public void setUnitPrice(BigDecimal unitPrice) throws RemoteException;

    public String getCurrency() throws RemoteException;

    public void setCurrency(String currency) throws RemoteException;

    public String getStatus() throws RemoteException;

    public void setStatus(String status) throws RemoteException;

    public Timestamp getLastUpdate() throws RemoteException;

    public void setLastUpdate(Timestamp lastUpdateTime) throws RemoteException;

    public Integer getUserIdUpdate() throws RemoteException;

    public void setUserIdUpdate(Integer userIdUpdate) throws RemoteException;
}
