package admin.command;

import static db.DB.*;
import java.util.StringTokenizer;
import org.hibernate.Session;
import common.CmdLine;
import db.Channel;

public class PrintChannel extends AbstractCommand {

    public PrintChannel(Object app, CmdLine cmdLine) {
        super(app, cmdLine);
    }

    public String getName() {
        return "print-channel";
    }

    public void run(Object params) {
        StringTokenizer tok = (StringTokenizer) params;
        Session s = null;
        try {
            String name = tok.nextToken();
            s = begin();
            Channel c = (Channel) queryOne(s, Channel.class, "channelName", name);
            if (c == null) {
                System.out.println(String.format("no such channel %s", name));
            } else {
                System.out.println(c);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            if (s != null) {
                try {
                    rollback(s);
                } catch (Exception e) {
                }
            }
        }
    }
}
