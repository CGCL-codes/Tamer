package org.activebpel.samples.script.wsdl;

import java.rmi.RemoteException;
import org.activebpel.samples.script.schema.*;

/**
 * Test client to exercise Admin Web Service API calls generated
 * by running wsdl2java on the ActiveBpelAdmin WSDL definition.
 */
public class TestClient {

    /**
    * Remote proxy.
    */
    ActiveBpelAdminProxy mRemote;

    /**
    * Last PID found, if any.
    */
    long mLastPID;

    /**
    * Get the API version for the engine.
    * 
    * @return String
    * @throws RemoteException
    */
    public String getAPIVersion() throws RemoteException {
        return mRemote.getAPIVersion(new AesVoidType()).getResponse();
    }

    /**
    * Gets the configuration from the server via the engine admin
    */
    public AesConfigurationType getConfiguration() throws Exception {
        AesVoidType configInput = new AesVoidType();
        AesConfigurationType config = mRemote.getConfiguration(configInput);
        return config;
    }

    /**
    * Sets the configuration on the server
    *
    * @param aConfig
    */
    public void setConfiguration(AesConfigurationType aConfig) throws Exception {
        mRemote.setConfiguration(aConfig);
    }

    /**
    * Displays a list of running, completed or faulted processes.
    *
    * @return Number of processes in the list.
    * @throws RemoteException
    */
    public int displayProcessList() throws RemoteException {
        AesProcessFilterType filter = new AesProcessFilterType(new AesProcessFilter());
        AesProcessListType data = mRemote.getProcessList(filter);
        AesProcessListResult list = data.getResponse();
        if (list.getTotalRowCount() <= 0) {
            System.out.println("No process info to display.");
            return 0;
        } else {
            AesProcessInstanceDetail[] details = list.getRowDetails();
            System.out.println("PID\tState\tName");
            for (int i = 0; i < list.getTotalRowCount(); i++) {
                AesProcessInstanceDetail detail = details[i];
                System.out.println(detail.getProcessId() + "\t" + detail.getStateReason() + "\t" + detail.getName());
                mLastPID = detail.getProcessId();
            }
            return list.getTotalRowCount();
        }
    }

    /**
    * Display the process log for process, provided its PID.
    *
    * @param pid
    * @throws RemoteException
    */
    public void displayProcessLog(long pid) throws RemoteException {
        AesProcessType proc = new AesProcessType();
        proc.setPid(pid);
        System.out.println("Log for process " + pid);
        System.out.println(mRemote.getProcessLog(proc).getResponse());
    }

    /**
    * Report bad stuff.
    * @param e Exception
    */
    public static void reportError(Exception e) {
        System.out.println("An error occurred: " + e.getLocalizedMessage());
        e.printStackTrace();
    }

    /**
    * Main - called when the app is run to exercise client test(s).
    *
    * @param args  Command-line application arguments.
    */
    public static void main(String[] args) {
        try {
            TestClient client = new TestClient();
            System.out.println("Engine Admin API Version is " + client.getAPIVersion());
            System.out.println(client.getConfiguration().getXmlString());
            if (client.displayProcessList() > 0) client.displayProcessLog(client.mLastPID);
        } catch (Exception e) {
            reportError(e);
        }
    }
}
