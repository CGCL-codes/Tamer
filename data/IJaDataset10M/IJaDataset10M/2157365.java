package jaxlib.ee.socketserver.jmx;

import java.io.IOException;
import jaxlib.jmx.annotation.JmxMetric;

/**
 * @author  jw
 * @since   JaXLib 1.0
 * @version $Id: NetworkInterfaceMBean.java 2295 2007-03-28 05:23:41Z joerg_wassmer $
 */
@javax.management.MXBean
public interface NetworkInterfaceMBean {

    public String getDisplayName() throws IOException;

    public String getHardwareAddress() throws IOException;

    public String getName() throws IOException;

    @JmxMetric(value = JmxMetric.COUNTER)
    public int getMTU() throws IOException;

    public boolean isLoopback() throws IOException;

    public boolean isMulticasting() throws IOException;

    public boolean isPointToPoint() throws IOException;

    public boolean isUp() throws IOException;

    public boolean isVirtual() throws IOException;

    public long ping(String host) throws IOException;

    public long ping(String host, int timeout) throws IOException;

    public long ping(String host, int timeout, int maxHops) throws IOException;
}
