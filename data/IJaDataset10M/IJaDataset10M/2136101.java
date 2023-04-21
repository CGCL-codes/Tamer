package com.ixora.rms.agents.websphere.v50.proxy;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Properties;
import com.ibm.websphere.pmi.PmiException;
import com.ibm.websphere.pmi.PmiModuleConfig;
import com.ibm.websphere.pmi.client.CpdCollection;
import com.ibm.websphere.pmi.client.PerfLevelSpec;

/**
 * @author Daniel Moraru
 */
public interface PmiClientProxy extends Remote {

    long getId() throws RemoteException;

    void configure(Properties p) throws RemoteException;

    String getNLSValue(String text, String module) throws RemoteException;

    PerfDescriptor[] listNodes() throws RemoteException, PmiException;

    PerfDescriptor[] listServers(PerfDescriptor pd) throws RemoteException, PmiException;

    PerfDescriptor[] listMembers(PerfDescriptor pd, boolean recursive) throws RemoteException, PmiException;

    PerfLevelSpec[] getInstrumentationLevel(String node, String server) throws RemoteException, PmiException;

    void setInstrumentationLevel(String a, String server, String[] path, int level, boolean b) throws RemoteException, PmiException;

    PmiModuleConfig[] getConfigs() throws RemoteException;

    CpdCollection[] gets(PerfDescriptor[] lst, boolean b) throws RemoteException, PmiException;

    int getErrorCode() throws RemoteException;

    String getErrorMessage() throws RemoteException;

    int getErrorCode(String node) throws RemoteException;

    String getErrorMessage(String node) throws RemoteException;

    void end() throws RemoteException;
}
