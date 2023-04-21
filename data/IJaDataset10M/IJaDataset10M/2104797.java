package model.commission;

import model.Vendor;
import org.joda.time.ReadableInterval;

public interface CommisionsManager {

    public abstract CommissionSummary commissionAt(Vendor vendor, ReadableInterval interval);
}
