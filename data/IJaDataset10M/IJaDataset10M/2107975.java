package jhomenet.server.cfg;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import jhomenet.commons.ParsingException;
import jhomenet.commons.JHomenetException;
import jhomenet.commons.exceptions.NotYetImplementedException;
import jhomenet.commons.hw.driver.HardwareCompatibility;
import jhomenet.commons.persistence.*;
import jhomenet.server.cfg.HardwareContainerLoaderConfig.HubDef;
import jhomenet.server.persistence.*;

/**
 * An implementation of the <code>HardwareConfiguration</code> hardware configuration
 * interface.
 * 
 * @author Dave Irwin (jhomenet at gmail dot com)
 */
public final class HardwareConfigurationImpl implements HardwareConfiguration {

    /**
     * Define the root logger.
     */
    private static Logger logger = Logger.getLogger(HardwareConfigurationImpl.class.getName());

    /**
     * Hardware compatibility map including driver compatibility list.
     */
    private final HardwareCompatibility hardwareCompatibilityMap = new HardwareCompatibility();

    /**
     * Hardware persistence details.
     */
    private PersistenceTypes hardwarePersistenceType = HardwareConfiguration.PersistenceTypes.UNKNOWN;

    private String hardwarePersistenceFilename;

    /**
     * Hardware data persistence details.
     */
    private PersistenceTypes hardwareDataPersistenceType = HardwareConfiguration.PersistenceTypes.UNKNOWN;

    private String hardwareDataPersistenceFilename;

    /**
     * A map of hardware descriptions where the hardware classname is the key.
     */
    private final Map<String, String> hardwareDescriptions = new HashMap<String, String>();

    /**
     * A list of all the available hardware classnames.
     */
    private final List<String> hardwareClassnames = new ArrayList<String>();

    /**
     * Hardware container loader variables.
     */
    private final List<HardwareContainerLoaderConfig> hwContainerLoaderConfigs = new ArrayList<HardwareContainerLoaderConfig>();

    /**
     * Hardware configuration repository. The repository is the object that
     * is actually responsible for loading the hardware configuration from
     * the underlying resource be it a database, XML file, or text file.
     */
    private HardwareConfigurationRepo hardwareConfigurationRepo;

    /**
     * Reference to the server configuration.
     */
    private final ServerSystemConfigurationImpl serverConfig;

    /**
     * Constructor.
     * 
     * @param serverConfig
     */
    public HardwareConfigurationImpl(ServerSystemConfigurationImpl serverConfig) {
        super();
        if (serverConfig == null) throw new IllegalArgumentException("Server configuration cannot be null!");
        this.serverConfig = serverConfig;
        init();
    }

    /**
     * Initialize the hardware configuration.
     */
    @SuppressWarnings("unchecked")
    private void init() {
        logger.debug("Initializing hardware configuration...");
        ServerSystemConfiguration.ConfigurationTypes type = serverConfig.getHardwareConfigurationType();
        logger.debug("Hardware configuration type set to: " + type.toString());
        if (type.equals(ServerSystemConfiguration.ConfigurationTypes.XML)) {
            String filename = serverConfig.getHardwareConfigurationFilename();
            if (filename != null) {
                this.hardwareConfigurationRepo = new HardwareConfigurationXmlDom4jRepository(filename);
            } else {
                this.hardwareConfigurationRepo = new HardwareConfigurationXmlDom4jRepository();
            }
        } else {
            logger.error("Unknown hardware configuration type: " + type.toString());
        }
    }

    /**
     * @see jhomenet.server.cfg.HardwareConfiguration#load()
     */
    public final HardwareConfiguration load() {
        logger.debug("Loading hardware configuration...");
        try {
            hardwareConfigurationRepo.load(this);
        } catch (ParsingException pe) {
            logger.error("Error while loading hardware repository: " + pe.getMessage());
        }
        return this;
    }

    /**
     * @see jhomenet.server.cfg.HardwareConfiguration#getHardwareClassnames()
     */
    public List<String> getHardwareClassnames() {
        return hardwareClassnames;
    }

    /**
     * @see jhomenet.server.cfg.HardwareConfiguration#getHardwareContainerLoaderConfigs()
     */
    public List<HardwareContainerLoaderConfig> getHardwareContainerLoaderConfigs() {
        return new ArrayList<HardwareContainerLoaderConfig>(hwContainerLoaderConfigs);
    }

    /**
     * @see jhomenet.server.cfg.HardwareConfiguration#getHardwareCompatibilityMap()
     */
    public HardwareCompatibility getHardwareCompatibilityMap() {
        return hardwareCompatibilityMap;
    }

    /**
     * @see jhomenet.server.cfg.HardwareConfiguration#getHardwareDescription(java.lang.String)
     */
    public String getHardwareDescription(String hardwareClassname) {
        return hardwareDescriptions.get(hardwareClassname);
    }

    /**
     * @see jhomenet.server.cfg.HardwareConfiguration#getHardwareDescription(java.lang.Class)
     */
    @SuppressWarnings("unchecked")
    public String getHardwareDescription(Class hardwareClass) {
        return hardwareDescriptions.get(hardwareClass.getName());
    }

    /**
     * @see jhomenet.server.cfg.HardwareConfiguration#getHardwareDataPersistenceFacade()
     */
    @SuppressWarnings("unchecked")
    public HardwareDataPersistenceFacade getHardwareDataPersistenceFacade() throws JHomenetException {
        logger.debug("Retrieving hardware data DAO...");
        logger.debug("  Hardware data persistence type: " + hardwareDataPersistenceType.toString());
        logger.debug("  Hardware data persistence filename: " + hardwareDataPersistenceFilename);
        if (hardwareDataPersistenceType.equals(PersistenceTypes.HIBERNATE)) {
            return AbstractHardwareDataPersistenceFacade.getHardwareDataPersistenceFacade(AbstractHardwareDataPersistenceFacade.TYPE.HIBERNATE);
        }
        throw new JHomenetException("Unknown hardware data persistence type specified: " + hardwareDataPersistenceType.toString());
    }

    /**
     * @see jhomenet.server.cfg.HardwareConfiguration#getHardwarePersistenceLayer()
     */
    @SuppressWarnings("unchecked")
    public HardwarePersistenceFacade getHardwarePersistenceLayer() throws JHomenetException {
        if (hardwarePersistenceType.equals(PersistenceTypes.HIBERNATE)) {
            return HardwarePersistenceFacadeHibernate.instance();
        } else {
            throw new JHomenetException("Unknown hardware persistence type: " + hardwarePersistenceType);
        }
    }

    /**
     * @see jhomenet.server.cfg.HardwareConfiguration#setHardwarePersistenceType(jhomenet.server.cfg.HardwareConfiguration.PersistenceTypes)
     */
    public void setHardwarePersistenceType(PersistenceTypes hardwarePersistenceType) {
        this.hardwarePersistenceType = hardwarePersistenceType;
    }

    /**
     * @see jhomenet.server.cfg.HardwareConfiguration#setHardwarePersistenceFilename(java.lang.String)
     */
    public void setHardwarePersistenceFilename(String filename) {
        this.hardwarePersistenceFilename = filename;
    }

    /**
     * @see jhomenet.server.cfg.HardwareConfiguration#setHardwareDataPersistenceType(jhomenet.server.cfg.HardwareConfiguration.PersistenceTypes)
     */
    public void setHardwareDataPersistenceType(PersistenceTypes hardwareDataPersistenceType) {
        this.hardwareDataPersistenceType = hardwareDataPersistenceType;
    }

    /**
     * @see jhomenet.server.cfg.HardwareConfiguration#setHardwareDataPersistenceFilename(java.lang.String)
     */
    public void setHardwareDataPersistenceFilename(String filename) {
        this.hardwareDataPersistenceFilename = filename;
    }

    /**
     * @see jhomenet.server.cfg.HardwareConfiguration#addHardwareClassname(java.lang.String)
     */
    public void addHardwareClassname(String classname) {
        this.hardwareClassnames.add(classname);
    }

    /**
     * @see jhomenet.server.cfg.HardwareConfiguration#addHardwareContainerLoader(jhomenet.server.cfg.HardwareContainerLoaderConfig)
     */
    public void addHardwareContainerLoader(HardwareContainerLoaderConfig config) {
        this.hwContainerLoaderConfigs.add(config);
    }

    /**
     * @see jhomenet.server.cfg.HardwareConfiguration#addHardwareCompatibility(java.lang.String, java.lang.String, java.lang.String)
     */
    public void addHardwareCompatibility(String classname, String driverHardwareType, String hardwareDriverClassname) {
        this.hardwareCompatibilityMap.add(classname, driverHardwareType, hardwareDriverClassname);
    }

    /**
     * @see jhomenet.server.cfg.HardwareConfiguration#addHardwareDescription(java.lang.String, java.lang.String)
     */
    public void addHardwareDescription(String classname, String description) {
        this.hardwareDescriptions.put(classname, description);
    }

    /** 
     * @see jhomenet.commons.cfg.SystemConfiguration#printConfiguration()
     */
    public void printConfiguration() {
    }
}
