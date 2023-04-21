package org.wsmostudio.bpmo.model;

import java.util.LinkedList;
import java.util.List;
import org.eclipse.draw2d.geometry.Dimension;

public class Condition extends WorkflowEntity {

    private static final long serialVersionUID = 1L;

    public String getCondition() {
        return PropertiesActionHandler.getCastedValue(Consts.PROP_CONDITION, this);
    }

    public void setCondition(String expr) {
        addPropertyValue(Consts.PROP_CONDITION, expr);
        firePropertyChange("size", this.size, this.size);
    }

    public Dimension getPreferredSize() {
        return new Dimension(90, 90);
    }

    protected List<String> listSupportedProperties() {
        List<String> result = new LinkedList<String>();
        result.add(Consts.PROP_CONDITION);
        return result;
    }
}
