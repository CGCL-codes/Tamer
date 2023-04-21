package thoto.jamyda.data;

import java.io.File;
import java.io.Serializable;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;
import thoto.jamyda.utils.GUIFactory;

public class AppData extends JLabel implements Serializable, Comparable<AppData> {

    private static final long serialVersionUID = -7769016934902298268L;

    private String appName;

    private String appIconPath;

    private String appPath;

    private String additionalDBCommands;

    private String dosboxConfigPath;

    private String remark;

    private String categoryPath;

    private boolean isAddedToFavorites;

    private Icon icon;

    private String filename;

    /**
	 * 
	 */
    public AppData() {
        setBorder(new EmptyBorder(5, 5, 5, 5));
    }

    /**
	 * Loads all data from the given file into this object
	 * 
	 * @param filename
	 */
    public boolean load(String filename) {
        try {
            JProperties p = new JProperties(filename);
            if (p.exists()) {
                setAppName(p.getString("appName"));
                setAppIconPath(p.getString("appIconPath"));
                setAppPath(p.getString("appPath"));
                setAdditionalDBCommands(p.getString("additionalDosboxCommands"));
                setDosboxConfigPath(p.getString("dosboxConfigPath"));
                setRemark(p.getString("remark"));
                setCategoryPath(p.getString("categoryPath"));
                setAddedToFavorites(p.getBoolean("isInFavorites"));
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    /**
	 * Saves this object to a file.<br>
	 * 
	 */
    public void save() {
        JProperties p = new JProperties(getFilename());
        p.setString("appName", getAppName());
        p.setString("appIconPath", getAppIconPath());
        p.setString("appPath", getAppPath());
        p.setString("additionalDosboxCommands", getAdditionalDBCommands());
        p.setString("dosboxConfigPath", getDosboxConfigPath());
        p.setString("remark", getRemark());
        p.setString("categoryPath", getCategoryPath());
        p.setBoolean("isInFavorites", isAddedToFavorites());
        p.save();
    }

    /**
	 *
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
    public int compareTo(AppData o) {
        return getAppName().compareTo(o.getAppName());
    }

    /**
	 *
	 * @see java.awt.Component#toString()
	 */
    public String toString() {
        return getAppName();
    }

    /**
	 *
	 * @see javax.swing.JLabel#getIcon()
	 */
    public Icon getIcon() {
        return this.icon;
    }

    /**
	 *
	 * @see javax.swing.JLabel#getText()
	 */
    public String getText() {
        return getAppName();
    }

    /**
	 * Returns 
	 *
	 * @return the appName
	 */
    public String getAppName() {
        return appName;
    }

    /**
	 * Sets
	 *
	 * @param appName the appName to set
	 */
    public void setAppName(String appName) {
        this.appName = appName;
        this.filename = AppConstants.DIR_APPS_CONFIG + appName + AppConstants.FILE_SUFFIX_JAMYDA;
    }

    /**
	 * Returns 
	 *
	 * @return the appIconPath
	 */
    public String getAppIconPath() {
        return appIconPath;
    }

    /**
	 * Sets
	 *
	 * @param appIconPath the appIconPath to set
	 */
    public void setAppIconPath(String appIconPath) {
        this.appIconPath = appIconPath;
        this.icon = null;
        if (appIconPath != null && appIconPath.trim().length() > 0) {
            File f = new File(appIconPath);
            if (f.exists()) this.icon = new ImageIcon(appIconPath);
        }
        if (this.icon == null) this.icon = GUIFactory.getDefaultAppIcon();
    }

    /**
	 * Returns 
	 *
	 * @return the appPath
	 */
    public String getAppPath() {
        return appPath;
    }

    /**
	 * Sets
	 *
	 * @param appPath the appPath to set
	 */
    public void setAppPath(String appPath) {
        this.appPath = appPath;
    }

    /**
	 * Returns 
	 *
	 * @return the dosboxConfigPath
	 */
    public String getDosboxConfigPath() {
        return dosboxConfigPath;
    }

    /**
	 * Sets
	 *
	 * @param dosboxConfigPath the dosboxConfigPath to set
	 */
    public void setDosboxConfigPath(String dosboxConfigPath) {
        this.dosboxConfigPath = dosboxConfigPath;
    }

    /**
	 * Returns 
	 *
	 * @return the remark
	 */
    public String getRemark() {
        return remark;
    }

    /**
	 * Sets
	 *
	 * @param remark the remark to set
	 */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
	 * Returns 
	 *
	 * @return the filename
	 */
    public String getFilename() {
        return filename;
    }

    /**
	 * Returns 
	 *
	 * @return the additionalDBCommands
	 */
    public String getAdditionalDBCommands() {
        return additionalDBCommands;
    }

    /**
	 * Sets
	 *
	 * @param additionalDBCommands the additionalDBCommands to set
	 */
    public void setAdditionalDBCommands(String additionalDBCommands) {
        this.additionalDBCommands = additionalDBCommands;
    }

    /**
	 * Returns 
	 *
	 * @return the categoryPath
	 */
    public String getCategoryPath() {
        return categoryPath;
    }

    /**
	 * Sets
	 *
	 * @param categoryPath the categoryPath to set
	 */
    public void setCategoryPath(String categoryPath) {
        this.categoryPath = categoryPath;
    }

    /**
	 * Returns 
	 *
	 * @return the isAddedToFavorites
	 */
    public boolean isAddedToFavorites() {
        return isAddedToFavorites;
    }

    /**
	 * Sets
	 *
	 * @param isAddedToFavorites the isAddedToFavorites to set
	 */
    public void setAddedToFavorites(boolean isAddedToFavorites) {
        this.isAddedToFavorites = isAddedToFavorites;
    }
}
