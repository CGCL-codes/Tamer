package iwork.state.ehimpl.beacon;

import iwork.eheap2.*;
import java.io.*;
import java.util.*;

/**
 * In our design, configuration information is considered a special
 * case of configuration information. ConfigManager is resposible for
 * maintaining configuration information for the
 * workspace. Configuration information is also modeled as state
 * variables but represents those state variables that (currently)
 * cannot be determined programatically at run-time but have to be set
 * manually by sys admins. Examples include such state variables as
 * dimensions of the workspace, physical coordinates of the various
 * devices in the workspace etc. ConfigManager can be run from the
 * command line and takes a file containing configuration infrmation
 * as a command-line argument. Once the ConfigManager is running, all
 * the configuration information may be accessed by any application
 * using the state API just like "proper" state variables.
 **/
public class ConfigManager {

    public static void main(String[] args) {
        try {
            if (args.length != 1 || System.getProperty("heapMachine") == null) System.out.println("Usage:  java -DheapMachine=<heapMachine> " + "iwork.state.ehimpl.beacon.ConfigManager " + "<configFile>");
            String configFile = args[0];
            String heapMachine = System.getProperty("heapMachine");
            int beacon_period = 0;
            if (System.getProperty("BEACON") != null) {
                beacon_period = (new Integer(System.getProperty("BEACON"))).intValue();
            } else {
                beacon_period = 2000;
            }
            FileInputStream fis = new FileInputStream(configFile);
            Properties prop = new Properties();
            prop.load(fis);
            Enumeration mEnum = prop.propertyNames();
            BeaconEvent beacon = new BeaconEvent("ConfigManager", beacon_period * 2);
            while (mEnum.hasMoreElements()) {
                String name = (String) mEnum.nextElement();
                String val = prop.getProperty(name);
                beacon.addVariable(name, val);
            }
            EventHeap eh = new EventHeap(heapMachine);
            while (true) {
                eh.putEvent(beacon);
                Thread.currentThread().sleep(beacon_period);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
