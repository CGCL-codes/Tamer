package org.matsim.basic.signalsystemsconfig;

import java.util.Map;
import org.matsim.interfaces.basic.v01.Id;

/**
 * @author dgrether
 *
 */
public interface BasicSignalSystemConfigurations {

    public BasicSignalSystemConfigurationsBuilder getBuilder();

    /**
	 * 
	 * @return a map containing all signal system configurations organized 
	 * by the Id of the SignalSystem
	 */
    public Map<Id, BasicSignalSystemConfiguration> getSignalSystemConfigurations();
}
