package fr.antifirewall.util;

import java.util.Enumeration;
import java.util.Properties;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * @author Kawets
 */
public class LogManager {

    /** 
	 * @deprecated A mettre dans un fichier de configuration
	 */
    public static final String FICHIER_PROPRIETES = "log4j";

    static boolean dejaInitialise = false;

    public static void init() {
        if (!dejaInitialise) {
            Properties properties = chargeProprietes(FICHIER_PROPRIETES);
            PropertyConfigurator.configure(properties);
            dejaInitialise = true;
        }
    }

    public static Logger getLogger(String nom) {
        init();
        return Logger.getLogger(nom);
    }

    private static Properties chargeProprietes(String nomFichier) {
        ResourceBundle bundle = ResourceBundle.getBundle(nomFichier);
        Enumeration keys = bundle.getKeys();
        Properties proprietes = new Properties();
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            String valeur = bundle.getString(key);
            proprietes.put(key, valeur);
        }
        return proprietes;
    }
}
