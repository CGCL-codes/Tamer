package org.matsim.core.basic.signalsystemsconfig;

import java.util.ArrayList;
import java.util.List;
import org.matsim.api.basic.v01.Id;

/**
 * @author dgrether
 *
 */
public class BasicAdaptiveSignalSystemControlInfoImpl implements BasicAdaptiveSignalSystemControlInfo {

    private List<Id> signalGroupIds;

    private String adaptiveControlerClass;

    public void addSignalGroupId(Id id) {
        if (this.signalGroupIds == null) {
            this.signalGroupIds = new ArrayList<Id>();
        }
        this.signalGroupIds.add(id);
    }

    public String getAdaptiveControlerClass() {
        return this.adaptiveControlerClass;
    }

    public List<Id> getSignalGroupIds() {
        return this.signalGroupIds;
    }

    public void setAdaptiveControlerClass(String adaptiveControler) {
        this.adaptiveControlerClass = adaptiveControler;
    }
}
