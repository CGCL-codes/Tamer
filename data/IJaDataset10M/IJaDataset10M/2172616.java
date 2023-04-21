package org.matsim.core.basic.v01.households;

import java.util.ArrayList;
import org.matsim.api.basic.v01.Id;
import org.matsim.core.basic.v01.households.BasicIncome.IncomePeriod;

/**
 * @author dgrether
 */
public class BasicHouseholdBuilderImpl implements BasicHouseholdBuilder {

    public BasicHouseholdImpl createHousehold(Id householdId) {
        BasicHouseholdImpl hh = new BasicHouseholdImpl(householdId);
        hh.setMemberIds(new ArrayList<Id>());
        hh.setVehicleIds(new ArrayList<Id>());
        return hh;
    }

    public BasicIncome createIncome(double income, IncomePeriod period) {
        return new BasicIncomeImpl(income, period);
    }
}
