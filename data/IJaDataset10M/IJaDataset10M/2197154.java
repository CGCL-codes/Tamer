package eu.mpower.framework.fsa.j2me.core;

import eu.mpower.framework.fsa.j2me.types.DomoticMessage;
import eu.mpower.framework.fsa.j2me.types.IEEE11073Message;
import eu.mpower.framework.fsa.j2me.types.Value;
import java.util.*;
import java.io.*;

/**
 * This class represents the system where are centralized all the sensors or devices.
 *
 * @author DI
 * @version 0.1
 *
 */
public class FrameImp implements Frame {

    protected Class Class;

    private transient Hashtable sensors;

    private transient int num_sensors = 0;

    public transient int next_ID_Sensor = 0;

    private static transient FrameImp mpower = null;

    private transient boolean running = false;

    public String classpath;

    public String fileName;

    protected Hashtable list_sensors;

    private static transient DataBase dataBase;

    private static transient ESBComponent esb_component;

    /**
     * Constructor of the class with the name of a folder where the configuration files are stored.
     *
     * @param workspace The folder where are or will be the configuration files.
     */
    public FrameImp(String workspace) {
        sensors = new Hashtable();
        list_sensors = new Hashtable();
    }

    /**
     * Constructor of the class without parameters. The constructor initilaize several variables and
     * load the properties.
     *
     */
    public FrameImp() {
        sensors = new Hashtable();
        list_sensors = new Hashtable();
    }

    public static FrameImp getMpower() {
        if (mpower == null) {
            FrameImp.setMpower(new FrameImp());
        }
        return FrameImp.mpower;
    }

    /**
     *
     * This method change the unique frame with another one.
     *
     * @param mpower The new frame to overwrite the old frame..
     */
    public static void setMpower(FrameImp mpower) {
        FrameImp.mpower = mpower;
    }

    /**
     *
     * This method returns true if the frame has already started or false if hasn't.
     *
     * @return Returns false if started hasn't been called or stop has been called, and true if the frame 
     * has been started.
     */
    public boolean isRunning() {
        return running;
    }

    /**
     *
     * This method returns the number of sensors registered in the frame.
     *
     * @return The number of inserted sensors.
     */
    public int getNum_sensors() {
        return num_sensors;
    }

    /**
     * This method initialize the frame and all the sensors inserted into it.
     */
    public void start() {
        running = true;
        Enumeration en_sensors = sensors.elements();
        Sensor sensor;
        while (en_sensors.hasMoreElements()) {
            sensor = (Sensor) en_sensors.nextElement();
            sensor.onStart();
        }
    }

    /**
     * This method stop the frame and all the sensors inserted into it..
     */
    public void stop() {
        if (running == true) {
            running = false;
            Enumeration en_sensors = sensors.elements();
            Sensor sensor;
            while (en_sensors.hasMoreElements()) {
                sensor = (Sensor) en_sensors.nextElement();
                sensor.onStop();
            }
            sensors = new Hashtable();
            num_sensors = 0;
            next_ID_Sensor = 0;
        }
    }

    /**
     *
     * This method insert a new sensor into the frame and generate an unique IDSensor inside of the frame
     * for this sensor.
     *
     * @param sensor The new sensor to insert into the frame.
     */
    public void insertSensor(Sensor sensor) {
        if (sensors == null) {
            sensors = new Hashtable();
        }
        if (sensor.getIDSensor() == null) {
            sensor.setIDSensor(new Integer(next_ID_Sensor).toString());
            sensors.put(sensor.getIDSensor(), sensor);
            next_ID_Sensor++;
            num_sensors++;
        } else {
            Integer i = Integer.valueOf(sensor.getIDSensor());
            int n = i.intValue();
            sensors.put(sensor.getIDSensor(), sensor);
            num_sensors++;
            if (n > next_ID_Sensor) {
                next_ID_Sensor = n + 1;
            } else {
                next_ID_Sensor++;
            }
        }
    }

    /**
     *
     * This method register database that is going to be used for storing 
     * information.
     *
     * @param db The instance of database to be used
     */
    public void registerDataBase(DataBase db) {
        dataBase = db;
    }

    /**
     *
     * This method remove sensor from the frame.
     *
     * @param sensor The sensor to delete from the frame.
     */
    public void removeSensor(Sensor sensor) {
        sensors.remove(sensor);
        list_sensors.remove(sensors.get(sensor.getIDSensor()));
        num_sensors--;
    }

    /**
     *
     * This method search a specific sensor registered in the frame by his ID
     *
     * @param IDSensor The  identificator of the sensor to search.
     * @return The sensor if exists or null if doesn't exist.
     */
    public Sensor searchSensor(String IDSensor) {
        sensors.containsKey(IDSensor);
        return (Sensor) sensors.get(IDSensor);
    }

    /**
     *
     * This method print a list with all the sensors registered in the frame.
     *
     */
    public void listSensors() {
        Enumeration en_sensors = sensors.elements();
        System.out.println("VALOR SIZE:" + sensors.size());
        Sensor sensor;
        System.out.println("*********************************");
        System.out.println("****List of sensors in Frame****");
        System.out.println("*********************************");
        while (en_sensors.hasMoreElements()) {
            sensor = (Sensor) en_sensors.nextElement();
            System.out.println(sensor.toString());
            if (!sensor.adapters.isEmpty()) {
                sensor.listAdapters();
            }
        }
        System.out.println("*********************************");
        System.out.println("*****Final list of sensors*******");
        System.out.println("*********************************");
    }

    /**
     * This method is used for inser new data into database
     * 
     * @param id_sensor ID from sensor which is inserting data
     * @param type Type of data that is being inserted
     * @param value Value of data
     * @param time Time when data has been collected
     */
    public static void insertDataDB(String id_sensor, String type, String value, String time) {
        if (dataBase != null) {
            dataBase.insertDataDB(id_sensor, type, value, time);
        }
    }

    /**
     * This method insert a new device into the database.
     * 
     * @param ID_Sensor
     * @param ID_Device
     * @param max
     */
    public static void insertDeviceDB(String ID_Sensor, String ID_Device, int max) {
        if (dataBase != null) {
            dataBase.insertDeviceDB(ID_Sensor, ID_Device, max);
        }
    }

    /**
     * This method insert a new type of sensor's value.
     * @param name The name of this type.
     * @param description The description of the new type.
     * 
     */
    public static void insertTypeDB(String name, String description) {
        if (dataBase != null) {
            dataBase.insertTypeDB(name, description);
        }
    }

    /**
     * This method is used for fetching information from database
     * 
     * @param id_sensor ID of sensor which information want to be consulted
     * @param type Type of data of sensor to be consulted
     * @param n Number of values to be consulted
     * @return A list with result of the consult to the database
     */
    public static Vector queryDataDB(String id_sensor, String type, int n) {
        if (dataBase != null) {
            return dataBase.queryDataDB(id_sensor, type, n);
        }
        return null;
    }

    /**
     * This method is used to check if a device already exist into the database
     * 
     * @param idsensor Device to be checked
     * @return True if already exist, false otherwise
     */
    public static boolean queryDeviceDB(String idsensor) {
        if (dataBase != null) {
            return dataBase.queryDeviceDB(idsensor);
        }
        return false;
    }

    /**
     * This method is used to check if a type of data already exist into the database
     * 
     * @param type Type of data to be checked
     * @return True if already exist, false otherwise
     */
    public static boolean queryTypeDB(String type) {
        if (dataBase != null) {
            return dataBase.queryTypeDB(type);
        }
        return false;
    }

    public DomoticMessage sensPointFisDom(java.lang.String deviceID) {
        DomoticMessage dm = new DomoticMessage();
        dm.setTime(new Date(System.currentTimeMillis()).toString());
        if (deviceID == null) {
            dm.setError(5);
            dm.setDescription("Error: The parameter you give is not correctly.");
            return dm;
        }
        try {
            Sensor sensor = searchSensor(deviceID);
            if (sensor == null) {
                dm.setError(3);
                dm.setDescription("Error: This sensor's doesn't exists.");
                return dm;
            }
            try {
                if (sensor.isConnected() == false) {
                    sensor.setDataReadedOk(false);
                    dm.setError(2);
                    dm.setDescription("Error: Impossible to get value from the sensor.");
                    return dm;
                } else {
                    dm.getValue().removeAllElements();
                    Vector l = sensor.queryFisicSensor();
                    Enumeration e = l.elements();
                    dm.getValue().setSize(l.size());
                    while (e.hasMoreElements()) {
                        dm.getValue().addElement(e.nextElement());
                    }
                    dm.setState(sensor.getState());
                    dm.setID(sensor.getIDSensor());
                    dm.setError(0000);
                    dm.setDescription(sensor.getNameVariable() + " : " + sensor.Description);
                }
            } catch (Exception e) {
            }
            return dm;
        } catch (NullPointerException n) {
            dm.setError(3);
            dm.setDescription("Error: This sensor's doesn't exists.");
            return dm;
        }
    }

    /**
     * This method change the value of a specific actuator. Acts over an actuator.
     *
     * @param deviceID The identificator of the sensor to query.
     * @param message The new value or the message to sen to the phisycal device/sensor.
     * @return A DomoticMessage with the information of the sensor and the sensorial data.
     */
    public DomoticMessage actPointFisDom(java.lang.String deviceID, Value message) {
        DomoticMessage dm = new DomoticMessage();
        if (deviceID == null) {
            dm.setError(5);
            dm.setDescription("Error: The parameter you give is not correctly.");
            return dm;
        }
        try {
            Sensor sensor = searchSensor(deviceID);
            if (sensor == null) {
                dm.setError(3);
                dm.setDescription("Error: This sensor's doesn't exists.");
                return dm;
            }
            try {
                if (sensor.isConnected() == false) {
                    sensor.setDataReadedOk(false);
                    dm.setError(2);
                    dm.setDescription("Error: Impossible to get value from the sensor.");
                    return dm;
                }
                sensor.setValue(message);
            } catch (Exception e) {
            }
            return dm;
        } catch (NullPointerException n) {
            dm.setError(3);
            dm.setDescription("Error: This sensor's doesn't exists.");
            return dm;
        }
    }

    /**
     * This method returns a domotic message with several values of a specific sensor
     *
     * @param deviceID The identificator of the sensor to query.
     * @param num The N values nedded. If doesn't exist sufficient, returns all the stored values.
     * @return A DomoticMessage with the information of the sensor and N  values of the sensorial data.
     */
    public DomoticMessage sensPointLogicDom(java.lang.String deviceID, int num) {
        DomoticMessage dm = new DomoticMessage();
        Integer n = new Integer(num);
        if (deviceID == null || n == null) {
            dm.setError(5);
            dm.setDescription("Error: The parameters you give are not correctly.");
            return dm;
        }
        Sensor sensor = searchSensor(deviceID);
        if (sensor == null) {
            dm.setError(2);
            dm.setDescription("Error: This sensor's doesn't exists.");
            return dm;
        }
        if (num < 1 || num > sensor.getMax_History()) {
            dm.setError(5);
            if (num > 1) {
                dm.setDescription("Error: This sensor's doesn't support more than " + sensor.getMax_History() + " history values.");
            } else {
                dm.setDescription("Error: The parameter is not correct. Please put a number greather than 1.");
            }
            return dm;
        }
        dm.setID(sensor.IDSensor);
        dm.setDescription(sensor.getDescription());
        dm.setError(0);
        dm.setState(sensor.getState());
        Vector l = sensor.queryLogicSensor(num);
        Enumeration e = l.elements();
        dm.getValue().setSize(l.size());
        while (e.hasMoreElements()) {
            dm.getValue().addElement(e.nextElement());
        }
        return dm;
    }

    /**
     * This method returns a IEEE message with the value of a specific medical device.
     *
     * @param deviceID The identificator of the medical device to query.
     * @return A IEEE11073Message with the information of the sensor and the sensorial data.
     */
    public IEEE11073Message sensPointFisMed(java.lang.String deviceID) {
        IEEE11073Message im = new IEEE11073Message();
        if (deviceID == null) {
            im.setIeeemessage("Error: The parameters you give are not correctly");
            return im;
        }
        try {
            Sensor sensor = searchSensor(deviceID);
            if (sensor == null) {
                im.setIeeemessage("Error: This sensor's doesn't exists");
                return im;
            }
            try {
                im.setIeeemessage(sensor.queryFisicMedicalDevice());
            } catch (Exception e) {
                im.setIeeemessage("Error: Error getting values");
                return im;
            }
        } catch (NullPointerException n) {
            n.printStackTrace();
            im.setIeeemessage("Error: This sensor doesn't exists");
            return im;
        }
        return im;
    }

    /**
     * This method sends a message to a specific medical device.
     *
     * @param deviceID The identificator of the medical device to query.
     * @param message The message to send to the medical device.
     * @return A IEEE11073Message with the information of the sensor and the sensorial data.
     */
    public IEEE11073Message actPointFisMed(java.lang.String deviceID, Value message) {
        return null;
    }

    /**
     * This method returns a IEEE message with several values of a specific medical device
     *
     * @param deviceID The identificator of the medical device to query.
     * @param num The number of nedded values.
     * @return A IEEE11073Message with the information of the sensor and the n values of the medical device.
     */
    public IEEE11073Message sensPointLogicMed(java.lang.String deviceID, int num) {
        IEEE11073Message im = new IEEE11073Message();
        Integer n = new Integer(num);
        if (n == null) {
            im.setIeeemessage("Error: The parameter you give is not correct");
            return im;
        }
        if (deviceID == null) {
            im.setIeeemessage("Error: The parameter you give is not correct");
            return im;
        }
        try {
            Sensor sensor = searchSensor(deviceID);
            if (sensor == null) {
                im.setIeeemessage("Error: This sensor's doesn't exists");
                return im;
            }
            try {
                sensor.setReadMessage(sensor.queryLogicMedicalDevice(num));
                im.setIeeemessage(sensor.ReadMessage);
            } catch (Exception e) {
                e.printStackTrace();
                im.setIeeemessage("ERROR obteniendo valores");
                return im;
            }
        } catch (NullPointerException enp) {
            enp.printStackTrace();
            im.setIeeemessage("Error: This sensor doesn't exists");
            return im;
        }
        return im;
    }
}
