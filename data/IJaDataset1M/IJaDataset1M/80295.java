package org.adempiere.webui.component;

import java.util.List;
import org.zkoss.zk.ui.Component;

/**
 *
 * @author  <a href="mailto:agramdass@gmail.com">Ashley G Ramdass</a>
 * @date    Feb 25, 2007
 * @version $Revision: 0.10 $
 */
public class Rows extends org.zkoss.zul.Rows {

    private static final long serialVersionUID = 1L;

    private boolean noStrip = false;

    public Row newRow() {
        Row row = new Row();
        appendChild(row);
        return row;
    }

    public void setNoStrip(boolean b) {
        noStrip = b;
        String style = noStrip ? "border: none" : null;
        List<?> list = getChildren();
        for (Object o : list) {
            if (o instanceof Row) {
                Row row = (Row) o;
                row.setStyle(style);
            }
        }
    }

    @Override
    public boolean insertBefore(Component child, Component refChild) {
        boolean b = super.insertBefore(child, refChild);
        if (b && child instanceof Row && noStrip) {
            Row row = (Row) child;
            row.setStyle("border: none");
        }
        return b;
    }
}
