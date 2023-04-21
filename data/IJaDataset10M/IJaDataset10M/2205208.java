package eu.mpower.framework.sensor.fsa.adapter.bluetooth;

/**
 * This class aim to check whether connection bluetooth device is alive
 * @author Sixto Franco Martinez
 * @version 0.1 
 */
public class ConnectionTimeOut extends Thread {

    private AdapterBluetooth adapter;

    private long currenttime;

    private long lasttime;

    private long timeout;

    /**
     * The constructor for class ConnectionTimeOut
     * @param a AdapterBluetooth associated
     * @param t Timeout to set
     */
    public ConnectionTimeOut(AdapterBluetooth a, long t) {
        adapter = a;
        timeout = t;
    }

    public void setLasttime(long lasttime) {
        this.lasttime = lasttime;
    }

    /**
     * This thread is checking whether bluetooth connection is alive
     */
    public void run() {
        currenttime = System.currentTimeMillis();
        lasttime = currenttime;
        long difference;
        while (!this.isInterrupted()) {
            try {
                currenttime = System.currentTimeMillis();
                difference = currenttime - lasttime;
                if (difference > timeout) {
                    adapter.setSensorOut(true);
                    adapter.disconnect();
                    break;
                }
                Thread.sleep(timeout);
            } catch (InterruptedException ex) {
                this.interrupt();
            }
        }
    }
}
