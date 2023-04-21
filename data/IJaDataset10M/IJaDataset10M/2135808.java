package org.doit.muffin.filter;

import java.awt.*;
import java.awt.event.*;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.TreeSet;
import org.doit.muffin.*;
import org.doit.util.*;

public class StatsFrame extends MuffinFrame implements ActionListener, WindowListener {

    /**
	 * Serializable should define this:
	 */
    private static final long serialVersionUID = 1L;

    Prefs prefs;

    Stats parent;

    TextArea text;

    public StatsFrame(Prefs prefs, Stats parent) {
        super(Strings.getString("Stats.title"));
        this.prefs = prefs;
        this.parent = parent;
        text = new TextArea();
        text.setEditable(false);
        add("Center", text);
        Button b;
        Panel buttonPanel = new Panel();
        buttonPanel.setLayout(new GridLayout(1, 4));
        b = new Button(Strings.getString("Stats.update"));
        b.setActionCommand("doUpdate");
        b.addActionListener(this);
        buttonPanel.add(b);
        b = new Button(Strings.getString("Stats.reset"));
        b.setActionCommand("doReset");
        b.addActionListener(this);
        buttonPanel.add(b);
        b = new Button(Strings.getString("close"));
        b.setActionCommand("doClose");
        b.addActionListener(this);
        buttonPanel.add(b);
        b = new Button(Strings.getString("help"));
        b.setActionCommand("doHelp");
        b.addActionListener(this);
        buttonPanel.add(b);
        add("South", buttonPanel);
        addWindowListener(this);
        pack();
        setSize(getPreferredSize());
        show();
    }

    void reset() {
        text.setText("");
        parent.reset();
    }

    void print(Hashtable h) {
        String key;
        Integer count;
        int total = 0;
        for (Iterator i = (new TreeSet(h.keySet())).iterator(); i.hasNext(); ) {
            key = (String) i.next();
            count = (Integer) h.get(key);
            text.append("    " + key + ": " + count + "\n");
            total += count.intValue();
        }
        text.append(Strings.getString("total") + ": " + total + "\n");
    }

    void printUniqueServers() {
        String s;
        Integer i, y;
        Hashtable servers = new Hashtable(100);
        for (Enumeration e = parent.servers.keys(); e.hasMoreElements(); ) {
            s = (String) e.nextElement();
            i = (Integer) parent.servers.get(s);
            StringTokenizer st = new StringTokenizer(s, "/");
            s = (String) st.nextToken();
            if (s.startsWith("Netscape")) {
                s = new String("Netscape");
            }
            if (servers.containsKey(s)) {
                y = (Integer) servers.get(s);
                y = new Integer(y.intValue() + i.intValue());
                servers.put(s, y);
            } else {
                servers.put(s, i);
            }
        }
        text.append(Strings.getString("Stats.uniqueServers") + ": " + servers.size() + "\n");
        print(servers);
        text.append("\n");
    }

    void update() {
        text.setText("");
        text.append(Strings.getString("Stats.requests") + ": ");
        text.append(parent.requests + "\n");
        text.append("\n");
        text.append(Strings.getString("Stats.replies") + ": ");
        text.append(parent.replies + "\n");
        text.append("\n");
        text.append(Strings.getString("Stats.hosts") + ": " + parent.hosts.size() + "\n");
        print(parent.hosts);
        text.append("\n");
        text.append(Strings.getString("Stats.servers") + ": " + parent.servers.size() + "\n");
        print(parent.servers);
        text.append("\n");
        printUniqueServers();
        text.append("Content-types:\n");
        print(parent.contentTypes);
        text.append("\n");
        text.append("Content-lengths:\n");
        print(parent.contentLengths);
        text.append("\n");
    }

    public void actionPerformed(ActionEvent event) {
        String arg = event.getActionCommand();
        if ("doUpdate".equals(arg)) {
            update();
        } else if ("doReset".equals(arg)) {
            reset();
        } else if ("doClose".equals(arg)) {
            setVisible(false);
        } else if ("doHelp".equals(arg)) {
            new HelpFrame("Stats");
        }
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowDeactivated(WindowEvent e) {
    }

    public void windowClosing(WindowEvent e) {
        setVisible(false);
    }

    public void windowClosed(WindowEvent e) {
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowOpened(WindowEvent e) {
    }
}
