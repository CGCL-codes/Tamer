package org.lopatka.idonc.web.page.component;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * @author Bartek
 */
public abstract class ThreeColumnContentPanel extends Panel {

    private static final long serialVersionUID = 1L;

    public ThreeColumnContentPanel(String id) {
        super(id);
        add(getLeftColumn("leftColumn"));
        add(getMainColumn("mainColumn"));
        add(getRightColumn("rightColumn"));
    }

    public abstract Component getLeftColumn(String columnId);

    public abstract Component getMainColumn(String columnId);

    public abstract Component getRightColumn(String columnId);
}
