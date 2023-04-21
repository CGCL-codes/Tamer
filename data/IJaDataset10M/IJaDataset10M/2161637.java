package ghm.followgui;

import javax.swing.MenuSelectionManager;
import com.memoire.bu.BuRegistry;

/**
 * @author deniger
 * @version $Id: DefaultNoExitSystemInterface.java,v 1.8 2006-11-16 15:04:55 deniger Exp $
 */
public class DefaultNoExitSystemInterface extends DefaultSystemInterface {

    /**
   * @param app
   */
    public DefaultNoExitSystemInterface(final FollowApp app) {
        super(app);
    }

    /**
   * @see ghm.followgui.SystemInterface#exit(int)
   */
    public void exit(final int code) {
        app_.popupMenu_.removeAll();
        MenuSelectionManager.defaultManager().clearSelectedPath();
        if (BuRegistry.getModel().contains(app_)) {
            BuRegistry.unregister(app_);
        }
    }
}
