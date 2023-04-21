package com.wavechain.interrogatorManagement;

import java.rmi.RemoteException;

public interface InterrogatorManagementSLSBRemote extends javax.ejb.EJBObject {

    public void createPhysicalInterrogator(String pInterrogatorName) throws Exception, RemoteException;

    public void updatePhysicalInterrogator(String pInterrogatorName) throws Exception, RemoteException;

    public void createLogicalInterrogator(String lInterrogatorName) throws Exception, RemoteException;

    public void updateLogicalInterrogator(String lInterrogatorName) throws Exception, RemoteException;

    public void associatePhysicalInterrogatorWithLogicalInterrogator(String pInterrogatorName, String lInterrogatorName) throws Exception, RemoteException;

    public void disAssociatePhysicalInterrogatorWithLogicalInterrogator(String pInterrogatorName, String lInterrogatorName) throws Exception, RemoteException;

    public void deletePhysicalInterrogator(String pInterrogatorName) throws Exception, RemoteException;

    public void deleteLogicalInterrogator(String lInterrogatorName) throws Exception, RemoteException;
}
