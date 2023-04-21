package de.tobiasmaasland.voctrain.client.options;

import java.util.Properties;
import org.apache.log4j.Logger;
import de.tobiasmaasland.voctrain.client.util.Preferences;
import de.tobiasmaasland.voctrain.client.util.PreferencesConstants;
import fi.mmm.yhteinen.swing.core.YModel;

public class OptionsModel extends YModel {

    private static Logger log = Logger.getLogger(OptionsModel.class);

    private int file1days = Integer.parseInt(Preferences.getInstance().getKey(PreferencesConstants.FILE_1_DAYS));

    private int file2days = Integer.parseInt(Preferences.getInstance().getKey(PreferencesConstants.FILE_2_DAYS));

    private int file3days = Integer.parseInt(Preferences.getInstance().getKey(PreferencesConstants.FILE_3_DAYS));

    private int file4days = Integer.parseInt(Preferences.getInstance().getKey(PreferencesConstants.FILE_4_DAYS));

    private int file5days = Integer.parseInt(Preferences.getInstance().getKey(PreferencesConstants.FILE_5_DAYS));

    private String theme = Preferences.getInstance().getKey(PreferencesConstants.THEME_CLASS);

    public Properties getPreferences() {
        Properties retProps = Preferences.getInstance().getPreferences();
        retProps.put(PreferencesConstants.FILE_1_DAYS, Integer.toString(file1days));
        retProps.put(PreferencesConstants.FILE_2_DAYS, Integer.toString(file2days));
        retProps.put(PreferencesConstants.FILE_3_DAYS, Integer.toString(file3days));
        retProps.put(PreferencesConstants.FILE_4_DAYS, Integer.toString(file4days));
        retProps.put(PreferencesConstants.FILE_5_DAYS, Integer.toString(file5days));
        if (!(theme == null)) {
            retProps.put(PreferencesConstants.THEME_CLASS, theme);
        } else {
            log.warn("theme is null. Setting default theme/");
            retProps.put(PreferencesConstants.THEME_CLASS, "com.jgoodies.looks.plastic.theme.SkyRed");
        }
        return retProps;
    }

    /**
	 * @return the theme
	 */
    public String getTheme() {
        return theme;
    }

    /**
	 * @param inTheme
	 *            the theme to set
	 */
    public void setTheme(String inTheme) {
        this.theme = inTheme;
    }

    /**
	 * Standard constructor.
	 */
    public OptionsModel() {
    }

    /**
	 * @return the file1days
	 */
    public int getFile1days() {
        return file1days;
    }

    /**
	 * @param inFile1days
	 *            the file1days to set
	 */
    public void setFile1days(int inFile1days) {
        this.file1days = inFile1days;
    }

    /**
	 * @return the file2days
	 */
    public int getFile2days() {
        return file2days;
    }

    /**
	 * @param inFile2days
	 *            the file2days to set
	 */
    public void setFile2days(int inFile2days) {
        this.file2days = inFile2days;
    }

    /**
	 * @return the file3days
	 */
    public int getFile3days() {
        return file3days;
    }

    /**
	 * @param inFile3days
	 *            the file3days to set
	 */
    public void setFile3days(int inFile3days) {
        this.file3days = inFile3days;
    }

    /**
	 * @return the file4days
	 */
    public int getFile4days() {
        return file4days;
    }

    /**
	 * @param inFile4days
	 *            the file4days to set
	 */
    public void setFile4days(int inFile4days) {
        this.file4days = inFile4days;
    }

    /**
	 * @return the file5days
	 */
    public int getFile5days() {
        return file5days;
    }

    /**
	 * @param inFile5days
	 *            the file5days to set
	 */
    public void setFile5days(int inFile5days) {
        this.file5days = inFile5days;
    }
}
