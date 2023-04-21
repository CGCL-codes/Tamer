package mswing;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Cette classe charge la configuration � partir d'un fichier properties et donne acc�s aux
 * param�tres de ce fichier. <BR>
 * Ce fichier mswing.properties est inclus dans le mswing.jar. Pour personnaliser la configuration
 * pour un projet et une langue, ouvrir le jar avec Winzip, copier le fichier properties, modifier
 * le, et d�finisser votre classpath d'ex�cution avec votre jar contenant votre mswing.properties,
 * avant le mswing.jar pour que votre version des param�tres soit trouv�e avant celle par d�faut. <br>
 * Ou sinon vous pouvez d�finir une propri�t� syst�me "mswing.configFile" d�finissant sans
 * l'extension properties le nom du fichier de param�trage.
 *
 * @author Emeric Vernat
 */
public final class MSwingConfig {

    private static final String FILE_NAME = System.getProperty("mswing.configFile", "mswing");

    private static ResourceBundle resourceBundle = ResourceBundle.getBundle(FILE_NAME);

    /**
	 * Constructeur. (private : pas d'instance)
	 */
    private MSwingConfig() {
        super();
    }

    /**
	 * R�cup�re un bool�en dans le fichier (avec chargement si n�cessaire).
	 *
	 * @return boolean
	 * @param key String
	 * @param defaultValue boolean
	 */
    public static boolean getBoolean(String key, boolean defaultValue) {
        return "true".equalsIgnoreCase(getString(key, String.valueOf(defaultValue)));
    }

    /**
	 * R�cup�re un char dans le fichier (avec chargement si n�cessaire).
	 *
	 * @return char
	 * @param key String
	 * @param defaultValue char
	 */
    public static char getChar(String key, char defaultValue) {
        try {
            return getString(key, String.valueOf(defaultValue)).charAt(0);
        } catch (final StringIndexOutOfBoundsException e) {
            return defaultValue;
        }
    }

    /**
	 * R�cup�re une Color dans le fichier (avec chargement si n�cessaire).
	 *
	 * @return java.awt.Color
	 * @param key String
	 * @param defaultValue java.awt.Color
	 */
    public static java.awt.Color getColor(String key, java.awt.Color defaultValue) {
        final String colorHexa = getString(key, null);
        try {
            if (colorHexa != null) {
                return java.awt.Color.decode(colorHexa);
            } else {
                return defaultValue;
            }
        } catch (final NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
	 * R�cup�re un double dans le fichier (avec chargement si n�cessaire).
	 *
	 * @return double
	 * @param key String
	 * @param defaultValue double
	 */
    public static double getDouble(String key, double defaultValue) {
        try {
            return Double.parseDouble(getString(key, String.valueOf(defaultValue)));
        } catch (final NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
	 * R�cup�re un int dans le fichier (avec chargement si n�cessaire).
	 *
	 * @return int
	 * @param key String
	 * @param defaultValue int
	 */
    public static int getInt(String key, int defaultValue) {
        try {
            return Integer.parseInt(getString(key, String.valueOf(defaultValue)));
        } catch (final NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
	 * R�cup�re une String dans le fichier (avec chargement si n�cessaire).
	 *
	 * @return String
	 * @param key String
	 * @param defaultValue String
	 */
    public static String getString(String key, String defaultValue) {
        try {
            String result = System.getProperty("mswing." + key);
            if (result != null) {
                result = result.trim();
            }
            if (result == null || result.length() == 0) {
                result = resourceBundle.getString(key);
                if (result != null) {
                    result = result.trim();
                }
            }
            return result != null && result.length() != 0 ? result : defaultValue;
        } catch (final MissingResourceException e) {
            MUtilities.printStackTrace(e);
            return defaultValue;
        }
    }

    /**
	 * Recharge le fichier de configuration (et la configuration des boutons).
	 */
    public static void reload() {
        MParameters.getButtonsConfig().clear();
        resourceBundle = ResourceBundle.getBundle(FILE_NAME);
    }
}
