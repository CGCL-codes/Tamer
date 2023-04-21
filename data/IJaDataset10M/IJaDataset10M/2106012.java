package jhomenet.ui.factories;

import java.util.Map;
import java.util.HashMap;
import java.util.Enumeration;
import java.util.Properties;
import java.io.IOException;
import java.awt.Image;
import java.net.URL;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import jhomenet.ui.panel.PanelFactory;
import org.apache.log4j.Logger;

/**
 * TODO: Class description.
 *
 * @author Dave Irwin (jhomenet at gmail dot com)
 */
public class IconFactory {

    /**
	 * Define a logging mechanism.
	 */
    private static Logger logger = Logger.getLogger(IconFactory.class.getName());

    /**
	 * Hardware icon filename properties file.
	 */
    private static final String hardwareIconProperties = "/resources/hardwareIcons.properties";

    /**
	 * Available default icon filenames
	 */
    private static final String toolsIconFilename = "/resources/images/tools.png";

    private static final String configureIconFilename = "/resources/images/configure32.png";

    private static final String statsIconFilename = "/resources/images/stats32.png";

    private static final String weatherChannelIconFilename = "/resources/images/TWClogo_32px.png";

    private static final String refreshIconFilename = "/resources/images/refresh24.png";

    /**
	 * Available default icons.
	 */
    public static final Icon toolsIcon = new ImageIcon(PanelFactory.class.getResource(toolsIconFilename));

    public static final Icon configureIcon = new ImageIcon(PanelFactory.class.getResource(configureIconFilename));

    public static final Icon statsIcon = new ImageIcon(PanelFactory.class.getResource(statsIconFilename));

    public static final Icon weatherChannelIcon = new ImageIcon(PanelFactory.class.getResource(weatherChannelIconFilename));

    public static final Icon refreshIcon = new ImageIcon(PanelFactory.class.getResource(refreshIconFilename));

    /**
	 * Hardware icons
	 */
    private static final Map<String, String> hardwareIconFilenames = new HashMap<String, String>();

    /**
	 * The icon folder name.
	 */
    private static final String defaultIconFolder = "/resources/images/";

    /**
	 * Available scales.
	 */
    public static enum AvailableScales {

        /**
		 * 16x16
		 */
        SCALE_16x16, /**
		 * 32x32
		 */
        SCALE_32x32
    }

    /**
	 * Keeps track of the available scales.
	 */
    private static final Map<AvailableScales, ScaleComponent> scaleComponents = new HashMap<AvailableScales, ScaleComponent>();

    static {
        scaleComponents.put(AvailableScales.SCALE_16x16, new ScaleComponent(16, 16));
        scaleComponents.put(AvailableScales.SCALE_32x32, new ScaleComponent(32, 32));
        loadHardwareIcons();
    }

    /**
	* Non-instantiable constructor.
	*/
    private IconFactory() {
    }

    /**
	 * 
	 */
    private static void loadHardwareIcons() {
        Properties properties = new Properties();
        try {
            properties.load(IconFactory.class.getResourceAsStream(hardwareIconProperties));
            Enumeration e = properties.propertyNames();
            for (; e.hasMoreElements(); ) {
                String hardwareClassname = (String) e.nextElement();
                String iconFilename = (String) properties.get(hardwareClassname);
                hardwareIconFilenames.put(hardwareClassname, iconFilename);
            }
        } catch (IOException ioe) {
            logger.error("I/O exception while loading hardware icons: " + ioe.getMessage(), ioe);
        }
    }

    /**
	 * 
	 * @param hardwareClassname
	 * @return
	 */
    public static final String getHardwareIconFilename(String hardwareClassname) {
        return hardwareIconFilenames.get(hardwareClassname);
    }

    /**
	 * 
	 * @param hardwareClassname
	 * @return
	 */
    public static final Icon getHardwareIcon(String hardwareClassname) {
        return buildIcon(hardwareIconFilenames.get(hardwareClassname));
    }

    /**
	 * Build an icon. This method does not perform any type of scaling on the
	 * icon. 
	 * 
	 * @param iconFilename The icon filename
	 * @return
	 */
    public static final Icon buildIcon(String iconFilename) {
        return buildIcon(null, iconFilename);
    }

    /**
	 * 
	 * @param folder
	 * @param iconFilename
	 * @return
	 */
    public static final Icon buildIcon(String folder, String iconFilename) {
        if (!(iconFilename != null && iconFilename.length() > 0)) return null;
        URL url = null;
        if (folder == null) url = IconFactory.class.getResource(defaultIconFolder + iconFilename); else url = IconFactory.class.getResource(folder + iconFilename);
        logger.debug("Icon url: " + url);
        if (url == null) return new ImageIcon(iconFilename); else return new ImageIcon(url);
    }

    /**
	 * Build a scaled icon. This method scales the icon to a 16x16 icon by
	 * default.
	 * 
	 * @param iconFilename The icon filename
	 * @return
	 */
    public static final Icon buildScaledIcon(String iconFilename) {
        return buildScaledIcon(iconFilename, AvailableScales.SCALE_16x16);
    }

    /**
	 * Build a scaled icon. This method scales the icon given the desired
	 * scale. 
	 * 
	 * @param iconFilename The icon filename
	 * @param desiredScale The desired icon scale
	 * @return
	 */
    public static final Icon buildScaledIcon(String iconFilename, AvailableScales desiredScale) {
        if (!(iconFilename != null && iconFilename.length() > 0)) return null;
        URL url = IconFactory.class.getResource(defaultIconFolder + iconFilename);
        logger.debug("Icon url: " + url);
        ImageIcon image = null;
        if (url == null) image = new ImageIcon(iconFilename); else image = new ImageIcon(url);
        if (image != null) {
            ScaleComponent scale = scaleComponents.get(desiredScale);
            return new ImageIcon(image.getImage().getScaledInstance(scale.getXScale(), scale.getYScale(), Image.SCALE_SMOOTH));
        } else return null;
    }

    /**
	 * 
	 */
    private static class ScaleComponent {

        private final Integer xScale;

        private final Integer yScale;

        private ScaleComponent(Integer xScale, Integer yScale) {
            this.xScale = xScale;
            this.yScale = yScale;
        }

        /**
		 * @return the xScale
		 */
        final Integer getXScale() {
            return xScale;
        }

        /**
		 * @return the yScale
		 */
        final Integer getYScale() {
            return yScale;
        }
    }
}
