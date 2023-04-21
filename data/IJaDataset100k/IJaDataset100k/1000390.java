package jpmp.manager;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import jpmp.common.Constant;
import jpmp.common.OsHelper;
import jpmp.device.UsbDevice;

/**
 * Device manager implementation
 * 
 * @author cmi
 * <p></p>
 *
 */
public class DeviceManager {

    /**
	 * singleton instance
	 */
    private static DeviceManager _instance = null;

    /**
	 * holder for the detected devices
	 */
    private Map deviceList = new HashMap();

    public static synchronized DeviceManager getInstance() throws Throwable {
        if (_instance == null) {
            _instance = new DeviceManager();
        }
        return _instance;
    }

    /**
	 * Constructor 
	 * @throws Throwable
	 */
    private DeviceManager() throws Throwable {
        loadPmpLib();
    }

    /**
	 * Getter for the list of detected USB devices
	 * @return the list of detected USB devices
	 */
    public Map getDeviceList() {
        return deviceList;
    }

    /**
	 * Set the list of detected USB devices
	 * @param deviceList new Map of devices
	 */
    public void setDeviceList(Map deviceList) {
        this.deviceList = deviceList;
    }

    /**
	 * Load native library to access USB devices 
	 * <p>
	 * Try to load the  native library from the java.library.path
	 * or from the current directory
	 * </p>
	 * @throws Throwable
	 */
    public void loadPmpLib() throws Throwable {
        boolean libloaded = false;
        String shortLibName = "";
        String fullLibName = "";
        if (OsHelper.isLinux()) {
            shortLibName = Constant.shortLinuxLibname;
            fullLibName = Constant.fullLinuxLibname;
        }
        if (OsHelper.isWindows()) {
            shortLibName = Constant.shortWindowsLibname;
            fullLibName = Constant.fullWindowsLibname;
        }
        if (shortLibName.trim().length() == 0) {
            throw new Exception("Unknown OS");
        }
        try {
            System.loadLibrary(shortLibName);
            libloaded = true;
        } catch (Throwable t) {
        }
        if (!libloaded) {
            File f = new File(".");
            String currentDir = f.getCanonicalPath();
            System.load(currentDir + File.separator + fullLibName);
        }
    }

    /**
	 * Dump the device list, for debugging
	 */
    public void dump() {
        synchronized (this) {
            System.out.println("===========================================");
            if (deviceList.size() > 0) {
                for (Iterator it = deviceList.keySet().iterator(); it.hasNext(); ) {
                    String key = (String) it.next();
                    UsbDevice usbdev = (UsbDevice) deviceList.get(key);
                    System.out.println(usbdev.dump());
                }
            } else {
                System.out.println("No device detected.");
            }
            System.out.println("===========================================");
        }
    }

    /**
	 * Add a USB device to the list
	 * <p>
	 * This method is called from the native code. 
	 * </p>
	 * @param usbdev device to add to the list
	 */
    public void addDevice(UsbDevice usbdev) {
        synchronized (this) {
            if (usbdev != null) {
                System.out.println(usbdev.dump());
                if (!deviceList.containsKey(usbdev.getCanonical())) {
                    deviceList.put(usbdev.getCanonical(), usbdev);
                }
            }
        }
    }

    /**
	 * Remove USB device from the list
	 * <p>
	 * This method is called from the native code. 
	 * </p>
	 * @param deviceid device to remove from the list
	 */
    public void removeDevice(String deviceid) {
        synchronized (this) {
            if (deviceList.containsKey(deviceid)) {
                deviceList.remove(deviceid);
            }
        }
    }

    /**
	 * Detect USB devices
	 * @return 0 if success
	 */
    public long scanDevices() {
        synchronized (this) {
            long result = 0;
            deviceList.clear();
            result = detectDevices();
            return result;
        }
    }

    /**
	 * Initialize native device manager
	 * @return 0 if success
	 */
    public native long createInstance();

    /**
	 * Release native device manager
	 * <p>This method should be called to free all allocations on native side</p>
	 * @return 0 if success
	 */
    public native long releaseInstance();

    /**
	 * Set the verbose level on native side
	 * <p> 0 - no trace</p>
	 * <p> >1 - output trace messages</p>
	 * 
	 * @param verbose verbose level
	 * @return 0 if success
	 */
    public native long setVerbose(int verbose);

    /**
	 * Detect USB devices, native.
	 * 
	 * @return 0 if success
	 */
    private native long detectDevices();

    /**
	 * Check if device identified by deviceidentifier is present, native.
	 * @param canonical device identification string @see UsbDevice.canonical
	 * @return 0 if success
	 */
    public native long checkDevice(String deviceidentifier);

    /**
	 * Release device identified by canonical, native.
	 * @param canonical device identification string @see UsbDevice.canonical
	 * @return 0 if success
	 */
    public native long releaseDevice(String canonical);
}
