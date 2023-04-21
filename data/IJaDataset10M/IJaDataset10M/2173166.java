package it.unibg.cs.jtvguide.model;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

public interface ScheduleInterface extends Iterable<Program> {

    public void add(Program p);

    public List<Program> getOnAirPrograms();

    public List<Program> getPrograms(Date d);

    public List<Program> getProgramsByName(String pattern);

    public List<Program> getProgramsFromDateOn(Date d);

    public List<Program> getProgramsFromDateToDate(Date from, Date to);

    public List<Program> getProgramsFromNowOn();

    public List<Program> getUpcomingPrograms();

    public Iterator<Program> iterator();
}
