package com.patientis.client.deploy;

import com.patientis.framework.logging.Log;

/**
 * One line class description
 *
 * 
 * <br/>  
 */
public class ChangePorts {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        try {
            Log.println("root directory: " + args[0]);
            Log.println("http port: " + args[1]);
            int base = Integer.parseInt(args[1]);
            Log.println("rmi port: " + (base + 1));
            Log.println("rmi port: " + (base + 2));
            JBoss.updatePorts(args[0], base);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
