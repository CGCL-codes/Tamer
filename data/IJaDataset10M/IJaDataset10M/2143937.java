package jhomenet.comm.server;

import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.*;
import jhomenet.gui.bean.RegisterBean;
import jhomenet.hw.*;
import jhomenet.comm.Notifiable;

public interface RemoteHardwareRegistry extends java.rmi.Remote {

    /***
     * Get a list of registered hardware.
     *
     * @return A list of registered hardware.
     * @throws RemoteException
     */
    public ConcurrentHashMap<String, HomenetHardware> getRegisteredHardwareList() throws RemoteException;

    /***
     * Get a list of unregistered hardware IDs.
     *
     * @return A list of unregistered hardware IDs.
     * @throws RemoteException
     */
    public ArrayList<String> getUnregisteredHardwareList() throws RemoteException;

    /***
     * Get a reference to a registered hardware.
     *
     * @return A reference to a registered hardware
     * @throws RemoteException
     */
    public HomenetHardware getRegisteredHardware(String hardwareID) throws RemoteException;

    /***
     * Get the driver hardware type given the hardware ID.
     *
     * @param hardwareID
     * @return The driver hardware type.
     * @throws RemoteException
     */
    public String getDriverHardwareType(String hardwareID) throws RemoteException;

    /***
     * Read the current value of a hardware object given the hardware ID.
     *
     * @param hardwareID
     * @return The current sensor value.
     * @throws RemoteException
     */
    public double readCurrentValue(String hardwareID) throws RemoteException;

    /***
     * Read the current value of a hardware object as a string given the
     * hardware ID.
     *
     * @param hardwareID
     * @return The current sensor value as a string.
     * @throws RemoteException
     */
    public String readCurrentValueAsString(String hardwareID) throws RemoteException;

    /***
     * Get all the data stored in the database associated with a particular
     * hardware ID.
     *
     * @param hardareID
     * @return A hardware data object of all the hardware's data.
     * @throws RemoteException
     */
    public HardwareDataObject getHardwareData(String hardareID) throws RemoteException;

    /***
     * Get the name of the data columsn.
     *
     * @return The data column names.
     * @throws RemoteException
     */
    public Vector<String> getHardwareDataColumnNames() throws RemoteException;

    /***
     * Execute a sensor hardware registration command.
     *
     * @param bean
     * @throws RemoteException
     */
    public Boolean registerSensorHardware(RegisterBean bean) throws RemoteException;

    /***
     * Execute a device hardware registration command.
     *
     * @param bean
     * @throws RemoteException
     */
    public Boolean registerDeviceHardware(RegisterBean bean) throws RemoteException;

    /***
     * Register an object with the server. Used for RMI callbacks.
     *
     * @param notifier
     * @throws RemoteException
     */
    public void registerForNotifications(Notifiable notifier) throws RemoteException;

    /***
     * Set the configuration string for the hardware object.
     *
     * @param hardwareID
     * @param configKey
     * @param configValue
     * @throws RemoteException
     */
    public void setConfiguration(String hardwareID, String configKey, String configValue) throws RemoteException;

    public void setNorthOffset(String hardwareID) throws RemoteException;
}
