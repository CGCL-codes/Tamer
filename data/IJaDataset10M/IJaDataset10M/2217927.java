package org.octaedr.upnp.cp;

import java.net.URL;
import java.util.UUID;
import org.octaedr.logging.Log;
import org.octaedr.upnp.core.UPnPException;
import org.w3c.dom.Document;

abstract class DescriptionRetrievalThread extends Thread {

    protected final UUID udn;

    protected final URL location;

    protected final int maxAge;

    private boolean cancelled;

    public DescriptionRetrievalThread(final UUID udn, final URL location, final int maxAge) {
        this.udn = udn;
        this.location = location;
        this.maxAge = maxAge;
        this.cancelled = false;
    }

    public void cancelDownload() {
        this.cancelled = true;
    }

    public boolean isCanceled() {
        return this.cancelled;
    }

    public void run() {
        DeviceInfo deviceInfo = prepareDeviceInfo();
        processFinished(deviceInfo);
    }

    public abstract void processFinished(DeviceInfo deviceInfo);

    private boolean prepareDeviceServices(final DeviceInfo deviceInfo, final URL rootLocation, Document deviceDocument) {
        for (int i = 0; i < deviceInfo.getServiceCount(); ++i) {
            ServiceInfo service = deviceInfo.getService(i);
            if (!prepareServiceInfo(service)) {
                return false;
            }
        }
        for (int i = 0; i < deviceInfo.getSubdeviceCount(); ++i) {
            DeviceInfo subdevice = deviceInfo.getSubdevice(i);
            if (prepareDeviceServices(subdevice, rootLocation, deviceDocument)) {
                return false;
            }
        }
        return true;
    }

    private DeviceInfo prepareDeviceInfo() {
        String deviceDocumentText = DocumentTools.downloadDocument(this.location);
        if (deviceDocumentText == null) {
            if (Log.debug) Log.debug("DeviceManager", "Cannot download device description document. Location: " + this.location);
            return null;
        }
        Document deviceDocument = DocumentTools.parseDocument(deviceDocumentText);
        if (deviceDocument == null) {
            if (Log.debug) Log.debug("DeviceManager", "Invalid XML with device description. Location: " + this.location);
            return null;
        }
        DeviceInfo deviceInfo = null;
        try {
            deviceInfo = DeviceDescriptionParser.parseXML(this.location, this.udn, deviceDocument);
        } catch (UPnPException e) {
            Log.debug("DeviceManager", "Invalid device description document: " + this.location + "; " + e.getMessage());
            return null;
        }
        if (!prepareDeviceServices(deviceInfo, this.location, deviceDocument)) {
            return null;
        }
        deviceInfo.init();
        return deviceInfo;
    }

    private boolean prepareServiceInfo(final ServiceInfo serviceInfo) {
        URL scpdURL = serviceInfo.getSCPDURL();
        String serviceDocumentText = DocumentTools.downloadDocument(scpdURL);
        if (serviceDocumentText == null) {
            if (Log.debug) Log.debug("DeviceManager", "Cannot download service description document. Location: " + scpdURL.toExternalForm());
            return false;
        }
        Document serviceDocument = DocumentTools.parseDocument(serviceDocumentText);
        if (serviceDocument == null) {
            if (Log.debug) Log.debug("DeviceManager", "Invalid XML with service description. Location: " + scpdURL.toExternalForm());
            return false;
        }
        try {
            return ServiceDescriptionParser.parseXML(serviceInfo, serviceDocument);
        } catch (UPnPException e) {
            if (Log.debug) {
                Log.debug("DeviceManager", "Invalid service description document: " + scpdURL.toExternalForm());
                Log.debug("DeviceManager", "Invalid service description document: " + e.getMessage());
            }
            return false;
        }
    }
}
