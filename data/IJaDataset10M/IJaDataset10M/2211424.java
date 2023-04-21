package org.hfbk.vis.mcp;

import java.awt.Container;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Panel;
import java.awt.ScrollPane;
import java.awt.TextField;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.List;
import org.hfbk.ui.FlowBreakLayout;
import org.hfbk.util.Sleeper;

public class StatsPanel {

    Panel panel;

    static void update(VisMCP2 mcp, Panel pane) {
        List<Pinger.Ping> clients = mcp.clients;
        for (Pinger.Ping c : clients) {
            StatsPanel p = panels.get(c.address);
            if (p == null) {
                p = new StatsPanel();
                panels.put(c.address, p);
                pane.add(p.panel);
            }
            String statDump = mcp.dispatch("javascript:stats.update(client); stats.dump();");
            for (String line : statDump.split("\n")) {
                int split = line.indexOf('\t');
                if (split > 0) {
                    String key = line.substring(0, split);
                    String value = line.substring(split + 1);
                    TextField f = p.fields.get(key);
                    if (f == null) {
                        p.panel.add(new Label(key));
                        f = new TextField(20);
                        f.setEditable(false);
                        p.fields.put(key, f);
                        p.panel.add(f);
                    }
                    f.setText(value);
                }
            }
        }
    }

    static HashMap<SocketAddress, StatsPanel> panels = new HashMap<SocketAddress, StatsPanel>();

    HashMap<String, TextField> fields = new HashMap<String, TextField>();

    public StatsPanel() {
        panel = new Panel(new FlowBreakLayout());
    }

    public static void main(String[] args) {
        final VisMCP2 mcp = new VisMCP2();
        Sleeper.sleep(2000);
        Frame f = new Frame();
        final Panel pane = new Panel();
        new Thread() {

            public void run() {
                while (true) {
                    update(mcp, pane);
                    Sleeper.sleep(1000);
                }
            }
        }.start();
        Container scroller = new ScrollPane();
        scroller.add(pane);
        f.add(scroller);
        f.setSize(400, 400);
        f.setVisible(true);
    }
}
