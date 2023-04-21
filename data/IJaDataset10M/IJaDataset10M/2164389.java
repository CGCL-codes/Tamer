package webirc.client.gui.menu;

import java.util.Vector;
import java.util.Iterator;

/**
 * @author Ayzen
 * @version 1.0 25.02.2007 16:05:57
 */
public class MenuListenerCollection extends Vector {

    public void fireMenuClosed() {
        for (Iterator it = iterator(); it.hasNext(); ) {
            MenuListener listener = (MenuListener) it.next();
            listener.onMenuClosed();
        }
    }
}
