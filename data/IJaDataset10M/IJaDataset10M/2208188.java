package org.broadleafcommerce.openadmin.client.event;

import com.google.gwt.event.shared.EventHandler;

/**
 * 
 * @author jfischer
 *
 */
public interface NewItemCreatedEventHandler extends EventHandler {

    void onNewItemCreated(NewItemCreatedEvent event);
}
