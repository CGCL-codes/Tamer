package jcox.jplc.usb;

import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import javax.usb.UsbDevice;
import jcox.jplc.device.InsteonMessageReceptionListener;
import jcox.jplc.device.InsteonNetworkBridge;
import jcox.jplc.device.InsteonConnectionStatusListener;
import jcox.jplc.device.SendInsteonResponse;
import jcox.jplc.ibios.Acknowledgement;
import jcox.jplc.ibios.FlatMemoryMapAddress;
import jcox.jplc.ibios.IBIOSBroker;
import jcox.jplc.ibios.request.DownloadRequest;
import jcox.jplc.ibios.request.MaskRequest;
import jcox.jplc.ibios.response.DownloadResponse;
import jcox.jplc.ibios.response.GetVersionResponse;
import jcox.jplc.ibios.response.MaskResponse;
import jcox.jplc.message.InsteonMessage;
import jcox.jplc.message.InsteonMessageRequestAdapter;
import jcox.jplc.thread.QueueProcessorListener;
import jcox.jplc.thread.QueueProcessorManager;
import jcox.jplc.thread.InsteonConnectionStatusNotifier;
import jcox.jplc.usb.exception.PLCNotConnectedException;
import jcox.jplc.usb.exception.USBAssignmentException;
import jcox.jplc.usb.exception.USBConfigurationException;
import jcox.jplc.usb.system.USBConfigurationAdapter;
import jcox.jplc.usb.system.USBHubAdapter;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

/**
 * 
 * Implementation of a InsteonNetworkBridge that is
 * an INSTEON PowerLinc Controller V2 USB.
 * 
 * @author jcox
 *
 */
@Singleton
public class PLC implements InsteonNetworkBridge, QueueProcessorListener {

    static final Logger log = Logger.getLogger(PLC.class);

    private final IBIOSBroker ibiosBroker;

    private final USBHubAdapter usbHubAdapter;

    private final USBConfigurationAdapter usbConfigurationAdapter;

    private final QueueProcessorManager queueProcessorManager;

    private final ExecutorService executorService;

    private final InsteonMessageRequestAdapter insteonMessageRequestAdapter;

    private final Set<InsteonMessageReceptionListener> insteonMessageReceptionListeners;

    private final InsteonConnectionStatusNotifier insteonConnectionStatusNotifier;

    private final Semaphore ibiosPermit;

    volatile boolean connected = false;

    private UsbDevice device;

    @Inject
    public PLC(IBIOSBroker ibiosBroker, USBHubAdapter usbHubAdapter, USBConfigurationAdapter usbConfigurationAdapter, QueueProcessorManager queueProcessorManager, @Named("executorService") ExecutorService executorService, InsteonMessageRequestAdapter insteonMessageRequestAdapter, @Named("insteonMessageReceptionListeners") Set<InsteonMessageReceptionListener> insteonMessageReceptionListeners, InsteonConnectionStatusNotifier insteonConnectionStatusNotifier, @Named("ibiosPermit") Semaphore ibiosPermit) {
        super();
        this.ibiosBroker = ibiosBroker;
        this.usbHubAdapter = usbHubAdapter;
        this.usbConfigurationAdapter = usbConfigurationAdapter;
        this.queueProcessorManager = queueProcessorManager;
        this.executorService = executorService;
        this.insteonMessageRequestAdapter = insteonMessageRequestAdapter;
        this.insteonMessageReceptionListeners = insteonMessageReceptionListeners;
        this.insteonConnectionStatusNotifier = insteonConnectionStatusNotifier;
        this.ibiosPermit = ibiosPermit;
    }

    @Override
    public void connect() throws PLCNotConnectedException, USBConfigurationException, USBAssignmentException {
        synchronized (this) {
            if (connected) {
                log.info("The PLC was already connected.");
                return;
            }
            device = usbHubAdapter.getPLC();
            usbConfigurationAdapter.assignUSBConfiguration(device);
            queueProcessorManager.startProcessorThreads();
            connected = true;
            insteonConnectionStatusNotifier.notifyAllListenersOfConnect();
        }
        log.info("The PLC is now connected to the USB subsytem.");
    }

    @Override
    public void disconnect() {
        synchronized (this) {
            if (!connected) {
                log.info("The PLC was already disconnected.");
                return;
            }
            queueProcessorManager.stopProcessorThreads();
            usbConfigurationAdapter.unassignUSBConfiguration();
            connected = false;
            insteonConnectionStatusNotifier.notifyAllListenersOfDisonnect();
        }
        log.info("PLC is diconnected.");
    }

    @Override
    public boolean isBridgeAvailable() {
        if (connected) {
            return true;
        } else {
            try {
                return usbHubAdapter.getPLC() != null;
            } catch (Exception e) {
                return false;
            }
        }
    }

    @Override
    public boolean isConnected() {
        return connected;
    }

    @Override
    public Future<GetVersionResponse> ping() throws PLCNotConnectedException, InterruptedException {
        throwPLCNotConnectedExceptionIfNotConnected();
        ibiosPermit.acquire();
        Future<GetVersionResponse> response;
        try {
            response = ibiosBroker.getVersion();
        } finally {
            ibiosPermit.release();
        }
        return response;
    }

    @Override
    public Future<SendInsteonResponse> sendInsteon(final InsteonMessage message) throws PLCNotConnectedException {
        throwPLCNotConnectedExceptionIfNotConnected();
        Validate.notNull(message, "The insteon message can not be null.");
        log.debug("Submitting send insteon message to the executor service");
        return executorService.submit(new Callable<SendInsteonResponse>() {

            @Override
            public SendInsteonResponse call() throws Exception {
                log.debug("Downloading insteon message to plc");
                try {
                    ibiosPermit.acquire();
                    Future<DownloadResponse> downloadTask = ibiosBroker.download(new DownloadRequest(FlatMemoryMapAddress.INSTEON_MESSAGE_CONSTRUCION_BUFFER, insteonMessageRequestAdapter.convert(message)));
                    DownloadResponse downloadResponse = null;
                    try {
                        downloadResponse = downloadTask.get();
                    } catch (InterruptedException ie) {
                        log.warn("Received Interrupt exception while waiting for Download Response.  Cancelling.");
                        downloadTask.cancel(true);
                        throw ie;
                    }
                    if (downloadResponse.getAcknowledgement() == Acknowledgement.NAK) return new SendInsteonResponse(Acknowledgement.NAK);
                    Future<MaskResponse> maskTask = ibiosBroker.mask(new MaskRequest(FlatMemoryMapAddress.SEND_INSTEON_MESSAGE_CONTROL, (byte) 0x10, (byte) 0xFF));
                    MaskResponse maskResponse = null;
                    try {
                        maskResponse = maskTask.get();
                    } catch (InterruptedException ie) {
                        log.warn("Received Interrupt exception while waiting for Mask Response.  Cancelling.");
                        maskTask.cancel(true);
                        throw ie;
                    }
                    return new SendInsteonResponse(maskResponse.getAcknowledgement());
                } finally {
                    ibiosPermit.release();
                }
            }
        });
    }

    @Override
    public boolean registerInsteonMessageReceiver(InsteonMessageReceptionListener listener) {
        return insteonMessageReceptionListeners.add(listener);
    }

    @Override
    public boolean unregisterInsteonMessageReceiver(InsteonMessageReceptionListener listener) {
        return insteonMessageReceptionListeners.remove(listener);
    }

    private void throwPLCNotConnectedExceptionIfNotConnected() throws PLCNotConnectedException {
        if (!connected) throw new PLCNotConnectedException();
    }

    @Override
    public boolean registerInsteonConnectionStatusListener(InsteonConnectionStatusListener listener) {
        return insteonConnectionStatusNotifier.addInsteonConnectionStatusListener(listener);
    }

    @Override
    public boolean unregisterInsteonConnectionStatusListener(InsteonConnectionStatusListener listener) {
        return insteonConnectionStatusNotifier.removeInsteonConnectionStatusListener(listener);
    }

    @Override
    public void processingEnded() {
        synchronized (this) {
            if (!connected) {
                log.info("Queue processors have stopped.");
                return;
            }
            usbConfigurationAdapter.unassignUSBConfiguration();
            connected = false;
            insteonConnectionStatusNotifier.notifyAllListenersOfDisonnect();
        }
        log.info("PLC is diconnected.");
    }
}
