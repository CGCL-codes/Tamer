package com.ek.mitapp;

import org.apache.log4j.*;
import java.io.*;
import java.text.*;
import java.util.*;
import com.ek.mitapp.ui.util.UISettings;
import com.ek.mitapp.util.*;

/**
 * TODO: Class description.
 * <br>
 * Id: $Id$
 *
 * @author dirwin
 */
public class AppSettings {

    /**
	 * Define the root logger.
	 */
    private static Logger logger = Logger.getLogger(AppSettings.class.getName());

    /**
	 * Create a default application settings object.
	 */
    private static AppSettings defaultAppSettings;

    /**
	 * File separator.
	 */
    public static final String SEPARATOR = System.getProperty("file.separator");

    /**
	 * Defines the working directory.
	 */
    private static String workingFolder;

    /**
	 * Output folder
	 */
    private static final String defaultOutputFolder = SEPARATOR + "output";

    private static String outputFolder = defaultOutputFolder;

    /**
	 * Resources folder
	 */
    private static final String resourcesFolder = SEPARATOR + "resources";

    /**
	 * Input folder
	 */
    private static final String inputFolder = resourcesFolder + SEPARATOR + "input";

    /**
	 * Template folder
	 */
    private static final String templateFolder = inputFolder + SEPARATOR + "template";

    /**
	 * Images folder.
	 */
    private static final String imagesFolder = "resources" + SEPARATOR + "images";

    private static final String configFolder = "resources" + SEPARATOR + "conf";

    /**
	 * Excel application related.
	 */
    private static final String excelAppName = "excel.exe";

    private static String excelAppPath = KnownFolders.Excel11.getFolder() + AppSettings.SEPARATOR + excelAppName;

    /**
	 * Known locations of the Microsoft Excel application.
	 */
    private static enum KnownFolders {

        Office("C:\\Program Files\\Microsoft Office"), Excel10("C:\\Program Files\\Microsoft Office\\OFFICE10"), Excel11("C:\\Program Files\\Microsoft Office\\OFFICE11");

        /**
		 * The full path of the Excel application.
		 */
        private String folder;

        /**
		 * Default constructor.
		 * 
		 * @param folder
		 */
        private KnownFolders(String folder) {
            this.folder = folder;
        }

        /**
		 * Get the full path of the location.
		 * @return
		 */
        public String getFolder() {
            return folder;
        }

        /**
		 * @see java.lang.Object#toString()
		 */
        @Override
        public String toString() {
            return getFolder();
        }
    }

    /**
	 * Date format.
	 */
    public static final SimpleDateFormat defaultDateFormat = new SimpleDateFormat("yyyyMMddkkmm");

    /**
	 * Currency format.
	 */
    public static final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();

    /**
	 * Define the version of the software.
	 */
    private static final String buildFile = "jexcel.build.number";

    private static final String KEY_BUILD_ID = "build.number";

    private static final String majorVersion = "0";

    private static final String minorVersion = "1";

    private static String buildId;

    /**
	 * UISettings object
	 */
    private static UISettings defaultUISettings;

    static {
        initializeVersionInformation();
        initializeExcelAppLocation();
    }

    /**
	 * Default non-instantiable constructor.
	 */
    public AppSettings() {
    }

    /**
	 * Get the default application settings.
	 *
	 * @return The default application settings
	 */
    public static synchronized AppSettings getDefaultSettings() {
        if (defaultAppSettings == null) {
            defaultAppSettings = new AppSettings();
            PropertyConfigurator.configure(defaultAppSettings.getConfigFolder() + SEPARATOR + "log4j.properties");
            defaultUISettings = UISettings.loadSettings(defaultAppSettings);
            workingFolder = new File("").getAbsolutePath();
            printDefaultSettings();
        }
        return defaultAppSettings;
    }

    /**
	 * Print the default settings information.
	 */
    private static void printDefaultSettings() {
        logger.debug("Printing default settings...");
        logger.debug("  Working folder: " + workingFolder);
        logger.debug("  Input folder: " + inputFolder);
        logger.debug("  Output folder: " + outputFolder);
        logger.debug("  Template folder: " + templateFolder);
        logger.debug("  Images folder: " + imagesFolder);
        logger.debug("  Config folder: " + configFolder);
        logger.debug("  Excel app path: " + excelAppPath);
    }

    /**
	 * Initialize the version information.
	 */
    private static void initializeVersionInformation() {
        Properties buildInfoProperties = new Properties();
        try {
            buildInfoProperties.load(ClassLoader.getSystemResourceAsStream(configFolder + SEPARATOR + buildFile));
            buildId = buildInfoProperties.getProperty(KEY_BUILD_ID);
        } catch (IOException ioe) {
            logger.error("Error loading version information: " + ioe.getMessage());
        }
    }

    /**
	 * Initialize the location of the Microsoft Excel application.
	 */
    private static void initializeExcelAppLocation() {
        File f = null;
        for (KnownFolders folder : KnownFolders.values()) {
            f = new File(folder.getFolder() + AppSettings.SEPARATOR + excelAppName);
            if (f.exists()) {
                excelAppPath = f.getAbsolutePath();
                break;
            }
        }
    }

    /**
	 * @return The UI settings
	 */
    public final UISettings getDefaultUISettings() {
        return defaultUISettings;
    }

    /**
	 * @return The working directory
	 */
    public final String getWorkingFolder() {
        return workingFolder;
    }

    /**
	 * @return The input folder
	 */
    public final String getInputFolder() {
        return inputFolder;
    }

    /**
	 * @return The input path
	 */
    public final String getInputPath() {
        return workingFolder + getInputFolder();
    }

    /**
	 * @return The template folder
	 */
    public final String getTemplateFolder() {
        return workingFolder + templateFolder;
    }

    /**
	 * @return The full path (directory + filename) to the Excel application.
	 */
    public final String getExcelAppPath() {
        return AppSettings.excelAppPath;
    }

    /**
	 * Set the full path of the Microsoft Excel application.
	 * 
	 * @param fullPath
	 * @throws IOException
	 */
    public final void setExcelappFullPath(String fullPath) throws IOException {
        FileUtils.checkFile(new File(fullPath));
        AppSettings.excelAppPath = fullPath;
    }

    /**
	 * 
	 * @return
	 */
    public final String getOfficePath() {
        return KnownFolders.Office.getFolder();
    }

    /**
	 * @return The output folder
	 */
    public String getOutputFolder() {
        return outputFolder;
    }

    /**
	 * @return The output path
	 */
    public String getOutputPath() {
        return workingFolder + getOutputFolder();
    }

    /**
	 * @return The images folder
	 */
    public String getImagesFolder() {
        return imagesFolder;
    }

    /**
	 * @return The buildId
	 */
    public static String getBuildId() {
        return buildId;
    }

    /**
	 * Get the major version.
	 * 
	 * @return The major version
	 */
    public static String getMajorVersion() {
        return majorVersion;
    }

    /**
	 * Get the minor version.
	 * 
	 * @return The minor version
	 */
    public static String getMinorVersion() {
        return minorVersion;
    }

    /**
	 * @return Return the version information string
	 */
    public static String getVersionString() {
        return "(v" + getMajorVersion() + "." + getMinorVersion() + " build " + getBuildId() + ")";
    }

    /**
	 * @return Returns the configFolder.
	 */
    public final String getConfigFolder() {
        return configFolder;
    }
}
