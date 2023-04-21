package net.sf.dz3.device.sensor.impl.onewire;

import java.io.IOException;
import java.util.ConcurrentModificationException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import org.apache.log4j.NDC;
import com.dalsemi.onewire.OneWireAccessProvider;
import com.dalsemi.onewire.OneWireException;
import com.dalsemi.onewire.adapter.DSPortAdapter;
import com.dalsemi.onewire.adapter.OneWireIOException;
import com.dalsemi.onewire.container.HumidityContainer;
import com.dalsemi.onewire.container.OneWireContainer;
import com.dalsemi.onewire.container.SwitchContainer;
import com.dalsemi.onewire.container.TemperatureContainer;
import com.dalsemi.onewire.utils.OWPath;
import net.sf.dz3.device.sensor.TemperatureSensor;
import net.sf.dz3.device.sensor.impl.AbstractTemperatureSensor;
import net.sf.jukebox.datastream.logger.impl.DataBroadcaster;
import net.sf.jukebox.datastream.signal.model.DataSample;
import net.sf.jukebox.datastream.signal.model.DataSink;
import net.sf.jukebox.datastream.signal.model.DataSource;
import net.sf.jukebox.sem.RWLock;
import net.sf.jukebox.service.ActiveService;

/**
 * An entity capable of resolving a device by address.
 * 
 * Loosely based on {@code net.sf.dz.daemon.onewire.owapi.OneWireServer}.
 * 
 * This class behaves like a singleton, but is not built like one - the intent is to instantiate
 * it with Spring Framework, which will take care of creating as few instances as needed.
 * In any case, OWAPI code will, hopefully, take care of the rest. 
 *  
 * @author Copyright &copy; <a href="mailto:vt@freehold.crocodile.org">Vadim Tkachenko</a> 2001-2009
 */
public class DeviceFactory extends ActiveService implements OneWireNetworkEventListener, DataSource<Double> {

    private final DataBroadcaster<Double> dataBroadcaster = new DataBroadcaster<Double>();

    /**
     * Constant to use as a key for humidity data.
     */
    public static final String DATA_HUM = "humidity";

    /**
     * Constant to use as a key for temperature data.
     */
    public static final String DATA_TEMP = "temperature";

    /**
     * Constant to use as a key for switch data.
     */
    public static final String DATA_SWITCH = "switch";

    /**
     * Adapter port.
     * 
     * This value is injected via constructor. If the port is bad, the device factory
     * will fail to {@link #start()}. 
     */
    private final String adapterPort;

    /**
     * Adapter speed.
     * 
     * This value is injected via constructor. If the value given is bad, it will be
     * defaulted to {@link DSPortAdapter#SPEED_REGULAR}.
     */
    private final int adapterSpeed;

    /**
     * Mapping from the adapter speed value to speed name.
     */
    private final Map<Integer, String> speedInt2speedName = new TreeMap<Integer, String>();

    /**
     * 1-Wire adapter.
     * 
     * Initialized in {@link #startup()}.
     */
    private DSPortAdapter adapter = null;

    /**
     * Read/write lock controlling the exclusive access to the 1-Wire devices.
     * <p>
     * This seems to be a better idea than using
     * <code>beginExclusive()/endExclusive()</code>. OneWire API uses
     * <code>Thread.sleep(50)</code>, which doesn't guarantee first come,
     * first served order, but rather a random order depending on the thread
     * timings. This can produce the wait times for the DS2406 handler as long
     * as 120 seconds, and I suspect it could be worse.
     * <p>
     * On the contrary, while the <code>RWLock</code> still doesn't guarantee
     * the FIFO order, it does provide access as soon as the resource is free -
     * the timings are significantly better. Next step would be to modify the
     * <code>RWLock</code> to guarantee the FIFO order, whether it makes sense
     * to do it or not - let's wait and see.
     */
    private RWLock lock = new RWLock();

    /**
     * Device map. The key is the address, the value is the device container.
     * This map is duplicated by address to device maps contained in the
     * {@link #path2device path to device} map.
     */
    private ContainerMap address2dcGlobal = new ContainerMap();

    /**
     * Device map.
     * <p>
     * The key is the device path, the value is a sorted map where the key is
     * the hardware address, and the value is the device container. Such a
     * complication is required to optimize the access by opening the minimal
     * number of paths and eliminating redundancy.
     */
    private Map<OWPath, ContainerMap> path2device = new TreeMap<OWPath, ContainerMap>();

    /**
     * Data map.
     */
    private DataMap dataMap = new DataMap();

    /**
     * Low-level state map. The key is the device address, the value is last
     * known state obtained using <code>readDevice()</code>.
     */
    private Map<String, byte[]> stateMap = new TreeMap<String, byte[]>();

    /**
     * The network monitor.
     */
    private OneWireNetworkMonitor monitor;

    /**
     * Create an instance.
     * 
     * @param port Port to use.
     * @param speed Speed to use (choices are "regular", "flex", "overdrive", "hyperdrive".
     */
    public DeviceFactory(String port, String speed) {
        NDC.push("DeviceFactory");
        try {
            if (port == null || "".equals(port)) {
                throw new IllegalArgumentException("port can't be null or empty");
            }
            adapterPort = port;
            logger.info("Port:  " + adapterPort);
            Map<String, Integer> speedName2speedInt = new TreeMap<String, Integer>();
            speedName2speedInt.put("overdrive", DSPortAdapter.SPEED_OVERDRIVE);
            speedName2speedInt.put("hyperdrive", DSPortAdapter.SPEED_HYPERDRIVE);
            speedName2speedInt.put("flex", DSPortAdapter.SPEED_FLEX);
            speedName2speedInt.put("regular", DSPortAdapter.SPEED_REGULAR);
            speedInt2speedName.put(DSPortAdapter.SPEED_OVERDRIVE, "overdrive");
            speedInt2speedName.put(DSPortAdapter.SPEED_HYPERDRIVE, "hyperdrive");
            speedInt2speedName.put(DSPortAdapter.SPEED_FLEX, "flex");
            speedInt2speedName.put(DSPortAdapter.SPEED_REGULAR, "regular");
            Integer speedValue = speedName2speedInt.get(speed);
            if (speedValue == null) {
                logger.warn("Unknown speed '" + speed + ", defaulted to regular");
            }
            adapterSpeed = speedValue == null ? DSPortAdapter.SPEED_REGULAR : speedValue;
            logger.info("Speed: " + speedInt2speedName.get(adapterSpeed));
        } finally {
            NDC.pop();
        }
    }

    /**
     * Get a server lock.
     *
     * @return The server lock.
     */
    public final RWLock getLock() {
        return lock;
    }

    /**
     * Get an instance of a temperature sensor.
     * 
     * @param address 1-Wire address.
     * 
     * @return An instance of a temperature sensor, unconditionally. In case when
     * the device with a given address is not present on a bus, the instance returned will keep
     * producing error samples over and over, with "Not Present" being the error. 
     */
    public TemperatureSensor getTemperatureSensor(String address) {
        NDC.push("getTemperatureSensor");
        try {
            Set<DeviceContainer> devices = address2dcGlobal.get(address);
            if (devices == null) {
                return createTemperatureProxy(address);
            }
            for (Iterator<DeviceContainer> i = devices.iterator(); i.hasNext(); ) {
                DeviceContainer dc = i.next();
                logger.info("Found: " + dc + ", " + dc.getType());
                if ("T".equals(dc.getType())) {
                    return (TemperatureSensor) dc;
                }
            }
            logger.warn("Address " + address + " present, but no temperature sensors were found at this address, likely configuration error. Creating proxy container anyway");
            return createTemperatureProxy(address);
        } finally {
            NDC.pop();
        }
    }

    /**
     * Create a proxy for a sensor that hasn't yet been discovered, but was already requested.
     * 
     * @param address Address to create the proxy for.
     * 
     * @return Proxy instance.
     */
    private TemperatureSensor createTemperatureProxy(String address) {
        TemperatureProxy proxy = new TemperatureProxy(address, 1000);
        proxy.start();
        return proxy;
    }

    @Override
    protected void startup() throws Throwable {
        NDC.push("startup");
        try {
            Set<String> portsAvailable = getPortsAvailable();
            if (adapter == null) {
                throw new IllegalArgumentException("Port '" + adapterPort + "' unavailable, valid values: " + portsAvailable);
            }
            if (!adapter.selectPort(adapterPort)) {
                throw new IllegalArgumentException("Unable to select port '" + adapterPort + "', make sure it's the right one (available: " + portsAvailable + ")");
            }
            try {
                adapter.reset();
            } catch (OneWireIOException ex) {
                if ("Error communicating with adapter".equals(ex.getMessage())) {
                    throw new IOException("Port '" + adapterPort + "' doesn't seem to have adapter connected, check others: " + portsAvailable, ex);
                }
            }
            logger.info("Adapter class: " + adapter.getClass().getName());
            logger.info("Adapter port:  " + adapterPort);
            try {
                logger.info("Setting adapter speed to " + speedInt2speedName.get(adapterSpeed));
                adapter.setSpeed(adapterSpeed);
            } catch (Throwable t) {
                logger.error("Failed to set adapter speed, cause:", t);
            }
            monitor = new OneWireNetworkMonitor(adapter, lock);
            monitor.start();
            monitor.addListener(this);
            monitor.getSemUp().waitFor();
            logger.info("started");
        } finally {
            NDC.pop();
        }
    }

    /**
     * Find all available ports and assign the adapter for the one we need,
     * if possible.
     * @return
     */
    private Set<String> getPortsAvailable() {
        NDC.push("getPortsAvailable");
        try {
            Set<String> portsAvailable = new TreeSet<String>();
            for (Enumeration<DSPortAdapter> adapters = OneWireAccessProvider.enumerateAllAdapters(); adapters.hasMoreElements(); ) {
                DSPortAdapter a = adapters.nextElement();
                logger.debug("Adapter found: " + a.getAdapterName());
                for (Enumeration<String> ports = a.getPortNames(); ports.hasMoreElements(); ) {
                    String portName = ports.nextElement();
                    logger.debug("Port found: " + portName);
                    if (adapterPort.equals(portName)) {
                        adapter = a;
                    }
                    portsAvailable.add(portName);
                }
            }
            return portsAvailable;
        } finally {
            NDC.pop();
        }
    }

    @Override
    protected void shutdown() throws Throwable {
        NDC.push("shutdown");
        try {
            logger.info("Stopping monitor...");
            monitor.stop().waitFor();
            logger.info("Stopped");
        } finally {
            NDC.pop();
        }
    }

    /**
     * Keep polling the device state until stopped.
     *
     * @exception Throwable if anything goes wrong.
     */
    @Override
    protected final void execute() throws Throwable {
        NDC.push("execute");
        try {
            while (isEnabled()) {
                try {
                    poll();
                } catch (ConcurrentModificationException ex) {
                    logger.debug("Arrival/departure during poll, ignored", ex);
                } catch (Throwable t) {
                    logger.error("Poll broken:", t);
                    monitor.rescan().waitFor();
                }
            }
        } finally {
            NDC.pop();
        }
    }

    private void poll() throws InterruptedException, OneWireException {
        NDC.push("poll");
        try {
            DataMap localDataMap = new DataMap();
            Object lockToken = null;
            long start = System.currentTimeMillis();
            try {
                if (path2device.isEmpty()) {
                    logger.debug("No devices - forcing rescan");
                    lockToken = lock.release(lockToken);
                    monitor.rescan().waitFor();
                    return;
                }
                for (Iterator<OWPath> i = path2device.keySet().iterator(); i.hasNext(); ) {
                    try {
                        lockToken = lock.getWriteLock();
                        logger.debug("Got lock in " + (System.currentTimeMillis() - start) + "ms");
                        start = System.currentTimeMillis();
                        OWPath path = i.next();
                        try {
                            path.open();
                        } catch (NullPointerException npex) {
                            logger.error("null path encountered, all paths: " + path2device.keySet(), npex);
                            lockToken = lock.release(lockToken);
                            start = System.currentTimeMillis();
                            continue;
                        }
                        logger.debug("Path open: " + path + " in " + (System.currentTimeMillis() - start) + "ms");
                        ContainerMap address2dcForPath = path2device.get(path);
                        if (address2dcForPath == null || address2dcForPath.isEmpty()) {
                            logger.warn("Null address set for '" + path + "'");
                            lockToken = lock.release(lockToken);
                            Thread.sleep(500);
                            start = System.currentTimeMillis();
                            continue;
                        }
                        for (Iterator<String> ai = address2dcForPath.iterator(); ai.hasNext(); ) {
                            if (!isEnabled()) {
                                logger.info("Oops! Not enabled anymore...");
                                return;
                            }
                            String address = ai.next();
                            Set<DeviceContainer> dcSet = address2dcForPath.get(address);
                            if (dcSet == null) {
                                logger.warn("No sensors for " + address + "???");
                                continue;
                            }
                            for (Iterator<DeviceContainer> di = dcSet.iterator(); di.hasNext(); ) {
                                OneWireDeviceContainer dc = (OneWireDeviceContainer) di.next();
                                OneWireContainer owc = dc.container;
                                if (dc instanceof OneWireTemperatureContainer) {
                                    TemperatureContainer tc = (TemperatureContainer) owc;
                                    try {
                                        double temp = getTemperature(tc);
                                        logger.debug(address + ": " + temp + "C");
                                        localDataMap.put(address, DATA_TEMP, new Double(temp));
                                        stateChanged(dc, temp);
                                    } catch (OneWireIOException owioex) {
                                        throw owioex;
                                    } catch (Throwable t) {
                                        logger.warn("Failed to read " + address + ", cause:", t);
                                    }
                                } else if (dc instanceof OneWireSwitchContainer) {
                                    if (!"DS2409".equals(owc.getName())) {
                                        localDataMap.put(address, DATA_SWITCH, getState((SwitchContainer) owc));
                                    }
                                } else if (dc instanceof OneWireHumidityContainer) {
                                    HumidityContainer hc = (HumidityContainer) owc;
                                    long hstart = System.currentTimeMillis();
                                    byte[] state = hc.readDevice();
                                    hc.doHumidityConvert(state);
                                    double humidity = hc.getHumidity(state);
                                    logger.debug("Humidity: " + humidity + " (took " + (System.currentTimeMillis() - hstart) + "ms to figure out)");
                                    localDataMap.put(address, DATA_HUM, new Double(humidity));
                                    stateChanged(dc, humidity);
                                }
                            }
                        }
                    } finally {
                        lockToken = lock.release(lockToken);
                        logger.debug("Path complete in " + (System.currentTimeMillis() - start) + "ms");
                        Thread.sleep(100);
                        start = System.currentTimeMillis();
                    }
                }
                logger.debug("poll done");
                if (path2device.isEmpty()) {
                    Thread.sleep(1000);
                }
            } finally {
                lock.release(lockToken);
            }
            localDataMap.transferTo(dataMap);
            logger.debug("Data map: " + dataMap);
        } finally {
            NDC.pop();
        }
    }

    /**
     * Handle a network arrival.
     *
     * @param e Network arrival information.
     */
    @Override
    public void networkArrival(OneWireNetworkEvent e) {
        OwapiNetworkEvent e2 = (OwapiNetworkEvent) e;
        OneWireContainer owc = e2.getDeviceContainer();
        String address = owc.getAddressAsString();
        Set<DeviceContainer> dcSet = address2dcGlobal.get(address);
        if (dcSet != null) {
            for (Iterator<DeviceContainer> i = dcSet.iterator(); i.hasNext(); ) {
                DeviceContainer oldContainer = i.next();
                if (oldContainer != null) {
                    logger.warn("Arrival notification for device already present: " + e);
                    logger.warn("Duplicate device is: " + oldContainer);
                }
            }
        }
        if (owc instanceof TemperatureContainer) {
            for (int retry = 0; retry < 5; retry++) {
                try {
                    setHiRes((TemperatureContainer) owc, e2.path);
                    break;
                } catch (Throwable t) {
                    logger.warn("Failed to setHiRes on " + address + ", trying again (" + retry + ")");
                }
            }
        } else if (owc instanceof SwitchContainer) {
            SwitchState ss = (SwitchState) dataMap.get(address, DATA_SWITCH);
            if (ss != null) {
                SwitchContainer sc = (SwitchContainer) owc;
                Object lockToken = null;
                try {
                    lockToken = lock.getWriteLock();
                    byte[] state = sc.readDevice();
                    sc.setLatchState(0, ss.state[0], ss.smart, state);
                    sc.setLatchState(1, ss.state[1], ss.smart, state);
                    sc.writeDevice(state);
                    logger.warn("Restored state for " + address + ": " + ss);
                } catch (OneWireException ex) {
                    logger.error("Failed to restore switch state (" + address + "), cause:", ex);
                } catch (InterruptedException iex) {
                    logger.error("Failed to restore switch state (" + address + "), cause:", iex);
                } finally {
                    lock.release(lockToken);
                }
            }
        }
        ContainerMap address2dcForPath = path2device.get(e2.path);
        if (address2dcForPath == null) {
            address2dcForPath = new ContainerMap();
            path2device.put(e2.path, address2dcForPath);
        }
        Set<OneWireDeviceContainer> newDcSet = createContainer(owc);
        for (Iterator<OneWireDeviceContainer> i = newDcSet.iterator(); i.hasNext(); ) {
            DeviceContainer dc = i.next();
            logger.debug("Created container: " + dc);
            address2dcForPath.add(dc);
            address2dcGlobal.add(dc);
            DataSample<Double> signal = new DataSample<Double>(System.currentTimeMillis(), dc.getSignature(), dc.getSignature(), null, new IllegalStateException("Just Arrived"));
            dataBroadcaster.broadcast(signal);
        }
    }

    /**
     * Try to set the highest possible resolution available from the temperature
     * container.
     *
     * @param tc Temperature container to set the resolution of.
     * @param path Path to reach the container.
     */
    private void setHiRes(final TemperatureContainer tc, final OWPath path) {
        Object lockToken = null;
        try {
            lockToken = lock.getWriteLock();
            path.open();
            byte[] state = tc.readDevice();
            if (tc.hasSelectableTemperatureResolution()) {
                double[] resolution = tc.getTemperatureResolutions();
                String s = "";
                for (int idx = 0; idx < resolution.length; idx++) {
                    s += Double.toString(resolution[idx]) + " ";
                }
                logger.debug("Temperature resolutions available: " + s);
                tc.setTemperatureResolution(resolution[resolution.length - 1], state);
            }
            tc.writeDevice(state);
            stateMap.put(((OneWireContainer) tc).getAddressAsString(), state);
        } catch (Throwable t) {
            logger.warn("Failed to set high resolution on " + ((OneWireContainer) tc).getAddressAsString() + ", cause:", t);
        } finally {
            lock.release(lockToken);
        }
    }

    /**
     * Create DZ containers for 1-Wire container.
     *
     * @param owc 1-Wire device container.
     * @return Set of DZ device containers created for a given 1-Wire device
     * container.
     */
    private Set<OneWireDeviceContainer> createContainer(final OneWireContainer owc) {
        Set<OneWireDeviceContainer> result = new TreeSet<OneWireDeviceContainer>();
        if (owc instanceof HumidityContainer) {
            result.add(new OneWireHumidityContainer(owc));
        }
        if (owc instanceof TemperatureContainer) {
            result.add(new OneWireTemperatureContainer(owc));
        }
        if ((owc instanceof SwitchContainer) && !("DS2409".equals(owc.getName()))) {
            result.add(new OneWireSwitchContainer(this, owc));
        }
        if (result.isEmpty()) {
            if ("DS2409".equals(owc.getName())) {
                logger.info("Skipping (can only be a coupler) " + owc + ", generic container created");
            } else {
                logger.info("createContainer(): don't know how to handle " + owc + ", generic container created");
            }
            result.add(new OneWireDeviceContainer(owc));
        }
        return result;
    }

    /**
     * Handle a network departure.
     *
     * @param e Event to handle.
     */
    @Override
    public void networkDeparture(OneWireNetworkEvent e) {
        NDC.push("networkDeparture");
        try {
            OwapiNetworkEvent e2 = (OwapiNetworkEvent) e;
            Set<DeviceContainer> oldContainers = address2dcGlobal.remove(e.address);
            if (oldContainers == null) {
                logger.warn("Departure notification for device that is not present: " + e.address);
            }
            boolean removed = false;
            if (e2.path != null) {
                logger.debug("Departure on known path: " + e2.path);
                ContainerMap address2dcForPath = path2device.get(e2.path);
                if (address2dcForPath == null) {
                    logger.warn("networkDeparture(" + e + "): No devices for path " + e2.path);
                    removed = networkDeparture(e2.address);
                } else {
                    removed = networkDeparture(address2dcForPath, e2.address);
                }
                if (address2dcForPath != null && address2dcForPath.isEmpty()) {
                    logger.info("Empty path " + e2.path + ", removed");
                    path2device.remove(e2.path);
                }
            } else {
                removed = networkDeparture(e2.address);
            }
            if (!removed) {
                logger.warn("Got the departure notification before arrival notification for " + e.address);
            }
        } finally {
            NDC.pop();
        }
    }

    /**
     * Handle a network departure for a known path.
     *
     * @param address2dcForPath Address to device mapping for the path the
     * device is supposed to be.
     * @param address Device address to handle departure of.
     * @return true if device has been sucessfully unmapped, false otherwise.
     */
    private boolean networkDeparture(final ContainerMap address2dcForPath, final String address) {
        if (address2dcForPath == null) {
            throw new IllegalArgumentException("Null map for " + address);
        }
        Set<DeviceContainer> dcSet = address2dcForPath.get(address);
        if (dcSet == null || dcSet.isEmpty()) {
            return false;
        }
        address2dcForPath.remove(address);
        stateMap.remove(address);
        for (Iterator<DeviceContainer> di = dcSet.iterator(); di.hasNext(); ) {
            DeviceContainer dc = di.next();
            DataSample<Double> signal = new DataSample<Double>(System.currentTimeMillis(), dc.getSignature(), dc.getSignature(), null, new IOException("Departed"));
            dataBroadcaster.broadcast(signal);
        }
        return true;
    }

    /**
     * Handle a network departure for unknown path.
     *
     * @param address Device addres to handle departure of.
     * @return true if device has been sucessfully unmapped, false otherwise.
     */
    private boolean networkDeparture(final String address) {
        boolean removed = false;
        logger.info("Departure on unknown path");
        for (Iterator<OWPath> pi = path2device.keySet().iterator(); pi.hasNext(); ) {
            OWPath path = pi.next();
            ContainerMap address2dcForPath = path2device.get(path);
            if (address2dcForPath == null) {
                logger.warn("networkDeparture(" + address + "): No devices for path " + path);
                continue;
            }
            removed = networkDeparture(address2dcForPath, address);
            if (address2dcForPath.isEmpty()) {
                logger.info("Path doesn't contain any devices, removed: " + path);
                pi.remove();
            }
            if (removed) {
                break;
            }
        }
        return removed;
    }

    @Override
    public void networkFault(OneWireNetworkEvent e, String message) {
        throw new UnsupportedOperationException("Not Implemented: handling '" + message + "', event: " + e);
    }

    @Override
    public void addConsumer(DataSink<Double> consumer) {
        dataBroadcaster.addConsumer(consumer);
    }

    @Override
    public void removeConsumer(DataSink<Double> consumer) {
        dataBroadcaster.removeConsumer(consumer);
    }

    /**
     * Get the temperature container reading.
     *
     * @param tc Temperature container to get the reading from.
     * @exception OneWireException if there was a problem talking to 1-Wire&reg;
     * device.
     * @return Current temperature.
     * @throws OneWireIOException If there was a problem with 1-Wire subsystem.
     */
    final double getTemperature(final TemperatureContainer tc) throws OneWireException, OneWireIOException {
        long start = System.currentTimeMillis();
        long now = start;
        String address = ((OneWireContainer) tc).getAddressAsString();
        double lastTemp;
        byte[] state = stateMap.get(address);
        if (state == null) {
            logger.warn("device state is not available yet, possibly setHiRes failed");
            state = tc.readDevice();
        }
        now = System.currentTimeMillis();
        logger.debug("ReadDevice/0: " + (now - start));
        tc.doTemperatureConvert(state);
        now = System.currentTimeMillis();
        logger.debug("doTemperatureConvert: " + (now - start));
        state = tc.readDevice();
        now = System.currentTimeMillis();
        logger.debug("ReadDevice/1: " + (now - start));
        lastTemp = tc.getTemperature(state);
        if (lastTemp == 85.0) {
            throw new IllegalStateException("Temp read is 85C, ignored");
        }
        stateMap.put(address, state);
        return lastTemp;
    }

    /**
     * Get the switch container state.
     *
     * @param sc Switch container to get the state of.
     * @return The switch state object.
     * @exception OneWireException if there was a problem with 1-Wire API.
     */
    private SwitchState getState(final SwitchContainer sc) throws OneWireException {
        SwitchState ss = new SwitchState();
        byte[] state = sc.readDevice();
        ss.smart = sc.hasSmartOn();
        ss.state[0] = sc.getLatchState(0, state);
        ss.state[1] = sc.getLatchState(1, state);
        return ss;
    }

    /**
     * Broadcast the notification.
     *
     * @param dc Device container for the sensor whose state has changed.
     * @param value Current sensor reading.
     */
    @SuppressWarnings("unchecked")
    private synchronized void stateChanged(final DeviceContainer dc, final double value) {
        NDC.push("stateChanged");
        try {
            logger.debug(dc + ": " + value);
            DataSample<Double> signal = new DataSample<Double>(System.currentTimeMillis(), dc.getSignature(), dc.getSignature(), value, null);
            dataBroadcaster.broadcast(signal);
            ((AbstractSensorContainer) dc).stateChanged(value, null);
        } finally {
            NDC.pop();
        }
    }

    /**
     * Get a path object for a given address.
     *
     * @param address Address to get the path for.
     * @return A path object for a given address.
     * @exception NoSuchElementException if there's no path for given address.
     */
    public final OWPath getDevicePath(final String address) {
        for (Iterator<OWPath> i = path2device.keySet().iterator(); i.hasNext(); ) {
            OWPath path = i.next();
            ContainerMap address2dcForPath = path2device.get(path);
            if (address2dcForPath.containsKey(address)) {
                return path;
            }
        }
        throw new NoSuchElementException("No path found for '" + address + "'");
    }

    /**
     * Volatile switch state representation.
     */
    protected class SwitchState {

        /**
         * True if the switch supports smart operation.
         */
        boolean smart = false;

        /**
         * Switch state. VT: FIXME: Extend this for cases like DS2408 (where the
         * number of channels is not 2).
         */
        boolean[] state = { false, false };

        /**
         * @return String representation of the switch state.
         */
        @Override
        public final String toString() {
            String result = "[" + (smart ? "smart" : "dumb");
            result += "][";
            for (int idx = 0; idx < state.length; idx++) {
                if (idx != 0) {
                    result += ",";
                }
                result += state[idx];
            }
            result += "]";
            return result;
        }
    }

    private class TemperatureProxy extends AbstractTemperatureSensor implements DataSink<Double> {

        private OneWireTemperatureContainer temperatureContainer = null;

        public TemperatureProxy(String address, int pollIntervalMillis) {
            super(address, pollIntervalMillis);
        }

        @Override
        public synchronized DataSample<Double> getSensorTemperature() throws IOException {
            NDC.push("getSensorTemperature");
            try {
                if (temperatureContainer != null) {
                    throw new IllegalStateException("This shouldn't have happened");
                }
                Set<DeviceContainer> devices = address2dcGlobal.get(getAddress());
                if (devices == null) {
                    return new DataSample<Double>(System.currentTimeMillis(), "T" + getAddress(), "T" + getAddress(), null, new IllegalStateException("Not Present"));
                }
                for (Iterator<DeviceContainer> i = devices.iterator(); i.hasNext(); ) {
                    DeviceContainer dc = i.next();
                    logger.info("Found: " + dc + ", " + dc.getType());
                    if ("T".equals(dc.getType())) {
                        logger.info("T" + getAddress() + " arrived, starting proxying");
                        this.temperatureContainer = (OneWireTemperatureContainer) dc;
                        ((OneWireTemperatureContainer) dc).addConsumer(this);
                        return new DataSample<Double>(System.currentTimeMillis(), "T" + getAddress(), "T" + getAddress(), null, new IllegalStateException("Found, next reading will be good"));
                    }
                }
                return new DataSample<Double>(System.currentTimeMillis(), "T" + getAddress(), "T" + getAddress(), null, new IllegalStateException("Address is present, but no temperature sensors found - check configuration"));
            } finally {
                NDC.pop();
            }
        }

        @Override
        protected void shutdown() throws Throwable {
        }

        @Override
        protected void startup() throws Throwable {
        }

        @Override
        protected void execute() {
            if (getPollInterval() < 0) {
                throw new IllegalStateException("");
            }
            try {
                while (isEnabled()) {
                    if (temperatureContainer != null) {
                    } else {
                        getSensorTemperature();
                    }
                    Thread.sleep(getPollInterval());
                }
            } catch (Throwable t) {
                logger.fatal("Unexpected problem, shutting down:", t);
            }
        }

        @Override
        public synchronized void consume(DataSample<Double> signal) {
            if (temperatureContainer == null) {
                throw new IllegalStateException("How did we end up here?");
            }
            logger.debug("rebroadcast");
            broadcast(signal);
        }
    }
}
