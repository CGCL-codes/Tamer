package jat.demo.vr.SolarSystem;

import java.awt.event.*;
import java.awt.AWTEvent;
import java.util.Enumeration;
import javax.media.j3d.*;

/**
* This class is a simple behavior that invokes the KeyNavigator
* to modify the time.
*/
public class time_control extends Behavior {

    private WakeupOnAWTEvent wakeupOne = null;

    private WakeupCriterion[] wakeupArray = new WakeupCriterion[1];

    private WakeupCondition wakeupCondition = null;

    private final float TRANSLATE = 1.e6f;

    Constellation sr;

    public time_control(Constellation sr) {
        this.sr = sr;
        wakeupOne = new WakeupOnAWTEvent(KeyEvent.KEY_PRESSED);
        wakeupArray[0] = wakeupOne;
        wakeupCondition = new WakeupOr(wakeupArray);
    }

    /**
	*  Override Behavior's initialize method to setup wakeup criteria.
	*/
    public void initialize() {
        wakeupOn(wakeupCondition);
    }

    /**
	*  Override Behavior's stimulus method to handle the event.
	*/
    public void processStimulus(Enumeration criteria) {
        WakeupOnAWTEvent ev;
        WakeupCriterion genericEvt;
        AWTEvent[] events;
        while (criteria.hasMoreElements()) {
            genericEvt = (WakeupCriterion) criteria.nextElement();
            if (genericEvt instanceof WakeupOnAWTEvent) {
                ev = (WakeupOnAWTEvent) genericEvt;
                events = ev.getAWTEvent();
                processAWTEvent(events);
            }
        }
        wakeupOn(wakeupCondition);
    }

    /**
	*  Process a keyboard event
	*/
    private void processAWTEvent(AWTEvent[] events) {
        for (int n = 0; n < events.length; n++) {
            if (events[n] instanceof KeyEvent) {
                KeyEvent eventKey = (KeyEvent) events[n];
                if (eventKey.getID() == KeyEvent.KEY_PRESSED) {
                    int keyCode = eventKey.getKeyCode();
                    int keyChar = eventKey.getKeyChar();
                    switch(keyCode) {
                        case KeyEvent.VK_ADD:
                            break;
                        case KeyEvent.VK_SUBTRACT:
                            break;
                    }
                }
            }
        }
    }
}
