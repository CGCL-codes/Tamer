package de.shandschuh.jaolt.core;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

public class Settings {

    private static final String SETTINGS_FILE = Directory.APPLICATIONDATA_DIR + File.separator + Constants.APPLICATION_NAME_FILE + ".properties";

    private static final String KEY_LANGUAGE = "language";

    private static final String KEY_NOLICENSEDIALOG = "nolicensedialog";

    private static final String KEY_DEVELOPERMODE = "developermode";

    private static final String KEY_LASTMEMBER = "lastmember";

    private static final String KEY_SERACHVERSION = "searchversion";

    private static final String KEY_SEARCHSTATICDATA = "searchstaticdata";

    private static final String KEY_LAFCLASSNAME = "lafclassname";

    private static final String KEY_BOUNDS = "bounds";

    private static final String KEY_COLUMNS = "columns";

    private static final String KEY_NOTIFYCANCEL = "notifycancel";

    private static final String KEY_PICTUREDIR = "picturedir";

    private Language language;

    private boolean dontShowLicenseJDialog;

    private String lastMemberFileName;

    private boolean searchNewVersion;

    private boolean searchNewStaticData;

    private boolean developerMode;

    private String lafClassName;

    private Map<String, Rectangle> bounds;

    private int[][] columnWidthsAndOrder;

    private boolean notifyCancel;

    private File pictureDir;

    public Settings() {
        Properties properties = new Properties();
        try {
            FileInputStream stream = new FileInputStream(SETTINGS_FILE);
            properties.load(stream);
            language = Language.getLanguage(getLanguageIso(properties.getProperty(KEY_LANGUAGE, "en")));
            stream.close();
        } catch (Exception e) {
            language = Language.getSystemLanguage();
        }
        dontShowLicenseJDialog = "true".equals(properties.getProperty(KEY_NOLICENSEDIALOG));
        lastMemberFileName = properties.getProperty(KEY_LASTMEMBER);
        developerMode = "true".equals(properties.getProperty(KEY_DEVELOPERMODE));
        searchNewVersion = "true".equals(properties.getProperty(KEY_SERACHVERSION, "true"));
        searchNewStaticData = "true".equals(properties.getProperty(KEY_SEARCHSTATICDATA, "true"));
        lafClassName = properties.getProperty(KEY_LAFCLASSNAME, "");
        bounds = new HashMap<String, Rectangle>();
        for (Object key : properties.keySet()) {
            if (key != null && key.toString().startsWith(KEY_BOUNDS)) {
                String boundsString = properties.getProperty(key.toString(), "fullscreen");
                Rectangle bounds = null;
                if (!"fullscreen".equals(boundsString)) {
                    try {
                        String[] values = boundsString.split("[,]");
                        bounds = new Rectangle(Integer.parseInt(values[0]), Integer.parseInt(values[1]), Integer.parseInt(values[2]), Integer.parseInt(values[3]));
                        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
                        if (dimension.width <= bounds.x + 10 || dimension.height <= bounds.y + 10) {
                            bounds = null;
                        }
                    } catch (Exception exception) {
                    }
                }
                this.bounds.put(key.toString(), bounds);
            }
        }
        String columnsString = properties.getProperty(KEY_COLUMNS, "");
        columnWidthsAndOrder = null;
        if (columnsString != null && columnsString.length() > 0) {
            try {
                String[] widths = columnsString.split("[;]");
                columnWidthsAndOrder = new int[widths.length][];
                for (int n = 0, i = widths.length; n < i; n++) {
                    String[] values = widths[n].split("[,]");
                    columnWidthsAndOrder[n] = new int[values.length];
                    for (int k = 0, l = values.length; k < l; k++) {
                        columnWidthsAndOrder[n][k] = Integer.parseInt(values[k]);
                    }
                }
            } catch (Exception exception) {
                columnWidthsAndOrder = null;
            }
        }
        notifyCancel = "true".equals(properties.getProperty(KEY_NOTIFYCANCEL, "false"));
        pictureDir = new File(properties.getProperty(KEY_PICTUREDIR, "."));
    }

    public String getLastMemberFileName() {
        return lastMemberFileName;
    }

    public void setLastMemberFileName(String lastMemberFileName) {
        this.lastMemberFileName = lastMemberFileName;
    }

    public boolean isDontShowLicenseJDialog() {
        return dontShowLicenseJDialog;
    }

    public void setDontShowLicenseJDialog(boolean dontShowLicenseJDialog) {
        this.dontShowLicenseJDialog = dontShowLicenseJDialog;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public void setSearchNewVersion(boolean searchNewVersion) {
        this.searchNewVersion = searchNewVersion;
    }

    public boolean isSearchNewVersion() {
        return searchNewVersion;
    }

    public void setSearchNewStaticData(boolean searchNewStaticData) {
        this.searchNewStaticData = searchNewStaticData;
    }

    public boolean isSearchNewStaticData() {
        return searchNewStaticData;
    }

    public void setDeveloperMode(boolean developerMode) {
        this.developerMode = developerMode;
    }

    public boolean isDeveloperMode() {
        return developerMode;
    }

    public void setLafClassName(String lafClassName) {
        this.lafClassName = lafClassName;
    }

    public String getLafClassName() {
        return lafClassName;
    }

    public void setBounds(Rectangle bounds) {
        setBounds(bounds, null);
    }

    public void setBounds(Rectangle bounds, Class<?> clazz) {
        this.bounds.put(KEY_BOUNDS + (clazz != null ? ":" + clazz.getName() : ""), bounds);
    }

    public Rectangle getBounds() {
        return getBounds(null);
    }

    public Rectangle getBounds(Class<?> clazz) {
        return bounds.get(KEY_BOUNDS + (clazz != null ? ":" + clazz.getName() : ""));
    }

    public int[][] getColumnWidthsAndOrder() {
        return columnWidthsAndOrder;
    }

    public void setColumnWidthsAndOrder(int[][] columnWidthsAndOrder) {
        this.columnWidthsAndOrder = columnWidthsAndOrder;
    }

    public boolean isNotifyCancel() {
        return notifyCancel;
    }

    public void setNotifyCancel(boolean notifyCancel) {
        this.notifyCancel = notifyCancel;
    }

    public void setPictureDir(File pictureDir) {
        this.pictureDir = pictureDir;
    }

    public File getPictureDir() {
        return pictureDir;
    }

    public void save() throws IOException {
        Properties properties = new Properties();
        properties.setProperty(KEY_LANGUAGE, language.getLanguageIso().toLowerCase());
        properties.setProperty(KEY_NOLICENSEDIALOG, Boolean.toString(dontShowLicenseJDialog));
        properties.setProperty(KEY_LASTMEMBER, lastMemberFileName != null ? lastMemberFileName : "");
        properties.setProperty(KEY_DEVELOPERMODE, Boolean.toString(developerMode));
        properties.setProperty(KEY_SERACHVERSION, Boolean.toString(searchNewVersion));
        properties.setProperty(KEY_SEARCHSTATICDATA, Boolean.toString(searchNewStaticData));
        properties.setProperty(KEY_LAFCLASSNAME, lafClassName != null ? lafClassName : "");
        for (Entry<String, Rectangle> entry : bounds.entrySet()) {
            properties.setProperty(entry.getKey(), entry.getValue() != null ? entry.getValue().x + "," + entry.getValue().y + "," + entry.getValue().width + "," + entry.getValue().height : "fullscreen");
        }
        if (columnWidthsAndOrder != null) {
            StringBuilder builder = new StringBuilder();
            for (int n = 0, i = columnWidthsAndOrder.length; n < i; n++) {
                if (n > 0) {
                    builder.append(';');
                }
                for (int k = 0, l = columnWidthsAndOrder[n] != null ? columnWidthsAndOrder[n].length : 0; k < l; k++) {
                    if (k > 0) {
                        builder.append(',');
                    }
                    builder.append(columnWidthsAndOrder[n][k]);
                }
            }
            properties.setProperty(KEY_COLUMNS, builder.toString());
        }
        properties.setProperty(KEY_NOTIFYCANCEL, Boolean.toString(notifyCancel));
        properties.setProperty(KEY_PICTUREDIR, pictureDir != null ? pictureDir.toString() : "");
        FileOutputStream stream = new FileOutputStream(SETTINGS_FILE);
        properties.store(stream, null);
        stream.close();
    }

    private static String getLanguageIso(String property) {
        try {
            int code = Integer.parseInt(property);
            switch(code) {
                case 1:
                    return "de";
                case 2:
                    return "en";
                case 5:
                    return "fr";
                case 6:
                    return "it";
                case 13:
                    return "es";
                case 16:
                    return "jp";
                default:
                    return "en";
            }
        } catch (Exception e) {
            return property;
        }
    }
}
