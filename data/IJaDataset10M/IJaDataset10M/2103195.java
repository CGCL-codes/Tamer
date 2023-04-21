package org.epoline.mice;

import java.awt.Color;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.apache.log4j.Logger;

/**
 * Information holder on a Mice application or tool (such as a logon dialog).
 * Used for getting information about the application or tool without starting it.
 * The methods are public since new InfoBeans must be declarable outside
 * the org.epoline.mice package. 
 * <BR>
 * The InfoBean describes the application or tool in terms of its name and icon.
 *  <br><br>
 * This class also manages lists of available ApplicationInfobeans and PreferencesInfoBeans.
 * It also manages the plugged in LogonInfoBean, LogoffInfoBean and TeamSelectInfoBean.
 * <br><br>
 * Is part of the three-component architecture: InfoBean, MiceFrame and PreferencesPanel.
 *
 * @see org.epoline.mice.MiceFrame
 * @see org.epoline.mice.PreferencesPanel
 */
public abstract class InfoBean implements Comparable {

    /** can be returned by getCategory(). Intended for products that are not part of the business of EPO, 
	    but part of the supporting epoline technology. **/
    public static final String SYSTEM = "System";

    /** can be returned by getCategory(). Intended for products with an unofficial status. **/
    public static final String TEMP = "Experimental";

    /** can be returned by getCategory(). Intended for all default products. **/
    public static final String PRODUCT = "Products";

    /** can be returned by getCategory(). Intended for all products that should never be started by the user directly. **/
    public static final String HIDDEN = "Hidden";

    /** any application group must have a unique name **/
    private static final String DEFAULF_APPLICATION_GROUP_NAME = "Phoenix";

    private ImageIcon _smallImageIcon;

    private ImageIcon _imageIcon;

    private Map _parameters = new HashMap();

    /** all application infobeans **/
    private static List _applicationInfoBeans;

    /** all settings infobeans  **/
    private static List _settingsInfoBeans;

    /** the logon entry infobean **/
    private static LogonEntryInfoBean _logonEntryInfoBean;

    /** the skin infobean **/
    private static SkinInfoBean _skinInfoBean;

    /** the team select infobeans **/
    private static List _teamSelectInfoBeans = new ArrayList();

    /** prevent accessing the infoBean list concurrently **/
    private static Object _infoBeanLock = new Object();

    /** Concurrent access to properties is prevented by propertyLock **/
    private static Object _propertyLock = new Object();

    /** the properties read from epoline.ini */
    private static Hashtable _properties;

    private static File _iniFile;

    private static Logger _logger = Logger.getLogger(InfoBean.class);

    /**
	 * @see interface Comparable
	 */
    public int compareTo(Object other) {
        if ((other instanceof InfoBean) == false) {
            return 0;
        }
        return getName().compareTo(((InfoBean) other).getName());
    }

    /** 
	 * Only for testing purposes 
	 **/
    static void dropProperties() {
        _properties = null;
    }

    /**
	 * Is creating a list of info beans from the madras infobean list file. 
	 * But be careful, not all classes in the filelist will be part of the
	 * application info beans:
	 * Exceptions:	
	 * 					SkinInfoBean
	 * 					LogonEntryInfoBean
	 * 					LogonInfoBean
	 * 					TeamSelectInfoBean
	 * 
	 * Is creating also a list of the SettingInfoBean's
	 */
    private static void ensureInfoBeans() {
        if (_applicationInfoBeans == null) {
            synchronized (_infoBeanLock) {
                String fileName = Main.getInfoBeansFileName();
                String infoBeanName = null;
                _applicationInfoBeans = new ArrayList();
                _settingsInfoBeans = new ArrayList();
                try {
                    BufferedReader br = new BufferedReader(new FileReader(fileName));
                    for (int i = 0; ; i++) {
                        String line = br.readLine();
                        if (line == null) {
                            break;
                        }
                        infoBeanName = line.trim();
                        String parameterString = "";
                        int index = infoBeanName.indexOf(" ");
                        if (index >= 0) {
                            parameterString = infoBeanName.substring(index + 1);
                            infoBeanName = infoBeanName.substring(0, index);
                        }
                        if (infoBeanName.length() > 0 && infoBeanName.charAt(0) != '#') {
                            try {
                                Object instance = Class.forName(infoBeanName).newInstance();
                                if ((instance instanceof InfoBean) == false) {
                                    _logger.warn("File " + fileName + ": unexpected class: " + infoBeanName);
                                    continue;
                                }
                                InfoBean infoBean = (InfoBean) instance;
                                infoBean.parseParameters(parameterString);
                                if (_logger.isDebugEnabled() == true) {
                                    _logger.debug(fileName + ": " + infoBean.getName() + ", " + infoBean.getCategory());
                                }
                                if (infoBean instanceof SkinInfoBean) {
                                    _skinInfoBean = (SkinInfoBean) infoBean;
                                    continue;
                                }
                                if (infoBean instanceof LogonEntryInfoBean) {
                                    _logonEntryInfoBean = (LogonEntryInfoBean) infoBean;
                                    continue;
                                }
                                if (infoBean instanceof LogonInfoBean) {
                                    ApplicationGroup ag = ApplicationGroup.getByName(infoBean.getApplicationGroupName());
                                    ag.setLogonInfoBean((LogonInfoBean) infoBean);
                                    continue;
                                }
                                if (infoBean instanceof TeamSelectInfoBean) {
                                    ApplicationGroup ag = ApplicationGroup.getByName(infoBean.getApplicationGroupName());
                                    ag.setTeamSelectInfoBean((TeamSelectInfoBean) infoBean);
                                    _teamSelectInfoBeans.add(infoBean);
                                    continue;
                                }
                                if (infoBean instanceof ApplicationInfoBean) {
                                    _applicationInfoBeans.add(infoBean);
                                }
                                if (infoBean instanceof SettingsInfoBean) {
                                    _settingsInfoBeans.add(infoBean);
                                    continue;
                                }
                            } catch (ClassNotFoundException ex) {
                                _logger.error("File " + fileName + ": could not find class: " + infoBeanName);
                            } catch (InstantiationException ex) {
                                _logger.error("File " + fileName + ": could not instantiate class: " + infoBeanName);
                            } catch (IllegalAccessException ex) {
                                _logger.error("File " + fileName + ": could not access class: " + infoBeanName);
                            }
                        }
                    }
                    br.close();
                } catch (FileNotFoundException e) {
                    _logger.error("File " + fileName + ": not found", e);
                } catch (IOException e) {
                    _logger.error("File " + fileName + ": error when reading", e);
                }
            }
        }
    }

    private void ensureProperties() {
        if (_properties == null) {
            try {
                readProperties();
            } catch (Exception e) {
                _logger.error("Error reading properties from file " + Main.getIniFileName(), e);
            }
        }
    }

    final ApplicationGroup getApplicationGroup() {
        return ApplicationGroup.getByName(getApplicationGroupName());
    }

    /** 
	 * returns the name of the application group. To be overridden by subclasses.
	 * Temporarily returns "Phoenix"
	 */
    public String getApplicationGroupName() {
        return DEFAULF_APPLICATION_GROUP_NAME;
    }

    /** 
	 * return all application infobeans listed in epoline.lst 
	 **/
    public static List getApplicationInfoBeans() {
        ensureInfoBeans();
        return _applicationInfoBeans;
    }

    /** 
	 * short description of the bean, for instance suitable as a 
	 * tooltip for the icon. 
	 * Returns null if not subclassed. 
	 */
    public String getBeanInfo() {
        return null;
    }

    /** 
	 * the category this belongs to (see fields docmumentation). 
	 */
    public abstract String getCategory();

    public Color getColorApplication() {
        return Color.black;
    }

    public Color getColorApplicationFamily() {
        return Color.white;
    }

    public Color getColorApplicationType() {
        return Color.black;
    }

    /** @deprecated */
    public Icon getIcon() {
        return null;
    }

    /** 
	 * returns the ImageIcon identifying the application to the user. 
	 * By default uses getImageIconFilePath().
	 */
    public ImageIcon getImageIcon() {
        if (_imageIcon == null) {
            _imageIcon = MiceGlobal.getIcon(getImageIconFilePath());
        }
        return _imageIcon;
    }

    /** 
	 * returns the file name for the ImageIcon 
	 */
    public String getImageIconFileName() {
        return "Madras32.gif";
    }

    /** 
	 * returns the file path for the ImageIcon 
	 * By default returns "<package dir>/images/getImageIconFileName"
	 */
    public String getImageIconFilePath() {
        return getRelativePathForIcon();
    }

    /** 
	 * key used for the preferences. By default returns the name of the class. 
	 */
    public String getKey() {
        return getClass().toString();
    }

    public AbstractAction getLaunchAction() {
        return null;
    }

    /** 
	 * return logon entry infobean listed in epoline.lst 
	 */
    public static LogonEntryInfoBean getLogonEntryInfoBean() {
        ensureInfoBeans();
        return _logonEntryInfoBean;
    }

    /** 
	 * identifies the product to the user. By default returns getKey(). 
	 */
    public String getName() {
        return getKey();
    }

    /** 
	 * returns a named parameter associated with this InfoBean. 
	 * returns null if no definition.
	 * Parameters are specified in the file that lists the infobeans.
	 * 
	 * @see Main.setIniFileName()
	 */
    public String getParameter(String name) {
        return (String) _parameters.get(name);
    }

    /** 
	 * Returns a property associated with this InfoBean. 
	 * returns null if no definition, or if no proper Ini file was found.
	 * @see Main.setIniFileName()
	 */
    public Object getProperty(String label) {
        ensureProperties();
        if (_properties == null) {
            return null;
        }
        String key = getKey() + "." + label;
        synchronized (_propertyLock) {
            return _properties.get(key);
        }
    }

    /** 
	 * returns a property associated with this InfoBean. 
	 * returns the given defaultValue if no definition, or if no proper Ini file was found.
	 * @see Main.setIniFileName()
	 */
    public Object getProperty(String label, Object defaultValue) {
        Object value = getProperty(label);
        if (value == null) {
            value = defaultValue;
        }
        return value;
    }

    /** 
	 * Returns a relative path for an image at getImageIconFileName().
	 * The relative path is in the /images subdirectory 
	 * of the MiceGlobal.getResourceDirectoryName() of this InfoBean.
	 * By default this is "<package dir>/images/getImageIconFileName()"
	 */
    public String getRelativePathForIcon() {
        return MiceGlobal.getRelativePathForIcon(this, getImageIconFileName());
    }

    /** 
	 * Returns a relative path for an image at getSmallImageIconFileName().
	 * The relative path is in the /images subdirectory 
	 * of the MiceGlobal.getResourceDirectoryName() of this InfoBean.
	 * By default this is "<package dir>/images/getImageIconFileName()"
	 */
    public String getRelativePathForSmallIcon() {
        return MiceGlobal.getRelativePathForIcon(this, getSmallImageIconFileName());
    }

    /** 
	 * return all settings infobeans listed in epoline.lst 
	 **/
    public static List getSettingsInfoBeans() {
        ensureInfoBeans();
        return _settingsInfoBeans;
    }

    /** 
	 * return skin infobean listed in epoline.lst 
	 */
    public static SkinInfoBean getSkinInfoBean() {
        ensureInfoBeans();
        return _skinInfoBean;
    }

    /** 
	 * returns an ImageIcon suitable for displaying in a title bar or menu. By default it is the getIcon in a scaled form. 
	 */
    public ImageIcon getSmallImageIcon() {
        if (_smallImageIcon == null) {
            String fileName = getSmallImageIconFileName();
            if (fileName == null) {
                ImageIcon imageIcon = getImageIcon();
                Image image = imageIcon.getImage();
                int w = imageIcon.getIconWidth();
                int h = imageIcon.getIconHeight();
                if (h > 16) {
                    w = (16 * w) / h;
                    h = 16;
                }
                Image smallImage = image.getScaledInstance(w, h, Image.SCALE_SMOOTH);
                _smallImageIcon = new ImageIcon(smallImage);
            } else {
                _smallImageIcon = MiceGlobal.getIcon(getSmallImageIconFilePath());
            }
        }
        return _smallImageIcon;
    }

    /** 
	 * returns the file name for the ImageIcon 
	 */
    public String getSmallImageIconFileName() {
        return "Madras16.gif";
    }

    /** 
	 * returns the file path for the small ImageIcon 
	 * By default returns "<package dir>/images/getImageIconFileName"
	 */
    public String getSmallImageIconFilePath() {
        return getRelativePathForSmallIcon();
    }

    /** 
	 * return team select infobeans listed in epoline.lst 
	 */
    public static List getTeamSelectInfoBeans() {
        ensureInfoBeans();
        return _teamSelectInfoBeans;
    }

    /**
	 *
	 */
    public final UserProfile getUserProfile() {
        return getApplicationGroup().getUserProfile();
    }

    /**
	 * Parse the given parameter sting, e.g., "a=b version=4.1 emptyParam"
	 */
    void parseParameters(String parameterString) {
        StringTokenizer st = new StringTokenizer(parameterString.trim());
        while (st.hasMoreTokens()) {
            String paramNameValue = st.nextToken();
            int index = paramNameValue.indexOf("=");
            if (index < 0) {
                _parameters.put(paramNameValue, "");
            } else {
                String name = paramNameValue.substring(0, index);
                String value = paramNameValue.substring(index + 1);
                _parameters.put(name, value);
            }
        }
    }

    static void readProperties() throws Exception {
        synchronized (_propertyLock) {
            try {
                _iniFile = new File(Main.getIniFileName());
                FileInputStream fis = new FileInputStream(_iniFile);
                ObjectInputStream in = new ObjectInputStream(fis);
                _properties = (Hashtable) in.readObject();
                in.close();
            } catch (Exception e) {
                try {
                    _iniFile = new File(Main.getIniFileBakName());
                    FileInputStream fis = new FileInputStream(_iniFile);
                    ObjectInputStream in = new ObjectInputStream(fis);
                    _properties = (Hashtable) in.readObject();
                    in.close();
                } catch (Exception e1) {
                    _properties = new Hashtable();
                    throw e1;
                }
            }
        }
    }

    /** 
	 * removes a property. 
	 */
    public void removeProperty(String label) {
        ensureProperties();
        String key = (getKey() + "." + label).intern();
        synchronized (_propertyLock) {
            _properties.remove(key);
        }
    }

    public boolean requiresLogon() {
        return false;
    }

    /** 
	 * Sets a property for this InfoBean
	 * Does nothing if no proper Ini file was found.
	 * 
	 * @see Main.setIniFileName()
	 */
    public void setProperty(String label, Object value) {
        ensureProperties();
        if (_properties == null) {
            return;
        }
        String key = (getKey() + "." + label).intern();
        synchronized (_propertyLock) {
            _properties.put(key, value);
        }
    }

    static void writeProperties() throws IOException {
        synchronized (_propertyLock) {
            File iniTmpFile = new File(Main.getIniFileTmpName());
            FileOutputStream fos = new FileOutputStream(iniTmpFile);
            ObjectOutputStream out = new ObjectOutputStream(fos);
            out.writeObject(_properties);
            out.flush();
            out.close();
            fos.flush();
            fos.close();
            File iniBakFile = new File(Main.getIniFileBakName());
            if (iniBakFile.delete() == false) {
            }
            if (_iniFile.renameTo(iniBakFile) == false) {
                _logger.error("Could not rename " + _iniFile + " to " + iniBakFile);
            }
            _iniFile = new File(Main.getIniFileName());
            if (iniTmpFile.renameTo(_iniFile) == false) {
                _logger.error("Could not rename " + iniTmpFile + " to " + _iniFile);
            }
        }
    }
}
