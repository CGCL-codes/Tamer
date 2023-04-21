package ru.cos.sim.meters.framework;

import java.util.HashSet;
import java.util.Set;

/**
 * @author zroslaw
 *
 */
public class ScheduledData<T extends MeasuredData<T>> implements MeasuredData<ScheduledData<T>> {

    protected Set<PeriodData<T>> periodMeasuredDataSet = new HashSet<PeriodData<T>>();

    public ScheduledData(Set<PeriodData<T>> periodMeasuredDataSet) {
        super();
        this.periodMeasuredDataSet = periodMeasuredDataSet;
    }

    public void addPeriodMeasuredData(PeriodData<T> periodMeasuredData) {
        periodMeasuredDataSet.add(periodMeasuredData);
    }

    public Set<PeriodData<T>> getPeriodMeasuredDataSet() {
        return periodMeasuredDataSet;
    }

    @SuppressWarnings("unchecked")
    @Override
    public ScheduledData<T> clone() {
        ScheduledData<T> result = null;
        try {
            result = (ScheduledData<T>) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        Set<PeriodData<T>> periodMeasuredDataSet = new HashSet<PeriodData<T>>();
        for (PeriodData<T> periodData : this.periodMeasuredDataSet) {
            periodMeasuredDataSet.add(periodData.clone());
        }
        result.periodMeasuredDataSet = periodMeasuredDataSet;
        return result;
    }

    @Override
    public void normalize(ScheduledData<T> norma) {
        throw new UnsupportedOperationException();
    }
}
