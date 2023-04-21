package org.omg.CosTimerEvent;

public final class TimerEventServiceHolder implements org.omg.CORBA.portable.Streamable {

    public TimerEventService value;

    public TimerEventServiceHolder() {
    }

    public TimerEventServiceHolder(TimerEventService initial) {
        value = initial;
    }

    public void _read(org.omg.CORBA.portable.InputStream in) {
        value = TimerEventServiceHelper.read(in);
    }

    public void _write(org.omg.CORBA.portable.OutputStream out) {
        TimerEventServiceHelper.write(out, value);
    }

    public org.omg.CORBA.TypeCode _type() {
        return TimerEventServiceHelper.type();
    }
}
