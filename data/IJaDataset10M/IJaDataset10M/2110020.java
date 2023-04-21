package net.solarnetwork.node.support;

import java.util.List;
import net.solarnetwork.node.settings.SettingSpecifier;
import net.solarnetwork.node.settings.support.BasicTextFieldSettingSpecifier;

/**
 * Parameters to configure a serial port with.
 * 
 * <p>
 * The configurable properties of this class are:
 * </p>
 * 
 * <dl class="class-properties">
 * <dt></dt>
 * <dd></dd>
 * </dl>
 * 
 * @author matt
 * @version $Revision: 2079 $
 */
public class SerialPortBeanParameters extends SerialPortBean {

    private String serialPort = "/dev/ttyUSB0";

    private String commPortAppName = "SolarNode";

    private long maxWait;

    /**
	 * Get a list of setting specifiers for this bean.
	 * 
	 * @param prefix
	 *            the bean prefix
	 * @return setting specifiers
	 */
    public static List<SettingSpecifier> getDefaultSettingSpecifiers(String prefix) {
        List<SettingSpecifier> results = SerialPortBean.getDefaultSettingSpecifiers(prefix);
        results.add(new BasicTextFieldSettingSpecifier(prefix + "commPortAppName", "SolarNode"));
        results.add(new BasicTextFieldSettingSpecifier(prefix + "maxWait", "0"));
        return results;
    }

    public long getMaxWait() {
        return maxWait;
    }

    public void setMaxWait(long maxWait) {
        this.maxWait = maxWait;
    }

    public String getSerialPort() {
        return serialPort;
    }

    public void setSerialPort(String serialPort) {
        this.serialPort = serialPort;
    }

    public String getCommPortAppName() {
        return commPortAppName;
    }

    public void setCommPortAppName(String commPortAppName) {
        this.commPortAppName = commPortAppName;
    }
}
