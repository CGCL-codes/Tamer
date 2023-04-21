package org.zkoss.zkdemo.test2;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.sys.IdGenerator;

/**
 * An ID generator for ZK Test platform.
 * @author jumperchen
 * @since 5.0.0
 */
public class ZKTestIdGenerator implements IdGenerator {

    private static final String PREFIX = "zk_comp_";

    private static final String ID_NUMBER = "zk_id_num";

    public String nextComponentUuid(Desktop desktop, Component comp) {
        int i = Integer.parseInt(desktop.getAttribute(ID_NUMBER).toString());
        i++;
        desktop.setAttribute(ID_NUMBER, String.valueOf(i));
        return PREFIX + i;
    }

    public String nextDesktopId(Desktop desktop) {
        if (desktop.getAttribute(ID_NUMBER) == null) {
            desktop.setAttribute(ID_NUMBER, "0");
        }
        return null;
    }

    public String nextPageUuid(Page page) {
        return null;
    }
}
