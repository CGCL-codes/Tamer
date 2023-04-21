package com.shieldsbetter.paramour;

/**
 *
 * @author hamptos
 */
public class RecordingStopper {

    private boolean myRunningFlag = true;

    ;

    public void stop() {
        myRunningFlag = false;
    }

    public boolean stopped() {
        return !myRunningFlag;
    }
}
