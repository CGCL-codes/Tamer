package com.kescom.matrix.core.batch;

import java.util.Calendar;
import java.util.Date;
import com.kescom.matrix.core.db.IndexBaseND;

public class BatchPolicy extends IndexBaseND implements IBatchPolicy {

    private Date nextAllow;

    private int secondsBetweenAllow = -1;

    public boolean allowReady(IBatchJob job) {
        if (secondsBetweenAllow < 0) return true;
        if (nextAllow == null) {
            nextAllow = calcNextAllow();
            return true;
        }
        if ((new Date()).after(nextAllow)) {
            nextAllow = calcNextAllow();
            return true;
        }
        job.setNextPolicyCheckDate(nextAllow);
        return false;
    }

    private Date calcNextAllow() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, secondsBetweenAllow);
        return cal.getTime();
    }

    public int getSecondsBetweenAllow() {
        return secondsBetweenAllow;
    }

    public void setSecondsBetweenAllow(int secondsBetweenAllow) {
        this.secondsBetweenAllow = secondsBetweenAllow;
    }

    public Date getNextAllow() {
        return nextAllow;
    }

    public void setNextAllow(Date nextAllow) {
        this.nextAllow = nextAllow;
    }
}
