package org.wsmostudio.bpmo.model;

import java.util.List;
import org.eclipse.draw2d.geometry.Dimension;
import org.wsmo.service.Goal;

public class TaskGoal extends WorkflowEntity {

    private static final long serialVersionUID = 1L;

    public TaskGoal() {
        super();
        setName("New Task");
    }

    public void setWsmoGoal(Goal ref) {
        addPropertyValue(Consts.PROP_WSMO_GOAL, ref.getIdentifier().toString());
        firePropertyChange("size", null, ref);
    }

    public String getWsmoGoal() {
        return PropertiesActionHandler.getCastedValue(Consts.PROP_WSMO_GOAL, this);
    }

    public Dimension getPreferredSize() {
        return new Dimension(100, 70);
    }

    protected List<String> listSupportedProperties() {
        List<String> result = super.listSupportedProperties();
        result.add(Consts.PROP_WSMO_GOAL);
        result.add(Consts.PROP_DURATION);
        result.add(Consts.PROP_PRECONDS);
        result.add(Consts.PROP_POSTCONDS);
        result.add(Consts.PROP_INPUTS);
        result.add(Consts.PROP_OUTPUTS);
        result.add(Consts.PROP_ROLE);
        return result;
    }
}
