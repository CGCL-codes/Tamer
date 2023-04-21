package org.coosproject.modules.colab.lib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.coos.messaging.util.Log;
import org.coos.messaging.util.LogFactory;

@Entity
@Table(name = "COLabDevice")
public class COLabDevice {

    public static int BATTERY = 66;

    public static int HUMIDITY = 83;

    public static int TEMPERATURE = 84;

    public static int MOTION = 85;

    public static int BUZZER = 86;

    public static int LIGHT = 87;

    public static int DISTANCE = 88;

    public static int LCD = 89;

    public static HashMap<Integer, String> names = new HashMap<Integer, String>() {

        {
            put(COLabDevice.BATTERY, "Battery");
            put(COLabDevice.HUMIDITY, "Humidity");
            put(COLabDevice.TEMPERATURE, "Temperature");
            put(COLabDevice.MOTION, "Motion");
            put(COLabDevice.BUZZER, "Buzzer");
            put(COLabDevice.LIGHT, "Light");
            put(COLabDevice.DISTANCE, "Distance");
            put(COLabDevice.LCD, "LCD");
        }
    };

    public static boolean isActuator(COLabDevice dev) {
        if (dev.getData_type() == COLabDevice.LCD || dev.getData_type() == COLabDevice.BUZZER || dev.getData_type() == COLabDevice.LIGHT) return true;
        return false;
    }

    @Column(name = "DEVICE_TYPE")
    private int device_type;

    @Column(name = "DATA_TYPE")
    private int data_type;

    @Id
    @Column(name = "DEVICE_ID", nullable = true)
    private long device_id;

    @OneToMany(targetEntity = COLabData.class, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private Set<COLabData> dataQueue;

    @OneToOne(targetEntity = COLabData.class, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private COLabData newestData;

    @Column(name = "PING")
    private long ping = 0;

    @ManyToOne(targetEntity = COLabLocation.class, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private COLabLocation location;

    private long maxPopulateAge;

    private static Log LOGGER = LogFactory.getLog(COLabDevice.class);

    public COLabDevice() {
        maxPopulateAge = 2 * 24 * 3600;
    }

    public COLabDevice(long id) {
        maxPopulateAge = 2 * 24 * 3600;
        this.device_id = id;
    }

    public COLabDevice(COLabLocation location, int device_type, int data_type, long device_id, long ping) {
        this.location = location;
        this.device_type = device_type;
        this.data_type = data_type;
        this.device_id = device_id;
        this.ping = ping;
        maxPopulateAge = 2 * 24 * 3600;
    }

    public COLabDevice(COLabLocation location, int device_type, int data_type, long device_id) {
        this.location = location;
        this.device_type = device_type;
        this.data_type = data_type;
        this.device_id = device_id;
        LOGGER.info("in device constructor, id: " + this.device_id);
        maxPopulateAge = 2 * 24 * 3600;
    }

    public static COLabDevice deSeriliaze(COLabLocation loc, String s) {
        COLabDevice ret = null;
        try {
            String devDetail[] = s.split("\\.");
            System.out.println("s: \"" + s + "\" devDetail length: " + devDetail.length);
            if (devDetail.length < 3) return null;
            ret = new COLabDevice(loc, Integer.parseInt(devDetail[0]), Integer.parseInt(devDetail[1]), Integer.parseInt(devDetail[2]));
            ret.ping();
        } catch (Exception e) {
            return null;
        }
        return ret;
    }

    public static String serialize(COLabDevice dev) {
        String ret = "";
        ret = dev.getDevice_type() + "." + dev.getData_type() + "." + dev.getDevice_id();
        return ret;
    }

    @Override
    public String toString() {
        return COLabDevice.serialize(this);
    }

    public int getDevice_type() {
        return device_type;
    }

    public void setDevice_type(int deviceType) {
        device_type = deviceType;
    }

    public int getData_type() {
        return data_type;
    }

    public void setData_type(int dataType) {
        data_type = dataType;
    }

    public long getDevice_id() {
        return device_id;
    }

    public void setDevice_id(long deviceId) {
        device_id = deviceId;
    }

    public void addData(COLabData data) {
        newestData = data;
        dataQueue.add(data);
        this.ping();
    }

    public COLabData getNewestData() {
        return newestData;
    }

    public String activate(double data) {
        String ret = "";
        return ret;
    }

    public void ping() {
        this.ping(System.currentTimeMillis());
    }

    public void ping(Long time) {
        location.ping(time);
        ping = time;
        location.ping(ping);
    }

    public long age() {
        return System.currentTimeMillis() - ping;
    }

    public void populate(ArrayList<HashMap<String, Object>> data) {
        Iterator<HashMap<String, Object>> it = data.iterator();
        Long now = System.currentTimeMillis();
        while (it.hasNext()) {
            HashMap<String, Object> datainstance = it.next();
            if (((String) datainstance.get("dataid")).contains(Long.toString(this.device_id)) && ((String) datainstance.get("dataid")).contains(this.location.getCoaddr()) && ((Long) datainstance.get("datatime") - now < maxPopulateAge)) {
                this.dataQueue.add(new COLabData((Double) datainstance.get("data"), (Long) datainstance.get("datatime")));
                it.remove();
                this.updatePing((Long) datainstance.get("datatime"));
            }
        }
    }

    public void updatePing(Long time) {
        if (time > ping) {
            ping = time;
            location.updatePing(time);
        }
    }

    public void delete() {
        Iterator<COLabData> dataIt = dataQueue.iterator();
        while (dataIt.hasNext()) {
            dataIt.remove();
        }
    }

    public Set<COLabData> getDataQueue() {
        return dataQueue;
    }

    public void setDataQueue(Set<COLabData> dataQueue) {
        this.dataQueue = dataQueue;
    }

    public long getPing() {
        return ping;
    }

    public void setPing(long ping) {
        this.ping = ping;
    }

    public COLabLocation getLocation() {
        return location;
    }

    public void setLocation(COLabLocation location) {
        this.location = location;
    }
}
