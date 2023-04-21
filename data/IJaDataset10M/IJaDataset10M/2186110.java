package jcontrol.conect.data.settings;

import jcontrol.conect.tools.*;
import java.util.*;
import java.lang.*;
import java.io.*;
import java.awt.*;
import javax.swing.*;

/** 
 * This class defines some settings for the Conect taken from a
 * configuration-file. The settings are made by the user.
 * @author W. Sauter
 * @version $Revision: 1.1.1.1 $
 */
public class Options {

    protected static final String OPTIONS_PROPERTIES = Settings.getConectUserPath() + Settings.SLASH + "options";

    protected static final String OPTIONS_PROPERTIES_DEFAULT = "jcontrol/conect/properties/data/Options.properties";

    /** Default options. */
    public static Options defaultOptions;

    /** defaults for the user interface. */
    public static UIDefaults uiDefaults;

    /** Number of group addresses. */
    public static int groupAddressLevels;

    /** Url of database. */
    public static String url;

    /** Name of JDBC driver. */
    public static String jdbcDriver;

    /** Name of database user. */
    public static String user;

    /** Users password for the database. */
    public static String password;

    /** Ask for database url at program start. */
    public static boolean askUrl;

    /** Ask for database jdbc driver at program start. */
    public static boolean askJdbcDriver;

    /** Ask for user and password at program start. */
    public static boolean askPassword;

    /** Ask for history by program end. */
    public static boolean askHistoryEntry;

    /** Font. */
    public static Font font;

    /** Font name. */
    public static String fontName;

    /** Font style. */
    public static int fontStyle;

    /** Font size. */
    public static int fontSize;

    /** Look and feel. */
    public static String lookAndFeel;

    /** Flag to backup project at program end. */
    public static boolean backupProject;

    /** Path to backup project at program end. */
    public static String backupProjectPath;

    /** Auto save periode. */
    public static int autoSavePeriode;

    /** Selected Language. */
    public static String selectedLanguage;

    /** Editor used for Comm./Test. */
    public static String editor;

    /** Check method for physical address. */
    public static String checkPhysicalAddress;

    /** Locale for Conect. */
    public static Locale ConectLocale;

    /** Baud rate. */
    public static int baudRate;

    /** COM port. */
    public static String comPort;

    /** Ask for copy flag. */
    public static boolean askCopy;

    /** Ask for delete flag. */
    public static boolean askDelete;

    /** Ask for move and link flag. */
    public static boolean askMoveLink;

    /** Component designation flag. */
    public static boolean componentDesignation;

    /** Copy with devices flag. */
    public static boolean copyWithDevices;

    /** Create physical address after insert flag. */
    public static boolean createPhysicalAddress;

    /** Edit physical address of device flag. */
    public boolean editPhysicalAddress;

    /** Group address strategy. */
    public static String groupAddressStrategy;

    /** Maximum number of devices. */
    public static int maxNumberDevices;

    /** Maximum number of lines. */
    public static int maxNumberLines;

    /** Properties for options. */
    protected static Properties optionProperties;

    /** Vector with filtered manufacturers. */
    protected static Vector filteredManufacturers;

    /** 
     * Constructor. 
     * Loads options from properties-file. First tries to load properties from
     * the settings.properties file. If file does not exist, default props are
     * loaded from JAR-file resources.
     */
    public Options() {
        InputStream in = null;
        optionProperties = new Properties();
        try {
            DebugDialog.print("Loading options from file: " + OPTIONS_PROPERTIES, 5);
            in = new FileInputStream(OPTIONS_PROPERTIES);
        } catch (FileNotFoundException fnfex) {
            DebugDialog.print("Properties for options not found. Using defaults.", 3);
            in = ClassLoader.getSystemResourceAsStream(OPTIONS_PROPERTIES_DEFAULT);
        } catch (IOException ioex) {
            DebugDialog.print("Properties for options cannot be loaded: " + ioex, 1);
            LocalizedError.display(ioex);
        }
        try {
            optionProperties.load(in);
        } catch (IOException ioex) {
            DebugDialog.print("Properties for options cannot be loaded: " + ioex, 1);
            LocalizedError.display(ioex);
        }
        uiDefaults = new UIDefaults();
        fontName = optionProperties.getProperty("option.font.name");
        fontSize = Integer.parseInt(optionProperties.getProperty("option.font.size"));
        fontStyle = Integer.parseInt(optionProperties.getProperty("option.font.style"));
        ;
        font = new Font(fontName, fontStyle, fontSize);
        setFont(font);
        groupAddressLevels = Integer.parseInt(optionProperties.getProperty("option.addresslevels"));
        url = optionProperties.getProperty("option.url");
        jdbcDriver = optionProperties.getProperty("option.jdbcdriver");
        user = optionProperties.getProperty("option.user");
        password = optionProperties.getProperty("option.passwd");
        askUrl = Boolean.valueOf(optionProperties.getProperty("option.askurl")).booleanValue();
        askJdbcDriver = Boolean.valueOf(optionProperties.getProperty("option.askjdbc")).booleanValue();
        askPassword = Boolean.valueOf(optionProperties.getProperty("option.askpasswd")).booleanValue();
        askHistoryEntry = Boolean.valueOf(optionProperties.getProperty("option.askhistory")).booleanValue();
        setLookAndFeel(optionProperties.getProperty("option.lookandfeel"));
        backupProject = Boolean.valueOf(optionProperties.getProperty("option.backupproject")).booleanValue();
        backupProjectPath = optionProperties.getProperty("option.backupproject.path");
        autoSavePeriode = Integer.parseInt(optionProperties.getProperty("option.autosave"));
        setSelectedLanguage(optionProperties.getProperty("option.language"));
        editor = optionProperties.getProperty("option.editor");
        checkPhysicalAddress = optionProperties.getProperty("option.physicaladdress");
        baudRate = Integer.parseInt(optionProperties.getProperty("option.baudrate"));
        comPort = optionProperties.getProperty("option.comport");
        askCopy = Boolean.valueOf(optionProperties.getProperty("option.askcopy")).booleanValue();
        askDelete = Boolean.valueOf(optionProperties.getProperty("option.askdelete")).booleanValue();
        askMoveLink = Boolean.valueOf(optionProperties.getProperty("option.askmovelink")).booleanValue();
        componentDesignation = Boolean.valueOf(optionProperties.getProperty("option.designation")).booleanValue();
        copyWithDevices = Boolean.valueOf(optionProperties.getProperty("option.copywithdevices")).booleanValue();
        createPhysicalAddress = Boolean.valueOf(optionProperties.getProperty("option.createaddress")).booleanValue();
        editPhysicalAddress = Boolean.valueOf(optionProperties.getProperty("option.editaddress")).booleanValue();
        groupAddressStrategy = optionProperties.getProperty("option.addressstrategy");
        maxNumberDevices = Integer.parseInt(optionProperties.getProperty("option.maxdevices"));
        maxNumberLines = Integer.parseInt(optionProperties.getProperty("option.maxlines"));
        filteredManufacturers = Settings.getFilteredManufacturers();
    }

    /**
     * Initialize the Options.
     */
    public static void initializeOptions() {
        defaultOptions = new Options();
    }

    public static Options getDefaultOptions() {
        return defaultOptions;
    }

    /** 
     * Store settings to a file..
     */
    public void storeOptions() {
        try {
            FileOutputStream out = new FileOutputStream(OPTIONS_PROPERTIES);
            DebugDialog.print("Storing options file: " + OPTIONS_PROPERTIES, 4);
            optionProperties.store(out, "Option Properties");
        } catch (IOException ioex) {
            DebugDialog.print("Error saving options file: " + OPTIONS_PROPERTIES + "\n" + ioex, 2);
            LocalizedError.display(ioex);
        }
    }

    /**
     * Update user interface manager.
     */
    protected void updateUIManager() {
        UIManager.put("Button.font", font);
        UIManager.put("ToggleButton.font", font);
        UIManager.put("RadioButton.font", font);
        UIManager.put("CheckBox.font", font);
        UIManager.put("ColorChooser.font", font);
        UIManager.put("ComboBox.font", font);
        UIManager.put("Label.font", font);
        UIManager.put("List.font", font);
        UIManager.put("MenuBar.font", font);
        UIManager.put("MenuItem.font", font);
        UIManager.put("RadioButtonMenuItem.font", font);
        UIManager.put("CheckBoxMenuItem.font", font);
        UIManager.put("Menu.font", font);
        UIManager.put("PopupMenuItem.font", font);
        UIManager.put("OptionPane.font", font);
        UIManager.put("Panel.font", font);
        UIManager.put("ProgressBar.font", font);
        UIManager.put("Viewport.font", font);
        UIManager.put("TabbedPane.font", font);
        UIManager.put("Table.font", font);
        UIManager.put("TableHeader.font", font);
        UIManager.put("TextField.font", font);
        UIManager.put("PasswordField.font", font);
        UIManager.put("TextArea.font", font);
        UIManager.put("TextPane.font", font);
        UIManager.put("EditorPane.font", font);
        UIManager.put("ToolBar.font", font);
        UIManager.put("ToolTip.font", font);
        UIManager.put("Tree.font", font);
    }

    /**
     * Get the value of font.
     * @return Value of font.
     */
    public Font getFont() {
        return font;
    }

    /**
     * Set the value of font.
     * @param v  Value to assign to font.
     */
    public void setFont(Font v) {
        this.font = v;
        updateUIManager();
    }

    /**
     * Get the value of fontName.
     * @return Value of fontName.
     */
    public String getFontName() {
        return fontName;
    }

    /**
     * Set the value of fontName.
     * @param v  Value to assign to fontName.
     */
    public void setFontName(String v) {
        this.fontName = v;
        optionProperties.setProperty("option.font.name", fontName);
        setFont(new Font(fontName, Font.PLAIN, fontSize));
    }

    /**
     * Get the value of fontSize.
     * @return Value of fontSize.
     */
    public int getFontSize() {
        return fontSize;
    }

    /**
     * Set the value of fontSize.
     * @param v  Value to assign to fontSize.
     */
    public void setFontSize(int v) {
        this.fontSize = v;
        optionProperties.setProperty("option.font.size", String.valueOf(fontSize));
        setFont(new Font(fontName, Font.PLAIN, fontSize));
    }

    /**
     * Get the value of groupAddressLevels.
     * @return Value of groupAddressLevels.
     */
    public int getGroupAddressLevels() {
        return groupAddressLevels;
    }

    /**
     * Set the value of groupAddressLevels.
     * @param v  Value to assign to groupAddressLevels.
     */
    public void setGroupAddressLevels(int v) {
        optionProperties.setProperty("option.addresslevels", String.valueOf(v));
        groupAddressLevels = v;
    }

    /**
     * Get the value of url.
     * @return Value of url.
     */
    public String getUrl() {
        return url;
    }

    /**
     * Set the value of url.
     * @param v  Value to assign to url.
     */
    public void setUrl(String v) {
        optionProperties.setProperty("option.url", v);
        url = v;
    }

    /**
     * Get the value of jdbcDriver.
     * @return Value of jdbcDriver.
     */
    public String getJdbcDriver() {
        return jdbcDriver;
    }

    /**
     * Set the value of jdbcDriver.
     * @param v  Value to assign to jdbcDriver.
     */
    public void setJdbcDriver(String v) {
        optionProperties.setProperty("option.jdbcdriver", v);
        jdbcDriver = v;
    }

    /**
     * Get the value of user.
     * @return Value of user.
     */
    public String getUser() {
        return user;
    }

    /**
     * Set the value of user.
     * @param v  Value to assign to user.
     */
    public void setUser(String v) {
        optionProperties.setProperty("option.user", v);
        user = v;
    }

    /**
     * Get the value of askUrl.
     * @return Value of askUrl.
     */
    public boolean getAskUrl() {
        return askUrl;
    }

    /**
     * Set the value of askUrl.
     * @param v  Value to assign to askUrl.
     */
    public void setAskUrl(boolean v) {
        Boolean bool = new Boolean(v);
        optionProperties.setProperty("option.askurl", bool.toString());
        this.askUrl = v;
    }

    /**
     * Get the value of askJdbcDriver.
     * @return Value of askJdbcDriver.
     */
    public boolean getAskJdbcDriver() {
        return askJdbcDriver;
    }

    /**
     * Set the value of askJdbcDriver.
     * @param v  Value to assign to askJdbcDriver.
     */
    public void setAskJdbcDriver(boolean v) {
        Boolean bool = new Boolean(v);
        optionProperties.setProperty("option.askjdbc", bool.toString());
        this.askJdbcDriver = v;
    }

    /**
     * Get the value of askPassword.
     * @return Value of askPassword.
     */
    public boolean getAskPassword() {
        return askPassword;
    }

    /**
     * Set the value of askPassword.
     * @param v  Value to assign to askPassword.
     */
    public void setAskPassword(boolean v) {
        Boolean bool = new Boolean(v);
        optionProperties.setProperty("option.askpasswd", bool.toString());
        askPassword = v;
    }

    /**
     * Get the value of askHistoryEntry.
     * @return Value of askHistoryEntry.
     */
    public boolean getAskHistoryEntry() {
        return askHistoryEntry;
    }

    /**
     * Set the value of askHistoryEntry.
     * @param v  Value to assign to askHistoryEntry.
     */
    public void setAskHistoryEntry(boolean v) {
        Boolean bool = new Boolean(v);
        optionProperties.setProperty("option.askhistory", bool.toString());
        this.askHistoryEntry = v;
    }

    /**
     * Get the value of password.
     * @return Value of password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set the value of password.
     * @param v  Value to assign to password.
     */
    public void setPassword(String v) {
        optionProperties.setProperty("option.passwd", v);
        password = v;
    }

    /**
     * Get the value of lookAndFeel.
     * @return Value of lookAndFeel.
     */
    public String getLookAndFeel() {
        return lookAndFeel;
    }

    /**
     * Set the value of lookAndFeel.
     * @param v  Value to assign to lookAndFeel.
     */
    public void setLookAndFeel(String v) {
        optionProperties.setProperty("option.lookandfeel", v);
        try {
            if (v.equals(optionProperties.getProperty("option.motif"))) UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel"); else if (v.equals(optionProperties.getProperty("option.metal"))) UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel"); else if (v.equals(optionProperties.getProperty("option.windows"))) UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel"); else if (v.equals(optionProperties.getProperty("option.mac"))) UIManager.setLookAndFeel("com.sun.java.swing.plaf.mac.MacLookAndFeel");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        lookAndFeel = v;
    }

    /**
     * Get the value of backupProject.
     * @return Value of backupProject.
     */
    public boolean getBackupProject() {
        return backupProject;
    }

    /**
     * Set the value of backupProject.
     * @param v  Value to assign to backupProject.
     */
    public void setBackupProject(boolean v) {
        Boolean bool = new Boolean(v);
        optionProperties.setProperty("option.backupproject", bool.toString());
        this.backupProject = v;
    }

    /**
     * Get the value of backupProjectPath.
     * @return Value of backupProjectPath.
     */
    public String getBackupProjectPath() {
        return backupProjectPath;
    }

    /**
     * Set the value of backupProjectPath.
     * @param v  Value to assign to backupProjectPath.
     */
    public void setBackupProjectPath(String v) {
        optionProperties.setProperty("option.backupproject.path", v);
        this.backupProjectPath = v;
    }

    /**
     * Get the value of autoSavePeriode.
     * @return Value of autoSavePeriode.
     */
    public int getAutoSavePeriode() {
        return autoSavePeriode;
    }

    /**
     * Set the value of autoSavePeriode.
     * @param v  Value to assign to autoSavePeriode.
     */
    public void setAutoSavePeriode(int v) {
        optionProperties.setProperty("option.autosave", String.valueOf(v));
        this.autoSavePeriode = v;
    }

    /**
     * Get the value of selectedLanguage.
     * @return Value of selectedLanguage.
     */
    public String getSelectedLanguage() {
        return selectedLanguage;
    }

    /**
     * Set the value of selectedLanguage.
     * @param v  Value to assign to selectedLanguage.
     */
    public void setSelectedLanguage(String v) {
        optionProperties.setProperty("option.language", v);
        if (v.equalsIgnoreCase("English")) ConectLocale = Locale.ENGLISH; else if (v.equalsIgnoreCase("German")) ConectLocale = Locale.GERMAN; else if (v.equalsIgnoreCase("Auto")) ConectLocale = Locale.getDefault();
        Locale.setDefault(ConectLocale);
        this.selectedLanguage = v;
    }

    /**
     * Get the value of editor.
     * @return Value of editor.
     */
    public String getEditor() {
        return editor;
    }

    /**
     * Set the value of editor.
     * @param v  Value to assign to editor.
     */
    public void setEditor(String v) {
        optionProperties.setProperty("option.editor", v);
        this.editor = v;
    }

    /**
     * Get the value of checkPhysicalAddress.
     * @return Value of checkPhysicalAddress.
     */
    public String getCheckPhysicalAddress() {
        return checkPhysicalAddress;
    }

    /**
     * Set the value of checkPhysicalAddress.
     * @param v  Value to assign to checkPhysicalAddress.
     */
    public void setCheckPhysicalAddress(String v) {
        optionProperties.setProperty("option.physicaladdress", v);
        this.checkPhysicalAddress = v;
    }

    /**
     * Get the value of ConectLocale.
     * @return Value of ConectLocale.
     */
    public Locale getConectLocale() {
        return ConectLocale;
    }

    /**
     * Set the value of ConectLocale.
     * @param v  Value to assign to ConectLocale.
     */
    public void setConectLocale(Locale v) {
        this.ConectLocale = v;
    }

    /**
     * Get the value of baudRate.
     * @return Value of baudRate.
     */
    public int getBaudRate() {
        return baudRate;
    }

    /**
     * Set the value of baudRate.
     * @param v  Value to assign to baudRate.
     */
    public void setBaudRate(int v) {
        optionProperties.setProperty("option.baudrate", String.valueOf(v));
        this.baudRate = v;
    }

    /**
     * Get the value of comPort.
     * @return Value of comPort.
     */
    public String getComPort() {
        return comPort;
    }

    /**
     * Set the value of comPort.
     * @param v  Value to assign to comPort.
     */
    public void setComPort(String v) {
        optionProperties.setProperty("option.comport", v);
        this.comPort = v;
    }

    /**
     * Get the value of askCopy.
     * @return Value of askCopy.
     */
    public boolean getAskCopy() {
        return askCopy;
    }

    /**
     * Set the value of askCopy.
     * @param v  Value to assign to askCopy.
     */
    public void setAskCopy(boolean v) {
        Boolean bool = new Boolean(v);
        optionProperties.setProperty("option.askcopy", bool.toString());
        this.askCopy = v;
    }

    /**
     * Get the value of askDelete.
     * @return Value of askDelete.
     */
    public boolean getAskDelete() {
        return askDelete;
    }

    /**
     * Set the value of askDelete.
     * @param v  Value to assign to askDelete.
     */
    public void setAskDelete(boolean v) {
        Boolean bool = new Boolean(v);
        optionProperties.setProperty("option.askdelete", bool.toString());
        this.askDelete = v;
    }

    /**
     * Get the value of askMoveLink.
     * @return Value of askMoveLink.
     */
    public boolean getAskMoveLink() {
        return askMoveLink;
    }

    /**
     * Set the value of askMoveLink.
     * @param v  Value to assign to askMoveLink.
     */
    public void setAskMoveLink(boolean v) {
        Boolean bool = new Boolean(v);
        optionProperties.setProperty("option.askmovelink", bool.toString());
        this.askMoveLink = v;
    }

    /**
     * Get the value of componentDesignation.
     * @return Value of componentDesignation.
     */
    public boolean getComponentDesignation() {
        return componentDesignation;
    }

    /**
     * Set the value of componentDesignation.
     * @param v  Value to assign to componentDesignation.
     */
    public void setComponentDesignation(boolean v) {
        Boolean bool = new Boolean(v);
        optionProperties.setProperty("option.designation", bool.toString());
        this.componentDesignation = v;
    }

    /**
     * Get the value of copyWithDevices.
     * @return Value of copyWithDevices.
     */
    public boolean getCopyWithDevices() {
        return copyWithDevices;
    }

    /**
     * Set the value of copyWithDevices.
     * @param v  Value to assign to copyWithDevices.
     */
    public void setCopyWithDevices(boolean v) {
        Boolean bool = new Boolean(v);
        optionProperties.setProperty("option.copywithdevices", bool.toString());
        this.copyWithDevices = v;
    }

    /**
     * Get the value of createPhysicalAddress.
     * @return Value of createPhysicalAddress.
     */
    public boolean getCreatePhysicalAddress() {
        return createPhysicalAddress;
    }

    /**
     * Set the value of createPhysicalAddress.
     * @param v  Value to assign to createPhysicalAddress.
     */
    public void setCreatePhysicalAddress(boolean v) {
        Boolean bool = new Boolean(v);
        optionProperties.setProperty("option.createaddress", bool.toString());
        this.createPhysicalAddress = v;
    }

    /**
     * Get the value of editPhysicalAddress.
     * @return Value of editPhysicalAddress.
     */
    public boolean getEditPhysicalAddress() {
        return editPhysicalAddress;
    }

    /**
     * Set the value of editPhysicalAddress.
     * @param v  Value to assign to editPhysicalAddress.
     */
    public void setEditPhysicalAddress(boolean v) {
        Boolean bool = new Boolean(v);
        optionProperties.setProperty("option.editaddress", bool.toString());
        this.editPhysicalAddress = v;
    }

    /**
     * Get the value of groupAddressStrategy.
     * @return Value of groupAddressStrategy.
     */
    public String getGroupAddressStrategy() {
        return groupAddressStrategy;
    }

    /**
     * Set the value of groupAddressStrategy.
     * @param v  Value to assign to groupAddressStrategy.
     */
    public void setGroupAddressStrategy(String v) {
        optionProperties.setProperty("option.addressstrategy", v);
        this.groupAddressStrategy = v;
    }

    /**
     * Get the value of maxNumberDevices.
     * @return Value of maxNumberDevices.
     */
    public int getMaxNumberDevices() {
        return maxNumberDevices;
    }

    /**
     * Set the value of maxNumberDevices.
     * @param v  Value to assign to maxNumberDevices.
     */
    public void setMaxNumberDevices(int v) {
        optionProperties.setProperty("option.maxdevices", String.valueOf(v));
        this.maxNumberDevices = v;
    }

    /**
     * Get the value of maxNumberLines.
     * @return Value of maxNumberLines.
     */
    public int getMaxNumberLines() {
        return maxNumberLines;
    }

    /**
     * Set the value of maxNumberLines.
     * @param v  Value to assign to maxNumberLines.
     */
    public void setMaxNumberLines(int v) {
        optionProperties.setProperty("option.maxlines", String.valueOf(v));
        this.maxNumberLines = v;
    }

    /**
     * Get the value of filteredManufacturers.
     * @return Value of filteredManufacturers.
     */
    public Vector getFilteredManufacturers() {
        filteredManufacturers = Settings.getFilteredManufacturers();
        return filteredManufacturers;
    }

    /**
     * Set the value of filteredManufacturers.
     * @param v  Value to assign to filteredManufacturers.
     */
    public void setFilteredManufacturers(Vector v) {
        this.filteredManufacturers = v;
    }
}
