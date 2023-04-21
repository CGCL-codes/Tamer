package org.pojosoft.ria.gwt.client.meta;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Metadata for defining a menu
 *
 * @author POJO Software
 * @see MenuItemMeta
 */
public class MenuMeta implements IsSerializable {

    /**
   * The menu item definitions
   */
    public MenuItemMeta[] menuItems;
}
