package net.sourceforge.eclipsetrader.core.db;

import java.util.HashMap;
import java.util.Map;

public class ChartObject extends PersistentObject {

    private ChartTab parent;

    private String pluginId;

    private Map parameters = new HashMap();

    public ChartObject() {
    }

    public ChartObject(Integer id) {
        super(id);
    }

    public ChartTab getParent() {
        return parent;
    }

    public void setParent(ChartTab chartTab) {
        this.parent = chartTab;
    }

    public String getPluginId() {
        return pluginId;
    }

    public void setPluginId(String pluginId) {
        this.pluginId = pluginId;
        setChanged();
    }

    public Map getParameters() {
        return parameters;
    }

    public void setParameters(Map parameters) {
        this.parameters = parameters;
        setChanged();
    }

    public synchronized void setChanged() {
        super.setChanged();
        if (getParent() != null) getParent().setChanged();
    }
}
