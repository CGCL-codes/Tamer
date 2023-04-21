package org.dbpt.performance;

import java.util.List;

public class MeasureSwitcher extends ContainerItem {

    private MeasureHolder measureHolder;

    public MeasureSwitcher() {
        super();
    }

    public MeasureSwitcher(List<DBPTItem> children) {
        super(children);
    }

    public MeasureSwitcher(DBPTItem child1, DBPTItem child2, DBPTItem child3) {
        super(child1, child2, child3);
    }

    public MeasureSwitcher(DBPTItem child1, DBPTItem child2) {
        super(child1, child2);
    }

    public MeasureSwitcher(DBPTItem child) {
        super(child);
    }

    public MeasureHolder getMeasureHolder() {
        return measureHolder;
    }

    public void setMeasureHolder(MeasureHolder measureHolder) {
        this.measureHolder = measureHolder;
    }

    @Override
    public void doIteration() {
        super.doIteration();
        if (measureHolder != null) {
            measureHolder.fixMeasure(this);
        }
    }
}
