package ti.usb;

import java.io.IOException;
import java.util.LinkedList;

public abstract class USBApi {

    /**
   * Method checks if the underlying os supports usb.
   *
   * @return int value indicating the support level for usb.
   *             If there are not multiple flavours of the underlying OS
   *             like windows and the OS supports usb then just return USB_SUPPORTED_PLATFORM.
   */
    public abstract int checkOsSupportForUsb();

    /**
   * Open's the usb device for communication
   * 
   * @return int Returns a zero if successfull ELS non zero value.
   */
    public abstract int open() throws IOException;

    /**
   * Closes the usb device.
   *
   */
    public abstract void close() throws IOException;

    /**
   * Writes to the usb device.
   *
   * @param buf byte array containg the data to be sent.
   * @param offset the start offset in the data buffer.
   * @param bufLength the number of bytes to send.
   *
   * @return int the number of bytes sent succesfully.
   *
   */
    public abstract int write(byte[] buf, int offset, int bufLength) throws IOException;

    /**
   * Reads from the USB device
   * 
   * @param buf the buffer into which the data is read
   * @param offset the start offset in the data buffer.
   * @param bufLength the number of bytes to read.
   *
   * @return int the number of bytes sent succesfully.
   *
   */
    public abstract int read(byte[] buf, int offset, int bufLength) throws IOException;

    /**
   * Queries for the presence of a usb device
   *
   * @param none
   *
   * @return True if a usb device is present else False
   */
    public abstract boolean isDevicePresent();

    /**
   * Support level indicating that we are unable to determine the type of
   * platform... probably means that USB won't work properly
   * 
   * @see #checkForSupportedPlatform
   */
    public static final int USB_UNKNOWN_PLATFORM = 1;

    /**
   * Support level indicating that we haven't sufficiently tested USB on
   * this platform... USB may work.
   * 
   * @see #checkForSupportedPlatform
   */
    public static final int USB_UNTESTED_PLATFORM = 2;

    /**
   * Support level indicating that USB is not supported on this platform
   * 
   * @see #checkForSupportedPlatform
   */
    public static final int USB_UNSUPPORTED_PLATFORM = 3;

    /**
   * Support level indicating that USB is supported on this platform
   * 
   * @see #checkForSupportedPlatform
   */
    public static final int USB_SUPPORTED_PLATFORM = 4;

    /**
   * Method to get the underlying OS-dependent USBApi native interface class object.
   * This method queries the list of supported OS native USBApi sub classes and returns
   * the appropriate class object.
   *
   * @param NONE 
   *
   * @return USBApi class object exposing the USB native interface support 
   *                for the underlying OS
   */
    public static USBApi getUSBApi() {
        USBApi usbApi = null;
        String osName = System.getProperty("os.name");
        for (int index = 0; index < supportedUSBApiClasses.length; index++) {
            String className = "";
            if ((className = supportedUSBApiClasses[index].classNameForOs(osName)) != null) {
                try {
                    Class usbClass = Class.forName(className);
                    usbApi = (USBApi) usbClass.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        return usbApi;
    }

    /**
   * Adds the specified listener to the listener list.
   *
   * @param DeviceStatusListener listener object to be added to the listener list.
   *
   */
    public static void addDeviceStatusListener(DeviceStatusListener listener) {
        synchronized (deviceStatusListenerList) {
            deviceStatusListenerList.add(listener);
        }
    }

    /**
   * Removes the specified listener from the listener list.
   *
   * @param DeviceStatusListener listener object to be removed from the listener list.
   *
   */
    public static void removeDeviceStatusListener(DeviceStatusListener listener) {
        synchronized (deviceStatusListenerList) {
            deviceStatusListenerList.remove(listener);
        }
    }

    /**
   * Method called from the native library when a usb device is connected.
   *
   * The method is made "public" so that it can be invoked from the native library.
   * Also this method should be treated as a private method of this class and should not be 
   * invoked from any other class.
   *
   */
    public static void deviceAdded() {
        fireDeviceStatusChanged(new DeviceStatusEvent(DeviceStatusEvent.DEVICE_ADDED));
    }

    /**
   * Method called from the native library when a usb device is disconnected.
   *
   * The method is made "public" so that it can be invoked from the native library.
   * Also this method should be treated as a private method of this class and should not be 
   * invoked from any other class.
   *
   */
    public static void deviceRemoved() {
        fireDeviceStatusChanged(new DeviceStatusEvent(DeviceStatusEvent.DEVICE_REMOVED));
    }

    /**
   * Method to notify all registered listeneres that the usb device status has changed.
   */
    protected static void fireDeviceStatusChanged(DeviceStatusEvent evt) {
        synchronized (deviceStatusListenerList) {
            java.util.ListIterator itr = deviceStatusListenerList.listIterator(0);
            while (itr.hasNext()) {
                ((DeviceStatusListener) itr.next()).deviceStatusChanged(evt);
            }
        }
    }

    private static LinkedList deviceStatusListenerList = new java.util.LinkedList();

    private static final USBApiClassType[] supportedUSBApiClasses = { new USBApiClassType("Windows", "ti.usb.WindowsUSBApi") };

    /**
   * Class which contains type information for USBApi classes.
   * Information includes :
   *   osName    : which specifies the "Target OS" like Windows, Linux, Unix....
   *               supported by this class type.
   *   className : name of the USBApi sub class providing the native 
   *               interface support.
   * 
   */
    public static final class USBApiClassType {

        private String osName;

        private String className;

        /**
     * Constructor
     */
        public USBApiClassType(String osName, String className) {
            this.osName = osName;
            this.className = className;
        }

        /**
     * Given the underlying OS name this method checks if 
     * the class-type OS name matches with that.
     * If the provided OS name matches with that of the class OS-name then
     * the name of the USBApi class exposing the native interface is returned.
     * Else null is returned.
     *
     * @param osName The OS name on which chimera is running.
     * 
     * @return String The name of the USBApi sub class OR null.
     *
     */
        public String classNameForOs(String osName) {
            if (osName.indexOf(this.osName) >= 0) {
                return className;
            }
            return null;
        }

        public String toString() {
            return osName + " : " + className;
        }
    }
}
