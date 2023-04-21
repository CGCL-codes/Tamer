package org.gwtcmis.service.object.event;

import com.google.gwt.event.shared.EventHandler;

/**
 * Created by The eXo Platform SAS.
 * 
 * @author <a href="mailto:zhulevaanna@gmail.com">Anna Zhuleva</a>
 * @version $Id:
 */
public interface ObjectMovedHandler extends EventHandler {

    /**
    * @param event event
    */
    void onObjectMoved(ObjectMovedEvent event);
}
