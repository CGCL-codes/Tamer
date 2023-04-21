package org.zkoss.zktest.test;

import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.sys.IdGenerator;

/**
 * Used to see the status when IdGenerator is called.
 *
 * @author tomyeh
 */
public class ViewIdGenerator implements IdGenerator {

    public String nextComponentUuid(Desktop desktop, Component comp) {
        System.out.println("nextComponentUuid for " + comp.getClass() + ", parent=" + comp.getParent() + ", page=" + comp.getPage());
        return null;
    }

    public String nextPageUuid(Page page) {
        return null;
    }

    public String nextDesktopId(Desktop desktop) {
        return null;
    }
}
