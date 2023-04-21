package com.jmbaai.bombsight.input.serial;

/**
 * Tests the PNI module and connection in a pull configuration (rotations are
 * pulled by the app as fast as possible).
 * 
 * @author Jon Barrilleaux of JMB and Associates, Inc.
 * 
 */
public class TestPniPull {

    public static void main(String[] args) {
        PniConnection pniConnection = new PniConnection();
        pniConnection.openConnection("com1");
        try {
            String version = pniConnection.getModuleVersion();
            System.out.println("TestPniPull: version=" + version);
            float[] rotation = new float[3];
            while (true) {
                try {
                    pniConnection.getModuleRotation(rotation);
                    System.out.println("TestPniPull: hpr=" + " " + String.format("%7.2f°", rotation[0]) + " " + String.format("%7.2f°", rotation[1]) + " " + String.format("%7.2f°", rotation[2]));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            pniConnection.closeConnection();
        }
    }
}
