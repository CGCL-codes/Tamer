package org.vrforcad.controller.devices.spheredevice;

/**
 * This class check the inactivity time of SphereDevice.
 * After five minutes of inactivity, stop the device.
 * 
 * @version 1.0 
 * @author Daniel Cioi <dan.cioi@vrforcad.org>
 */
public class TemporizatorDevice extends Thread implements Runnable {

    private boolean temporizator = true;

    public void run() {
        while (temporizator) {
            try {
                Thread.sleep(1000 * 60);
            } catch (InterruptedException e) {
            }
            long curentTime = System.currentTimeMillis();
        }
    }
}
