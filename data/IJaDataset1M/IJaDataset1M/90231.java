package eu.more.measurementservicegui.model.log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import javax.xml.namespace.QName;
import org.apache.log4j.Logger;
import org.soda.dpws.DPWSException;
import org.soda.dpws.DeviceExplorer;
import org.soda.dpws.DeviceProxy;
import org.soda.dpws.ServiceProxy;
import org.soda.dpws.cache.CachedDevice;
import org.soda.dpws.metadata.DeviceInfo;
import org.soda.dpws.metadata.DeviceMetadata;
import eu.more.JXTAErrorTransmissionService.generated.JXTAErrorTransmissionServiceInvoker;
import eu.more.JXTAErrorTransmissionService.generated.jaxb.GetLatestMessagesResponse;
import eu.more.JXTAErrorTransmissionService.generated.jaxb.GetLatestNMessages;
import eu.more.JXTAErrorTransmissionService.generated.jaxb.impl.GetLatestNMessagesImpl;
import eu.more.JXTAErrorTransmissionService.generated.jaxb.impl.MessageImpl;
import eu.more.measurementservicegui.model.ServiceConstants;

/**
 * @author pdaum
 *
 */
public class LogMessageModel extends Observable {

    private HashMap<String, JXTAErrorTransmissionServiceInvoker> logMessageInvoker = new HashMap<String, JXTAErrorTransmissionServiceInvoker>();

    private Logger logger = Logger.getLogger(LogMessageModel.class);

    private List<MessageImpl> messages = new ArrayList<MessageImpl>();

    private JXTAErrorTransmissionServiceInvoker invoker;

    private class DiscoverRunner implements Runnable {

        public void run() {
            logMessageInvoker.clear();
            ArrayList<String> scopes = new ArrayList<String>();
            try {
                logger.info("Try to explore devices");
                DeviceExplorer explorer = new DeviceExplorer();
                List<CachedDevice> lookup = explorer.lookup(new ArrayList<QName>(), scopes);
                if (lookup == null) {
                    logger.info("No devices found!");
                    notifyObservers(ServiceConstants.SERVICE_DISCOVERY_FAILED);
                } else {
                    logger.info("Devices found! Iterating!");
                    for (Iterator<CachedDevice> devIter = lookup.iterator(); devIter.hasNext(); ) {
                        DeviceProxy deviceProxy = devIter.next();
                        DeviceMetadata deviceMetadata = deviceProxy.getDeviceMetadata();
                        DeviceInfo deviceInfo = deviceMetadata.getDeviceInfo();
                        String serialNumber = deviceInfo.getSerialNumber();
                        List<Serializable> types = new ArrayList<Serializable>();
                        List<ServiceProxy> hostedServices = deviceProxy.getHostedServices(types);
                        logger.info("Iterating over services!");
                        for (ServiceProxy serviceProxy : hostedServices) {
                            String id = serviceProxy.getId();
                            logger.info("Service [" + id + "] found!");
                            if (id.equals("http://www.ist-more.org/JXTAErrorTransmissionService")) {
                                String identifier = serialNumber;
                                logger.info("JXTAErrorTransmissionService [" + identifier + "] found!");
                                logMessageInvoker.put(identifier, new JXTAErrorTransmissionServiceInvoker(serviceProxy));
                            }
                        }
                        if (logMessageInvoker.isEmpty()) {
                            logger.info("No JXTAErrorTransmissionService services discovered!");
                            setChanged();
                            notifyObservers(ServiceConstants.SERVICE_DISCOVERY_FAILED);
                        } else setChanged();
                        notifyObservers(ServiceConstants.SERVICE_DISCOVERY_SUCCESS);
                    }
                }
            } catch (DPWSException e) {
                logger.error(e);
            }
        }
    }

    public void discoverServices() {
        DiscoverRunner r = new DiscoverRunner();
        new Thread(r).start();
        setChanged();
        notifyObservers(ServiceConstants.SERVICE_DISCOVERY_STARTED);
    }

    @SuppressWarnings("unchecked")
    public void getLatestMessages(int count, String invokerID) {
        if (!logMessageInvoker.containsKey(invokerID)) {
            notifyObservers(ServiceConstants.REQUEST_FAILED_NO_INVOKER);
            return;
        }
        invoker = logMessageInvoker.get(invokerID);
        try {
            GetLatestNMessages parameters = new GetLatestNMessagesImpl();
            parameters.setIn(count);
            GetLatestMessagesResponse response = invoker.invokeGetLatestNMessages(parameters);
            if (response != null) {
                setMessages(response.getMessages());
            } else {
                logger.info("No log messages retrieved!");
            }
            setChanged();
            notifyObservers(ServiceConstants.REQUEST_SUCCESS);
        } catch (DPWSException exc) {
            logger.error("While accessing the selected JXTA error transmission service this error is occured!", exc);
            setChanged();
            notifyObservers(ServiceConstants.REQUEST_FAILED);
        }
    }

    public HashMap<String, JXTAErrorTransmissionServiceInvoker> getServices() {
        return logMessageInvoker;
    }

    public void initialize() {
    }

    /**
   * @param messages
   *          the messages to set
   */
    private void setMessages(List<MessageImpl> messages) {
        this.messages = messages;
    }

    /**
   * @return the messages
   */
    public List<MessageImpl> getMessages() {
        return messages;
    }

    /**
   *
   * @return <tt>false</tt> if currently no service is discovered, else <tt>true</tt>
   */
    public boolean getCurrentState() {
        return logMessageInvoker.isEmpty();
    }
}
