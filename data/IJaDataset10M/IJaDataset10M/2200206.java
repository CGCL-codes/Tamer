package org.gwtcmis.service.acl.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Event is fired when ACL was applied to the object
 * 
 * Created by The eXo Platform SAS.
 *	
 * @author <a href="mailto:zhulevaanna@gmail.com">Ann Zhuleva</a>
 * @version $Id:   ${date} ${time}
 *
 */
public class ACLAppliedEvent extends GwtEvent<ACLAppliedHandler> {

    /**
    * Type.
    */
    public static final GwtEvent.Type<ACLAppliedHandler> TYPE = new GwtEvent.Type<ACLAppliedHandler>();

    /**
    * @param handler handler
    * 
    * @see com.google.gwt.event.shared.GwtEvent#dispatch(com.google.gwt.event.shared.EventHandler)
    */
    @Override
    protected void dispatch(ACLAppliedHandler handler) {
        handler.onACLApplied(this);
    }

    /**
    * @see com.google.gwt.event.shared.GwtEvent#getAssociatedType()
    * 
    * @return Type {@link ACLAppliedHandler}
    */
    @Override
    public Type<ACLAppliedHandler> getAssociatedType() {
        return TYPE;
    }
}
