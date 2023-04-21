package BluetoothTCKAgent;

import java.lang.*;
import javax.bluetooth.*;
import java.util.*;

public class DiscoveryListenerImpl implements DiscoveryListener {

    Boolean finished;

    private boolean done;

    private int type = -1;

    private int code;

    private ServiceRecord[] sRecord;

    public boolean calledDeviceDiscovered = false;

    public boolean calledServiceSearchCompleted = false;

    public boolean reached = false;

    public Hashtable idList = new Hashtable();

    public Hashtable discList = new Hashtable();

    RemoteDevice remoteDev = null;

    String btAddress = null;

    String server;

    DeviceClass deviceClass = null;

    public DiscoveryListenerImpl() {
        done = false;
    }

    public DiscoveryListenerImpl(Boolean syn) {
        finished = syn;
        done = false;
    }

    public DiscoveryListenerImpl(Boolean syn, String btAdr) {
        finished = syn;
        btAddress = btAdr.toUpperCase();
        System.out.println("DiscoveryListenerImpl(): Searching for " + btAddress);
    }

    public void deviceDiscovered(RemoteDevice btDevice, DeviceClass sdClass) {
        calledDeviceDiscovered = true;
        if (btAddress != null) {
            if (btAddress.equals(btDevice.getBluetoothAddress())) {
                remoteDev = btDevice;
                deviceClass = sdClass;
            }
        }
    }

    public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
        ServiceRecord tempRecords[];
        int i = 0;
        int j = 0;
        if (sRecord == null) {
            sRecord = servRecord;
        } else {
            tempRecords = new ServiceRecord[sRecord.length + servRecord.length];
            for (i = 0; i < sRecord.length; i++) {
                tempRecords[j++] = sRecord[i];
            }
            for (i = 0; i < servRecord.length; i++) {
                tempRecords[j++] = servRecord[i];
            }
            sRecord = tempRecords;
        }
        reached = true;
        discList.put(new Integer(transID), new Integer(transID));
    }

    public void inquiryCompleted(int discType) {
        done = true;
        type = discType;
        synchronized (finished) {
            try {
                finished.notifyAll();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void serviceSearchCompleted(int transID, int respCode) {
        calledServiceSearchCompleted = true;
        code = respCode;
        if (!inRespList(transID)) {
            idList.put(new Integer(transID), new Integer(code));
        }
        synchronized (finished) {
            try {
                finished.notifyAll();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public ServiceRecord[] getRecordArray() {
        return sRecord;
    }

    public DeviceClass getDeviceClass() {
        return deviceClass;
    }

    public int getType() {
        return type;
    }

    public int getRespCode(int id) {
        if (inRespList(id)) {
            return ((Integer) idList.get(new Integer(id))).intValue();
        }
        return -1;
    }

    public int getDiscCode(int id) {
        if (inDiscList(id)) {
            return ((Integer) discList.get(new Integer(id))).intValue();
        }
        return -1;
    }

    public boolean isDone() {
        return done;
    }

    public RemoteDevice getRemoteDevice() {
        return remoteDev;
    }

    public boolean inRespList(int id) {
        return idList.containsKey(new Integer(id));
    }

    public boolean inDiscList(int id) {
        return discList.containsKey(new Integer(id));
    }
}
