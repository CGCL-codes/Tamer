package org.jdiameter.common.impl.app.cxdx;

import org.jdiameter.api.Message;
import org.jdiameter.api.cxdx.events.JLocationInfoRequest;
import org.jdiameter.common.impl.app.AppRequestEventImpl;

/**
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class JLocationInfoRequestImpl extends AppRequestEventImpl implements JLocationInfoRequest {

    private static final long serialVersionUID = 1L;

    /**
   * 	
   * @param message
   */
    public JLocationInfoRequestImpl(Message message) {
        super(message);
        message.setRequest(true);
    }
}
