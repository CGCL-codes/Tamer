package BluetoothNetwork;

import GameLogic.GameController;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.L2CAPConnection;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.lcdui.Alert;

/**
 *
 * @author  Luis Albinati (luis.albinati@gmail.com)
 */
public class BluetoothClient implements Runnable {

    private InquiryListener inq_listener;

    private ServiceListener serv_listener;

    private boolean listening = true;

    private Thread t;

    private boolean connectionMade;

    private GameController controller;

    public Thread getT() {
        return t;
    }

    public boolean isListening() {
        return listening;
    }

    private UI.MobiBoxMidlet midlet;

    private String deviceName;

    private L2CAPConnection con;

    /** Creates a new instance of BluetoothClient
     * @param midlet 
     */
    public BluetoothClient(UI.MobiBoxMidlet midlet) {
        this.midlet = midlet;
        t = new Thread(this);
    }

    public void run() {
        System.out.println("Starting client - please wait...");
        Alert client = new Alert("Please wait...");
        client.setTitle("Starting Client...");
        client.setString("Please wait until connection is established, This may take a while...");
        midlet.getDisplay().setCurrent(client, midlet.getBluetoothList());
        try {
            LocalDevice local_device = LocalDevice.getLocalDevice();
            DiscoveryAgent disc_agent = local_device.getDiscoveryAgent();
            local_device.setDiscoverable(DiscoveryAgent.GIAC);
            inq_listener = new InquiryListener();
            synchronized (inq_listener) {
                disc_agent.startInquiry(DiscoveryAgent.GIAC, inq_listener);
                try {
                    inq_listener.wait();
                } catch (InterruptedException e) {
                }
            }
            Enumeration devices = inq_listener.cached_devices.elements();
            UUID[] u = new UUID[] { new UUID("00000000000010008000006057028A06", false) };
            int attrbs[] = { 0x0100 };
            serv_listener = new ServiceListener();
            while (devices.hasMoreElements()) {
                synchronized (serv_listener) {
                    disc_agent.searchServices(attrbs, u, (RemoteDevice) devices.nextElement(), serv_listener);
                    try {
                        serv_listener.wait();
                    } catch (InterruptedException e) {
                    }
                }
            }
        } catch (BluetoothStateException e) {
            System.out.println(e);
        }
        if (serv_listener.service != null) {
            try {
                String url;
                url = serv_listener.service.getConnectionURL(0, false);
                deviceName = LocalDevice.getLocalDevice().getFriendlyName();
                con = (L2CAPConnection) Connector.open(url);
                send("Connection established with " + getName());
                byte[] b = new byte[1000];
                while (listening) {
                    if (con.ready()) {
                        con.receive(b);
                        String s = new String(b, 0, b.length);
                        System.out.println("Received from server: " + s.trim());
                        midlet.setAlert(s.trim());
                        listening = false;
                        controller = midlet.getController();
                    }
                    if (listening == false) {
                        connectionMade = true;
                    }
                }
                while (connectionMade) {
                    if (con.ready()) {
                        b = new byte[1000];
                        con.receive(b);
                        String s = new String(b, 0, b.length);
                        System.out.println("Client recieved:" + s);
                        int code = Integer.parseInt(s.trim());
                        controller.move(code, "networkCall");
                    }
                }
            } catch (IOException g) {
                System.out.println(g);
            }
        }
    }

    public void send(String s) {
        byte[] b = s.getBytes();
        try {
            System.out.println("Client sent:" + s);
            con.send(b);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private String getName() {
        return deviceName;
    }
}

class InquiryListener implements DiscoveryListener {

    public Vector cached_devices;

    public InquiryListener() {
        cached_devices = new Vector();
    }

    public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
        int major = cod.getMajorDeviceClass();
        if (!cached_devices.contains(btDevice)) {
            cached_devices.addElement(btDevice);
        }
    }

    public void inquiryCompleted(int discType) {
        synchronized (this) {
            this.notify();
        }
    }

    public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
    }

    public void serviceSearchCompleted(int transID, int respCode) {
    }
}

class ServiceListener implements DiscoveryListener {

    public ServiceRecord service;

    public ServiceListener() {
    }

    public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
        service = servRecord[0];
        System.out.println("foundService");
    }

    public void serviceSearchCompleted(int transID, int respCode) {
        synchronized (this) {
            this.notify();
        }
    }

    public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
    }

    public void inquiryCompleted(int discType) {
    }
}
