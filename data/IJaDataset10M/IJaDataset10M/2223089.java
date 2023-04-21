package org.broadleafcommerce.openadmin.client.event;

import com.google.gwt.event.shared.EventHandler;

/**
 * 
 * @author jfischer
 *
 */
public interface SearchItemSelectedEventHandler extends EventHandler {

    void onSearchItemSelected(SearchItemSelectedEvent event);
}
