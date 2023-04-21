package org.gwtcmis.service.object.event;

import com.google.gwt.event.shared.EventHandler;

/**
 * Created by The eXo Platform SAS.
 *	
 * @author <a href="mailto:zhulevaanna@gmail.com">Ann Zhuleva</a>
 * @version $Id:   ${date} ${time}
 *
 */
public interface TreeDeletedHandler extends EventHandler {

    /**
    * @param event event
    */
    void onTreeDeleted(TreeDeletedEvent event);
}
