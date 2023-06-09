package org.openmobster.perf.framework;

import java.io.InputStream;
import org.w3c.dom.Document;
import org.apache.log4j.Logger;
import org.openmobster.core.security.identity.IdentityController;
import org.openmobster.core.security.identity.GroupController;
import org.openmobster.core.security.device.DeviceController;
import org.openmobster.core.security.Provisioner;
import org.openmobster.core.common.event.EventManager;
import org.openmobster.core.common.validation.ObjectValidator;
import org.openmobster.device.agent.frameworks.mobileObject.StorageMonitor;
import org.openmobster.device.agent.frameworks.mobileObject.MobileObjectDatabase;
import org.openmobster.device.agent.service.database.Database;
import org.openmobster.device.agent.sync.engine.SyncDataSource;
import org.openmobster.device.agent.sync.engine.SyncEngine;
import org.openmobster.device.agent.sync.SyncService;
import org.openmobster.device.agent.test.framework.Configuration;
import org.openmobster.core.common.IOUtilities;
import org.openmobster.core.common.XMLUtilities;
import org.openmobster.core.common.ServiceManager;
import org.openmobster.core.common.database.HibernateManager;

/**
 * @author openmobster
 *
 */
public final class SimulatedDeviceStack {

    private static Logger log = Logger.getLogger(SimulatedDeviceStack.class);

    private static long deviceCounter = 0;

    private DeviceStackRunner runner;

    public SimulatedDeviceStack() {
    }

    public synchronized void start() {
        try {
            this.setupRunner();
        } catch (Exception e) {
            log.error(this, e);
            throw new RuntimeException(e);
        }
    }

    public synchronized void stop() {
    }

    public DeviceStackRunner getRunner() {
        return this.runner;
    }

    private synchronized void setupRunner() throws Exception {
        this.runner = new DeviceStackRunner();
        DeviceStackRunner originalRunner = (DeviceStackRunner) ServiceManager.locate("deviceStack");
        String deviceId = "" + SimulatedDeviceStack.deviceCounter++;
        String imei = "IMEI:" + deviceId;
        String user = "blah" + deviceId + "@gmail.com";
        this.runner.setDeviceId(imei);
        this.runner.setServerId(originalRunner.getServerId());
        this.runner.setService(originalRunner.getService());
        this.runner.setUser(user);
        this.runner.setCredential(originalRunner.getCredential());
        String server = PerfSuite.getCloudServer();
        if (server != null && server.trim().length() > 0) {
            this.runner.setServerIp(server);
        }
        this.runner.setConfiguration(new Configuration());
        this.setUpProvisioner(this.runner);
        this.setUpSyncStack(this.runner);
        this.runner.start();
    }

    private synchronized void setUpProvisioner(DeviceStackRunner runner) throws Exception {
        DeviceStackRunner originalRunner = (DeviceStackRunner) ServiceManager.locate("deviceStack");
        Provisioner originalProvisioner = originalRunner.getProvisioner();
        Provisioner provisioner = new Provisioner();
        IdentityController identityController = new IdentityController();
        GroupController groupController = new GroupController();
        DeviceController deviceController = new DeviceController();
        EventManager eventManager = originalProvisioner.getEventManager();
        ObjectValidator objectValidator = originalProvisioner.getDomainValidator();
        provisioner.setDomainValidator(objectValidator);
        provisioner.setEventManager(eventManager);
        HibernateManager securityHibernateManager = new HibernateManager();
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("security-perf-hibernate.cfg.xml");
        String xml = new String(IOUtilities.readBytes(is));
        Document doc = XMLUtilities.parse(xml.replace("${device}", runner.getDeviceId()));
        securityHibernateManager.startSessionFactory(doc);
        identityController.setHibernateManager(securityHibernateManager);
        groupController.setHibernateManager(securityHibernateManager);
        deviceController.setHibernateManager(securityHibernateManager);
        provisioner.setIdentityController(identityController);
        provisioner.setGroupController(groupController);
        provisioner.setDeviceController(deviceController);
        runner.setProvisioner(provisioner);
    }

    private synchronized void setUpSyncStack(DeviceStackRunner runner) throws Exception {
        MobileObjectDatabase newDb = new MobileObjectDatabase();
        StorageMonitor storageMonitor = (StorageMonitor) ServiceManager.locate("mobileObject://StorageMonitor");
        HibernateManager mobileObjectHibernateManager = new HibernateManager();
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("hibernate-perf-mobileObject.cfg.xml");
        String xml = new String(IOUtilities.readBytes(is));
        Document doc = XMLUtilities.parse(xml.replace("${device}", runner.getDeviceId()));
        mobileObjectHibernateManager.startSessionFactory(doc);
        newDb.setHibernateManager(mobileObjectHibernateManager);
        newDb.setStorageMonitor(storageMonitor);
        newDb.start();
        runner.setDeviceDatabase(newDb);
        HibernateManager simulatorHibernateManager = new HibernateManager();
        is = Thread.currentThread().getContextClassLoader().getResourceAsStream("hibernate-perf-simulator.cfg.xml");
        xml = new String(IOUtilities.readBytes(is));
        doc = XMLUtilities.parse(xml.replace("${device}", runner.getDeviceId()));
        simulatorHibernateManager.startSessionFactory(doc);
        Database newSyncDb = new Database();
        newSyncDb.setHibernateManager(simulatorHibernateManager);
        SyncDataSource newSyncDataSource = new SyncDataSource();
        newSyncDataSource.setDatabase(newSyncDb);
        newSyncDataSource.start();
        SyncEngine newSyncEngine = new SyncEngine();
        newSyncEngine.setMobileObjectDatabase(newDb);
        newSyncEngine.setSyncDataSource(newSyncDataSource);
        SyncService syncService = new SyncService();
        syncService.setSyncEngine(newSyncEngine);
        runner.setDeviceSyncEngine(newSyncEngine);
        runner.setSyncService(syncService);
    }
}
