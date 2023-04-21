package org.mitre.jsip.event;

import JACE.tests.ASX.*;
import JACE.ASX.*;
import java.util.*;

public class CallEventMulticaster extends JACE.tests.ASX.ProducerConsumer implements CallListener {

    private static CallEventMulticaster cemc = new CallEventMulticaster();

    private Vector array = new Vector();

    private Vector events = new Vector();

    public boolean shutdown = false;

    public static CallEventMulticaster getMulticaster() {
        return cemc;
    }

    public CallListener addListener(CallListener a) {
        array.add(a);
        return a;
    }

    public CallListener remove(CallListener cel) {
        array.remove(cel);
        return cel;
    }

    public void shutdown() {
        shutdown = true;
    }

    public void responseReceived(CallEvent ce) {
        SvcEvent eve = new SvcEvent(ce, CallEvent.RESPONSE);
        events.add(new MessageBlock(eve));
        System.out.println("Added Response to Q");
    }

    public void requestReceived(CallEvent ce) {
        SvcEvent eve = new SvcEvent(ce, CallEvent.REQUEST);
        events.add(new MessageBlock(eve));
        System.out.println("Added Request to Q");
    }

    public void dispatchEvents() {
        while (events.size() > 0) {
            put((MessageBlock) events.elementAt(0), null);
            events.remove(0);
        }
    }

    public int svc() {
        while (!shutdown) {
            SvcEvent eve = null;
            try {
                eve = (SvcEvent) getq().obj();
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
            try {
                switch(eve.type) {
                    case CallEvent.REQUEST:
                        for (int i = 0; i < array.size(); i++) {
                            CallListener cl = (CallListener) array.elementAt(i);
                            if (cl == null) {
                                System.out.println("Error null vallListener");
                            } else {
                                cl.requestReceived(eve.ev);
                            }
                        }
                        break;
                    case CallEvent.RESPONSE:
                        for (int i = 0; i < array.size(); i++) {
                            ((CallListener) array.get(i)).responseReceived(eve.ev);
                        }
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println(" Call Event Multicster Exiting");
        array.removeAllElements();
        return 0;
    }
}

class SvcEvent {

    public CallEvent ev;

    public int type;

    public SvcEvent(CallEvent event, int type) {
        this.ev = event;
        this.type = type;
    }
}
