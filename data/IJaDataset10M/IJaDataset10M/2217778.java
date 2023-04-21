package jhomenet.server.hw.mngt;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.log4j.Logger;
import jhomenet.commons.event.EventCategory;
import jhomenet.commons.event.EventLogger;
import jhomenet.commons.hw.Hardware;
import jhomenet.commons.hw.RegisteredHardware;
import jhomenet.commons.hw.UnregisteredHardware;
import jhomenet.commons.hw.mngt.HardwareManager;
import jhomenet.commons.hw.mngt.HardwareManagerException;
import jhomenet.commons.hw.mngt.HardwareRegistry;
import jhomenet.commons.hw.mngt.HardwareOrphanedEvent;
import jhomenet.commons.hw.mngt.HardwareConfirmedEvent;
import jhomenet.commons.hw.mngt.HardwareRegistryException;
import jhomenet.commons.hw.mngt.HardwareUpdatedEvent;
import jhomenet.commons.hw.mngt.HardwareRegisteredEvent;
import jhomenet.commons.hw.mngt.HardwareUnregisteredEvent;
import jhomenet.commons.hw.mngt.NoSuchHardwareException;
import jhomenet.commons.hw.driver.HardwareDriverException;
import jhomenet.commons.hw.driver.HardwareDriver;
import jhomenet.commons.hw.driver.HardwareDriverInitializer;
import jhomenet.commons.hw.driver.HardwareDriverManager;
import jhomenet.commons.persistence.HardwarePersistenceFacade;
import jhomenet.commons.persistence.PersistenceException;
import jhomenet.server.JHomeNetServer;

/**
 * A registry used to manage the registration of hardware objects with the
 * jHomenet server.
 * 
 * @author Dave Irwin (jhomenet at gmail dot com)
 */
public final class DefaultHardwareRegistry implements HardwareRegistry {

    /**
     * Define a logger.
     */
    private static final Logger logger = Logger.getLogger(DefaultHardwareRegistry.class.getName());

    /**
     * Reference to the hardware manager.
     */
    private final HardwareManager hardwareManager;

    /**
     * Reference to the hardware manager.
     */
    private final HardwareDriverManager hardwareDriverManager;

    /**
     * Reference to the hardware persistence layer.
     */
    private HardwarePersistenceFacade hardwarePersistenceLayer;

    /**
     * Maintains the list of current registered hardware. Uses's the hardware's
     * address as the key to map data structure. Updated to concurrent hash map
     * to provide thread safety while accessing.
     */
    private final Map<String, RegisteredHardware> registeredHardwareList = new ConcurrentHashMap<String, RegisteredHardware>();

    /**
     * Maintains the list of currently unregistered hardware.
     */
    private final Map<String, UnregisteredHardware> unregisteredHardwareList = new ConcurrentHashMap<String, UnregisteredHardware>();

    /**
     * Maintains a list of currently orphaned hardware.
     */
    private final Map<String, Hardware> orphanedHardwareList = new ConcurrentHashMap<String, Hardware>();

    /**
     * Used for providing fine grained synchronization.
     */
    private final Lock lock = new ReentrantLock();

    /**
     * Flag to indicate whether the hardware registry is initialized.
     */
    private volatile boolean isInitialized = false;

    /**
     * Default constructor.
     * 
     * @param hardwareManager
     * @param hardwareDriverManager
     */
    public DefaultHardwareRegistry(HardwareManager hardwareManager, HardwareDriverManager hardwareDriverManager) {
        super();
        if (hardwareManager == null) throw new IllegalArgumentException("Hardware manager cannot be null");
        if (hardwareDriverManager == null) throw new IllegalArgumentException("Hardware driver manager cannot be null");
        this.hardwareManager = hardwareManager;
        this.hardwareDriverManager = hardwareDriverManager;
    }

    /**
     * 
     * @param persistenceLayer
     */
    public final void setHardwarePersistenceLayer(HardwarePersistenceFacade persistenceLayer) {
        if (persistenceLayer == null) throw new IllegalArgumentException("Hardware persistence layer cannot be null!");
        this.hardwarePersistenceLayer = persistenceLayer;
    }

    /**
     * Restore the hardware registry. This method is responsible for doing the
     * following:
     * 
     * 1) detect the hardware currently on the 1-Wire network
     * 2) restore previously registered hardware stored in the database
     * 
     * NOTE: Be sure to set the hardware driver manager prior to calling this
     * method.
     * 
     * @see jhomenet.commons.hw.mngt.HardwareRegistry#restoreRegistry()
     */
    public final void restoreRegistry() {
        logger.debug("Initializing hardware registry");
        detectHardware();
        restoreHardware();
        printAllHardware();
        isInitialized = true;
    }

    /**
     * This method detects any hardware currently installed on the communication
     * network. If the hardware hasn't been detected in the past, it is added to
     * the list of unregistered hardware. If detected hardware is currently
     * registered, then its 1-Wire container reference is updated.
     */
    public final synchronized void detectHardware() {
        logger.debug("Detecting hardware");
        hardwareDriverManager.cleanAndBuildContainerList();
        List<String> presentHardwareAddrs = hardwareDriverManager.getHardwareAddrs();
        for (String hardwareAddr : presentHardwareAddrs) addUnregisteredHardware(hardwareAddr, hardwareDriverManager.getDriverHardwareType(hardwareAddr));
        updateOrphanedHardwareList(presentHardwareAddrs);
        printAllHardware();
    }

    /**
     * Update the orphaned hardware list.
     * 
     * @param orphanedHardware
     */
    private void updateOrphanedHardwareList(List<String> presentHardwareAddrs) {
        final Map<String, Hardware> currentKnownHardware = new HashMap<String, Hardware>();
        currentKnownHardware.putAll(this.unregisteredHardwareList);
        currentKnownHardware.putAll(this.registeredHardwareList);
        for (String hardwareAddr : currentKnownHardware.keySet()) {
            if (presentHardwareAddrs.contains(hardwareAddr)) {
                this.removeOrphanedHardware(currentKnownHardware.get(hardwareAddr));
            } else {
                this.addOrphanedHardware(currentKnownHardware.get(hardwareAddr));
            }
        }
    }

    /**
     * 
     * @param hardwareAddr
     */
    private synchronized void addOrphanedHardware(Hardware hardware) {
        if (!this.orphanedHardwareList.containsKey(hardware.getHardwareAddr())) {
            this.orphanedHardwareList.put(hardware.getHardwareAddr(), hardware);
            hardwareManager.fireHardwareOrphanedEvent(new HardwareOrphanedEvent(this, hardware));
            EventLogger.addNewWarningEvent(DefaultHardwareRegistry.class.getName(), "New orphaned hardware: " + hardware.getHardwareAddr(), EventCategory.HARDWARE);
        }
    }

    /**
     * 
     * @param hardware
     */
    private void removeOrphanedHardware(Hardware hardware) {
        if (this.orphanedHardwareList.containsKey(hardware.getHardwareAddr())) {
            this.orphanedHardwareList.remove(hardware.getHardwareAddr());
            hardwareManager.fireHardwareConfirmedEvent(new HardwareConfirmedEvent(this, hardware));
            EventLogger.addNewInfoEvent(DefaultHardwareRegistry.class.getName(), "Hardware confirmed: " + hardware.getHardwareAddr(), EventCategory.HARDWARE);
        }
    }

    /**
     * This method attempts to restore registered hardware from the hardware
     * persistence layer. The process of restoring the hardware is as follows:
     * <br>
     * 1) Read in the hardware address IDs from the database 2) Check if the
     * hardware address is currently in the unregistered hardware list 3) If the
     * hardware is present in the unregistered hardware list, read the remaining
     * hardware information from the database, create an instance of the
     * hardware, and store the hardware in the current list of registered
     * hardware.
     */
    private void restoreHardware() {
        logger.debug("Restoring registered hardware hardware persistence layer");
        try {
            Collection<RegisteredHardware> hardwareList = hardwarePersistenceLayer.retrieveRegisteredHardware();
            if (hardwareList != null) {
                logger.debug("Retrieved non-null hardware list from persistence layer (size=" + hardwareList.size() + ")");
                lock.lock();
                try {
                    for (RegisteredHardware hw : hardwareList) {
                        String hardwareId = hw.getHardwareAddr();
                        logger.debug("Attempting to register hardware " + hardwareId);
                        if (unregisteredHardwareList.containsKey(hardwareId)) {
                            logger.debug("Hardware " + hardwareId + " unregistered...trying to register");
                            try {
                                hardwareManager.registerHardware(hw);
                            } catch (HardwareManagerException hre) {
                                logger.debug("Error registering hardware: " + hre.getMessage());
                            }
                        } else {
                            logger.debug("Hardware " + hardwareId + " not unregistered...can't register");
                        }
                    }
                } finally {
                    lock.unlock();
                }
            } else {
                logger.error("Can't restore hardware -> retrieved null hardware list");
            }
        } catch (PersistenceException pe) {
            logger.error("Error while retrieving persisted hardware list: " + pe.getMessage(), pe);
        }
    }

    /**
     * @return the isInitialized
     */
    public final boolean isInitialized() {
        return isInitialized;
    }

    /**
     * @see jhomenet.commons.hw.mngt.HardwareRegistry#registerOrUpdateHardware(jhomenet.commons.hw.RegisteredHardware)
     */
    public synchronized RegisteredHardware registerOrUpdateHardware(RegisteredHardware registeredHardware) throws HardwareRegistryException {
        if (registeredHardware == null) throw new IllegalArgumentException("Hardware object cannot be null");
        logger.debug("Adding/Updating hardware, hardware address=" + registeredHardware.getHardwareAddr());
        final String hardwareAddr = registeredHardware.getHardwareAddr();
        lock.lock();
        try {
            if (isRegistered(hardwareAddr)) {
                this.registeredHardwareList.put(hardwareAddr, registeredHardware);
                hardwareManager.fireHardwareUpdatedEvent(new HardwareUpdatedEvent(this, registeredHardware));
                EventLogger.addNewInfoEvent(DefaultHardwareRegistry.class.getName(), "Hardware " + hardwareAddr + " updated", EventCategory.HARDWARE);
            } else if (!isRegistered(hardwareAddr)) {
                this.registeredHardwareList.put(hardwareAddr, registeredHardware);
                UnregisteredHardware unregisteredHardware = unregisteredHardwareList.remove(hardwareAddr);
                hardwareManager.fireHardwareRegisteredEvent(new HardwareRegisteredEvent(this, registeredHardware, unregisteredHardware));
                EventLogger.addNewInfoEvent(DefaultHardwareRegistry.class.getName(), "Hardware " + hardwareAddr + " registered", EventCategory.HARDWARE);
            }
            try {
                HardwareDriver hardwareDriver = this.hardwareDriverManager.getHardwareDriver(registeredHardware.getHardwareAddr(), registeredHardware.getHardwareClassname());
                if (registeredHardware instanceof HardwareDriverInitializer) {
                    HardwareDriverInitializer<HardwareDriver> initializer = (HardwareDriverInitializer<HardwareDriver>) registeredHardware;
                    initializer.setHardwareDriver(hardwareDriver);
                } else {
                    logger.debug("Cannot set hardware driver due to class inconsistency");
                }
            } catch (HardwareDriverException hde) {
                logger.error("Error retrieving hardware driver: " + hde.getMessage());
            }
            registeredHardware = hardwarePersistenceLayer.storeHardware(registeredHardware);
        } finally {
            lock.unlock();
        }
        logger.debug("Hardware " + hardwareAddr + " registered/updated in the registry");
        return registeredHardware;
    }

    /**
     * @see jhomenet.commons.hw.mngt.HardwareRegistry#unregisterHardware(jhomenet.commons.hw.RegisteredHardware)
     */
    public UnregisteredHardware unregisterHardware(RegisteredHardware registeredHardware) throws HardwareRegistryException {
        if (registeredHardware == null) throw new IllegalArgumentException("Hardware cannot be null");
        final String hardwareAddr = registeredHardware.getHardwareAddr();
        lock.lock();
        try {
            if (isRegistered(hardwareAddr)) {
                UnregisteredHardware unregisteredHardware = new UnregisteredHardware(hardwareAddr, this.hardwareDriverManager.getDriverHardwareType(registeredHardware.getHardwareAddr()));
                this.unregisteredHardwareList.put(hardwareAddr, unregisteredHardware);
                this.registeredHardwareList.remove(hardwareAddr);
                hardwarePersistenceLayer.deleteHardware(registeredHardware);
                logger.debug("Hardware " + hardwareAddr + " unregistered");
                hardwareManager.fireHardwareUnregisteredEvent(new HardwareUnregisteredEvent(this, unregisteredHardware, registeredHardware));
                return unregisteredHardware;
            } else {
                logger.error("Can't unregistered hardware " + hardwareAddr + " -> isn't registered");
                throw new HardwareRegistryException("Can't unregister hardware because hardware isn't currently registered");
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * @see jhomenet.commons.hw.mngt.HardwareRegistry#addUnregisteredHardware(java.lang.String)
     */
    public void addUnregisteredHardware(String hardwareAddr, String driverHardwareType) {
        lock.lock();
        try {
            if ((!registeredHardwareList.containsKey(hardwareAddr)) && (!unregisteredHardwareList.containsKey(hardwareAddr))) {
                UnregisteredHardware unregisteredHardware = new UnregisteredHardware(hardwareAddr, driverHardwareType);
                this.unregisteredHardwareList.put(hardwareAddr, unregisteredHardware);
                hardwareManager.fireHardwareUnregisteredEvent(new HardwareUnregisteredEvent(this, unregisteredHardware, null));
            } else {
                logger.debug("Hardware " + hardwareAddr + " already present in the system");
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * @see jhomenet.commons.hw.mngt.HardwareRegistry#isRegistered(java.lang.String)
     */
    public boolean isRegistered(String hardwareAddr) throws HardwareRegistryException {
        lock.lock();
        try {
            if (registeredHardwareList.containsKey(hardwareAddr) && (!unregisteredHardwareList.containsKey(hardwareAddr))) {
                return true;
            } else if ((!registeredHardwareList.containsKey(hardwareAddr)) && (unregisteredHardwareList.containsKey(hardwareAddr))) {
                return false;
            } else {
                throw new HardwareRegistryException("Hardware address " + hardwareAddr + " unsynchronized");
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * @see jhomenet.commons.hw.mngt.HardwareRegistry#isOrphaned(java.lang.String)
     */
    @Override
    public final Boolean isOrphaned(String hardwareAddr) {
        if (this.orphanedHardwareList.containsKey(hardwareAddr)) return Boolean.TRUE; else return Boolean.FALSE;
    }

    /**
     * Prints all the hardware.
     */
    public final void printAllHardware() {
        this.printRegisteredHardware();
        this.printUnregisteredHardware();
        this.printOrphanedHardware();
    }

    /**
     * Print the list of registered hardware.
     */
    public final void printRegisteredHardware() {
        logger.debug("Printing registered hardware list:");
        Collection<RegisteredHardware> hardwareElements = registeredHardwareList.values();
        for (RegisteredHardware hw : hardwareElements) logger.debug("  " + hw.toString());
    }

    /**
     * Print the list of unregistered hardware.
     */
    public final void printUnregisteredHardware() {
        logger.debug("Printing unregistered hardware list:");
        for (String hardwareAddr : unregisteredHardwareList.keySet()) logger.debug("  [" + hardwareAddr + "]");
    }

    /**
     * Print the list of currently orphaned hardware.
     */
    public final void printOrphanedHardware() {
        logger.debug("Printing orphaned hardware list:");
        for (String hardwareAddr : orphanedHardwareList.keySet()) logger.debug("  [" + hardwareAddr + "]");
    }

    /**
     * Return a reference to an unregistered hardware object.
     * 
     * @see jhomenet.commons.hw.mngt.HardwareRegistry#getUnregisteredHardware(java.lang.String)
     */
    public UnregisteredHardware getUnregisteredHardware(String hardwareAddr) throws NoSuchHardwareException {
        UnregisteredHardware hw = unregisteredHardwareList.get(hardwareAddr);
        if (hw == null) throw new NoSuchHardwareException(hardwareAddr, "Hardware does not existing in the registry");
        return hw;
    }

    /**
     * Return a list of the currently registered hardware. This method perform a
     * deep copy of the currently registered hardware list.
     * 
     * @see jhomenet.commons.hw.mngt.HardwareRegistry#getRegisteredHardwareList()
     */
    public List<RegisteredHardware> getRegisteredHardwareList() {
        return Collections.list(Collections.enumeration(registeredHardwareList.values()));
    }

    /**
     * Get a reference to a hardware object.
     * 
     * @see jhomenet.commons.hw.mngt.HardwareRegistry#getRegisteredHardware(java.lang.String)
     */
    public RegisteredHardware getRegisteredHardware(String hardwareAddr) throws NoSuchHardwareException {
        RegisteredHardware hw = registeredHardwareList.get(hardwareAddr);
        if (hw == null) throw new NoSuchHardwareException(hardwareAddr, "Hardware does not exist in the registry");
        return hw;
    }

    /**
     * Return a list of unregistered hardware IDs.
     * 
     * @return A list of the unregistered hardware IDs.
     */
    public List<UnregisteredHardware> getUnregisteredHardwareList() {
        List<UnregisteredHardware> hwList = new ArrayList<UnregisteredHardware>();
        Collection<UnregisteredHardware> hwCollection = unregisteredHardwareList.values();
        for (UnregisteredHardware hw : hwCollection) {
            hwList.add(hw.copy());
        }
        return hwList;
    }

    /**
     * Get the number of registered hardware objects.
     * 
     * @return The number of currently registered hardware.
     */
    public int getNumRegisteredHardware() {
        return registeredHardwareList.size();
    }

    /**
     * Get the number of unregistered hardware objects.
     * 
     * @return The number of currently unregistered hardware.
     */
    public int getNumUnregisteredHardware() {
        return unregisteredHardwareList.size();
    }
}
