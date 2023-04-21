package org.gwt.formlayout.showcase.client;

import org.gwt.formlayout.showcase.client.tutorial.Basics1Item;
import org.gwt.formlayout.showcase.client.tutorial.Basics2Item;
import org.gwt.formlayout.showcase.client.tutorial.Basics3Item;
import org.gwt.formlayout.showcase.client.tutorial.Basics4Item;
import org.gwt.formlayout.showcase.client.tutorial.Basics5Item;
import org.gwt.formlayout.showcase.client.tutorial.Basics6Item;
import org.gwt.formlayout.showcase.client.tutorial.Basics7Item;
import org.gwt.formlayout.showcase.client.tutorial.Basics8Item;
import org.gwt.formlayout.showcase.client.tutorial.Basics9Item;
import org.gwt.formlayout.showcase.client.tutorial.Builder10Item;
import org.gwt.formlayout.showcase.client.tutorial.Builder11Item;
import org.gwt.formlayout.showcase.client.tutorial.Builder1Item;
import org.gwt.formlayout.showcase.client.tutorial.Builder2Item;
import org.gwt.formlayout.showcase.client.tutorial.Builder3Item;
import org.gwt.formlayout.showcase.client.tutorial.Builder4Item;
import org.gwt.formlayout.showcase.client.tutorial.Builder5Item;
import org.gwt.formlayout.showcase.client.tutorial.Builder6Item;
import org.gwt.formlayout.showcase.client.tutorial.Builder7Item;
import org.gwt.formlayout.showcase.client.tutorial.Builder8Item;
import org.gwt.formlayout.showcase.client.tutorial.Builder9Item;
import org.gwt.formlayout.showcase.client.tutorial.Factory1Item;
import org.gwt.formlayout.showcase.client.tutorial.PanelDataListItem;
import org.gwt.formlayout.showcase.client.tutorial.QuickStartItem;
import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.event.DataListEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.DataList;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;

public class MenuPanel extends ContentPanel {

    private DataList list;

    public MenuPanel() {
        setLayout(new FitLayout());
        setHeading("Description");
        list = new DataList();
        list.add(new QuickStartItem());
        list.add(new Basics1Item());
        list.add(new Basics2Item());
        list.add(new Basics3Item());
        list.add(new Basics4Item());
        list.add(new Basics5Item());
        list.add(new Basics6Item());
        list.add(new Basics7Item());
        list.add(new Basics8Item());
        list.add(new Basics9Item());
        list.add(new Builder1Item());
        list.add(new Builder2Item());
        list.add(new Builder3Item());
        list.add(new Builder4Item());
        list.add(new Builder5Item());
        list.add(new Builder6Item());
        list.add(new Builder7Item());
        list.add(new Builder8Item());
        list.add(new Builder9Item());
        list.add(new Builder10Item());
        list.add(new Builder11Item());
        list.add(new Factory1Item());
        list.addListener(Events.SelectionChange, new Listener<DataListEvent>() {

            public void handleEvent(DataListEvent be) {
                PanelDataListItem item = (PanelDataListItem) be.getSelected().get(0);
                DescriptionPanel descriptionPanel = Registry.get("descriptionPanel");
                descriptionPanel.setMessage(item.getDescription());
                final ContentPanel mainPanel = Registry.get("mainPanel");
                mainPanel.removeAll();
                mainPanel.add(item.getPanel());
                mainPanel.layout();
            }
        });
        add(list);
        Registry.register("menuPanel", this);
    }
}
