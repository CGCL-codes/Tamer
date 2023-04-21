package app_files.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DataElement;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.lcdui.Image;
import javax.microedition.media.Manager;
import javax.microedition.media.Player;

/**
 * Initialize BT device, search for BT services,
 * presents them to user and picks his/her choice,
 * finally download the choosen image and present
 * it to user.
 *
 * @version ,
 */
final class BTMusicClient implements Runnable, DiscoveryListener {

    /** Describes this server */
    private static final UUID MUSIC_SERVER_UUID = new UUID("F0E0D0C0B0A000908070605040302013", false);

    /** The attribute id of the record item with images names. */
    private static final int MUSIC_NAMES_ATTRIBUTE_ID = 0x4323;

    /** Shows the engine is ready to work. */
    private static final int READY = 0;

    /** Shows the engine is searching bluetooth devices. */
    private static final int DEVICE_SEARCH = 1;

    /** Shows the engine is searching bluetooth services. */
    private static final int SERVICE_SEARCH = 2;

    /** Keeps the current state of engine. */
    private int state = READY;

    /** Keeps the discovery agent reference. */
    private DiscoveryAgent discoveryAgent;

    /** Keeps the parent reference to process specific actions. */
    private GUIMusicClient parent;

    /** Becomes 'true' when this component is finalized. */
    private boolean isClosed;

    /** Process the search/download requests. */
    private Thread processorThread;

    /** Collects the remote devices found during a search. */
    private Vector devices = new Vector();

    /** Collects the services found during a search. */
    private Vector records = new Vector();

    /** Keeps the device discovery return code. */
    private int discType;

    /** Keeps the services search IDs (just to be able to cancel them). */
    private int[] searchIDs;

    /** Keeps the image name to be load. */
    private String imageNameToLoad;

    /** Keeps the table of {name, Service} to process the user choice. */
    private Hashtable base = new Hashtable();

    /** Informs the thread the download should be canceled. */
    private boolean isDownloadCanceled;

    /** Optimization: keeps service search pattern. */
    private UUID[] uuidSet;

    /** Optimization: keeps attributes list to be retrieved. */
    private int[] attrSet;

    /**
     * Constructs the bluetooth server, but it is initialized
     * in the different thread to "avoid dead lock".
     */
    BTMusicClient(GUIMusicClient parent) {
        this.parent = parent;
        processorThread = new Thread(this);
        processorThread.start();
    }

    /**
     * Process the search/download requests.
     */
    public void run() {
        boolean isBTReady = false;
        try {
            LocalDevice localDevice = LocalDevice.getLocalDevice();
            discoveryAgent = localDevice.getDiscoveryAgent();
            isBTReady = true;
        } catch (Exception e) {
            System.err.println("Can't initialize bluetooth: " + e);
        }
        parent.completeInitialization(isBTReady);
        if (!isBTReady) {
            return;
        }
        uuidSet = new UUID[2];
        uuidSet[0] = new UUID(0x1101);
        uuidSet[1] = MUSIC_SERVER_UUID;
        attrSet = new int[1];
        attrSet[0] = MUSIC_NAMES_ATTRIBUTE_ID;
        processImagesSearchDownload();
    }

    /**
     * Invoked by system when a new remote device is found -
     * remember the found device.
     */
    public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
        if (devices.indexOf(btDevice) == -1) {
            devices.addElement(btDevice);
        }
    }

    /**
     * Invoked by system when device discovery is done.
     * <p>
     * Remember the discType
     * and process its evaluation in another thread.
     */
    public void inquiryCompleted(int discType) {
        this.discType = discType;
        synchronized (this) {
            notify();
        }
    }

    public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
        for (int i = 0; i < servRecord.length; i++) {
            records.addElement(servRecord[i]);
        }
    }

    public void serviceSearchCompleted(int transID, int respCode) {
        int index = -1;
        for (int i = 0; i < searchIDs.length; i++) {
            if (searchIDs[i] == transID) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            System.err.println("Unexpected transaction index: " + transID);
        } else {
            searchIDs[index] = -1;
        }
        for (int i = 0; i < searchIDs.length; i++) {
            if (searchIDs[i] != -1) {
                return;
            }
        }
        synchronized (this) {
            notify();
        }
    }

    /** Sets the request to search the devices/services. */
    void requestSearch() {
        synchronized (this) {
            notify();
        }
    }

    /** Cancel's the devices/services search. */
    void cancelSearch() {
        synchronized (this) {
            if (state == DEVICE_SEARCH) {
                discoveryAgent.cancelInquiry(this);
            } else if (state == SERVICE_SEARCH) {
                for (int i = 0; i < searchIDs.length; i++) {
                    discoveryAgent.cancelServiceSearch(searchIDs[i]);
                }
            }
        }
    }

    /** Sets the request to load the specified image. */
    void requestLoad(String name) {
        synchronized (this) {
            imageNameToLoad = name;
            notify();
        }
    }

    /** Cancel's the image download. */
    void cancelLoad() {
        isDownloadCanceled = true;
    }

    /**
     * Destroy a work with bluetooth - exits the accepting
     * thread and close notifier.
     */
    void destroy() {
        synchronized (this) {
            isClosed = true;
            isDownloadCanceled = true;
            notify();
        }
        try {
            processorThread.join();
        } catch (InterruptedException e) {
        }
    }

    /**
     * Processes images search/download until component is closed
     * or system error has happen.
     */
    private synchronized void processImagesSearchDownload() {
        while (!isClosed) {
            state = READY;
            try {
                wait();
            } catch (InterruptedException e) {
                System.err.println("Unexpected interruption: " + e);
                return;
            }
            if (isClosed) {
                return;
            }
            if (!searchDevices()) {
                return;
            } else if (devices.size() == 0) {
                continue;
            }
            if (!searchServices()) {
                return;
            } else if (records.size() == 0) {
                continue;
            }
            if (!presentUserSearchResults()) {
                continue;
            }
            while (true) {
                isDownloadCanceled = false;
                try {
                    wait();
                } catch (InterruptedException e) {
                    System.err.println("Unexpected interruption: " + e);
                    return;
                }
                if (isClosed) {
                    return;
                }
                if (imageNameToLoad == null) {
                    break;
                }
                Image img = loadImage();
                if (isClosed) {
                    return;
                }
                if (isDownloadCanceled) {
                    continue;
                }
                continue;
            }
        }
    }

    /**
     * Search for bluetooth devices.
     *
     * @return false if should end the component work.
     */
    private boolean searchDevices() {
        state = DEVICE_SEARCH;
        devices.removeAllElements();
        try {
            discoveryAgent.startInquiry(DiscoveryAgent.GIAC, this);
        } catch (BluetoothStateException e) {
            System.err.println("Can't start inquiry now: " + e);
            parent.informSearchError("Can't start device search");
            return true;
        }
        try {
            wait();
        } catch (InterruptedException e) {
            System.err.println("Unexpected interruption: " + e);
            return false;
        }
        if (isClosed) {
            return false;
        }
        switch(discType) {
            case INQUIRY_ERROR:
                parent.informSearchError("Device discovering error...");
            case INQUIRY_TERMINATED:
                devices.removeAllElements();
                break;
            case INQUIRY_COMPLETED:
                if (devices.size() == 0) {
                    parent.informSearchError("No devices in range");
                }
                break;
            default:
                System.err.println("system error:" + " unexpected device discovery code: " + discType);
                destroy();
                return false;
        }
        return true;
    }

    /**
     * Search for proper service.
     *
     * @return false if should end the component work.
     */
    private boolean searchServices() {
        state = SERVICE_SEARCH;
        records.removeAllElements();
        searchIDs = new int[devices.size()];
        boolean isSearchStarted = false;
        for (int i = 0; i < devices.size(); i++) {
            RemoteDevice rd = (RemoteDevice) devices.elementAt(i);
            try {
                searchIDs[i] = discoveryAgent.searchServices(attrSet, uuidSet, rd, this);
            } catch (BluetoothStateException e) {
                System.err.println("Can't search services for: " + rd.getBluetoothAddress() + " due to " + e);
                searchIDs[i] = -1;
                continue;
            }
            isSearchStarted = true;
        }
        if (!isSearchStarted) {
            parent.informSearchError("Can't search services.");
            return true;
        }
        try {
            wait();
        } catch (InterruptedException e) {
            System.err.println("Unexpected interruption: " + e);
            return false;
        }
        if (isClosed) {
            return false;
        }
        if (records.size() == 0) {
            parent.informSearchError("No proper services were found");
        }
        return true;
    }

    /**
     * Gets the collection of the images titles (names)
     * from the services, prepares a hashtable to match
     * the image name to a services list, presents the images names
     * to user finally.
     *
     * @return false if no names in found services.
     */
    private boolean presentUserSearchResults() {
        base.clear();
        for (int i = 0; i < records.size(); i++) {
            ServiceRecord sr = (ServiceRecord) records.elementAt(i);
            DataElement de = sr.getAttributeValue(MUSIC_NAMES_ATTRIBUTE_ID);
            if (de == null) {
                System.err.println("Unexpected service - missed attribute");
                continue;
            }
            Enumeration deEnum = (Enumeration) de.getValue();
            while (deEnum.hasMoreElements()) {
                de = (DataElement) deEnum.nextElement();
                String name = (String) de.getValue();
                Object obj = base.get(name);
                if (obj != null) {
                    Vector v;
                    if (obj instanceof ServiceRecord) {
                        v = new Vector();
                        v.addElement(obj);
                    } else {
                        v = (Vector) obj;
                    }
                    v.addElement(sr);
                    obj = v;
                } else {
                    obj = sr;
                }
                base.put(name, obj);
            }
        }
        return parent.showImagesNames(base);
    }

    /**
     * Loads selected image data.
     */
    private Image loadImage() {
        if (imageNameToLoad == null) {
            System.err.println("Error: imageNameToLoad=null");
            return null;
        }
        ServiceRecord[] sr = null;
        Object obj = base.get(imageNameToLoad);
        if (obj == null) {
            System.err.println("Error: no record for: " + imageNameToLoad);
            return null;
        } else if (obj instanceof ServiceRecord) {
            sr = new ServiceRecord[] { (ServiceRecord) obj };
        } else {
            Vector v = (Vector) obj;
            sr = new ServiceRecord[v.size()];
            for (int i = 0; i < v.size(); i++) {
                sr[i] = (ServiceRecord) v.elementAt(i);
            }
        }
        for (int i = 0; i < sr.length; i++) {
            StreamConnection conn = null;
            String url = null;
            if (isDownloadCanceled) {
                return null;
            }
            try {
                url = sr[i].getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
                conn = (StreamConnection) Connector.open(url);
            } catch (IOException e) {
                System.err.println("Note: can't connect to: " + url);
                continue;
            }
            try {
                OutputStream out = conn.openOutputStream();
                out.write(imageNameToLoad.length());
                out.write(imageNameToLoad.getBytes());
                out.flush();
                out.close();
            } catch (IOException e) {
                System.err.println("Can't write to server for: " + url);
                try {
                    conn.close();
                } catch (IOException ee) {
                }
                continue;
            }
            byte[] imgData = null;
            try {
                InputStream in = conn.openInputStream();
                Player player;
                player = Manager.createPlayer(in, "audio/wma");
                player.realize();
                player.prefetch();
                player.start();
                in.close();
            } catch (IOException e) {
                System.err.println("Can't read from server for: " + url);
                continue;
            } catch (Exception e) {
            } finally {
                try {
                    conn.close();
                } catch (IOException e) {
                }
            }
            Image img = null;
            try {
                img = Image.createImage(imgData, 0, imgData.length);
            } catch (Exception e) {
                System.err.println("Error: wrong image data from: " + url);
                continue;
            }
            return img;
        }
        return null;
    }
}
