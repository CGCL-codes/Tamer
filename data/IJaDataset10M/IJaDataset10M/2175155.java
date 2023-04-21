package ui.util;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import org.apache.log4j.Logger;

/**
 * @author skunk
 *
 */
public class GeneralUtil {

    private static final transient Logger logger = Logger.getLogger(GeneralUtil.class.getName());

    /**
	 * This method is helpfull to center on the screen a Component like a JFrame or a JDialog.
	 * @param component The component to be centered on the screen.
	 */
    public static void centerComponent(Component component) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = component.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        component.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    }
}
