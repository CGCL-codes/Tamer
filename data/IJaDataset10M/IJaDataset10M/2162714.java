package edu.ucdavis.genomics.metabolomics.binbase.cluster.jmx;

/**
 * notoficiation configuration
 * 
 * @author wohlgemuth
 */
public interface NotifierJMXMBean extends javax.management.MBeanRegistration {

    /**
	 * we enable twitter notification
	 * @return
	 */
    public boolean isEnableTwitter();

    /**
	 * 
	 * @param enableTwitter
	 */
    public void setEnableTwitter(boolean enableTwitter);

    /**
	 * we enable email notification
	 * @return
	 */
    public boolean isEnableEmail();

    public void setEnableEmail(boolean enableEmail);

    /**
	 * we enable that all reports are be forwarded
	 * @return
	 */
    public boolean isEnableReportForwarding();

    public void setEnableReportForwarding(boolean enableReportForwarding);

    /**
	 * fires a simple test message with the highest possible priority
	 * @throws Exception 
	 */
    public void fireTestMessage() throws Exception;
}
