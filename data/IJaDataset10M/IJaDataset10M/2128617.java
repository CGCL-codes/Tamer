package com.ryanm.sage.handlers;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message;
import com.ryanm.sage.Handler;
import com.ryanm.sage.ProcessUtil;
import com.ryanm.sage.ProcessUtil.Listener;
import com.ryanm.sage.SheevaSage;

/**
 * iPlayer downloads? Yes please.
 * 
 * @author ryanm
 */
public class Iplayer extends Handler {

    private File base = new File(System.getProperty("sheevasage.iplayerdir"));

    {
        base.mkdirs();
    }

    private Map<String, String> status = new TreeMap<String, String>();

    @Override
    public boolean handle(Message m, XMPPConnection connection) {
        String c = m.getBody().toLowerCase().trim();
        if (c.startsWith("iplayer")) {
            SheevaSage.reply(m, "I'll have a look", connection);
            final String program = c.substring(7).trim();
            final LinkedList<String> possibles = new LinkedList<String>();
            Listener l = new Listener() {

                boolean pastMatches = false;

                @Override
                public void line(String line) {
                    if (line != null) {
                        if (line.equals("Matches:")) {
                            pastMatches = true;
                        } else if (pastMatches && line.toLowerCase().contains(program)) {
                            possibles.add(line);
                        }
                    }
                }
            };
            try {
                ProcessUtil.execute(true, l, base, "get_iplayer", "-f", "--type", "all");
            } catch (IOException e) {
                SheevaSage.reply(m, "list command error", connection);
                e.printStackTrace();
            }
            if (possibles.isEmpty()) {
                SheevaSage.reply(m, "You sure that's available?", connection);
                return true;
            } else if (possibles.size() == 1) {
                SheevaSage.reply(m, "\nGrabbing " + possibles.getFirst(), connection);
                try {
                    get(possibles.getFirst());
                } catch (IOException e) {
                    SheevaSage.reply(m, "Something's gone wonky!", connection);
                    e.printStackTrace();
                }
            } else {
                StringBuffer buff = new StringBuffer("Which one do you mean?");
                for (String prog : possibles) {
                    buff.append("\n");
                    buff.append(prog);
                }
                SheevaSage.reply(m, buff.toString(), connection);
            }
            return true;
        }
        return false;
    }

    @Override
    public String status() {
        if (!status.isEmpty()) {
            StringBuilder buff = new StringBuilder("Grabbing from iPlayer:");
            for (String prog : status.keySet()) {
                buff.append(prog);
                buff.append("\n\t\t");
                buff.append(status.get(prog));
                buff.append("\n");
            }
            status.clear();
            return buff.toString();
        }
        return null;
    }

    private static final FilenameFilter fnf = new FilenameFilter() {

        @Override
        public boolean accept(File dir, String name) {
            return name.endsWith(".mp4");
        }
    };

    private void get(String line) throws IOException {
        int split = line.indexOf(":");
        String progNumber = line.substring(0, split);
        final String progName = line.substring(split + 1);
        Listener l = new Listener() {

            @Override
            public void line(String out) {
                status.put(progName, out == null ? "Complete" : out);
                if (out == null) {
                    for (File f : base.listFiles(fnf)) {
                        String old = f.getPath();
                        String newName = old.substring(0, old.length() - 3) + "m4v";
                        f.renameTo(new File(newName));
                        try {
                            TwonkyRefresh.refresh();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };
        ProcessUtil.execute(false, l, base, "get_iplayer", "-f", "--type", "all", "--get", progNumber);
    }
}
