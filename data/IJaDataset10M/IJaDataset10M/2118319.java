package com.kescom.matrix.core.olap.measure;

import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import com.kescom.matrix.core.olap.ICubeRuntime;
import com.kescom.matrix.core.olap.IMeasurement;
import com.kescom.matrix.core.series.IDataPoint;
import com.kescom.matrix.core.series.ISeries;

public class SeriesLastDataPointValueAggregationMeasurement implements IMeasurement {

    public enum Operator {

        SUM, AVG, COUNT
    }

    ;

    private String name = "LastDataPointValue";

    private int fieldIndex = 0;

    private Operator operator = Operator.AVG;

    private class MyState {

        long count = 0;

        double sum = 0;
    }

    @SuppressWarnings("unchecked")
    public Object getMeasurementResult(List list, ICubeRuntime runtime) {
        MyState state = new MyState();
        for (Object obj : (List<Object>) list) {
            if (!(obj instanceof ISeries)) continue;
            ISeries series = (ISeries) obj;
            long lastTime = series.getLastTime();
            if (lastTime < 0) continue;
            addDataPoint(state, series, lastTime);
        }
        switch(operator) {
            case AVG:
                return (state.count != 0) ? (state.sum / state.count) : 0.0;
            case COUNT:
                return state.count;
            case SUM:
                return state.sum;
            default:
                return null;
        }
    }

    private void addDataPoint(MyState state, ISeries series, long time) {
        IDataPoint dp = series.getDataPointByTime(time);
        Object values[] = dp.getValues();
        if (fieldIndex >= values.length) return;
        Object value = values[fieldIndex];
        if (!(value instanceof Number)) return;
        double dValue = ((Number) value).doubleValue();
        state.count++;
        state.sum += dValue;
    }

    public DetachedCriteria addMeasurementProjections(DetachedCriteria criteria, ICubeRuntime runtime) {
        return criteria;
    }

    public String getName(ICubeRuntime runtime) {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFieldIndex(int fieldIndex) {
        this.fieldIndex = fieldIndex;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }
}
