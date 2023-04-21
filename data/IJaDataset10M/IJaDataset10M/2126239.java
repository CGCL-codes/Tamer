package jhomenet.server.hw.driver.tini;

import jhomenet.commons.hw.driver.HardwareContainer;
import jhomenet.commons.hw.driver.HardwareDriverException;
import jhomenet.commons.hw.driver.TemperatureDriver;

/**
 *
 * @author Dave Irwin (jhomenet at gmail dot com)
 */
public class TiniTempDriver implements TemperatureDriver {

    /**
	 * 
	 */
    private static final Integer numChannels = 1;

    /**
	 * 
	 */
    private final TiniTempContainer container;

    /**
	 * 
	 * @param container
	 */
    public TiniTempDriver(HardwareContainer container) {
        super();
        if (container instanceof TiniTempContainer) this.container = (TiniTempContainer) container; else throw new ClassCastException("Container cannot be cast to a TiniTempContainer");
    }

    /**
	 * @see jhomenet.commons.hw.driver.TemperatureDriver#getTemperature()
	 */
    @Override
    public Double getTemperature() throws HardwareDriverException {
        return this.getTemperature(0);
    }

    /**
	 * @see jhomenet.commons.hw.driver.TemperatureDriver#getTemperature(java.lang.Integer)
	 */
    @Override
    public Double getTemperature(Integer channel) throws HardwareDriverException {
        return this.container.getTemperature(channel);
    }

    /**
	 * @see jhomenet.commons.hw.driver.HardwareDriver#getNumberSupportedChannels()
	 */
    @Override
    public Integer getNumberSupportedChannels() {
        return numChannels;
    }
}
