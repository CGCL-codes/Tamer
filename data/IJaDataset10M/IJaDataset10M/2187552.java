package ti.usb;

/**
 * The listener interface for receiving usb device status change events.
 * The class that is interested in processing an action event implements this interface
 * and the object created with that class is registered with USBApi class, using the 
 * USBApi's class "addUsbDeviceStatusListener" method. When a usb device status change event occurs then
 * that objects usbDeviceStatusChanged method is invoked.
 */
public interface DeviceStatusListener {

    /**
   * Invoked when the usb device status changes.
   * 
   * @param DeviceStatusEvent event object containing information regarding the 
   *                             occured event.
   */
    public void deviceStatusChanged(DeviceStatusEvent evt);
}
