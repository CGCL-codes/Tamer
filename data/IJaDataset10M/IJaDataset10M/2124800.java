package jolie.net;

import java.util.Enumeration;
import javax.bluetooth.DataElement;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;

public class BTServiceDiscoveryListener implements DiscoveryListener {

    private final UUID uuid;

    private boolean completed = false;

    private ServiceRecord serviceRecord = null;

    public BTServiceDiscoveryListener(UUID uuid) {
        this.uuid = uuid;
    }

    public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
    }

    public ServiceRecord getResult() {
        synchronized (this) {
            while (!completed) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                }
            }
        }
        return serviceRecord;
    }

    public void inquiryCompleted(int discType) {
    }

    public void servicesDiscovered(int transID, ServiceRecord[] serviceRecords) {
        DataElement e;
        ServiceRecord r;
        Enumeration<DataElement> en;
        boolean keepRun = true;
        for (int i = 0; i < serviceRecords.length && keepRun; i++) {
            r = serviceRecords[i];
            if ((e = r.getAttributeValue(0x0001)) != null) {
                if (e.getDataType() == DataElement.DATSEQ) {
                    en = (Enumeration<DataElement>) e.getValue();
                    Object o;
                    while (en.hasMoreElements()) {
                        o = en.nextElement().getValue();
                        if (o instanceof UUID) {
                            if (((UUID) o).equals(uuid)) {
                                serviceRecord = r;
                                keepRun = false;
                            }
                        }
                    }
                } else if (e.getDataType() == DataElement.UUID) {
                    if (((UUID) e.getValue()).equals(uuid)) {
                        serviceRecord = r;
                        keepRun = false;
                    }
                }
            }
        }
    }

    public void serviceSearchCompleted(int transID, int respCode) {
        synchronized (this) {
            completed = true;
            this.notify();
        }
    }
}
