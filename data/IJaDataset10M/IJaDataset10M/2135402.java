package org.gwtcmis.service.policy.event;

import com.google.gwt.event.shared.EventHandler;

/**
 * @author <a href="mailto:zhulevaanna@gmail.com">Ann Zhuleva</a>
 * @version $Id:   $
 *
 */
public interface PolicyRemovedHandler extends EventHandler {

    /**
    * @param event event
    */
    void onPolicyRemoved(PolicyRemovedEvent event);
}
