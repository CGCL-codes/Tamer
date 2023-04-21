package net.sf.chronos4j.hrmTest.domain;

import org.joda.time.ReadableInterval;
import net.sf.chronos4j.Timeline;
import net.sf.chronos4j.TimelineElement;

public class DepartmentTimeline extends Timeline<Department> {

    @Override
    public TimelineElement<Department> makeTLE(ReadableInterval during, Department value) {
        return new DepartmentHistoryTLE(during, value);
    }
}
