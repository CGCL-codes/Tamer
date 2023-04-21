package nl.kbna.dioscuri.module.pit;

/**
 * Class Clock
 * This class implements a pulsing clock mechanism. Based on a user-defined sleepTime the 
 * clock sends a pulse to the PIT-counters after sleeping.
 * 
 * Note:
 * - maybe this clock should be separated from PIT as a real PIT does not contain a clock
 * - This clock imitates the system clock (crystal timer in hardware)
 * - The (maximum) operating frequency of this clock should be 1193181 Hz (0.00083809581 ms/cycle).
 * 
 * 
 */
public class Clock extends Thread {

    private PIT pit;

    private Counter[] counters;

    private boolean keepRunning;

    private long sleepTime;

    /**
     * Constructor Clock
     * 
     * @param PIT pit the owner of this clock
     * @param Counter[] counters containing all counters of PIT that should receive a clockpulse
     */
    public Clock(PIT pit, Counter[] counters) {
        this.pit = pit;
        this.counters = counters;
        keepRunning = true;
        sleepTime = 1000;
    }

    /**
     * Implements the run method of Thread
     * 
     */
    public void run() {
        while (keepRunning) {
            for (int c = 0; c < counters.length; c++) {
                counters[c].clockPulse();
            }
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
                keepRunning = false;
            }
        }
    }

    /**
     * Calibrates the system clock in comparison with host machine
     * It sets a penalty time to which the clock should pause until the next pulse may be send.
     * TODO: Implement calibration of clock
     * 
     */
    private void calibrate() {
    }

    /**
     * Sets the keepRunning toggle
     * keepRunning states if the clockthread should keep running or not
     * 
     * @param boolean status
     */
    protected void setKeepRunning(boolean status) {
        keepRunning = status;
    }

    /**
     * Retrieves the current clockrate of this clock in milliseconds
     * 
     * @return long milliseconds defining how long the clock sleeps before sending a pulse
     */
    public long getClockRate() {
        return this.sleepTime;
    }

    /**
     * Sets the rate for this clock
     * 
     * @param long milliseconds defines how long the clock should wait periodically before sending a pulse
     */
    public void setClockRate(long milliseconds) {
        this.sleepTime = milliseconds;
    }
}
