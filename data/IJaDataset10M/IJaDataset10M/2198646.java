package org.jdiameter.common.impl.app.gx;

import org.jdiameter.api.Request;
import org.jdiameter.api.app.AppSession;
import org.jdiameter.api.gx.events.GxReAuthRequest;
import org.jdiameter.common.impl.app.AppRequestEventImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:carl-magnus.bjorkell@emblacom.com"> Carl-Magnus Björkell </a>
 */
public class GxReAuthRequestImpl extends AppRequestEventImpl implements GxReAuthRequest {

    private static final long serialVersionUID = 1L;

    protected Logger logger = LoggerFactory.getLogger(GxReAuthRequestImpl.class);

    public GxReAuthRequestImpl(AppSession session, String destRealm, String destHost) {
        super(session.getSessions().get(0).createRequest(code, session.getSessionAppId(), destRealm, destHost));
    }

    public GxReAuthRequestImpl(Request request) {
        super(request);
    }
}
