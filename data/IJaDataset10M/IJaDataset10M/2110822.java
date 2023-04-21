package org.mbari.vcr.timer;

import org.mbari.vcr.IVCR;
import org.mbari.vcr.IVCRState;
import org.mbari.vcr.rs422.DeviceType;
import org.mbari.vcr.rs422.VCR;
import org.mbari.vcr.rs422.VCRReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TimerTask that reads the VTimeCode if the vcr is playing, otherwise it
 * requests the LTimeCode
 *
 * @author brian
 */
public class ReadTimecodeTimerTask extends VCRTimerTask {

    private static final Logger log = LoggerFactory.getLogger(ReadTimecodeTimerTask.class);

    private DeviceType deviceType;

    /**
     * Constructs ...
     */
    public ReadTimecodeTimerTask() {
        super();
    }

    public void run() {
        IVCR vcr = getVcr();
        if (vcr != null) {
            IVCRState state = vcr.getVcrState();
            if (state != null) {
                if ((deviceType != null) && deviceType.isVTimecodeSupported() && state.isPlaying()) {
                    vcr.requestVTimeCode();
                } else {
                    vcr.requestLTimeCode();
                }
            }
        }
    }

    @Override
    public void setVcr(IVCR vcr) {
        super.setVcr(vcr);
        deviceType = DeviceType.UNKNOWN;
        if (vcr instanceof VCR) {
            synchronized (vcr) {
                vcr.requestDeviceType();
                VCRReply vcrReply = (VCRReply) vcr.getVcrReply();
                deviceType = DeviceType.getDeviceType(vcrReply.getData());
            }
            if (log.isDebugEnabled()) {
                log.debug("Found VCR DeviceType = " + deviceType.getDescription());
            }
        }
    }
}
