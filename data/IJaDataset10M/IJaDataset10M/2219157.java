package connex.core.Presence;

import connex.core.net.*;
import java.util.Enumeration;

public class StatusController extends Thread {

    private boolean start = true;

    private long period = 1000 * 60 * 1;

    protected StatusController() {
    }

    protected void stopIt() {
        start = false;
        this.interrupt();
        try {
            this.join();
        } catch (InterruptedException ex) {
        }
    }

    public void run() {
        int i = 0;
        Enumeration<Member> num;
        while (start) {
            try {
                Thread.sleep(period);
                if (!start) {
                    break;
                }
            } catch (InterruptedException ex) {
            }
            PresenceService.getInstance().getMsgProcessor().sendStatusUpdate(PresenceService.getInstance().getStatus());
            if (i == 3 && null != PresenceService.getInstance().getMemberCollection().elements()) {
                num = PresenceService.getInstance().getMemberCollection().elements();
                while (num.hasMoreElements()) {
                    Member memb = num.nextElement();
                    if (memb.isOutOfTime()) {
                        PresenceEvent e = new PresenceEvent(PresenceEvent.OFFLINE, memb.getName(), memb.getID());
                        memb.setStatus(PresenceEvent.OFFLINE);
                        PresenceService.getInstance().getMsgProcessor().informUpdateMember(e);
                    }
                }
                i = 0;
            }
            i++;
        }
    }
}
