package jhomenet.commons.hw.driver;

import java.util.List;

/**
 *
 * @author Dave Irwin (jhomenet at gmail dot com)
 */
public interface HardwareGateway {

    /**
	 * 
	 * @return
	 */
    public List<String> getHardwareAddrs();

    /**
	 * 
	 * @param hardwareAddr
	 * @return
	 */
    public String getHardwareType(String hardwareAddr);

    /**
	 * 
	 * @param hardwareAddr
	 * @param channel
	 * @return
	 */
    public Double getTemperature(String hardwareAddr, Integer channel);
}
