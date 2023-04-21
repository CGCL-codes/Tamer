package net.sf.doolin.gui.swing;

import java.net.URL;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Accessor for the Doolin icons.
 * 
 * @author Damien Coraboeuf
 * 
 */
public class DoolinIcons {

    private static final Logger log = LoggerFactory.getLogger(DoolinIcons.class);

    /**
	 * Add icon
	 */
    public static final String ADD = "/net/sf/doolin/gui/Add.png";

    /**
	 * Close icon
	 */
    public static final String CLOSE = "/net/sf/doolin/gui/Close.png";

    /**
	 * Delete icon
	 */
    public static final String DELETE = "/net/sf/doolin/gui/Delete16.gif";

    /**
	 * Returns the icon located at the given resource path.
	 * 
	 * @param path
	 *            Resource path
	 * @return Icon or <code>null</code> if not found
	 */
    public static Icon getIcon(String path) {
        URL url = DoolinIcons.class.getResource(path);
        if (url != null) {
            return new ImageIcon(url);
        } else {
            log.warn(String.format("Cannot find icon at path [%s]", path));
            return null;
        }
    }

    /**
	 * Not instanciable
	 */
    private DoolinIcons() {
    }
}
