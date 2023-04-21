package org.bpmsuite.vo.leave;

/**
 * @author Dirk Weiser
 **/
public abstract class AbstractLeaveQuota implements LeaveQuota {

    public Float getAnnualLeave() {
        float remaining = getAnnualLeaveRemaining().floatValue();
        float taken = getAnnualLeaveTaken().floatValue();
        return new Float(remaining + taken);
    }

    public void takeLeave(float leave) throws LeaveQuotaException {
        float remaining = getAnnualLeaveRemaining().floatValue() - leave;
        float taken = getAnnualLeaveTaken().floatValue() + leave;
        if (remaining < 0) {
            throw new LeaveQuotaException("Not enough available leave in this quota!");
        }
        setAnnualLeaveRemaining(new Float(remaining));
        setAnnualLeaveTaken(new Float(taken));
    }
}
