package org.simplextensions.ui.preferences.xml;

import org.simplextensions.registry.IServiceRegistry;
import org.simplextensions.ui.ISimpleXtensionsUIService;
import org.simplextensions.ui.preferences.IPreferences;
import org.simplextensions.ui.preferences.IPreferencesPersister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Implementation of {@link IPreferencesPersister} which uses jaxb
 * marshaling/unmarshaling and xml file as persistent storage.
 * 
 * XML storage file is located in ${preferencesservice.xml.storage.file}. By
 * default
 * <ul>
 * <li>
 * <b>preferencesservice.xml.storage.dir</b>=${user.home}/${application.name
 * }/Preferences<br/>
 * <br/>
 * <li><b>application.name=SimpleXtensions</b>
 * </ul>
 * and these values are taken from {@link ISimpleXtensionsUIService}.<br/>
 * value of <b>user.home</b> is taken from System.getProperty()
 * 
 * 
 * 
 * @author krzyzak
 * 
 */
public class XMLPreferencesPersister implements IPreferencesPersister {

    private static final Logger log = LoggerFactory.getLogger(XMLPreferencesPersister.class);

    public static final String XMLStorageDirProperty = "preferencesservice.xml.storage.dir";

    private IServiceRegistry serviceRegistry;

    private Map<String, UserPreferencesSet> preferencesMap = new HashMap<String, UserPreferencesSet>();

    private File preferencesDir;

    private JAXBContext newInstance;

    private Marshaller marshaller;

    private Unmarshaller unmarshaller;

    public IPreferences get(String id, String bundleId) {
        UserPreferencesSet map = preferencesMap.get(bundleId);
        if (map == null) {
            InputStream inputStream = null;
            try {
                File file = getFile(bundleId);
                inputStream = new FileInputStream(file);
                map = (UserPreferencesSet) unmarshaller.unmarshal(inputStream);
            } catch (Exception e) {
                log.error("", e);
                map = new UserPreferencesSet();
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        log.error("", e);
                    }
                }
            }
            preferencesMap.put(bundleId, map);
        }
        IPreferences iPreferences = null;
        for (UserPreferences up : map.getPreferences()) {
            if (up.getName().equals(id)) {
                iPreferences = toPreferences(up, bundleId);
            }
        }
        if (iPreferences == null) {
            iPreferences = new Preferences(id, bundleId);
        }
        return iPreferences;
    }

    public void save(IPreferences preferences) {
        log.debug("Saving: " + preferences.getBundleId());
        UserPreferencesSet userPreferencesSet = preferencesMap.get(preferences.getBundleId());
        if (userPreferencesSet == null) {
            preferencesMap.put(preferences.getBundleId(), userPreferencesSet = new UserPreferencesSet());
        }
        Iterator<UserPreferences> iterator = userPreferencesSet.getPreferences().iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getName().equals(preferences.getID())) iterator.remove();
        }
        userPreferencesSet.getPreferences().add(toUserPreferences(preferences));
        OutputStream os = null;
        try {
            os = new FileOutputStream(getFile(preferences.getBundleId()));
            marshaller.marshal(new ObjectFactory().createPreferencesSets(userPreferencesSet), os);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (os != null) try {
                os.close();
            } catch (Exception e) {
                log.error("", e);
            }
        }
    }

    private File getFile(String bundleId) throws IOException {
        String bundlePreferencesFileName = preferencesDir.getCanonicalPath() + File.separator + bundleId + ".xml";
        File bundlePreferencesFile = new File(bundlePreferencesFileName);
        if (!bundlePreferencesFile.exists()) {
            bundlePreferencesFile.createNewFile();
        }
        return bundlePreferencesFile;
    }

    public void init(IServiceRegistry serviceRegistry) {
        log.debug("Initializing XML Persister: " + getClass().getSimpleName());
        this.serviceRegistry = serviceRegistry;
        ISimpleXtensionsUIService sxUIService = this.serviceRegistry.getService(ISimpleXtensionsUIService.class);
        String dir = sxUIService.getProperty(XMLStorageDirProperty);
        preferencesDir = new File(dir);
        if (!preferencesDir.exists()) {
            log.debug("Creating preferences dir: " + dir);
            preferencesDir.mkdirs();
        }
        try {
            newInstance = JAXBContext.newInstance(UserPreferencesSet.class);
            marshaller = newInstance.createMarshaller();
            marshaller.setProperty("jaxb.formatted.output", true);
            unmarshaller = newInstance.createUnmarshaller();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    private UserPreferences toUserPreferences(IPreferences iPreferences) {
        UserPreferences userPreferences = new UserPreferences();
        userPreferences.setName(iPreferences.getID());
        for (String key : iPreferences.getKeys()) {
            Object value = iPreferences.get(key);
            Parameter<?> parameter = null;
            if (value instanceof IPreferences) {
                parameter = toUserPreferences((IPreferences) value);
            } else if (value instanceof Boolean) {
                parameter = new BooleanParameter((Boolean) value);
            } else if (value instanceof Date) {
                parameter = new DateParameter((Date) value);
            } else if (value instanceof Integer) {
                parameter = new IntegerParameter((Integer) value);
            } else if (value instanceof BigDecimal) {
                parameter = new DecimalParameter((BigDecimal) value);
            } else if (value instanceof String) {
                parameter = new StringParameter((String) value);
            }
            if (parameter != null) {
                parameter.setName(key);
                userPreferences.getParameters().add(parameter);
            }
        }
        return userPreferences;
    }

    IPreferences toPreferences(UserPreferences up, String bundleId) {
        Preferences preferences = new Preferences(up.getName(), bundleId);
        for (Parameter<?> p : up.getParameters()) {
            if (p instanceof UserPreferences) {
                preferences.set(p.name, toPreferences((UserPreferences) p, bundleId));
            } else {
                preferences.set(p.name, p.getValue());
            }
        }
        return preferences;
    }
}
