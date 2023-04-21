package greybird.srv.mxbeans;

/**
 * ServerControlMBean is a wrapper for NetworkServer used to expose the 
 * management capabilities of Apache Derby Network Server to JMX clients.
 * 
 * @version 0.1
 * 
 */
public interface ServerControlMXBean {

    public boolean start();

    public void sendRunInfo();

    public ServerMetaData getMetaData();

    public boolean isRunning();

    public void stop();

    public int getPort();

    public void setPort(int port);

    public String getHost();

    public void setHost(String host);

    public String getHome();

    public void setHome(String home);
}
