package at.redcross.tacos.web.beans.dto;

import java.math.BigDecimal;
import java.util.Date;
import at.redcross.tacos.dbal.entity.EntityImpl;
import at.redcross.tacos.dbal.entity.RosterEntry;

public class RosterStatisticEntry extends EntityImpl {

    private static final long serialVersionUID = 7278065110302498134L;

    /** the wrapped entry */
    private final RosterEntry entity;

    /** the amount of hours */
    private final double amount;

    /**
     * Creates a new statistic entry for the given entry
     */
    public RosterStatisticEntry(RosterEntry entity) {
        this.entity = entity;
        this.amount = calcAmount(entity);
    }

    @Override
    public String getDisplayString() {
        return entity.getDisplayString() + " amount=" + amount;
    }

    @Override
    public Object getOid() {
        return null;
    }

    private double calcAmount(RosterEntry entry) {
        Date start = entry.getRealStartDateTime();
        Date end = entry.getRealEndDateTime();
        if (start == null || end == null) {
            return 0;
        }
        double duration = (end.getTime() - start.getTime()) / 1000.0 / 3600.0;
        BigDecimal bigDecimal = new BigDecimal(String.valueOf(duration));
        return bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public RosterEntry getRosterEntry() {
        return entity;
    }

    public double getAmount() {
        return amount;
    }
}
