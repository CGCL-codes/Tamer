package org.epoline.impexp.jsf.loader;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.epoline.impexp.jsf.loader.dl.LoaderProxy;
import org.epoline.impexp.jsf.loader.dl.LoaderProxyInterface;
import org.epoline.jsf.client.ServiceNotAvailableException;
import org.epoline.jsf.client.ServiceProvider;
import org.epoline.jsf.client.ServiceProviderAttribute;
import org.epoline.jsf.entries.ServiceAttribute;
import org.epoline.jsf.services.core.JiniService;
import org.epoline.jsf.utils.Util;
import org.epoline.monitoring.MonitoringFrame;
import org.epoline.service.support.BaseServiceMonitor;
import org.epoline.service.support.PropertyException;
import org.epoline.service.support.ServiceSupport;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;

/**
 * Class that will be used by the GUI Monitor
 */
public class LoaderServiceMonitor extends BaseServiceMonitor {

    /**
	 * String used in XML for specifying the status of the Loader system
	 */
    public static final String LOADER_STATUS = "LoaderStatus";

    public static final String JOBS_STATUS_0 = "JOBS_0";

    public static final String JOBS_STATUS_1 = "JOBS_1";

    public static final String JOBS_STATUS_2 = "JOBS_2";

    public static final String JOBS_STATUS_3 = "JOBS_3";

    public static final String JOBS_STATUS_4 = "JOBS_4";

    public static final String JOBS_STATUS_5 = "JOBS_5";

    public static final String JOBS_STATUS_6 = "JOBS_6";

    public static final String JOBS_STATUS_7 = "JOBS_7";

    public static final String JOBS_DELAYED = "JOBS_DELAYED";

    public static final String JOBS_LOADED = "JOBS_LOADED";

    private String loaderStatus;

    private String jobs_0;

    private String jobs_1;

    private String jobs_2;

    private String jobs_3;

    private String jobs_4;

    private String jobs_5;

    private String jobs_6;

    private String jobs_7;

    private String jobs_delayed;

    private String jobs_loaded;

    private Properties properties;

    private Logger log;

    /**
	 * ServiceMonitor constructor.
	 * @param proxy The proxy to be used for checking the status of the service
	 * @param props The object containing required properties
	 * @param aJiniService The jiniService object, required for terminating the service via the GUI
	 * @exception IOException error creating the Monitoring object
	 */
    public LoaderServiceMonitor(LoaderProxy proxy, Properties props, JiniService aJiniService, Logger log) throws IOException {
        super(proxy, proxy.getClass().getName(), Integer.parseInt(props.getProperty("org.epoline.monitoring.port", "0")), aJiniService);
        this.log = log;
        init();
        properties = props;
        ServiceProvider.setContext(props);
        showProperties(props, "Properties");
        try {
            start();
        } catch (Exception e) {
            log.error("Error starting monitor", e);
        }
    }

    /**
	 * Get the current status of the service (discover it) This is the left button in the GUI Monitor
	 * @return MonitoringFrame.SERVICE_OK if all is OKE, otherwhise MonitoringFrame.SERVICE_KO
	 */
    public String getServerStatus() {
        try {
            String attributes = ServiceSupport.getProperty(properties, "org.epoline.jsf.attributes", null, null, null);
            String hostAttrib = "hostname=" + InetAddress.getLocalHost().getHostName();
            ServiceAttribute servAttribs[] = Util.NvpList2ServiceAttribute(new String[] { attributes, hostAttrib });
            ServiceProviderAttribute spa[] = new ServiceProviderAttribute[servAttribs.length];
            for (int i = 0; i < servAttribs.length; i++) {
                spa[i] = new ServiceProviderAttribute(servAttribs[i].name, servAttribs[i].value);
            }
            ServiceProvider.getInstance().getService(LoaderProxyInterface.NAME, spa);
            return MonitoringFrame.SERVICE_OK;
        } catch (PropertyException e) {
            log.error("Service not found", e);
            return MonitoringFrame.SERVICE_KO;
        } catch (UnknownHostException e) {
            log.error("Service not found", e);
            return MonitoringFrame.SERVICE_KO;
        } catch (ServiceNotAvailableException e) {
            log.error("Service not found", e);
            return MonitoringFrame.SERVICE_KO;
        } catch (ClassNotFoundException e) {
            log.error("Service not found", e);
            return MonitoringFrame.SERVICE_KO;
        }
    }

    /**
	 * Get the current status for Loader
	 * @return The status for Loader
	 */
    public String getLoaderStatus() {
        getData();
        return loaderStatus;
    }

    /**
	 * Get the total jobs in status 0
	 * @return The status for Loader
	 */
    public String getStatus0Jobs() {
        getData();
        return jobs_0;
    }

    /**
	 * Get the total jobs in status 1
	 * @return The status for Loader
	 */
    public String getStatus1Jobs() {
        getData();
        return jobs_1;
    }

    /**
	 * Get the total jobs in status 2
	 * @return The status for Loader
	 */
    public String getStatus2Jobs() {
        getData();
        return jobs_2;
    }

    /**
	 * Get the total jobs in status 3
	 * @return The status for Loader
	 */
    public String getStatus3Jobs() {
        getData();
        return jobs_3;
    }

    /**
	 * Get the total jobs in status 4
	 * @return The status for Loader
	 */
    public String getStatus4Jobs() {
        getData();
        return jobs_4;
    }

    /**
	 * Get the total jobs in status 5
	 * @return The status for Loader
	 */
    public String getStatus5Jobs() {
        getData();
        return jobs_5;
    }

    /**
	 * Get the total jobs in status 6
	 * @return The status for Loader
	 */
    public String getStatus6Jobs() {
        getData();
        return jobs_6;
    }

    /**
	 * Get the total jobs in status 7
	 * @return The status for Loader
	 */
    public String getStatus7Jobs() {
        getData();
        return jobs_7;
    }

    /**
	 * Get the total jobs delayed
	 * @return The status for Loader
	 */
    public String getDelayedJobs() {
        getData();
        return jobs_delayed;
    }

    /**
	 * Get the total jobs loaded
	 * @return The status for Loader
	 */
    public String getLoadedJobs() {
        getData();
        return jobs_loaded;
    }

    /**
	 * Process the XML data is return from service, it will contain the details that need to be displayed
	 * @param doc The XML Document that contains the data
	 */
    protected void handleDataFromProxy(Document doc) {
        loaderStatus = "???";
        try {
            Element e = doc.getRootElement();
            Attribute a = e.getAttribute(LOADER_STATUS);
            if (a != null) loaderStatus = a.getValue();
            a = e.getAttribute(JOBS_STATUS_0);
            if (a != null) jobs_0 = a.getValue();
            a = e.getAttribute(JOBS_STATUS_1);
            if (a != null) jobs_1 = a.getValue();
            a = e.getAttribute(JOBS_STATUS_2);
            if (a != null) jobs_2 = a.getValue();
            a = e.getAttribute(JOBS_STATUS_3);
            if (a != null) jobs_3 = a.getValue();
            a = e.getAttribute(JOBS_STATUS_4);
            if (a != null) jobs_4 = a.getValue();
            a = e.getAttribute(JOBS_STATUS_5);
            if (a != null) jobs_5 = a.getValue();
            a = e.getAttribute(JOBS_STATUS_6);
            if (a != null) jobs_6 = a.getValue();
            a = e.getAttribute(JOBS_STATUS_7);
            if (a != null) jobs_7 = a.getValue();
            a = e.getAttribute(JOBS_DELAYED);
            if (a != null) jobs_delayed = a.getValue();
            a = e.getAttribute(JOBS_LOADED);
            if (a != null) jobs_loaded = a.getValue();
        } catch (Exception e) {
            log.error("Error handling data from proxy", e);
        }
    }

    /**
	 * Do all the things you need to do for initializing the MOnitoring GUI.
	 * In this case add the "Loader Status" field in the runtime info page.
	 */
    protected void init() {
        try {
            addMethod("Runtime Info", "getLoaderStatus", "Loader status");
            addMethod("Runtime Info", "getStatus0Jobs", "Jobs waiting for Document Creation");
            addMethod("Runtime Info", "getStatus1Jobs", "Jobs waiting for Document acceptance");
            addMethod("Runtime Info", "getStatus2Jobs", "Jobs waiting for Image Loading");
            addMethod("Runtime Info", "getStatus3Jobs", "Jobs waiting for Document Update");
            addMethod("Runtime Info", "getStatus4Jobs", "Jobs waiting for Distribution");
            addMethod("Runtime Info", "getStatus5Jobs", "Jobs waiting for Printing");
            addMethod("Runtime Info", "getStatus6Jobs", "Jobs waiting for Backup");
            addMethod("Runtime Info", "getStatus7Jobs", "Jobs waiting for Extraction");
            addMethod("Runtime Info", "getDelayedJobs", "Jobs waiting for retry");
            addMethod("Runtime Info", "getLoadedJobs", "Documents Loaded");
        } catch (Exception e) {
            log.error("Error init monitor", e);
        }
    }
}
