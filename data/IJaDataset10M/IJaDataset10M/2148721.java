package ru.cos.sim.agents.tlns.xml;

import org.jdom.Element;
import org.jdom.Namespace;
import ru.cos.sim.agents.tlns.data.RegularTLNData;
import ru.cos.sim.agents.tlns.data.ScheduleTableData;
import ru.cos.sim.mdf.MDFReader;

/**
 * 
 * @author zroslaw
 */
public class RegularTLNDataReader {

    private static final Namespace NS = MDFReader.MDF_NAMESPACE;

    private static final String SCHEDULE_TIME_SHIFT = "scheduleTimeShift";

    private static final String SCHEDULE = "Schedule";

    public static RegularTLNData read(Element tlnElement) {
        RegularTLNData regularTLNData = new RegularTLNData();
        Element shiftElement = tlnElement.getChild(SCHEDULE_TIME_SHIFT, NS);
        regularTLNData.setScheduleTimeShift(Float.parseFloat(shiftElement.getText()));
        Element scheduleElement = tlnElement.getChild(SCHEDULE, NS);
        ScheduleTableData schedule = ScheduleTableDataReader.read(scheduleElement);
        regularTLNData.setScheduleTable(schedule);
        return regularTLNData;
    }
}
