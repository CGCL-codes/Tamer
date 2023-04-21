package org.primordion.xholon.service;

import java.awt.Toolkit;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import org.primordion.xholon.base.IMessage;
import org.primordion.xholon.base.ISignal;
import org.primordion.xholon.base.IXholon;
import org.primordion.xholon.base.IXholonTime;
import org.primordion.xholon.base.Message;
import org.primordion.xholon.base.XholonTime;
import org.primordion.xholon.base.XholonTimerTask;

/**
 * A simple Time service.
 * This is intended as an example of an ad-hoc service, that could be created on-the-fly,
 * and can then be immediately used by a client.
 * Both this service and the client should have no effect on the rest of the app.
 * To use:
 * (1) Paste this service after any other service.
 * (2) Paste any appropriate client as a last child of any app-specific node.
 * <ul>
 * <li>time = 101</li>
 * <li>date = 103</li>
 * <li>start timer = 105</li>
 * <li>stop timer = 107</li>
 * </ul>
 * <p>&lt;TimeService implName="org.primordion.xholon.service.TimeService"/></p>
 * @author <a href="mailto:ken@primordion.com">Ken Webb</a>
 * @see <a href="http://www.primordion.com/Xholon">Xholon Project website</a>
 * @since 0.8.1 (Created on April 30, 2010)
 */
public class TimeService extends AbstractXholonService {

    public static final int SIG_TIME_REQUEST = 101;

    public static final int SIG_TIME_RESPONSE = 102;

    public static final int SIG_DATE_REQUEST = 103;

    public static final int SIG_DATE_RESPONSE = 104;

    public static final int SIG_TIMER_START_REQUEST = 105;

    public static final int SIG_TIMER_START_RESPONSE = 106;

    public static final int SIG_TIMER_STOP_REQUEST = 107;

    public static final int SIG_TIMER_STOP_RESPONSE = 108;

    private static IXholonTime xhTime = new XholonTime();

    private XholonTimerTask xhTimerTask = null;

    public IMessage processReceivedSyncMessage(IMessage msg) {
        switch(msg.getSignal()) {
            case SIG_TIME_REQUEST:
                {
                    return new Message(SIG_TIME_RESPONSE, time(), msg.getReceiver(), msg.getSender());
                }
            case SIG_DATE_REQUEST:
                {
                    return new Message(SIG_TIME_RESPONSE, date(), msg.getReceiver(), msg.getSender());
                }
            case SIG_TIMER_START_REQUEST:
                {
                    startTimer(Integer.parseInt((String) msg.getData()));
                    return new Message(SIG_TIMER_START_RESPONSE, null, msg.getReceiver(), msg.getSender());
                }
            case SIG_TIMER_STOP_REQUEST:
                {
                    stopTimer();
                    return new Message(SIG_TIMER_STOP_RESPONSE, null, msg.getReceiver(), msg.getSender());
                }
            default:
                return super.processReceivedSyncMessage(msg);
        }
    }

    public void processReceivedMessage(IMessage msg) {
        switch(msg.getSignal()) {
            case ISignal.SIGNAL_TIMEOUT:
                System.out.println("timed out");
                Toolkit.getDefaultToolkit().beep();
                break;
            default:
                break;
        }
    }

    public void sendMessage(int signal, Object data, IXholon sender) {
        switch(signal) {
            case ISignal.SIGNAL_TIMEOUT:
                processReceivedMessage(new Message(signal, data, sender, this));
                break;
            default:
                super.sendMessage(signal, data, sender);
                break;
        }
    }

    /**
	 * Get the current time.
	 * @return The current time as a String. ex: 8:29 PM
	 */
    protected String time() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        String amPm = calendar.getDisplayName(Calendar.AM_PM, Calendar.LONG, Locale.getDefault());
        return new StringBuffer().append(hour).append(":").append(minute).append(" ").append(amPm).toString();
    }

    /**
	 * Get the current date.
	 * @return The current date as a String. ex: April 30, 2010
	 */
    protected String date() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
        int year = calendar.get(Calendar.YEAR);
        return new StringBuffer().append(month).append(" ").append(day).append(", ").append(year).toString();
    }

    /**
	 * Start the timer.
	 * @param duration Number of seconds.
	 */
    protected void startTimer(int duration) {
        stopTimer();
        xhTimerTask = xhTime.timeoutRelative(this, duration * 1000);
    }

    /**
	 * Stop the timer.
	 */
    protected void stopTimer() {
        if (xhTimerTask == null) {
            return;
        }
        xhTime.cancel(xhTimerTask);
        xhTimerTask = null;
    }

    public IXholon getService(String serviceName) {
        if (serviceName.equals(getXhcName())) {
            return this;
        } else {
            return null;
        }
    }
}
