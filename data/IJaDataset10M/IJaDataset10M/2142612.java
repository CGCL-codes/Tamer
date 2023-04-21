package org.pojosoft.catalog.web.gwt.client.menu;

import org.pojosoft.ria.gwt.client.ui.SearchComposite;
import java.util.HashMap;

/**
 * Launches the Catalog Search page when the Catalog menu item is clicked.
 *
 * @author POJO Software
 */
public class CatalogMenuCommand extends AbstractModuleSearchCommand {

    public void execute() {
        super.execute();
        render("CatalogSearch", SearchComposite.SEARCH_TYPE_DEFAULT, new HashMap());
    }
}
