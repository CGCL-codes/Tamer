package org.matsim.basic.signalsystemsconfig;

import org.matsim.interfaces.basic.v01.Id;

/**
 * @author dgrether
 */
public class BasicSignalSystemConfigurationImpl implements BasicSignalSystemConfiguration {

    private final Id lightSignalSystemId;

    private BasicSignalSystemControlInfo controlInfo;

    public BasicSignalSystemConfigurationImpl(final Id lightSignalSystemId) {
        this.lightSignalSystemId = lightSignalSystemId;
    }

    /**
	 * @see org.matsim.basic.signalsystemsconfig.BasicSignalSystemConfiguration#setSignalSystemControlInfo(org.matsim.basic.signalsystemsconfig.BasicSignalSystemControlInfo)
	 */
    public void setSignalSystemControlInfo(final BasicSignalSystemControlInfo controlInfo) {
        this.controlInfo = controlInfo;
    }

    /**
	 * @see org.matsim.basic.signalsystemsconfig.BasicSignalSystemConfiguration#getSignalSystemId()
	 */
    public Id getSignalSystemId() {
        return this.lightSignalSystemId;
    }

    /**
	 * @see org.matsim.basic.signalsystemsconfig.BasicSignalSystemConfiguration#getControlInfo()
	 */
    public BasicSignalSystemControlInfo getControlInfo() {
        return this.controlInfo;
    }
}
