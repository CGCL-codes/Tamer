package rath.jmsn;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JComboBox;
import k2msg.Friend;
import rath.msnm.entity.MsnFriend;

/**
 *
 * @author Jang-Ho Hwang, rath@linuxkorea.co.kr
 * @version $Id: EventViewer.java,v 1.3 2007/06/04 14:24:13 nevard Exp $
 */
public class EventViewer extends JComboBox implements ToolBox {

    private SimpleDateFormat sdf = new SimpleDateFormat("[HH:mm]");

    private int limitSize = 30;

    public EventViewer() {
        setFont(FONT);
    }

    /**
	 * 이 Event viewer가 한번에 보여줄 수 있는 최대 크기를 결정한다.
	 */
    public void setLimitSize(int size) {
        this.limitSize = size;
    }

    public int getLimitSize() {
        return this.limitSize;
    }

    private boolean fireEvent = true;

    public void addEvent(String msg, Friend friend) {
        String view = sdf.format(new Date()) + " " + msg;
        insertItemAt(new Event(view, friend), 0);
        ensureLimitOverflow();
        fireEvent = false;
        setSelectedIndex(0);
        fireEvent = true;
    }

    protected void ensureLimitOverflow() {
        if (getItemCount() > limitSize) {
            for (int i = getItemCount() - 1; i >= limitSize; i--) removeItemAt(i);
        }
    }

    /**
	 * Override. Only accept to EventDispatchThread call.
	 */
    protected void fireActionEvent() {
        if (fireEvent) super.fireActionEvent();
    }

    public static class Event {

        private String msg;

        private Friend friend;

        private Event(String msg, Friend friend2) {
            this.msg = msg;
            this.friend = friend2;
        }

        public Friend getFriend() {
            return this.friend;
        }

        public String toString() {
            return this.msg;
        }
    }

    ;

    public void clear() {
    }
}
