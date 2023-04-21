package org.dicom4j.apps.commons.application;

import java.io.PrintStream;
import java.util.Iterator;
import java.util.LinkedList;
import org.apache.log4j.Level;
import org.apache.log4j.PropertyConfigurator;
import org.dicom4j.apps.commons.application.i18n.I18nListener;
import org.dicom4j.apps.commons.application.i18n.I18nTranslator;
import org.dicom4j.apps.commons.dicomdevice.DicomDevice;
import org.dicom4j.apps.commons.dicomdevice.DicomDevices;
import org.dicom4j.apps.commons.dicomdevice.DicomDevicesListener;
import org.dicom4j.apps.commons.dicomdevice.DicomDevicesStore;
import org.dicom4j.apps.commons.utils.LoggingOutputStream;
import org.dolmen.swing.GUIUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Application
 *
 * @since 0.2
 * @author <a href="mailto:straahd@users.sourceforge.net">Laurent Lecomte 
 *
 */
public class Application {

    private static Logger logger = LoggerFactory.getLogger(Application.class);

    private ApplicationConfiguration configuration;

    private LinkedList<ApplicationListener> listeners = new LinkedList<ApplicationListener>();

    /**
	 * Application's name
	 */
    private String name = "";

    /**
	 * Application's version
	 */
    private String version = "";

    private I18nTranslator translator;

    private DicomDevices devices = new DicomDevices();

    private LinkedList<DicomDevicesListener> dicomDevicesListener = new LinkedList<DicomDevicesListener>();

    private LinkedList<I18nListener> i18Listener = new LinkedList<I18nListener>();

    private ApplicationContext applicationContext = new ApplicationContext();

    /**
	 * @return Returns the applicationContext.
	 */
    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
	 * @return Returns the devices.
	 */
    public DicomDevices getDevices() {
        return devices;
    }

    /**
	 * @param devices The devices to set.
	 */
    public void setDevices(DicomDevices devices) {
        this.devices = devices;
    }

    public Application() {
        this("");
    }

    public Application(String aName) {
        this(aName, "");
    }

    public Application(String name, String verion) {
        this.name = name;
        this.version = verion;
        System.setOut(new PrintStream(new LoggingOutputStream(org.apache.log4j.Logger.getRootLogger(), Level.INFO), true));
        System.setErr(new PrintStream(new LoggingOutputStream(org.apache.log4j.Logger.getRootLogger(), Level.ERROR), true));
    }

    public void setConfiguration(ApplicationConfiguration aConfig) {
        logger.debug("setConfiguration: " + aConfig);
        configuration = aConfig;
    }

    public ApplicationConfiguration getConfiguration() {
        return (ApplicationConfiguration) configuration;
    }

    public ApplicationListener addListener(ApplicationListener listener) {
        logger.debug("addListener: " + listener);
        this.listeners.add(listener);
        return listener;
    }

    public DicomDevicesListener addDicomDevicesListener(DicomDevicesListener listener) {
        logger.debug("addDicomDevicesListener: " + listener);
        this.dicomDevicesListener.add(listener);
        return listener;
    }

    public I18nListener addI18nListener(I18nListener listener) {
        logger.debug("addI18nListener: " + listener);
        this.i18Listener.add(listener);
        return listener;
    }

    public void start() throws Exception {
        logger.info("Trying to start application:" + getName());
        logger.info(this.applicationContext.toString());
        if (getConfiguration() == null) throw new Exception("No configuration was set, unable to start the application");
        PropertyConfigurator.configure(getConfiguration().getAppSettings().getLoggingFileName());
        translator = new I18nTranslator(I18n.i18n);
        GUIUtils.setLookAndFeel(configuration.getAppSettings().getChosenLookAndFeel());
        new DicomDevicesStore().retreive(this.devices, getConfiguration().getFullDeviceFilePath());
    }

    public void start(String configFileName) throws Exception {
        logger.info("Trying to start application:" + getName());
        logger.info(this.applicationContext.toString());
        loadConfiguration(configFileName);
        PropertyConfigurator.configure(getConfiguration().getAppSettings().getLoggingFileName());
        translator = new I18nTranslator(I18n.i18n);
        GUIUtils.setLookAndFeel(configuration.getAppSettings().getChosenLookAndFeel());
        new DicomDevicesStore().retreive(this.devices, getConfiguration().getFullDeviceFilePath());
    }

    public void stop() throws Exception {
        logger.debug("trying to stop the application");
        fireApplicationStopRequested();
        System.exit(0);
    }

    public void loadConfiguration(String configFileName) throws Exception {
        if (getConfiguration() == null) {
            throw new Exception("No configuration was set, unable to start the application");
        } else {
            configuration.load(applicationContext, configFileName);
            fireConfigurationUpdated();
        }
    }

    public void saveConfiguration() throws Exception {
        if (configuration != null) {
            configuration.save();
            fireConfigurationUpdated();
        }
    }

    public void applyConfiguration() throws Exception {
        if (configuration != null) {
            configuration.apply();
            fireConfigurationUpdated();
        }
    }

    public void fireConfigurationUpdated() {
        Iterator<ApplicationListener> it = listeners.iterator();
        while (it.hasNext()) {
            it.next().configurationUpdated(getConfiguration());
        }
    }

    public void fireDevicesLoaded() {
        Iterator<DicomDevicesListener> it = dicomDevicesListener.iterator();
        while (it.hasNext()) {
            it.next().devicesLoaded(this.devices);
        }
    }

    public void fireDevicesAdded(DicomDevice device) {
        Iterator<DicomDevicesListener> it = dicomDevicesListener.iterator();
        while (it.hasNext()) {
            it.next().deviceAdded(device);
            this.devices.add(device);
        }
    }

    public void fireDevicesDeleted(DicomDevice device) {
        Iterator<DicomDevicesListener> it = dicomDevicesListener.iterator();
        while (it.hasNext()) {
            it.next().deviceDeleted(device);
            this.devices.remove(device);
        }
    }

    public void fireTranslatorUpdated() {
        Iterator<I18nListener> it = i18Listener.iterator();
        while (it.hasNext()) {
            it.next().TranslatorUpdated(this.translator);
        }
    }

    public void fireApplicationStopRequested() {
        Iterator<ApplicationListener> it = listeners.iterator();
        while (it.hasNext()) {
            it.next().ApplicationStopRequested();
        }
    }

    public String getName() {
        return this.name;
    }

    public String getVersion() {
        return this.version;
    }

    public String translate(String key) {
        if (this.translator != null) {
            return this.translator.translate(key);
        } else {
            return key;
        }
    }

    public String toString() {
        return getName() + " application (version: " + getVersion() + ")";
    }
}
