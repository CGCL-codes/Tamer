package openjmx.adaptor.rmi;

/**
 * The management interface for the RMI adaptor
 *
 * @author <a href="mailto:biorn_steedom@users.sourceforge.net">Simone Bordet</a>
 * @version $Revision: 1.2 $
 */
public interface RMIAdaptorMBean {

    /**
	 * Starts this adaptor, so that it can accept incoming calls
	 * @see #stop
	 * @see #isRunning
	 */
    public void start() throws Exception;

    /**
	 * Stops this adaptor, so that it does not accept incoming calls anymore
	 * @see #start
	 */
    public void stop() throws Exception;

    /**
	 * Returns whether this adaptor has been started and not been stopped.
	 * @see #stop
	 */
    public boolean isRunning();

    /**
	 * Returns the JNDI name under which this RMI Adaptor is registered
	 */
    public String getJNDIName();

    /**
	 * Sets the JNDI name under which the RMI adaptor should be registered. <br>
	 * This method can be called only if this adaptor is not running.
	 */
    public void setJNDIName(String name);

    /**
	 * Puts a JNDI naming property for this adaptor. <br>
	 * This method can be called only if this adaptor is not running.
	 * @see #clearNamingProperties
	 */
    public void putNamingProperty(String property, String value);

    /**
	 * Reset the naming properties set for this adaptor. <br>
	 * This method can be called only if this adaptor is not running.
	 * @see #putNamingProperty
	 */
    public void clearNamingProperties();

    /**
	 * Returns the host name on which this adaptor is running
	 */
    public String getHostName();
}
