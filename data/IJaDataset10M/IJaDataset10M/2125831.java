package mpc.bluetooth;

import java.io.IOException;
import java.util.Vector;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;

/**
 * Class that discovers all bluetooth devices in the neighbourhood
 * and displays their name and bluetooth address.
 */
public class BluetoothDeviceDiscovery implements DiscoveryListener {

    private static Object lock = new Object();

    private static Vector vecDevices = new Vector();

    public static void main(String[] args) throws IOException {
        BluetoothDeviceDiscovery bluetoothDeviceDiscovery = new BluetoothDeviceDiscovery();
        LocalDevice localDevice = LocalDevice.getLocalDevice();
        System.out.println("Address: " + localDevice.getBluetoothAddress());
        System.out.println("Name: " + localDevice.getFriendlyName());
        DiscoveryAgent agent = localDevice.getDiscoveryAgent();
        System.out.println("Starting device inquiry...");
        agent.startInquiry(DiscoveryAgent.GIAC, bluetoothDeviceDiscovery);
        try {
            synchronized (lock) {
                lock.wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Device Inquiry Completed. ");
        int deviceCount = vecDevices.size();
        if (deviceCount <= 0) {
            System.out.println("No Devices Found .");
        } else {
            System.out.println("Bluetooth Devices: ");
            for (int i = 0; i < deviceCount; i++) {
                RemoteDevice remoteDevice = (RemoteDevice) vecDevices.elementAt(i);
                System.out.println((i + 1) + ". " + remoteDevice.getBluetoothAddress() + " (" + remoteDevice.getFriendlyName(true) + ")");
            }
        }
    }

    /**
	 * This call back method will be called for each discovered bluetooth devices.
	 */
    public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
        System.out.println("Device discovered: " + btDevice.getBluetoothAddress());
        if (!vecDevices.contains(btDevice)) {
            vecDevices.addElement(btDevice);
        }
    }

    public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
    }

    public void serviceSearchCompleted(int transID, int respCode) {
    }

    /**
	 * This callback method will be called when the device discovery is
	 * completed.
	 */
    public void inquiryCompleted(int discType) {
        synchronized (lock) {
            lock.notify();
        }
        switch(discType) {
            case DiscoveryListener.INQUIRY_COMPLETED:
                System.out.println("INQUIRY_COMPLETED");
                break;
            case DiscoveryListener.INQUIRY_TERMINATED:
                System.out.println("INQUIRY_TERMINATED");
                break;
            case DiscoveryListener.INQUIRY_ERROR:
                System.out.println("INQUIRY_ERROR");
                break;
            default:
                System.out.println("Unknown Response Code");
                break;
        }
    }
}
