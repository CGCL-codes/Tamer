package org.mobicents.servlet.sip.restcomm.callmanager.events;

public final class SignalDetectorEvent extends Event<SignalDetectorEventType> {

    private String digits;

    public SignalDetectorEvent(final Object source, final SignalDetectorEventType type) {
        super(source, type);
    }

    public String getDigits() {
        return digits;
    }

    public void setDigits(final String digits) {
        this.digits = digits;
    }
}
