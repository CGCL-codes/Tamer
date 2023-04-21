package cn.ac.ntarl.sso.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Timer;
import java.util.TimerTask;
import cn.ac.ntarl.umt.config.Config;
import cn.ac.ntarl.umt.profile.data.Attribute;
import cn.vlabs.simpleAuth.Principal;

public class AppLoginTicketManager {

    private Hashtable<String, UserTicket> loginTickets = new Hashtable();

    private static AppLoginTicketManager appLoginTicketManager = new AppLoginTicketManager();

    private static Config config = Config.getInstance();

    static {
        Timer tm = new Timer();
        tm.schedule(new ClearInvalidteTickets(appLoginTicketManager.loginTickets), 6 * 60 * 60 * 1000, 6 * 60 * 60 * 1000);
    }

    private AppLoginTicketManager() {
    }

    public static AppLoginTicketManager getInstance() {
        return appLoginTicketManager;
    }

    public synchronized void setTicket(String key, Collection<Principal> principals, ArrayList<Attribute> attributes) {
        if (key == null) {
            return;
        }
        UserTicket ut = new UserTicket();
        ut.setCreateTime(System.currentTimeMillis());
        ut.setPrincipals(principals);
        ut.setAttributes(attributes);
        try {
            loginTickets.put(key, ut);
        } catch (Exception e) {
        }
    }

    public synchronized UserTicket validateTicket(String ticket) {
        UserTicket ut = null;
        if (ticket != null) {
            ut = loginTickets.get(ticket);
            loginTickets.remove(ticket);
            if (ut != null) {
                long currentTime = System.currentTimeMillis();
                long creatingTime = ut.getCreateTime();
                int inter = config.getInt("sso.time.interval", 30);
                long linter = inter * 1000;
                if (currentTime - creatingTime <= linter) {
                    return ut;
                }
            }
        }
        return null;
    }

    private static class ClearInvalidteTickets extends TimerTask {

        private Hashtable<String, UserTicket> loginTickets = null;

        public ClearInvalidteTickets(Hashtable<String, UserTicket> loginTickets) {
            this.loginTickets = loginTickets;
        }

        public void run() {
            Enumeration<String> eu = loginTickets.keys();
            long currentTime = System.currentTimeMillis();
            int inter = config.getInt("sso.time.interval", 30);
            while (eu.hasMoreElements()) {
                try {
                    String key = eu.nextElement();
                    UserTicket ut = loginTickets.get(key);
                    if (ut != null) {
                        long creatingTime = ut.getCreateTime();
                        long linter = inter * 1000;
                        if (currentTime - creatingTime > linter) {
                            loginTickets.remove(key);
                        }
                    }
                } catch (Exception e) {
                    System.out.println("���ʧЧƱ�ݷ����쳣:" + e.getMessage());
                }
            }
            eu = null;
        }
    }
}
