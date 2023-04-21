package de.fmui.cmis.fileshare;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import de.fmui.cmis.fileshare.config.CmisTypeDocumentDefinitionType;
import de.fmui.cmis.fileshare.config.CmisTypeFolderDefinitionType;
import de.fmui.cmis.fileshare.config.ConfigType;
import de.fmui.cmis.fileshare.config.LoginType;
import de.fmui.cmis.fileshare.config.RepositoryType;
import de.fmui.cmis.fileshare.config.UserType;
import de.fmui.cmis.fileshare.jaxb.CmisRepositoryEntryType;
import de.fmui.cmis.fileshare.jaxb.CmisRepositoryInfoType;
import de.fmui.cmis.fileshare.jaxb.EnumBaseObjectTypeIds;

/**
 * OF Central class of CMIS FileShare.
 * 
 * @author Florian Müller
 */
public class RepositoryManager {

    private static final RepositoryManager INSTANCE = new RepositoryManager();

    private static final String CONFIG_FILE_PARAM = "de.fmui.cmisfs.config";

    private static final String CONFIG_FILE_SCHEMA_PARAM = "de.fmui.cmisfs.config.schema";

    private static final String CONFIG_FILE = "cmisfs-config.xml";

    private Log log = LogFactory.getLog("de.fmui.cmis.fileshare.RepositoryManager");

    private Map<String, IFileShareRepository> fRepositories;

    private Map<String, String> fLogins;

    private Types fTypes;

    /**
	 * Returns the only true instance.
	 */
    public static RepositoryManager getInstance() {
        return INSTANCE;
    }

    /**
	 * Private constructor.
	 */
    private RepositoryManager() {
        log.info("Initializing " + Constants.PRODUCT + " " + Constants.PRODUCT_VERSION + "...");
        fRepositories = new HashMap<String, IFileShareRepository>();
        fLogins = new HashMap<String, String>();
        fTypes = new Types();
        InputStream configStream = null;
        String configFile = System.getProperty(CONFIG_FILE_PARAM);
        try {
            if (configFile != null) {
                configStream = new FileInputStream(configFile);
            } else {
                configStream = this.getClass().getResourceAsStream("/" + CONFIG_FILE);
            }
            if (configStream == null) {
                configStream = new FileInputStream(System.getProperty("user.home") + File.separator + CONFIG_FILE);
            }
        } catch (Exception e) {
            log.warn("Configuration file '" + configFile + "' not found!");
            return;
        }
        try {
            setup(configStream);
        } catch (Exception e) {
            log.warn("Configuration file parsing error: " + e.getMessage());
        }
    }

    /**
	 * Set ups repositories and users.
	 */
    private void setup(InputStream stream) {
        ConfigType config = null;
        try {
            JAXBContext jc = JAXBContext.newInstance(ConfigType.class.getPackage().getName());
            Unmarshaller u = jc.createUnmarshaller();
            String schemaPath = System.getProperty(CONFIG_FILE_SCHEMA_PARAM);
            if (schemaPath != null) {
                Source cmisSchemaSource = new StreamSource(new File(schemaPath, "CMIS-Core.xsd"));
                Source configSchemaSource = new StreamSource(new File(schemaPath, "CMISFileShareConfig.xsd"));
                SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
                Schema configSchema = sf.newSchema(new Source[] { cmisSchemaSource, configSchemaSource });
                u.setSchema(configSchema);
            }
            JAXBElement<ConfigType> rootElement = (JAXBElement<ConfigType>) u.unmarshal(stream);
            config = rootElement.getValue();
        } catch (Exception e) {
            log.fatal("Parsing configuration file failed: " + e.getMessage(), e);
            return;
        }
        if ((config == null) || (config.getRepositories() == null)) {
            log.warn("No repositories defined!");
            return;
        }
        if (config.getLogins() == null) {
            log.warn("No logins defined!");
        } else {
            for (LoginType login : config.getLogins().getLogin()) {
                if ((login.getName() == null) || (login.getName().trim().length() == 0)) {
                    log.warn("Invalid login found! Skipped.");
                    continue;
                }
                if ((login.getPassword() == null) || (login.getPassword().length() == 0)) {
                    log.warn("Invalid password for login '" + login.getName() + "' ! Skipped.");
                    continue;
                }
                fLogins.put(login.getName().trim(), login.getPassword());
            }
        }
        if (config.getTypes() != null) {
            String packageName = de.fmui.cmis.fileshare.jaxb.CmisTypeDefinitionType.class.getPackage().getName();
            for (CmisTypeDocumentDefinitionType type : config.getTypes().getDocumentType()) {
                try {
                    if ((type.getId() == null) || (type.getId().trim().length() == 0)) {
                        log.warn("Type with invalid id! Skipped.");
                        continue;
                    }
                    if (EnumBaseObjectTypeIds.CMIS_DOCUMENT.value().equals(type.getBaseId())) {
                        log.warn("Type '" + type.getId() + "' has a wrong base type! Skipped.");
                        continue;
                    }
                    if ((type.getParentId() == null) || (type.getParentId().trim().length() == 0)) {
                        log.warn("Type '" + type.getId() + "' has no parent! Skipped.");
                        continue;
                    }
                    if (!fTypes.addType((de.fmui.cmis.fileshare.jaxb.CmisTypeDocumentDefinitionType) Mirror.mirror(packageName, type))) {
                        log.warn("Type '" + type.getId() + "' is invalid! Skipped.");
                    } else {
                        log.info("Added document type '" + type.getId() + "'.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            for (CmisTypeFolderDefinitionType type : config.getTypes().getFolderType()) {
                try {
                    if ((type.getId() == null) || (type.getId().trim().length() == 0)) {
                        log.warn("Type with invalid id! Skipped.");
                        continue;
                    }
                    if (EnumBaseObjectTypeIds.CMIS_FOLDER.value().equals(type.getBaseId())) {
                        log.warn("Type '" + type.getId() + "' has a wrong base type! Skipped.");
                        continue;
                    }
                    if ((type.getParentId() == null) || (type.getParentId().trim().length() == 0)) {
                        log.warn("Type '" + type.getId() + "' has no parent! Skipped.");
                        continue;
                    }
                    if (!fTypes.addType((de.fmui.cmis.fileshare.jaxb.CmisTypeFolderDefinitionType) Mirror.mirror(packageName, type))) {
                        log.warn("Type '" + type.getId() + "' is invalid! Skipped.");
                    } else {
                        log.info("Added folder type '" + type.getId() + "'.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        for (RepositoryType rep : config.getRepositories().getRepository()) {
            log.info("Initializing repository '" + rep.getRepositoryId() + "': " + rep.getRoot());
            try {
                IFileShareRepository fsr = addRepository(rep.getRepositoryId(), rep.getRoot());
                for (UserType user : rep.getUser()) {
                    fsr.addUser(user.getName(), user.isReadOnly());
                }
                fsr.setQueryHandler(rep.getQuery());
            } catch (Exception e) {
                log.warn("Could not initialize repository '" + rep.getRepositoryId() + "': " + e.getMessage(), e);
            }
        }
    }

    /**
	 * Registers a repository.
	 */
    public IFileShareRepository addRepository(String repositoryId, String root) {
        IFileShareRepository fsr = new FileShareRepository(repositoryId, root, fTypes);
        fRepositories.put(repositoryId, fsr);
        return fsr;
    }

    /**
	 * Gets a repository by id.
	 */
    public IFileShareRepository getRepository(String repositoryId) {
        return fRepositories.get(repositoryId);
    }

    /**
	 * Returns all repositories.
	 */
    public Collection<IFileShareRepository> getAllRepositories(CallContext context) {
        return fRepositories.values();
    }

    /**
	 * Returns a list of repository entries.
	 */
    public List<CmisRepositoryEntryType> getRepositories(CallContext context) throws CMISFileShareException {
        List<CmisRepositoryEntryType> result = new ArrayList<CmisRepositoryEntryType>();
        for (IFileShareRepository fsr : fRepositories.values()) {
            CmisRepositoryEntryType repEntry = new CmisRepositoryEntryType();
            CmisRepositoryInfoType repType = fsr.getRepositoryInfo(context);
            repEntry.setRepositoryId(repType.getRepositoryId());
            repEntry.setRepositoryName(repType.getRepositoryName());
            result.add(repEntry);
        }
        return result;
    }

    /**
	 * Authenticates a user against the configured logins.
	 */
    public boolean authenticate(String user, String password) {
        String pwd = fLogins.get(user);
        if (pwd == null) {
            return false;
        }
        return pwd.equals(password);
    }

    /**
	 * Returns all types.
	 */
    public ITypes getTypes() {
        return fTypes;
    }
}
