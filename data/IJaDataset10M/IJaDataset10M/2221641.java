package model.device;

/**
 * This exception will be thrown by ADMXDevices when there is an error accessing the DMX 
 * device.
 */
public class ADMXDeviceException extends Exception {

    /**
   * This constructor creates a new ADMXDeviceException. 
   * 
   * @param message The error message.
   */
    public ADMXDeviceException(String message) {
        super(message);
    }
}
