package drcl.inet.mac;

import java.math.*;
import drcl.inet.*;
import drcl.net.*;
import drcl.comp.*;

/**
 * RxTimer
 * 
 * @see Mac_802_11
 * @see Mac_802_11_Timer
 * @author Ye Ge
 */
public class RxTimer extends Mac_802_11_Timer {

    public RxTimer(Mac_802_11 h) {
        super(h);
        o_.setType(MacTimeoutEvt.Rx_timeout);
    }

    /** 
     * This method is called in Mac_802_11 class.
     */
    public void handle() {
        busy_ = false;
        paused_ = false;
        stime = 0.0;
        rtime = 0.0;
    }
}
