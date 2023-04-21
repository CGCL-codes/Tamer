package org.mondemand.log4j;

import org.mondemand.Client;
import org.mondemand.transport.LWESTransport;
import org.apache.log4j.Level;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.LocationInfo;
import java.net.InetAddress;
import java.util.Map;
import java.util.Iterator;

/**
 * This is a log4j appender that uses the MonDemand API and
 * is configured with the LWES transport.
 * @author Michael Lum
 */
public class MonDemandAppender extends AppenderSkeleton {

    private Client client = null;

    private String progId;

    private int immediateSendLevel;

    private LWESTransport transport = null;

    private String address;

    private int port;

    private String iface;

    private int ttl;

    /**
   * Sets the program identifier for this appender.
   * @param progId the program identifier
   */
    public void setProgId(String progId) {
        this.progId = progId;
    }

    /**
   * Gets the program identifier for this appender.
   * @return the program identifier
   */
    public String getProgId() {
        return progId;
    }

    /**
   * Sets the immediate send level for this appender.
   * @param level the log level to send messages immediately
   */
    public void setImmediateSendLevel(int level) {
        this.immediateSendLevel = level;
    }

    /**
   * Gets the immediate send level for this appender.
   * @return the log level where messages are sent immediately
   */
    public int getImmediateSendLevel() {
        return this.immediateSendLevel;
    }

    /**
   * Sets the LWES address
   * @param address the address to use
   */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
   * Gets the LWES address
   * @return the IP address LWES is using
   */
    public String getAddress() {
        return address;
    }

    /**
   * Sets the LWES port
   * @param port the port to use
   */
    public void setPort(int port) {
        this.port = port;
    }

    /**
   * Gets the LWES port
   * @return the port LWES is using
   */
    public int getPort() {
        return port;
    }

    /**
   * Sets the LWES network interface
   * @param iface the interface to use
   */
    public void setInterface(String iface) {
        this.iface = iface;
    }

    /**
   * Gets the LWES network interface
   * @return the interface in use
   */
    public String getInterface() {
        return iface;
    }

    /**
   * Sets the LWES network TTL (for multicast)
   * @param ttl the TTL to use
   */
    public void setTtl(int ttl) {
        this.ttl = ttl;
    }

    /**
   * Gets the LWES network TTL (for multicast)
   * @return the TTL value
   */
    public int getTtl() {
        return ttl;
    }

    /***************************
   * LOG4J HOOKS             *
   ***************************/
    public boolean requiresLayout() {
        return false;
    }

    public void close() {
        client.flush();
    }

    public void activateOptions() {
        if (this.client == null) {
            this.client = new Client(progId);
        }
        if (this.transport == null) {
            try {
                InetAddress address = InetAddress.getByName(this.address);
                InetAddress iface = InetAddress.getByName(this.iface);
                this.transport = new LWESTransport(address, port, iface);
                this.client.addTransport(this.transport);
            } catch (Exception e) {
                errorHandler.error("Unable to create emitter", e, 1);
            }
        }
    }

    protected void append(LoggingEvent event) {
        try {
            LocationInfo info = event.getLocationInformation();
            Map<?, ?> properties = event.getProperties();
            Iterator<?> iterator = properties.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<?, ?> entry = (Map.Entry<?, ?>) iterator.next();
                String key = (String) entry.getKey();
                String value = (String) entry.getValue();
                client.addContext(key, value);
            }
            client.log(info.getFileName(), Integer.parseInt(info.getLineNumber()), getMonDemandLevel(event.getLevel()), null, event.getRenderedMessage(), null);
            client.removeAllContexts();
        } catch (Exception ex) {
            errorHandler.error("Unable to call MonDemandAppender", ex, 1);
        }
    }

    private int getMonDemandLevel(Level p) {
        if (p == null) return org.mondemand.Level.ALL;
        if (p.equals(org.apache.log4j.Level.FATAL)) {
            return org.mondemand.Level.EMERG;
        } else if (p.equals(org.apache.log4j.Level.ERROR)) {
            return org.mondemand.Level.ERROR;
        } else if (p.equals(org.apache.log4j.Level.WARN)) {
            return org.mondemand.Level.WARNING;
        } else if (p.equals(org.apache.log4j.Level.INFO)) {
            return org.mondemand.Level.INFO;
        } else if (p.equals(org.apache.log4j.Level.DEBUG)) {
            return org.mondemand.Level.DEBUG;
        } else {
            return org.mondemand.Level.OFF;
        }
    }
}
