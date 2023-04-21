package com.luzan.app.map.gwt.ui.client;

import com.google.gwt.user.client.ui.*;
import com.luzan.common.gwt.bean.client.AbstractJSONBean;
import java.util.Map;
import java.util.HashMap;

public class MapItemsPreviewControl extends MapItemsListControl {

    static final ChangeListenerCollection changeListeners = new ChangeListenerCollection();

    static final Map itemsList = new HashMap();

    static final MapItemsPreviewControl instance = new MapItemsPreviewControl();

    public static MapItemsPreviewControl getInstance() {
        return instance;
    }

    private MapItemsPreviewControl() {
        changeListeners.add(new ChangeListener() {

            public void onChange(Widget sender) {
                boolean v = getItemsMap().size() > 0;
                if (!v) setCaption(new HTML("List is empty, nothing to preview")); else setCaption(new HTML("Previewing screen"));
            }
        });
    }

    public MapItemsListControl create(Object[] items) {
        okBtn.setVisible(false);
        setMenu(backLink);
        this.add(MapItemsDisplayControl.getInstance(), DockPanel.CENTER);
        MapItemsDisplayControl.getInstance().create(this, true);
        addItems(items, true);
        return this;
    }

    public AbstractJSONBean getBean() {
        return null;
    }

    public String getName() {
        return "Previewing screen";
    }

    protected Map getItemsMap() {
        return itemsList;
    }

    protected ChangeListenerCollection getChangeListeners() {
        return changeListeners;
    }
}
