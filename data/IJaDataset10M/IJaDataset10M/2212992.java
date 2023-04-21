package org.personalsmartspace.onm.servmsg.test.mock;

import org.personalsmartspace.onm.api.pss3p.ICallbackListener;
import org.personalsmartspace.onm.servmsg.test.CTMsgQueue;

public class MockCallbackListenerImpl implements ICallbackListener {

    public MockCallbackListenerImpl(CTMsgQueue msgQueue) {
        super();
    }

    @Override
    public void handleCallbackString(String returnObjectAsXML) {
    }

    @Override
    public void handleErrorMessage(String errorMessage) {
    }

    @Override
    public void handleCallbackObject(Object arg0) {
    }
}
