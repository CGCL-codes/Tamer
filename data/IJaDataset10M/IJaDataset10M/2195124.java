package ru.nsu.ccfit.pm.econ.net.engine.events;

import ru.nsu.ccfit.pm.econ.common.engine.events.IUDividendVoteEvent;
import ru.nsu.ccfit.pm.econ.net.annotations.SerializeThis;

/**
 * @author orfest
 * 
 */
public class DividendVoteEvent extends GameEvent implements IUDividendVoteEvent {

    @SerializeThis
    private long companyId = Long.MAX_VALUE;

    @SerializeThis
    private double selectedDPR = Double.MAX_VALUE;

    public DividendVoteEvent() {
    }

    public long getCompanyId() {
        return companyId;
    }

    public double getSelectedDPR() {
        return selectedDPR;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    public void setSelectedDPR(double selectedDPR) {
        this.selectedDPR = selectedDPR;
    }
}
