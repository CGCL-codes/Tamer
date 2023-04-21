package org.mbari.vcr.timer;

import org.mbari.util.IObserver;
import org.mbari.vcr.IVCR;
import org.mbari.vcr.IVCRState;
import org.mbari.vcr.VCRAdapter;

/**
 *
 * @author brian
 */
public class WriteTimeMonitor extends Monitor {

    public static final String MONITOR_NAME = "WriteTimeMonitor";

    private IObserver stateObserver = new IObserver() {

        public void update(final Object obj, final Object changeCode) {
            IVCRState state = (IVCRState) obj;
            if (!state.isRecording()) {
                stop();
            } else {
                start();
            }
        }
    };

    /**
     * Constructs ...
     */
    public WriteTimeMonitor() {
        this(new VCRAdapter());
        setIntervalMin(250);
        setInterval(500);
    }

    /**
     * Constructs ...
     *
     * @param vcr
     */
    public WriteTimeMonitor(final IVCR vcr) {
        super(WriteTimeTimerTask.class, MONITOR_NAME, vcr);
    }

    @Override
    public synchronized void setVcr(final IVCR vcr) {
        IVCR oldVcr = getVcr();
        if (oldVcr != null) {
            oldVcr.getVcrState().removeObserver(stateObserver);
        }
        vcr.getVcrState().addObserver(stateObserver);
        super.setVcr(vcr);
    }
}
