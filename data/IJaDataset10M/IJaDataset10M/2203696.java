package org.epoline.print.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.epoline.jsf.client.ServiceNotAvailableException;
import org.epoline.jsf.client.ServiceProvider;
import org.epoline.jsf.client.ServiceProviderAttribute;
import org.epoline.jsf.client.ServiceStatus;
import org.epoline.jsf.services.core.JiniService;
import org.epoline.monitoring.MonitoringFrame;
import org.epoline.print.shared.PrintManagerServiceProxy;
import org.epoline.print.shared.PrintManagerServiceProxyInterface;
import org.epoline.service.support.BaseServiceMonitor;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;

public class PrintManagerServiceMonitor extends BaseServiceMonitor {

    public static final String STATUS = "Status";

    public static final String TOTALPROCESSEDPRINTJOBS = "totalProcessedPrintJobs";

    public static final String TOTALADDEDPFJOBS = "totalAddedPFJobs";

    public static final String TOTALPROCESSEDPFJOBS = "totalProcessedPFJobs";

    public static final String TOTALFAILEDPRINTJOBS = "totalFailedPrintJobs";

    public static final String TOTALFAILEDADDEDPFJOBS = "totalFailedAddedPFJobs";

    public static final String TOTALADDEDBATCHJOBS = "totalAddedBatchJobs";

    public static final String TOTALPROCESSEDBATCHJOBS = "totalProcessedBatchJobs";

    private String status;

    private String totalProcessedPrintJobs;

    private String totalAddedPFJobs;

    private String totalProcessedPFJobs;

    private String totalFailedPrintJobs;

    private String totalFailedAddedPFJobs;

    private String totalAddedBatchJobs;

    private String totalProcessedBatchJobs;

    private ServiceProvider sp;

    private Logger log;

    /**
	 * ServiceMonitor constructor comment.
	 */
    public PrintManagerServiceMonitor(PrintManagerServiceProxy proxy, Properties props, JiniService aJiniService, Logger log) throws IOException {
        super(proxy, proxy.getClass().getName(), Integer.parseInt(props.getProperty("org.epoline.monitoring.port", "0")), aJiniService);
        this.log = log;
        ServiceProvider.setContext(props);
        sp = ServiceProvider.getInstance();
        init();
        showProperties(props, "Properties");
        try {
            start();
        } catch (Exception e) {
            log.error("Error starting monitor", e);
        }
    }

    public String getStatusStatus() {
        getData();
        return status;
    }

    public String getTotalProcessedPrintJobs() {
        getData();
        return totalProcessedPrintJobs;
    }

    public String getTotalAddedPFJobs() {
        getData();
        return totalAddedPFJobs;
    }

    public String getTotalProcessedPFJobs() {
        getData();
        return totalProcessedPFJobs;
    }

    public String getTotalFailedPrintJobs() {
        getData();
        return totalFailedPrintJobs;
    }

    public String getTotalFailedAddedPFJobs() {
        getData();
        return totalFailedAddedPFJobs;
    }

    public String getTotalAddedBatchJobs() {
        getData();
        return totalAddedBatchJobs;
    }

    public String getTotalProcessedBatchJobs() {
        getData();
        return totalProcessedBatchJobs;
    }

    /**
	 * Get the current status of the service (discover it) This is the left button in the GUI Monitor
	 * @return MonitoringFrame.SERVICE_OK if all is OKE, otherwhise MonitoringFrame.SERVICE_KO
	 */
    public String getServerStatus() {
        try {
            ServiceProviderAttribute spa[] = { new ServiceProviderAttribute("hostname", InetAddress.getLocalHost().getHostName()) };
            sp.getService(PrintManagerServiceProxyInterface.NAME, spa);
            return MonitoringFrame.SERVICE_OK;
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

    protected void handleDataFromProxy(Document doc) {
        status = "???";
        totalProcessedPrintJobs = "???";
        totalAddedPFJobs = "???";
        totalProcessedPFJobs = "???";
        totalFailedPrintJobs = "???";
        totalFailedAddedPFJobs = "???";
        try {
            Element e = doc.getRootElement();
            Attribute a1 = e.getAttribute(STATUS);
            if (a1 != null) status = a1.getValue();
            a1 = e.getAttribute(TOTALPROCESSEDPRINTJOBS);
            if (a1 != null) totalProcessedPrintJobs = a1.getValue();
            a1 = e.getAttribute(TOTALADDEDPFJOBS);
            if (a1 != null) totalAddedPFJobs = a1.getValue();
            a1 = e.getAttribute(TOTALPROCESSEDPFJOBS);
            if (a1 != null) totalProcessedPFJobs = a1.getValue();
            a1 = e.getAttribute(TOTALFAILEDPRINTJOBS);
            if (a1 != null) totalFailedPrintJobs = a1.getValue();
            a1 = e.getAttribute(TOTALFAILEDADDEDPFJOBS);
            if (a1 != null) totalFailedAddedPFJobs = a1.getValue();
            a1 = e.getAttribute(TOTALADDEDBATCHJOBS);
            if (a1 != null) totalAddedBatchJobs = a1.getValue();
            a1 = e.getAttribute(TOTALPROCESSEDBATCHJOBS);
            if (a1 != null) totalProcessedBatchJobs = a1.getValue();
        } catch (Exception e) {
            log.error("Error handling data from proxy", e);
        }
    }

    public String doCommand(String aCommand) throws Exception {
        if (aCommand == null) {
            return null;
        } else if (aCommand.equals("PurgeFirstBatchJob")) {
            return PrintManagementController.getInstance().getBatchPrintManager().purgeFirstJob() ? "Done" : "Nothing to do";
        }
        return null;
    }

    protected List getCommands(ServiceStatus status) {
        List retVal = new LinkedList();
        retVal.add("PurgeFirstBatchJob");
        return retVal;
    }

    protected void init() {
        try {
            addMethod("Runtime Info", "getStatusStatus", "Status");
            addMethod("Runtime Info", "getTotalProcessedPrintJobs", "Total Processed Print Jobs");
            addMethod("Runtime Info", "getTotalAddedPFJobs", "Number of added Paperfile Print Jobs");
            addMethod("Runtime Info", "getTotalProcessedPFJobs", "Number of Processed Paperfile Printjobs");
            addMethod("Runtime Info", "getTotalFailedPrintJobs", "Total Number of failed Print Jobs");
            addMethod("Runtime Info", "getTotalFailedAddedPFJobs", "Number of Failed addings of Paperfile Print Jobs");
            addMethod("Runtime Info", "getTotalAddedBatchJobs", "Number of added Batch Print Jobs");
            addMethod("Runtime Info", "getTotalProcessedBatchJobs", "Number of processed Batch Print Jobs");
            addMonitoredFile(PrintConfigManager.getInstance().getString("org.epoline.services.logfile", "./SomeFileThatDoesNotExist"), "Runtime Info", "Log4j Log");
            addMonitoredFile(PrintConfigManager.getInstance().getString("MUSEPAPERFILESTATUSFILE", "./SomeFileThatDoesNotExist"), "Runtime Info", "MUSE Log");
            for (int i = 1; i < 10; i++) {
                String theFile = PrintManagementController.getInstance().composeLastPFStatusFile(i);
                if (new java.io.File(theFile).exists()) {
                    addMonitoredFile(theFile, "Runtime Info", "Proc. Paperf. Log (" + theFile + ")");
                }
            }
        } catch (Exception e) {
            log.error("Error init monitor", e);
        }
    }
}
