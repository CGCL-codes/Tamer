package com.freeture.frmwk.properties;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.regexp.RE;
import com.freeture.frmwk.utils.FileUtilitaire;

public class PropertiesManager {

    public static final String REQUIRED = "REQUIRED";

    public static final String FORBIDDEN = "FORBIDDEN";

    private static final Log LOGGER = LogFactory.getLog(PropertiesManager.class);

    /**
	 * Retourne la premiere occurance trouv� correspondant au filtre
	 * @param propertyFilter
	 */
    public static String getValue(Properties properties, String propertyFilter) {
        String propertyMatched = null;
        RE regExp = null;
        try {
            regExp = new RE(propertyFilter);
            regExp.setMatchFlags(RE.MATCH_CASEINDEPENDENT);
        } catch (Exception e) {
            LOGGER.error(e);
        }
        Iterator<Entry<Object, Object>> iEntriesSet = properties.entrySet().iterator();
        while (iEntriesSet.hasNext()) {
            Map.Entry<Object, Object> anEntry = iEntriesSet.next();
            String aKey = (String) anEntry.getKey();
            if (regExp.match(aKey)) {
                propertyMatched = aKey;
                return propertyMatched;
            }
        }
        return propertyMatched;
    }

    /**
	 * Retourne la premiere occurance trouv� correspondant au filtre (Expression reguliere RegExp)
	 * 
	 * @param Filter
	 */
    public static List<String> getAllListValue(Properties properties, String filtre) {
        List<String> trouvees = new ArrayList<String>();
        RE regExp = null;
        try {
            regExp = new RE(filtre);
            regExp.setMatchFlags(RE.MATCH_CASEINDEPENDENT);
        } catch (Exception e) {
            LOGGER.error(e);
        }
        Iterator<Entry<Object, Object>> iEntriesSet = properties.entrySet().iterator();
        while (iEntriesSet.hasNext()) {
            Map.Entry<Object, Object> anEntry = iEntriesSet.next();
            String aKey = (String) anEntry.getKey();
            if (regExp.match(aKey)) {
                trouvees.add(aKey);
            }
        }
        return trouvees;
    }

    /**
	 * Verifie si le fichier existe @param resourcePath et @return le Fichier instancie
	 * si le fichier n'existe pas alors @throws IOException .
	 */
    public static File getFileRessource(String resourcePath) throws IOException {
        ClassLoader cl = FileUtilitaire.class.getClassLoader();
        URL resource = cl.getResource(resourcePath);
        if (resource == null) {
            resource = ClassLoader.getSystemResource(resourcePath);
        }
        if (resource == null) {
            String error = "La Ressource n'a pas �t� trouv� (Rep + ClassPath) : " + resourcePath;
            LOGGER.fatal(error);
            return null;
        }
        String resourceFullPath = resource.getPath();
        File inputFile = new File(resourceFullPath);
        return inputFile;
    }
}
