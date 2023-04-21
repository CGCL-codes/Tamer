package admin.command;

import static db.DB.begin;
import static db.DB.commit;
import static db.DB.queryOne;
import static db.DB.rollback;
import java.util.Set;
import java.util.StringTokenizer;
import org.hibernate.Session;
import common.CmdLine;
import db.Account;
import db.Channel;
import admin.Application;

public class OpenChannel extends AbstractCommand {

    public OpenChannel(Object app, CmdLine cmdLine) {
        super(app, cmdLine);
    }

    public String getName() {
        return "open-channel";
    }

    public void run(Object params) {
        StringTokenizer tok = (StringTokenizer) params;
        Session s = null;
        try {
            String name = tok.nextToken();
            s = begin();
            Channel c = (Channel) queryOne(s, Channel.class, "channelName", name);
            if (c != null) {
                Set<Account> members = c.getMembers();
                String[] presences = new String[members.size()];
                int i = 0;
                for (Account a : members) {
                    presences[i] = a.getAccountName();
                    i++;
                }
                Application app = (Application) getApp();
                app.sendMessage(new message.admin.OpenChannel(name, presences));
            }
            commit(s);
        } catch (Exception ex) {
            ex.printStackTrace();
            if (s != null) {
                try {
                    rollback(s);
                } catch (Exception innerex) {
                }
            }
        }
    }
}
