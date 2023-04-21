package net.sourceforge.iaxclient.jni;

import java.util.EventObject;
import java.util.Date;

public class IAXEvent extends EventObject {

    private Date date;

    public IAXEvent(LibJiaxc obj, long date) {
        super(obj);
        this.date = new Date(date);
    }

    public Date getDate() {
        return date;
    }
}
