package updater;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

/**
 *
 * @author Dominik Schaufelberger <dominik.schaufelberger@web.de>
 */
public class UpdaterConstants {

    private UpdaterConstants() {
    }

    public static final String CONFIG_FILE_NAME = "config.properties";

    public static final String CONFIG_FILE_URL = "." + File.separatorChar + "resources" + File.separatorChar + "config.properties";

    public static final String COOKBOOK_PROGRAM_PATH = "." + File.separatorChar + "Kochbuch.jar";

    public static final String URL_LATEST_VERSION_FILE = "http://sourceforge.net/projects/kochbuchsoft/files/version.txt";

    public static final String URL_LATEST_VERSION_ZIP_PREFIX = "http://sourceforge.net/projects/kochbuchsoft/files/Kochbuch/Kochbuch v";

    public static final String TMP_FILE_PREFIX = "cook_";

    public static final String TMP_FILE_SUFFIX = ".tmp";

    public static final String ZIP_FILE_SUFFIX = ".zip";

    public static final String PROPERTY_LOOKNFEEL_KEY = "user_set_lookNfeel";

    public static final String PROPERTY_VERSION_KEY = "version";

    public static final String PROPERTY_VERSION_SPLIT_REGEX = "\\.";

    public static final String URL_UNZIPPED_PREFIX = "resources" + File.separatorChar + "tmp" + File.separatorChar + "Kochbuch v";

    public static final String RESOURCE_FOLDER = "." + File.separatorChar + "resources" + File.separatorChar;

    public static final String RECIPE_FOLDER = "." + File.separatorChar + "recipes" + File.separatorChar;

    public static final String IMAGE_FOLDER = "." + File.separatorChar + "images" + File.separatorChar;

    public static final String IMAGE_LOGO = "." + File.separatorChar + "images" + File.separatorChar + "logo.jpg";

    public static final String ICON_LOGO = "." + File.separatorChar + "icons" + File.separatorChar + "logo_icon.jpg";

    public static final String INFO_FOLDER = "." + File.separatorChar + "info" + File.separatorChar;

    public static final String TMP_FILE_FOLDER = "." + File.separatorChar + "resources" + File.separatorChar + "tmp";

    public static void initalizeLookNFeel() throws FileNotFoundException, IOException {
        String lookNFeel = null;
        Properties config = new Properties();
        FileReader reader = null;
        try {
            config.load(new FileReader(CONFIG_FILE_URL));
            lookNFeel = config.getProperty(PROPERTY_LOOKNFEEL_KEY);
        } finally {
            if (reader != null) reader.close();
        }
        LookAndFeelInfo[] lnf = UIManager.getInstalledLookAndFeels();
        if (lookNFeel != null) {
            for (LookAndFeelInfo lnfInfo : lnf) {
                if (lnfInfo.getName().equals(lookNFeel)) {
                    lookNFeel = lnfInfo.getClassName();
                }
            }
            try {
                UIManager.setLookAndFeel(lookNFeel);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
