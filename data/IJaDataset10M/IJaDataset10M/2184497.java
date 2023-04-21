package edu.ucdavis.genomics.metabolomics.binbase.cluster.jmx;

import java.util.List;
import edu.ucdavis.genomics.metabolomics.util.status.notify.email.EmailConfiguration;

public interface EmailNotifierJMXMBean extends EmailConfiguration, javax.management.MBeanRegistration {

    /**
	 * returns the server name
	 * 
	 * @return
	 */
    public String getServer();

    /**
	 * sets the server
	 * 
	 * @param server
	 */
    public void setServer(String server);

    /**
	 * returns the username
	 * 
	 * @return
	 */
    public String getUsername();

    /**
	 * sets the username
	 * 
	 * @param username
	 */
    public void setUsername(String username);

    /**
	 * returns the password
	 * 
	 * @return
	 */
    public String getPassword();

    /**
	 * sets the password
	 * 
	 * @param password
	 */
    public void setPassword(String password);

    /**
	 * returns a list of all people who are registered to receive emails
	 * 
	 * @return
	 */
    public List<String> getEmailAddress();

    /**
	 * sets the address
	 * 
	 * @param address
	 */
    public void addEmailAddress(String address);

    /**
	 * returns the server port
	 * 
	 * @return
	 */
    public Integer getPort();

    public void setPort(Integer port);
}
