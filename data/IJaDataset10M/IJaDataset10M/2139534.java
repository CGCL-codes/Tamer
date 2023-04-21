package org.matsim.interfaces.core.v01;

import java.util.Map;
import java.util.SortedSet;
import org.matsim.basic.v01.BasicOpeningTime;
import org.matsim.basic.v01.BasicOpeningTime.DayType;
import org.matsim.interfaces.basic.v01.facilities.BasicActivityOption;

public interface ActivityOption extends BasicActivityOption {

    @Deprecated
    public void setCapacity(final int capacity);

    public void setOpentimes(Map<DayType, SortedSet<BasicOpeningTime>> opentimes);

    public Facility getFacility();

    public Map<DayType, SortedSet<BasicOpeningTime>> getOpentimes();
}
