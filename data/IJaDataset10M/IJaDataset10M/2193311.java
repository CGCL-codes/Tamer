package org.gwtcmis.service.repository.event;

import com.google.gwt.event.shared.EventHandler;

/**
 * Created by The eXo Platform SAS.
 * 
 * @author <a href="mailto:zhulevaanna@gmail.com">Anna Zhuleva</a>
 * @version $Id:
 */
public interface TypeDescendantsRecievedHandler extends EventHandler {

    /**
    * @param event event
    */
    void onTypeDescendantsReceived(TypeDescendantsRecievedEvent event);
}
