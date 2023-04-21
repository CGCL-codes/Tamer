package repast.simphony.data2;

import repast.simphony.engine.schedule.ISchedule;

/**
 * DataSource that returns the current tick count.
 * 
 * @author Nick Collier
 */
public class TickCountDataSource implements AggregateDataSource, NonAggregateDataSource {

    public static final String ID = "tick";

    private String id = ID;

    private ISchedule schedule;

    /**
   * Resets the schedule in this TickCountDataSource to the specified schedule.
   * 
   * @param schedule
   *          the new schedule
   */
    public void resetSchedule(ISchedule schedule) {
        this.schedule = schedule;
    }

    @Override
    public Class<?> getSourceType() {
        return void.class;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Class<Double> getDataType() {
        return Double.class;
    }

    @Override
    public Double get(Object obj) {
        double val = schedule.getTickCount();
        if (val < 0) val = 0;
        return val;
    }

    @Override
    public Double get(Iterable<?> objs, int size) {
        return get(null);
    }

    @Override
    public void reset() {
    }
}
