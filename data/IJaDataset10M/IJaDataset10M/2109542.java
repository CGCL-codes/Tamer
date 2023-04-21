package net.cygeek.tech.client.ui.wrapper;

import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.layout.FitLayout;
import com.google.gwt.user.client.ui.HorizontalPanel;
import net.cygeek.tech.client.ui.grid.AbstractGrid;
import net.cygeek.tech.client.util.Debug;
import net.cygeek.tech.client.ohrm;

/**
 * Created by IntelliJ IDEA.
 * User: Thilina
 * Date: May 26, 2009
 * Time: 2:21:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class GridWrapper extends Panel {

    AbstractGrid grid = null;

    public HorizontalPanel hp = new HorizontalPanel();

    public GridWrapper(AbstractGrid grid) {
        this.grid = grid;
        this.grid.setHeight(380);
        this.add(hp);
        Debug.gi().print("ohrm.centerPanel.getHeight()" + ohrm.centerPanel.getHeight());
        this.add(grid);
    }
}
