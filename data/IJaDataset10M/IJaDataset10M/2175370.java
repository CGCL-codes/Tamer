package org.mobicents.framework.servlet.sip.components;

import java.util.HashSet;
import java.util.Observable;
import javax.inject.Inject;
import org.mobicents.framework.servlet.sip.components.Container;
import org.mobicents.framework.servlet.sip.components.SipEntity;

/**
 * Active calls container. 
 * 
 * Also extends java.util.Observable in order to provide the ability to notify observers for any call state change according to the
 * CommunicationService
 * 
 * @author gvagenas 
 * gvagenas@gmail.com / devrealm.org
 *
 */
public class CallContainer extends Observable implements Container {

    private HashSet<CallComponent> calls = new HashSet<CallComponent>();

    public void add(SipEntity call) {
        calls.add((CallComponent) call);
        setChanged();
        notifyObservers(call);
    }

    public void remove(SipEntity call) {
        calls.remove(call);
        setChanged();
        notifyObservers(call);
    }

    public boolean contains(String call) {
        if (calls.contains(call)) {
            return true;
        } else return false;
    }
}
