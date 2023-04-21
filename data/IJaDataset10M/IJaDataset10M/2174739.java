package net.sourceforge.iaxclient;

import net.sourceforge.iaxclient.jni.IAXEvent;
import net.sourceforge.iaxclient.jni.LibJiaxc;

public class RegistrationEvent extends IAXEvent {

    private Registration reg;

    private int reply;

    private int msgcount;

    RegistrationEvent(LibJiaxc obj, long date, Registration reg, int reply, int msgcount) {
        super(obj, date);
        this.reg = reg;
        this.reply = reply;
        this.msgcount = msgcount;
    }

    public Registration getRegistration() {
        return reg;
    }

    public int getReply() {
        return reply;
    }

    public int getMsgCount() {
        return msgcount;
    }

    public String toString() {
        return "" + reg + " " + reply + " " + msgcount;
    }
}
