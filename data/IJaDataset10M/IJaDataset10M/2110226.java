package jolo.server.jmx;

/**
 * @author heathm
 */
public interface JoloServerMBean {

    public String OBJECT_NAME = "Jolo:service=Server";

    public String[] states = new String[] { "Stopped", "Stopping", "Starting", "Started", "Failed" };

    public int STOPPED = 0;

    public int STOPPING = 1;

    public int STARTING = 2;

    public int STARTED = 3;

    public int FAILED = 4;

    /**
     * Method to create this MBean.
     *
     * @throws Exception Throw Exception to pass any exceptions on to the MBean server.
     */
    public void create() throws Exception;

    /**
     * Method to start this MBean.
     *
     * @throws Exception Throws Exception to pass any excpetion on to the MBean server.
     */
    public void start() throws Exception;

    /**
     * Method to stop this MBean
     */
    public void stop();

    /**
     * Method to destroy this MBean.
     */
    public void destroy();

    /**
     * Returns the name of the cluster group used by the server.  The name of the group is
     * what determines what cluster the server belongs to.  A unique name can be
     * used to place multiple clusters on the same network.
     * @return The group name used by the server cluster.
     */
    public String getClusterGroup();

    public void setClusterGroup(String clusterGroup);
}
