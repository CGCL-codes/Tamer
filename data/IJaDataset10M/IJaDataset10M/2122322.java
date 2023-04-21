package org.matsim.basic.signalsystemsconfig;

import java.util.List;
import org.matsim.interfaces.basic.v01.Id;

/**
 * @author dgrether
 *
 */
public interface BasicAdaptiveSignalSystemControlInfo extends BasicSignalSystemControlInfo {

    void setAdaptiveControlerClass(String adaptiveControler);

    void addSignalGroupId(Id id);

    List<Id> getSignalGroupIds();

    String getAdaptiveControlerClass();
}
