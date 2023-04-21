package org.mobicents.servlet.sip.restcomm.callmanager.events;

public final class SignalDetectorEventType extends EventType {

    public static final SignalDetectorEventType DONE_DETECTING = new SignalDetectorEventType("Done Detecting");

    public static final SignalDetectorEventType SIGNAL_DETECTED = new SignalDetectorEventType("Signal Detected");

    public static final SignalDetectorEventType FAILED = new SignalDetectorEventType("Failed");

    protected SignalDetectorEventType(final String name) {
        super(name);
    }
}
