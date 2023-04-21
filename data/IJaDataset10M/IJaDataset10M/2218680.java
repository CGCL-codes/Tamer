package gwtm.client.ui.frameworktest;

import net.mygwt.ui.client.Style;
import net.mygwt.ui.client.widget.TabItem;

/**
 *
 * @author Yorgos
 */
public class TabItemTolog extends TabItem {

    /** Creates a new instance of TabItemTolog */
    public TabItemTolog() {
        super(Style.CLOSE);
        setIconStyle("icon-tolog");
        setText("Tolog");
    }

    public void clear() {
    }
}
