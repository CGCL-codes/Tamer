package com.ojt;

import org.apache.log4j.Logger;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 * Configuration d'OJT.
 * @author RGu
 * @since 20 ao�t 2009 (RGu) : Cr�ation
 */
public class OJTConfiguration {

    private static final String CONFIG_FILE_NAME = "config.xml";

    private static final String DEFAULT_SERIAL_PORT = "COM1";

    private static final String DEFAULT_OO_LAUNCH_COMMAND = "C:\\Program Files\\OpenOffice.org 3\\program\\soffice.exe";

    private static final String DEFAULT_EXPORT_FROM_MODELS = Boolean.TRUE.toString();

    private static final String DEFAULT_MODEL_FILE_PATH = "";

    private static final String DEFAULT_WEIGHING_POST = "1";

    private static final String DEFAULT_ADD_WEIGHING_SAVE_DIRECTORY_PATH = "";

    private static final String DEFAULT_SHOW_WARN_ON_EMPTY_WEIGHT = Boolean.FALSE.toString();

    private static final String DEFAULT_GENERATE_SOURCE_FILE_MENU = Boolean.TRUE.toString();

    private static final String DEFAULT_WEIGHING_MENU = Boolean.TRUE.toString();

    private static final String DEFAULT_SORT_MENU = Boolean.TRUE.toString();

    private static final String DEFAULT_WEIGHING_AND_SORT_MENU = Boolean.TRUE.toString();

    private static final String DEFAULT_COMPETITION_CATEGORIES = "";

    /** Port s�rie sur lequel est connect� la balance. */
    public static final String SERIAL_PORT = "SerialPort";

    /** Commande de lancement d'OpenOffice sur la machine. */
    public static final String OO_LAUNCH_COMMAND = "OOLaunchCommand";

    /** Cr�e les feuilles de poules � partir des mod�les */
    public static final String EXPORT_FROM_MODELS = "ExportFromModels";

    /** Cr�e les feuilles de poules � partir des mod�les */
    public static final String MODEL_FILE_PATH = "ModelFilePath";

    /** Num�ro de poste de pes�e */
    public static final String WEIGHING_POST = "WeighingPost";

    /** Chemin de sauvegarde suppl�mentaire des pes�es */
    public static final String ADD_WEIGHING_SAVE_DIRECTORY_PATH = "AddWeighingSaveDirectoryPath";

    public static final String SHOW_WARN_ON_EMPTY_WEIGHT = "ShowWarnningOnEmptyWeight";

    public static final String GENERATE_SOURCE_FILE_MENU = "GenerateSourceFileMenu";

    public static final String WEIGHING_MENU = "WeighingMenu";

    public static final String SORT_MENU = "SortMenu";

    public static final String WEIGHING_AND_SORT_MENU = "WeighingAndSortMenu";

    public static final String COMPETITION_CATEGORIES = "CompetitionCategories";

    private static final Logger LOGGER = Logger.getLogger(OJTConfiguration.class);

    /**
	 * Singleton de la configuration d'OJT
	 */
    private static OJTConfiguration instance;

    /**
	 * @return La configuration d'OJT, tir�e du fichier config.xml
	 */
    public static OJTConfiguration getInstance() {
        if (instance == null) {
            instance = new OJTConfiguration();
        }
        return instance;
    }

    private Properties props;

    /**
	 * La configuration d'OJT �tant un singleton, elle n'est instanci�e que par
	 * l'appel � la m�thode getInstance.
	 */
    private OJTConfiguration() {
        super();
        loadConfiguration();
    }

    /**
	 * @param propertyKey La clef de la propri�t� de configuration.
	 * @return La propri�t� correspondant � la clef.
	 */
    public String getProperty(final String propertyKey) {
        return props.getProperty(propertyKey);
    }

    /**
	 * @param key
	 * @param defaultValue valeur si la propri�t� n'existe pas
	 * @return la valeur de la propri�t� sous forme de boolean
	 */
    public boolean getPropertyAsBoolean(final String key, final boolean defaultValue) {
        if ((getProperty(key) != null) && !getProperty(key).isEmpty()) {
            return Boolean.valueOf(getProperty(key));
        }
        return defaultValue;
    }

    /**
	 * @param key
	 * @return la valeur de la propri�t� sous forme de boolean
	 */
    public boolean getPropertyAsBoolean(final String key) {
        return Boolean.valueOf(getProperty(key));
    }

    /**
	 * Permet d'ajouter ou de mettre � jour une propri�t�, le fichier sera
	 * �cras�
	 * @param propertyKey
	 * @param propertyValue
	 */
    public void updateProperty(final String propertyKey, final String propertyValue) {
        props.setProperty(propertyKey, propertyValue);
        storeConfiguration();
    }

    /**
	 * Charge la configuration d'OJT � partir du fichier config.xml.
	 */
    private void loadConfiguration() {
        try {
            props = new Properties();
            props.loadFromXML(new FileInputStream(CONFIG_FILE_NAME));
            checkConfiguration();
        } catch (final Exception ex) {
            LOGGER.error("Unable to load configuration, creation of the default one.", ex);
            initDefaultConfiguration();
        }
    }

    /**
	 * Initialise la configuration par d�faut.
	 */
    private void initDefaultConfiguration() {
        props = new Properties();
        props.put(SERIAL_PORT, DEFAULT_SERIAL_PORT);
        props.put(OO_LAUNCH_COMMAND, DEFAULT_OO_LAUNCH_COMMAND);
        props.put(EXPORT_FROM_MODELS, DEFAULT_EXPORT_FROM_MODELS);
        props.put(MODEL_FILE_PATH, DEFAULT_MODEL_FILE_PATH);
        props.put(WEIGHING_POST, DEFAULT_WEIGHING_POST);
        props.put(ADD_WEIGHING_SAVE_DIRECTORY_PATH, DEFAULT_ADD_WEIGHING_SAVE_DIRECTORY_PATH);
        props.put(SHOW_WARN_ON_EMPTY_WEIGHT, DEFAULT_SHOW_WARN_ON_EMPTY_WEIGHT);
        props.put(GENERATE_SOURCE_FILE_MENU, DEFAULT_GENERATE_SOURCE_FILE_MENU);
        props.put(WEIGHING_MENU, DEFAULT_WEIGHING_MENU);
        props.put(SORT_MENU, DEFAULT_SORT_MENU);
        props.put(WEIGHING_AND_SORT_MENU, DEFAULT_WEIGHING_AND_SORT_MENU);
        props.put(COMPETITION_CATEGORIES, DEFAULT_COMPETITION_CATEGORIES);
        storeConfiguration();
    }

    /**
	 * V�rifie que la configuration charg�e contient bien toutes les donn�es de
	 * configuration, et rajoute celles manquantes.
	 */
    private void checkConfiguration() {
        boolean configOK = true;
        if (getProperty(SERIAL_PORT) == null) {
            props.put(SERIAL_PORT, DEFAULT_SERIAL_PORT);
            configOK = false;
        }
        if (getProperty(OO_LAUNCH_COMMAND) == null) {
            props.put(OO_LAUNCH_COMMAND, DEFAULT_OO_LAUNCH_COMMAND);
            configOK = false;
        }
        if (getProperty(EXPORT_FROM_MODELS) == null) {
            props.put(EXPORT_FROM_MODELS, DEFAULT_EXPORT_FROM_MODELS);
            configOK = false;
        }
        if (getProperty(MODEL_FILE_PATH) == null) {
            props.put(MODEL_FILE_PATH, DEFAULT_MODEL_FILE_PATH);
            configOK = false;
        }
        if (getProperty(WEIGHING_POST) == null) {
            props.put(WEIGHING_POST, DEFAULT_WEIGHING_POST);
            configOK = false;
        }
        if (getProperty(ADD_WEIGHING_SAVE_DIRECTORY_PATH) == null) {
            props.put(ADD_WEIGHING_SAVE_DIRECTORY_PATH, DEFAULT_ADD_WEIGHING_SAVE_DIRECTORY_PATH);
            configOK = false;
        }
        if (getProperty(SHOW_WARN_ON_EMPTY_WEIGHT) == null) {
            props.put(SHOW_WARN_ON_EMPTY_WEIGHT, DEFAULT_SHOW_WARN_ON_EMPTY_WEIGHT);
            configOK = false;
        }
        if (getProperty(GENERATE_SOURCE_FILE_MENU) == null) {
            props.put(GENERATE_SOURCE_FILE_MENU, DEFAULT_GENERATE_SOURCE_FILE_MENU);
            configOK = false;
        }
        if (getProperty(WEIGHING_MENU) == null) {
            props.put(WEIGHING_MENU, DEFAULT_WEIGHING_MENU);
            configOK = false;
        }
        if (getProperty(SORT_MENU) == null) {
            props.put(SORT_MENU, DEFAULT_SORT_MENU);
            configOK = false;
        }
        if (getProperty(WEIGHING_AND_SORT_MENU) == null) {
            props.put(WEIGHING_AND_SORT_MENU, DEFAULT_WEIGHING_AND_SORT_MENU);
            configOK = false;
        }
        if (getProperty(COMPETITION_CATEGORIES) == null) {
            props.put(COMPETITION_CATEGORIES, DEFAULT_COMPETITION_CATEGORIES);
            configOK = false;
        }
        if (!configOK) {
            storeConfiguration();
        }
    }

    /**
	 * Stockage de la configuration.
	 */
    private void storeConfiguration() {
        try {
            final FileOutputStream out = new FileOutputStream(CONFIG_FILE_NAME);
            props.storeToXML(out, "Configuration de OJT");
            out.flush();
            out.close();
        } catch (final Exception ex) {
            LOGGER.error("Unable to store configuration.", ex);
        }
    }

    public static void main(final String[] args) {
        OJTConfiguration.getInstance();
    }
}
