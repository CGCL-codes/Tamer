package drcl.inet.mac;

import java.math.*;
import drcl.inet.*;
import drcl.net.*;
import drcl.comp.*;

/**
 * This class defines the beacon timer for power saving mode
 *
 * @see Mac_802_11
 * @see Mac_802_11_Timer
 * @author Rong Zheng
 */
public class BeaconTimer extends Mac_802_11_Timer {

    public BeaconTimer(Mac_802_11 h) {
        super(h);
        o_.setType(MacTimeoutEvt.Beacon_timeout);
    }

    public void handle() {
        busy_ = false;
        paused_ = false;
        stime = 0.0;
        rtime = 0.0;
    }
}
