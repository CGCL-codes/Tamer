package org.escapek.client.cmdb.tools;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.escapek.client.cmdb.CMDBActivator;
import org.escapek.core.dto.cmdb.CIDTO;
import org.escapek.core.dto.cmdb.CIPropertyDTO;
import org.escapek.core.dto.cmdb.definitions.CIClassDTO;
import org.escapek.core.dto.cmdb.definitions.CIPropertyDefDTO;
import org.escapek.core.dto.security.TicketDTO;
import org.escapek.core.exceptions.SecurityException;
import org.escapek.core.serviceManager.ServiceWrapper;
import org.escapek.core.serviceManager.Exceptions.ConnectionException;
import org.escapek.core.services.interfaces.IRemoteCmdbService;
import org.escapek.logger.LoggerPlugin;
import org.escapek.logger.LoggingConstants;

/**
 * CIClass management tools
 * <p>This class provides a set of static methods for CIClass, CIPropertyDef, CIProperty DTO manipulation.</p>
 * @author nicolasjouanin
 *
 */
public class CIClassTools {

    /**
	 * Get a Map of properties defined for a CI class
	 * @param ciClass CI class to use
	 * @return a map of properties definition
	 * @throws SecurityException
	 */
    public static Map<String, CIPropertyDefDTO> getCIClassPropertiesDef(CIClassDTO ciClass) throws SecurityException {
        try {
            IRemoteCmdbService cmdbService = ServiceWrapper.getInstance().getCmdbService();
            TicketDTO ticket = ServiceWrapper.getInstance().getTicket();
            Map<String, CIPropertyDefDTO> defMap = cmdbService.getCIClassPropertiesDef(ticket.getId(), ciClass.getId());
            return defMap;
        } catch (ConnectionException e) {
            LoggerPlugin.LogError(CMDBActivator.PLUGIN_ID, LoggingConstants.SERVER_CONNECTION, "Couldn't connect to remote CMDB service", e);
            return null;
        }
    }

    /**
	 * Initializes a map of empty property based on CI properties definition of a CI class.
	 * @param ciClass CI class to use as model
	 * @param ci CI instance for which properties are created
	 * @return a map of new Properties
	 */
    public static Map<String, CIPropertyDTO> createCIClassProperties(CIClassDTO ciClass, CIDTO ci) throws SecurityException {
        Map<String, CIPropertyDefDTO> defMap = getCIClassPropertiesDef(ciClass);
        HashMap<String, CIPropertyDTO> propMap = new HashMap<String, CIPropertyDTO>();
        for (Iterator<String> it = defMap.keySet().iterator(); it.hasNext(); ) {
            String key = it.next();
            CIPropertyDefDTO defProp = defMap.get(key);
            propMap.put(key, createCIProperty(defProp, ci));
        }
        return propMap;
    }

    /**
	 * Creates a CI Property instance from a CIProperty definition 
	 * @param defProp Property definition to use as model for the property
	 * @param ci CI instance for which the property is created
	 * @return CI Property created.
	 */
    public static CIPropertyDTO createCIProperty(CIPropertyDefDTO defProp, CIDTO ci) {
        CIPropertyDTO prop = new CIPropertyDTO();
        prop.setPropertyDefId(defProp.getId());
        prop.setValue(defProp.getDefaultValue());
        prop.setCIInstanceId(ci.getId());
        prop.setPropertyName(defProp.getPropertyName());
        return prop;
    }

    /**
	 * Compute a display string for the CI class.
	 * Computed label is first initialized to CI class display name. If it is empty,
	 * the class name is used.
	 * @param theClass CI class instance to compute label for
	 * @return a String label for display purpose
	 */
    public static String getCIClassLabel(CIClassDTO theClass) {
        if (theClass != null) {
            if (theClass.getDisplayName() != null && !theClass.getDisplayName().equals("")) return theClass.getDisplayName(); else return theClass.getClassName();
        }
        return null;
    }
}
