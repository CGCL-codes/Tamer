package org.matsim.basic.signalsystemsconfig;

import java.util.List;
import org.matsim.interfaces.basic.v01.Id;

public class BasicAdaptivePlanbasedSignalSystemControlInfoImpl extends BasicPlanBasedSignalSystemControlInfoImpl implements BasicAdaptivePlanBasedSignalSystemControlInfo {

    BasicAdaptiveSignalSystemControlInfo delegate;

    BasicAdaptivePlanbasedSignalSystemControlInfoImpl() {
        this.delegate = new BasicAdaptiveSignalSystemControlInfoImpl();
    }

    public void addSignalGroupId(Id id) {
        delegate.addSignalGroupId(id);
    }

    public String getAdaptiveControlerClass() {
        return delegate.getAdaptiveControlerClass();
    }

    public List<Id> getSignalGroupIds() {
        return delegate.getSignalGroupIds();
    }

    public void setAdaptiveControlerClass(String adaptiveControler) {
        delegate.setAdaptiveControlerClass(adaptiveControler);
    }
}
