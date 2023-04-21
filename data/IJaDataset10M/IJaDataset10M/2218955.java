package net.pandoragames.far.ui.swing;

import java.awt.Color;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import net.pandoragames.far.ui.FARConfig;

/**
 * Swing extension of the configuration class.
 * @author Olivier Wehner at 21.03.2008
 * <!-- copyright note --> 
 */
public class SwingConfig extends FARConfig {

    /**
	 * Constant for component padding.
	 */
    public static final int PADDING = 5;

    /**
	 * Constant for default component width. Only applies to input fields.
	 */
    public static final int COMPONENT_WIDTH = 200;

    /**
	 * Constant for large component width. Only applies to input fields.
	 */
    public static final int COMPONENT_WIDTH_LARGE = 300;

    /**
	 * Constant for maximal component width. Only applies to input fields.
	 */
    public static final int COMPONENT_WIDTH_MAX = 500;

    /**
	 * Property name for default file info.
	 */
    public static final String PPT_FILEINFO = "far.fileinfo";

    /**
	 * Property name for default file info.
	 */
    public static final String PPT_PLAINBYTES = "far.plainbytes";

    /**
	 * Color constant
	 */
    public static final Color GRAY_EXTRA_LIGHT = new Color(0xEE, 0xEE, 0xEE);

    /**
	 * Default file info displayed in the result table:<p>
	 * NOTHING: Nothing is displayed<br>
	 * SIZE: File size<br>
	 * LAST_MODIFIED: Last modified time stamp<br>
	 * READONLY: Warning if file is read only
	 */
    public enum DefaultFileInfo {

        NOTHING, SIZE, LAST_MODIFIED, READONLY
    }

    private Point screenCenter = new Point(400, 300);

    private int standardComponentHight = 20;

    private DefaultFileInfo defaultFileInfo = DefaultFileInfo.READONLY;

    private boolean showPlainBytes;

    private Set<KeyStroke> forwardKeys;

    private Set<KeyStroke> backwardKeys;

    public SwingConfig() {
        super();
        forwardKeys = new HashSet<KeyStroke>();
        forwardKeys.add(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0));
        backwardKeys = new HashSet<KeyStroke>();
        backwardKeys.add(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, KeyEvent.SHIFT_MASK));
    }

    /**
	 * Returns the screen center for centered popup windows
	 * @return screen center
	 */
    public Point getScreenCenter() {
        return screenCenter;
    }

    /**
	 * Returns the default component hight of a single line.
	 * @return default component hight for single line components.
	 */
    public int getStandardComponentHight() {
        return standardComponentHight;
    }

    /**
	 * Returns the access key code for the specified label or menu entry if
	 * any is defined. The access key code is an integer, representing an
	 * ASCII character. Normally only upper case characters should be used,
	 * but any valu between 32 (space) and 127 (DEL) is accepted.
	 * @param localizedCode localisation code for a label or menu entry
	 * @return coresponding access key code if any is defined, 0 otherwise. 
	 */
    public int getAccessKey(String localizedCode) {
        String code = localizedCode;
        if (!code.startsWith("key.")) code = "key." + code;
        String codeValue = getLocalizer().localize(code);
        if (code == null) code = "48";
        try {
            int intValue = Integer.valueOf(codeValue);
            return (intValue > 31 && intValue < 128) ? intValue : 0;
        } catch (NumberFormatException nfx) {
            return 0;
        }
    }

    /**
	 * Returns true if we are running on Mac OS X. To add some extra bells
	 * and whistles. And I thought Windows was a pain...! 
	 * @return true if the current machine is a Mac
	 */
    public static boolean isMacOSX() {
        return System.getProperty("os.name").toLowerCase().startsWith("mac os x");
    }

    /**
	 * Gee, I never thought I'd do something like this. But ok, everything
	 * for the user experience.
	 * @return true if this box runs on win xy
	 */
    public static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().startsWith("windows");
    }

    /**
	 * Default file info displayed in the result table.
	 * See {@link DefaultFileInfo DefaultFileInfo} for available options.
	 * @return Default file info displayed in the result table
	 */
    public DefaultFileInfo getDefaultFileInfo() {
        return defaultFileInfo;
    }

    /**
	 * Sets the default file info using the code name. The parameter must
	 * correspond to one of the legal code names of enum class
	 * {@link DefaultFileInfo DefaultFileInfo}}. Null values and
	 * illegal code names will be ignored
	 * @param fileInfoCode code name of class DefaultFileInfo.
	 */
    public void setDefaultFileInfo(String fileInfoCode) {
        if (fileInfoCode == null) return;
        try {
            defaultFileInfo = DefaultFileInfo.valueOf(fileInfoCode);
        } catch (IllegalArgumentException iax) {
            logger.warn("Attempt to set '" + fileInfoCode + "' as default file info. Ignored.");
        }
    }

    /**
	 * Returns true if plain byte values should be shown in Info column
	 * if the default file info property is set to "SIZE".
	 * @return true if plain byte values should be shown in Info column
	 */
    public boolean isShowPlainBytes() {
        return showPlainBytes;
    }

    /**
	 * Set to true if plain byte values should be shown in Info column
	 * if the default file info property is set to "SIZE".
	 * @param showPlainBytes true if plain byte values should be shown in Info column
	 */
    public void setShowPlainBytes(boolean showPlainBytes) {
        this.showPlainBytes = showPlainBytes;
    }

    /**
	 * Sets the focus traversal keys TAB and SHIFT+TAB for the specified component.
	 * This is typically necessary for Textareas.
	 * @param component to receive the standard focus traversal keys
	 */
    public void setFocusTraversalKeys(JComponent component) {
        component.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, forwardKeys);
        component.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, backwardKeys);
    }

    /**
	 * Overwrites the super implementation to load the default 
	 * file info propery.
	 */
    protected void loadFromProperties(Properties properties) {
        super.loadFromProperties(properties);
        try {
            defaultFileInfo = DefaultFileInfo.valueOf(properties.getProperty(PPT_FILEINFO));
        } catch (Exception x) {
            defaultFileInfo = DefaultFileInfo.READONLY;
        }
        showPlainBytes = Boolean.valueOf(properties.getProperty(PPT_PLAINBYTES)).booleanValue();
    }

    /**
	 * Overwrites the super implementation to store the default 
	 * file info propery.
	 */
    protected void writeToProperties(Properties properties) {
        super.writeToProperties(properties);
        properties.setProperty(PPT_FILEINFO, defaultFileInfo.name());
        properties.setProperty(PPT_PLAINBYTES, String.valueOf(showPlainBytes));
    }

    void setScreenCenter(Point screenCenter) {
        this.screenCenter = screenCenter;
    }

    void setStandardComponentHight(int standardComponentHight) {
        this.standardComponentHight = standardComponentHight;
    }
}
