package net.ramponi.perfmeter.rstat;

import net.ramponi.perfmeter.DefaultGraphModel;
import net.ramponi.perfmeter.GraphModelManager;

public abstract class RstatGraphModel extends DefaultGraphModel {

    String hostName;

    RstatGraphModel(GraphModelManager manager, String hostName, String category, int series, int capacity, float minValue, float maxValue) {
        super(manager, category, series, capacity, minValue, maxValue);
        this.hostName = hostName;
    }

    RstatGraphModel(GraphModelManager manager, String hostName, String category, int series, int capacity, float scales[]) {
        super(manager, category, series, capacity, scales);
        this.hostName = hostName;
    }

    RstatGraphModel(GraphModelManager manager, String hostName, String category, int series, int capacity, ScaleComputer sc) {
        super(manager, category, series, capacity, sc);
        this.hostName = hostName;
    }

    public abstract void process(Statscommon rs, int havedisk);
}
