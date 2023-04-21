package org.gwtcmis.service.acl.event;

import com.google.gwt.event.shared.EventHandler;

/**
 * Created by The eXo Platform SAS.
 *	
 * @author <a href="mailto:zhulevaanna@gmail.com">Ann Zhuleva</a>
 * @version $Id:   ${date} ${time}
 *
 */
public interface ACLReceivedHandler extends EventHandler {

    /**
    * @param event event
    */
    void onACLReceived(ACLReceivedEvent event);
}
