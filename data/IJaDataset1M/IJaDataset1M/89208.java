package jbotsimx;

import java.awt.*;
import java.awt.event.*;
import javax.swing.JFrame;
import jbotsim.Link;
import jbotsim.Node;
import jbotsim.Topology;
import jbotsim.Link.Mode;
import jbotsim.Link.Type;

@SuppressWarnings("serial")
public class JConsole extends TextArea implements TextListener {

    protected Topology topo;

    public JConsole(Topology topo) {
        super("", 6, 10, TextArea.SCROLLBARS_VERTICAL_ONLY);
        this.topo = topo;
        setBackground(Color.black);
        setForeground(Color.white);
        addTextListener(this);
    }

    public void textValueChanged(TextEvent event) {
        String s = getText();
        int to = s.lastIndexOf("\n");
        if (to == s.length() - 1 && to != -1) {
            int from = s.lastIndexOf("\n", to - 1) + 1;
            try {
                executeCommand(s.substring(from, to).split("\\s"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void executeCommand(String[] args) throws Exception {
        if (args[0].equals("clear")) {
            topo.clear();
        } else if (args[0].equals("print")) {
            this.setText(topo.toString());
        } else if (args[0].equals("add")) {
            if (args[1].equals("link")) {
                String id1 = args[2];
                String id2 = args[3];
                Node n1 = null, n2 = null;
                for (Node n : topo.getNodes()) {
                    if (n.toString().equals(id1)) n1 = n;
                    if (n.toString().equals(id2)) n2 = n;
                }
                if (n1 != null && n2 != null) topo.addLink(new Link(n1, n2, Type.UNDIRECTED, Mode.WIRED));
            }
        } else if (args[0].equals("load")) {
            topo.clear();
            String exemple = "communicationRange 0.0\nsensingRange 0.0\nA [345.0, 291.0]\nB [419.0, 239.0]\nC [367.0, 367.0]\nNode@671f95 [388.0, 305.0]\nA <--> B\nA <--> C\n";
            topo.fromString(exemple);
        } else if (args[0].equals("tikz")) {
            this.setText(jbotsimx.Tikz.exportTopology(topo));
        } else if (args[0].equals("rename")) {
            Node n = (Node) topo.getProperty("selectedNode");
            if (n != null) n.setProperty("id", args[1]);
        }
    }

    public static void main(String args[]) throws Exception {
        jbotsim.Topology tp = new jbotsim.Topology();
        Node.getModel("default").setSensingRange(25);
        JFrame win = new JFrame();
        jbotsim.ui.JViewer v = new jbotsim.ui.JViewer(tp, false);
        win.add(v.getJTopology(), BorderLayout.CENTER);
        win.add(new JConsole(tp), BorderLayout.SOUTH);
        win.setSize(800, 650);
        win.setVisible(true);
        Node.getModel("default").setCommunicationRange(50);
    }
}
