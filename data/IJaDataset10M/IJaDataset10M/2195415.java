package Network;

import java.util.Vector;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;

/**
 * Searches for matching services.
 * 
 * @author Steffen, Martin
 */
public class NetworkDiscoverer implements DiscoveryListener {

    /**
	 * Our own BluetoothDevice
	 */
    private LocalDevice localDev;

    /**
	 * NetworkDispatcher instance to be able to message
	 */
    private NetworkDispatcher networkDispatcher;

    /**
	 * Vector to save the found devices
	 */
    private Vector devices = new Vector();

    /**
	 * Vector to save the found services
	 */
    private Vector services = new Vector();

    /**
	 * Object to synchronize the asynchronous search
	 * (synchonous search is too slow)
	 */
    private final Object lock = new Object();

    /**
	 * our protocol level, that we have used to communicate
	 */
    private int usedLevel;

    /**
	 * DiscoveryAgent instance, used to search devices and services
	 */
    private DiscoveryAgent disAgent;

    /**
	 * Initializes the networkDispatcher instance and trys to get the Bluetooths device.
	 *
	 * @param nd NetworkDispatcher instance
	 * @throws BluetoothStateException if bluetooth device not turned on or not there at all
	 */
    public NetworkDiscoverer(NetworkDispatcher nd) throws BluetoothStateException {
        localDev = LocalDevice.getLocalDevice();
        networkDispatcher = nd;
    }

    /**
	 * Returns the Bluetooth-MAC-adress of the Bluetooth device
	 * @return the Bluetooth-MAC-adress of the Bluetooth device
	 */
    public String getMAC() {
        return localDev.getBluetoothAddress();
    }

    /**
	 * Searches for matching services, that we can use to communicate.
	 * Stop when the first matching service is found.
	 * <span style="font-size:small">(previously this method was searching services
	 * of all devices per level before stopping)</span>
	 *
	 * @param uuids array of UUIDs (1 UUID = protocol level)
	 */
    public void findServices(UUID[] uuids) {
        devices.removeAllElements();
        services.removeAllElements();
        try {
            disAgent = localDev.getDiscoveryAgent();
            message("Searching Devices...");
            System.out.println("Searching Devices...");
            disAgent.startInquiry(DiscoveryAgent.GIAC, this);
            synchronized (lock) {
                lock.wait();
            }
            message("Searching Services...");
            System.out.println("Searching Services...");
            int j;
            for (j = 0; j < uuids.length && services.isEmpty(); j++) {
                UUID[] currentUuid = { uuids[j] };
                for (int i = 0; i < devices.size() && services.isEmpty(); i++) {
                    disAgent.searchServices(null, currentUuid, (RemoteDevice) devices.elementAt(i), this);
                    synchronized (lock) {
                        lock.wait();
                    }
                }
                message("UUID-search @ Level " + j + " complete");
                message2("Stufe " + j + " durchsucht.");
            }
            --j;
            if (!services.isEmpty()) {
                usedLevel = j;
            }
        } catch (Exception e) {
        }
    }

    /**
	 * Trys to stop searching for devices
	 */
    public void stop() {
        disAgent.cancelInquiry(this);
    }

    /**
	 * Returns the found level of our protocol
	 * @return the level
	 */
    public int getUsedLevel() {
        return usedLevel;
    }

    /**
	 * Returns the NetworkServiceEntry that was found.
	 * <span style="font-size:small">(previously used to find the best of all found services,
	 * now there is maximal one service in the vector. see findServices)</span>
	 * @return the NetworkServiceEntry that was found.
	 */
    public NetworkServiceEntry bestService() {
        if (services.isEmpty()) {
            return null;
        }
        return (NetworkServiceEntry) services.elementAt(0);
    }

    /**
	 * Called when a device is found during an inquiry. An inquiry searches for devices that are
	 * discoverable. The same device may be returned multiple times.<br />
	 * Discovered device is saved in the devices Vector.
	 *
	 * @param btDevice the device that was found during the inquiry
	 * @param cod the service classes, major device class, and minor device class of the remote
	 *		device
	 */
    public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
        if (!devices.contains(btDevice)) {
            devices.addElement(btDevice);
        }
        message("Device found: " + btDevice.getBluetoothAddress());
        System.out.println("Device found: " + btDevice.getBluetoothAddress());
    }

    /**
	 * Called when an inquiry is completed. The discType will be INQUIRY_COMPLETED if the inquiry
	 * ended normally or INQUIRY_TERMINATED if the inquiry was canceled by a call to
	 * DiscoveryAgent.cancelInquiry(). The discType  will be INQUIRY_ERROR if an error occurred
	 * while processing the inquiry causing the inquiry to end abnormally.<br />
	 * <br />
	 * Notifies the lock object to stop blocking.
	 *
	 * @param discType the type of request that was completed; either INQUIRY_COMPLETED,
	 *		INQUIRY_TERMINATED, or INQUIRY_ERROR
	 */
    public void inquiryCompleted(int discType) {
        switch(discType) {
            case INQUIRY_COMPLETED:
                {
                    message("Inquiry completed");
                    System.out.println("Inquiry completed");
                    break;
                }
            case INQUIRY_ERROR:
                {
                    message("Inquiry error, Bluetooth turned on?");
                    System.out.println("Inquiry error");
                    break;
                }
            case INQUIRY_TERMINATED:
                {
                    message("Inquiry terminated");
                    System.out.println("Inquiry terminated");
                    break;
                }
            default:
                {
                    message("Inquiry, Komsiche Sache passiert");
                    System.out.println("Inquiry, Komsiche Sache passiert");
                    break;
                }
        }
        message2(devices.size() + " Geräte in der Umgebung gefunden.");
        synchronized (lock) {
            lock.notify();
        }
    }

    /**
	 * Called when service(s) are found during a service search.
	 * <br />
	 * servRecord is saved in the services Vector.
	 *
	 * @param transID the transaction ID of the service search that is posting the result
	 * @param servRecord Array of ServiceRecord, describing the discovered services
	 */
    public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
        for (int i = 0; i < servRecord.length; i++) {
            NetworkServiceEntry serviceEntry = new NetworkServiceEntry(servRecord[i]);
            if (!services.contains(serviceEntry)) {
                services.addElement(serviceEntry);
            }
            message("matching Service found");
            message2("Möglichen Verbindungspartner gefunden.");
        }
    }

    /**
	 * Called when a service search is completed or was terminated because of an error. Legal status
	 * values in the respCode argument include SERVICE_SEARCH_COMPLETED, SERVICE_SEARCH_TERMINATED,
	 * SERVICE_SEARCH_ERROR, SERVICE_SEARCH_NO_RECORDS  and SERVICE_SEARCH_DEVICE_NOT_REACHABLE.
	 * <br /><br />
	 * Notifies the lock object to stop blocking.
	 *
	 * @param transID the transaction ID identifying the request which initiated the service search
	 * @param respCode the response code that indicates the status of the transaction
	 */
    public void serviceSearchCompleted(int transID, int respCode) {
        switch(respCode) {
            case SERVICE_SEARCH_COMPLETED:
                {
                    message("Service search completed");
                    System.out.println("Service search completed");
                    break;
                }
            case SERVICE_SEARCH_TERMINATED:
                {
                    message("Service search terminated");
                    System.out.println("Service search terminated");
                    break;
                }
            case SERVICE_SEARCH_ERROR:
                {
                    message("Service search error");
                    System.out.println("Service search error");
                    break;
                }
            case SERVICE_SEARCH_NO_RECORDS:
                {
                    message("Service search no records");
                    System.out.println("Service search no records");
                    break;
                }
            case SERVICE_SEARCH_DEVICE_NOT_REACHABLE:
                {
                    message("Service search not reachable");
                    System.out.println("Service search not reachable");
                    break;
                }
            default:
                {
                    message("Service search, komische sachen oO");
                    System.out.println("Service search, komische sachen oO");
                    break;
                }
        }
        synchronized (lock) {
            lock.notify();
        }
    }

    /**
	 * Triggers the message method of NetworkDispatcher.
	 * @param msg message to show
	 */
    public void message(String msg) {
        networkDispatcher.message(msg);
    }

    /**
	 * Triggers the message2 method of NetworkDispatcher
	 * @param msg message to show
	 */
    public void message2(String msg) {
        networkDispatcher.message2(msg);
    }
}
