package org.personalsmartspace.onm.admgmt.test.mock;

import org.personalsmartspace.onm.api.pss3p.ICallbackListener;
import org.personalsmartspace.onm.api.pss3p.IMessageQueue;
import org.personalsmartspace.onm.api.pss3p.ONMException;
import org.personalsmartspace.onm.api.pss3p.ServiceMessage;

public class MockMessageQueue implements IMessageQueue {

    public void addServiceMessage(ServiceMessage message) throws ONMException {
    }

    public void addServiceMessage(ServiceMessage message, ICallbackListener listener) throws ONMException {
    }
}
