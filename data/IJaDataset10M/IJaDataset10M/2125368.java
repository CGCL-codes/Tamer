package com.patientis.client.deploy;

/**
 * @author patientos
 *
 */
public class DeployData {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        try {
            Deploy.createUpgradeDataOnly();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
