package com.jpark.jamse;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

/**
 * Bluetooth connection manager for device search
 * and connection handling. This manager only supports
 * SPP service connections, and a client triggered
 * request/response schema.
 * @author klumw
 */
public class ConnectionManager implements DiscoveryListener {

    public static final long BT_WAIT = 500;

    /** SerialPort Service class*/
    private static final UUID[] SPP_UUID = { new UUID("1101", true) };

    /** Servie Name attribute */
    private static final int[] ATTR_SET = { 0x0100 };

    /** Event handler for BT events*/
    private EventHandler observer;

    /** List of all device known*/
    private Vector allDevices;

    /** Table of discovered services.<br>
     *  key - friendly name of the device<br>
     *  value - device connection url
     */
    private Hashtable serviceTable;

    /** Last service record discovered*/
    private ServiceRecord lastServiceRecord;

    /** Phone device */
    private LocalDevice localDev;

    /** Count of found devices*/
    private int deviceCount;

    /** Notifier for stream connection*/
    private StreamConnectionNotifier connectionNotifier;

    /** Stream Connection*/
    private StreamConnection connection;

    /** DataOutputStream used for data transmission*/
    private DataOutputStream out;

    /** DatatInputStream used for server data receive*/
    private DataInputStream in;

    /** Flag indicating to use cached devcices only*/
    private boolean cachedOnly;

    /** Discovery agen used for device and service discovery*/
    private DiscoveryAgent agent;

    /** Services mapped to device */
    private Hashtable servicemap;

    private static Object lock = new Object();

    /**
     * Public constructor.
     * @param observer - callback handler for BT events
     */
    public ConnectionManager(EventHandler observer) {
        this.observer = observer;
        allDevices = new Vector();
        serviceTable = new Hashtable();
        servicemap = new Hashtable();
    }

    /**
     * Search for Devices.
     * @throws BluetoothStateException thrown in case the BT system cannot support
     * this operation in the present state
     */
    public void searchDevices() throws BluetoothStateException {
        allDevices.removeAllElements();
        serviceTable.clear();
        servicemap.clear();
        agent = LocalDevice.getLocalDevice().getDiscoveryAgent();
        agent.startInquiry(DiscoveryAgent.GIAC, this);
    }

    /**
     * Add discovered devices to the device list.
     * @param remoteDevice - new remote Device discovered
     * @param deviceClass - device class for this device e.g. Computer,Phone etc.
     */
    public void deviceDiscovered(RemoteDevice remoteDevice, DeviceClass deviceClass) {
        try {
            log("disc:" + remoteDevice.getFriendlyName(false));
        } catch (IOException ex) {
            log("disce:" + ex.getMessage());
        }
        allDevices.addElement(remoteDevice);
    }

    /**
     * Add discovered services to the service list.
     * @param transID - transaction id for this search
     * @param serviceRecords - service records found
     */
    public void servicesDiscovered(int transID, ServiceRecord[] serviceRecords) {
        log("svc_discovered");
        for (int i = 0; i < serviceRecords.length; i++) {
            try {
                String name = serviceRecords[i].getHostDevice().getFriendlyName(false);
                log("Dev:" + name + ",sr.size=" + serviceRecords.length);
                if (servicemap.get(name) == null) {
                    servicemap.put(name, new Vector());
                }
                Vector services = (Vector) servicemap.get(name);
                String url = serviceRecords[i].getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
                log("url=" + url);
                if (!services.contains(url)) {
                    services.addElement(url);
                    servicemap.put(name, services);
                }
            } catch (IOException ex) {
                log(ex.getMessage());
                observer.receiveEvent(null, new Event(this, Event.TYPE_BT, Event.SUB_BT_ERROR, ex.getMessage()));
            }
        }
    }

    /**
     * Service search finished notifier.
     * Calls service search for next device.
     * @param transID transaction id
     * @param respCode response code
     */
    public void serviceSearchCompleted(int transID, int respCode) {
        log("svs complete");
        observer.receiveEvent(null, new Event(this, Event.TYPE_BT, Event.SUB_BT_TRIGGER_SEARCH_NEXT));
    }

    /**
     * Device inquiry completed notifier.
     * Starts search for devices services that support SPP.
     * @param discType - result type for this inquiry
     */
    public void inquiryCompleted(int discType) {
        log("inq completed=" + discType);
        deviceCount = 0;
        observer.receiveEvent(null, new Event(this, Event.TYPE_BT, Event.SUB_BT_TRIGGER_SEARCH_NEXT));
    }

    /**
     * Search the next device in device list for
     * SPP service.
     *
     */
    public void searchNextDeviceForSPP() {
        if (allDevices.size() > 0 && deviceCount < allDevices.size()) {
            try {
                log("dvcount=" + deviceCount + ",adsize:" + allDevices.size());
                agent = LocalDevice.getLocalDevice().getDiscoveryAgent();
                agent.searchServices(ATTR_SET, SPP_UUID, (RemoteDevice) allDevices.elementAt(deviceCount++), this);
            } catch (BluetoothStateException ex) {
                log("BTE:" + ex.getMessage());
                observer.receiveEvent(null, new Event(this, Event.TYPE_BT, Event.SUB_BT_ERROR, ex.getMessage()));
            }
        } else {
            log("searchend");
            Enumeration en = servicemap.keys();
            while (en.hasMoreElements()) {
                String key = (String) en.nextElement();
                log("key:" + key);
                Vector urls = (Vector) servicemap.get(key);
                if (urls.size() > 1) {
                    for (int i = 0; i < urls.size(); i++) {
                        serviceTable.put(key + "[" + (i + 1) + "]", urls.elementAt(i));
                        log("end st.put");
                    }
                } else {
                    serviceTable.put(key, urls.elementAt(0));
                    log("svt.size=" + serviceTable.size());
                }
            }
            servicemap.clear();
            observer.receiveEvent(null, new Event(this, Event.TYPE_BT, Event.SUB_BT_SEARCH_END));
        }
    }

    /**
     * Return the service table<br>
     * Key - friendly device name<br>
     * Value - device connection url<br>
     * @return service table
     */
    public Hashtable getServiceRecords() {
        return serviceTable;
    }

    /**
     * Connect to the given device
     * @param deviceName - friendly name of the device
     * @throws IOException - in case of a connection problem
     */
    public void connect(String deviceName) throws IOException {
        String url = (String) serviceTable.get(deviceName);
        log("con.url:" + url);
        connection = (StreamConnection) Connector.open(url);
        log("physical con ok");
        in = connection.openDataInputStream();
        out = connection.openDataOutputStream();
    }

    /**
     * Send the given command to the server and
     * return a DataInputStream to read the response.
     * @param tx - command bytes to send
     * @return DataInputStream to read from
     * @throws IOException - in case of communication error
     */
    public DataInputStream send(byte[] tx) throws IOException {
        return send(tx, null);
    }

    /**
     * Send the given command and argument to the server
     * @param tx - command bytes to send
     * @param argument - optional argument for this command, might be null
     * @return DataInputStream to read the server response
     * @throws IOException - thrown in case of communication error
     */
    public synchronized DataInputStream send(byte[] tx, byte[] argument) throws IOException {
        out.write(tx, 0, tx.length);
        if (argument != null) {
            out.write(argument, 0, argument.length);
        }
        out.flush();
        try {
            Thread.sleep(BT_WAIT);
        } catch (InterruptedException ex) {
        }
        return in;
    }

    /**
     * Reconnect to the last connected device
     * @throws IOException - in case of communication error
     */
    public void reconnect() throws IOException {
        String name = lastServiceRecord.getHostDevice().getFriendlyName(false);
        if (name != null) {
            connect(name);
        }
    }

    /**
     * Cancel device inquiry.
     */
    public void cancelInquiry() {
        if (agent != null) {
            agent.cancelInquiry(this);
        }
    }

    /**
     * Disconnect from server.
     * This method tries to disconnect from the server.
     * No exceptions are thrown in case of a failed disconnect.
     * The application will always be in disconnected state afterwards.
     */
    public void disconnect() {
        ApplicationHelper.closeSavely(out);
        try {
            if (in != null) {
                in.close();
            }
        } catch (IOException ioe1) {
        }
        ApplicationHelper.closeSavely(connection);
    }

    /**
     * Debug log method.
     * @param msg msg to log
     */
    private void log(String msg) {
        System.out.println(msg);
        ApplicationHelper.addErrorMsg(msg);
    }
}
