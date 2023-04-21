package com.liferay.portlet;

import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.PortletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <a href="EventResponseImpl.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class EventResponseImpl extends StateAwareResponseImpl implements EventResponse {

    public String getLifecycle() {
        return PortletRequest.EVENT_PHASE;
    }

    public void setRenderParameters(EventRequest eventRequest) {
    }

    protected EventResponseImpl() {
        if (_log.isDebugEnabled()) {
            _log.debug("Creating new instance " + hashCode());
        }
    }

    protected void recycle() {
        if (_log.isDebugEnabled()) {
            _log.debug("Recycling instance " + hashCode());
        }
        super.recycle();
    }

    private static Log _log = LogFactory.getLog(EventResponseImpl.class);
}
