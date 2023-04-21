package com.tpc.control.sap;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;
import com.sap.conn.jco.AbapException;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoField;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoStructure;
import com.sap.conn.jco.ext.DestinationDataProvider;

public class SapConnector {

    private String myDestinationName;

    private String username;

    private String password;

    private String host;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getMyDestinationName() {
        return myDestinationName;
    }

    public void setMyDestinationName(String myDestinationName) {
        this.myDestinationName = myDestinationName;
    }

    public SapConnector(String myDestinationName, String host, String username, String password) {
        this.myDestinationName = myDestinationName;
        this.host = host;
        this.username = username;
        this.password = password;
        Properties connectProperties = new Properties();
        connectProperties.setProperty(DestinationDataProvider.JCO_ASHOST, host);
        connectProperties.setProperty(DestinationDataProvider.JCO_SYSNR, "00");
        connectProperties.setProperty(DestinationDataProvider.JCO_CLIENT, "900");
        connectProperties.setProperty(DestinationDataProvider.JCO_USER, username);
        connectProperties.setProperty(DestinationDataProvider.JCO_PASSWD, password);
        connectProperties.setProperty(DestinationDataProvider.JCO_LANG, "en");
        createDestinationDataFile(myDestinationName, connectProperties);
    }

    public void testConnection() throws JCoException {
        JCoDestination destination = JCoDestinationManager.getDestination(myDestinationName);
        System.out.println("Attributes:");
        System.out.println(destination.getAttributes());
        System.out.println();
    }

    private void createDestinationDataFile(String destinationName, Properties connectProperties) {
        File destCfg = new File(destinationName + ".jcoDestination");
        try {
            FileOutputStream fos = new FileOutputStream(destCfg, false);
            connectProperties.store(fos, "for tests only !");
            fos.close();
        } catch (Exception e) {
            throw new RuntimeException("Unable to create the destination files", e);
        }
    }

    public static void main(String[] args) {
        SapConnector connector = new SapConnector("ABAP_AS_WITHOUT_POOL", "192.168.42.197", "tprfc", "tpcork");
        try {
            connector.testConnection();
            JCoDestination destination = JCoDestinationManager.getDestination(connector.myDestinationName);
            JCoFunction function = destination.getRepository().getFunction("RFC_SYSTEM_INFO");
            if (function == null) throw new RuntimeException("RFC_SYSTEM_INFO not found in SAP.");
            try {
                function.execute(destination);
            } catch (AbapException e) {
                System.out.println(e.toString());
                return;
            }
            JCoStructure exportStructure = function.getExportParameterList().getStructure("RFCSI_EXPORT");
            System.out.println("System info for " + destination.getAttributes().getSystemID() + ":\n");
            for (int i = 0; i < exportStructure.getMetaData().getFieldCount(); i++) {
                System.out.println(exportStructure.getMetaData().getName(i) + ":\t" + exportStructure.getString(i));
            }
            System.out.println();
            System.out.println("The same using field iterator: \nSystem info for " + destination.getAttributes().getSystemID() + ":\n");
            for (JCoField field : exportStructure) {
                System.out.println(field.getName() + ":\t" + field.getString());
            }
            System.out.println();
        } catch (JCoException e) {
            e.printStackTrace();
        }
    }
}
