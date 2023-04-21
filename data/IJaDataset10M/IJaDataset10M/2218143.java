package test.jhomenet.server.persistence;

import javax.measure.unit.NonSI;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import jhomenet.commons.data.ValueData;
import jhomenet.commons.hw.mngt.HardwareManager;
import jhomenet.commons.hw.sensor.ValueSensor;
import jhomenet.commons.responsive.exec.DefaultResponsiveStringConverter;
import jhomenet.commons.responsive.exec.ResponsiveStringConverter;
import test.jhomenet.commons.hw.mngt.MockHardwareManager;
import test.jhomenet.commons.hw.sensor.MockTempSensor;

/**
 * TODO: Class description.
 * <p>
 * Id: $Id: $
 *
 * @author Dave Irwin (jhomenet at gmail dot com)
 */
public class ResponsivePersistenceTest {

    /**
	 * Define the root logger.
	 */
    private static Logger logger = Logger.getLogger(ResponsivePersistenceTest.class.getName());

    /**
	 * A mock hardware manager.
	 */
    private HardwareManager hardwareManager;

    /**
	 * Create mock value sensors.
	 */
    private final ValueSensor mockValueSensor1 = new MockTempSensor("0003", "mock temperature sensor");

    private final ValueSensor mockValueSensor2 = new MockTempSensor("0004", "mock temperature sensor");

    /**
	 * The sensor responsive object string converter reference.
	 */
    private ResponsiveStringConverter converter;

    /**
	 * 50 degrees fahrenheit value data object.
	 */
    private final ValueData fiftyDegreesF = new ValueData(50D, NonSI.FAHRENHEIT);

    /**
	 * 
	 */
    public ResponsivePersistenceTest() {
        super();
    }

    /**
	 * @throws java.lang.Exception
	 */
    @Before
    public void setUp() throws Exception {
        hardwareManager = new MockHardwareManager();
        hardwareManager.registerHardware(mockValueSensor1);
        hardwareManager.registerHardware(mockValueSensor2);
        converter = new DefaultResponsiveStringConverter(hardwareManager);
    }

    /**
	 * @throws java.lang.Exception
	 */
    @After
    public void tearDown() throws Exception {
    }

    /**
	 * 
	 */
    @Test
    public void persistConditionTest() {
    }
}
