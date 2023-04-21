package org.riverock.portlet.price;

/**
 *
 * $Author: serg_main $
 *
 * $Id: PositionItem.java,v 1.4 2006/06/05 19:18:41 serg_main Exp $
 *
 */
public class PositionItem {

    public String name = null;

    public String url = null;

    public long id_group_current = 0;

    public long id_group_top = 0;

    protected void finalize() throws Throwable {
        name = null;
        url = null;
        super.finalize();
    }

    public PositionItem(String n_, String u_, long id_curr, long id_top) {
        name = n_;
        url = u_;
        id_group_current = id_curr;
        id_group_top = id_top;
    }

    public PositionItem() {
    }
}
