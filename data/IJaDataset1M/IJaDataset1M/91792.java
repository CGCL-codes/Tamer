package jhomenet.server.hw.driver.dummy;

import org.apache.log4j.Logger;
import jhomenet.commons.hw.driver.HardwareDriverException;
import jhomenet.commons.hw.driver.HardwareContainer;
import jhomenet.commons.hw.driver.TemperatureDriver;
import jhomenet.commons.utils.FormatUtils;
import jhomenet.server.util.*;

/**
 * TODO: Class description.
 * 
 * @author Dave Irwin (jhomenet at gmail dot com)
 */
public class DummyTempDriver implements TemperatureDriver {

    /**
     * Define a logger.
     */
    private static Logger logger = Logger.getLogger(DummyTempDriver.class.getName());

    /**
     * Driver Id.
     */
    private final int id;

    private final long creationTime;

    /**
     * Reference to the container object.
     */
    private final HardwareContainer container;

    /**
     * Define the number of supported communication channels.
     */
    private final int numSupportedChannels = 1;

    /**
     * Create a new random number generator.
     */
    private static final RandomNumberGenerator r = new RandomNumberGenerator();

    /**
     * Constructor.
     * 
     * @param container
     */
    public DummyTempDriver(HardwareContainer container) {
        super();
        this.container = container;
        this.id = r.nextInt();
        this.creationTime = System.currentTimeMillis();
    }

    /** 
     * Return a raw test temperature value measured in Celcius. The test method will
     * return temperature values ranging from -40C to +60C (equivalent to -40F to
     * +140F).
     * 
     * @see jhomenet.commons.hw.driver.TemperatureDriver#getTemperature()
     */
    public Double getTemperature() throws HardwareDriverException {
        double value = r.nextUniform(-40d, 60d);
        logger.debug("Raw temperature: " + FormatUtils.formatDouble(value) + " C [" + container.getAddressAsString() + "]");
        return value;
    }

    /**
     * Return a raw test temperature value measured in Celcius. The test method will
     * return temperature values ranging from -40C to +60C (equivalent to -40F to
     * +140F).
     * 
     * @see jhomenet.commons.hw.driver.TemperatureDriver#getTemperature(java.lang.Integer)
     */
    public Double getTemperature(Integer channel) throws HardwareDriverException {
        return getTemperature();
    }

    /** 
     * @see jhomenet.commons.hw.driver.HardwareDriver#getNumberSupportedChannels()
     */
    public Integer getNumberSupportedChannels() {
        return numSupportedChannels;
    }
}
