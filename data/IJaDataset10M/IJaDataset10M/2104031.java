package org.smslib.queues;

import java.util.Date;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import org.smslib.OutboundMessage;

class DelayedOutboundMessage implements Delayed {

    OutboundMessage msg;

    Date at;

    public DelayedOutboundMessage(OutboundMessage msg, Date at) {
        setMsg(msg);
        setAt(at);
    }

    public int compareTo(java.util.concurrent.Delayed object) {
        if (getAt().getTime() < ((DelayedOutboundMessage) object).getAt().getTime()) return -1;
        if (getAt().getTime() > ((DelayedOutboundMessage) object).getAt().getTime()) return 1;
        return 0;
    }

    public long getDelay(TimeUnit unit) {
        long n = getAt().getTime() - System.currentTimeMillis();
        return unit.convert(n, TimeUnit.MILLISECONDS);
    }

    public OutboundMessage getMsg() {
        return this.msg;
    }

    public void setMsg(OutboundMessage msg) {
        this.msg = msg;
    }

    public Date getAt() {
        return this.at;
    }

    public void setAt(Date at) {
        this.at = at;
    }

    @Override
    public String toString() {
        return "Scheduled: " + getAt();
    }
}
