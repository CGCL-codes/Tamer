package org.doit.muffin.filter;

import org.doit.muffin.*;
import java.io.*;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.Enumeration;
import org.doit.muffin.regexp.Factory;
import org.doit.muffin.regexp.Pattern;
import org.doit.muffin.regexp.Matcher;

public class Translate implements FilterFactory {

    private FilterManager manager;

    private Prefs prefs;

    private TranslateFrame frame = null;

    MessageArea messages = null;

    private Vector passRulesIn = null;

    private Vector passRulesOut = null;

    private Vector reverseRulesIn = null;

    private Vector reverseRulesOut = null;

    public Translate() {
    }

    public void setManager(FilterManager manager) {
        this.manager = manager;
    }

    public void setPrefs(Prefs prefs) {
        this.prefs = prefs;
        boolean o = prefs.getOverride();
        prefs.setOverride(false);
        String filename = "translate";
        prefs.putString("Translate.rules", filename);
        prefs.setOverride(o);
        messages = new MessageArea();
        load();
    }

    public Prefs getPrefs() {
        return prefs;
    }

    public void viewPrefs() {
        if (frame == null) {
            frame = new TranslateFrame(prefs, this);
        }
        frame.setVisible(true);
    }

    public Filter createFilter() {
        Filter f = new TranslateFilter(this);
        f.setPrefs(prefs);
        return f;
    }

    public void shutdown() {
        if (frame != null) {
            frame.dispose();
        }
    }

    void save() {
        manager.save(this);
    }

    Request translate(Request request) {
        Pattern re = null;
        Matcher match = null;
        Enumeration e = passRulesIn.elements();
        String url = request.getURL();
        int index = 0;
        while (match == null && e.hasMoreElements()) {
            re = (Pattern) e.nextElement();
            match = re.getMatch(url);
            index++;
        }
        if (match != null) {
            String s = (String) passRulesOut.elementAt(index - 1);
            String sub = match.substituteInto(s);
            report(request, "PASS RULE #" + index + ": " + url + " -> " + sub);
            request.setURL(sub);
        }
        return request;
    }

    Reply translate(Reply reply) {
        Pattern re = null;
        Matcher match = null;
        Enumeration e = reverseRulesIn.elements();
        String url = reply.getHeaderField("Location");
        int index = 0;
        while (match == null && e.hasMoreElements()) {
            re = (Pattern) e.nextElement();
            match = re.getMatch(url);
            index++;
        }
        if (match != null) {
            String s = (String) reverseRulesOut.elementAt(index - 1);
            String sub = match.substituteInto(s);
            report(reply, "PASS RULE #" + index + ": " + url + " -> " + sub);
            reply.setHeaderField("Location", sub);
        }
        return reply;
    }

    void load() {
        InputStream in = null;
        try {
            UserFile file = prefs.getUserFile(prefs.getString("Translate.rules"));
            in = file.getInputStream();
            load(new InputStreamReader(in));
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            System.out.println(e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
    }

    void load(Reader reader) {
        passRulesIn = new Vector();
        passRulesOut = new Vector();
        reverseRulesIn = new Vector();
        reverseRulesOut = new Vector();
        try {
            BufferedReader in = new BufferedReader(reader);
            String s;
            Pattern blank = Factory.instance().getPattern("^[# \t\n]");
            while ((s = in.readLine()) != null) {
                if (blank.matches(s)) {
                    continue;
                }
                StringTokenizer st = new StringTokenizer(s, " \t");
                String type = st.nextToken();
                if (type.equalsIgnoreCase("PASS")) {
                    passRulesIn.addElement(Factory.instance().getPattern(st.nextToken()));
                    passRulesOut.addElement(st.nextToken());
                } else {
                    reverseRulesIn.addElement(Factory.instance().getPattern(st.nextToken()));
                    reverseRulesOut.addElement(st.nextToken());
                }
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void report(Request request, String message) {
        request.addLogEntry("Translate", message);
        messages.append(message + "\n");
    }

    void report(Reply reply, String message) {
        messages.append(message + "\n");
    }
}
