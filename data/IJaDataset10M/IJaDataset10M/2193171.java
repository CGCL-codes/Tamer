package net.webpasswordsafe.client.ui;

import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.google.gwt.user.client.Window;

/**
 * Will invoke a page reload when an event such as a server error occurs
 * 
 * @author Josh Drummond
 *
 */
public class ServerErrorListener implements Listener<MessageBoxEvent> {

    @Override
    public void handleEvent(MessageBoxEvent mbe) {
        Window.Location.reload();
    }
}
