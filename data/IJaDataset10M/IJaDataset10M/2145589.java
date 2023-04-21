package org.freebxml.omar.client.ui.web.client.browser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Ext;
import com.gwtext.client.widgets.menu.BaseItem;
import com.gwtext.client.widgets.menu.Item;
import com.gwtext.client.widgets.menu.ItemConfig;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.event.BaseItemListenerAdapter;

/**
 * Registry for user interface actions.
 * 
 * @author Andreas Veithen
 */
public class Actions {

    private static Actions instance;

    private final List contextActions = new ArrayList();

    private Actions() {
    }

    public static Actions getInstance() {
        if (instance == null) {
            instance = new Actions();
            instance.registerContextAction(new ViewRepositoryItemAction());
        }
        return instance;
    }

    public void registerContextAction(ContextAction contextAction) {
        contextActions.add(contextAction);
    }

    public Menu createContextMenu(final Browser browser, final RegistryObjectHandle registryObjectHandle) {
        Menu menu = new Menu(Ext.generateId());
        for (Iterator it = contextActions.iterator(); it.hasNext(); ) {
            final ContextAction contextAction = (ContextAction) it.next();
            if (contextAction.appliesTo(registryObjectHandle)) {
                Item item = new Item(contextAction.getLabel(), new ItemConfig());
                item.addBaseItemListener(new BaseItemListenerAdapter() {

                    public void onClick(BaseItem item, EventObject e) {
                        contextAction.execute(browser, registryObjectHandle);
                    }
                });
                menu.addItem(item);
            }
        }
        return menu;
    }
}
