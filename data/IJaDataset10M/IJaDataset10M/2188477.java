package drcl.net.graph;

import java.io.*;
import java.util.regex.Pattern;

public class TopologyReaderAlt extends TopologyReader {

    static boolean DEBUG = true;

    String parameter = null;

    public String parameter() {
        return parameter;
    }

    public void parse(Reader r_) throws Exception {
        BufferedReader r = new BufferedReader(r_);
        parameter = r.readLine() + "\n";
        String line_ = r.readLine();
        parameter = parameter + line_ + "\n";
        String[] ll = null;
        try {
            Pattern pattern_ = Pattern.compile("[ \t]");
            ll = pattern_.split(line_);
            int nnodes_ = Integer.parseInt(ll[0]);
            int nlinks_ = Integer.parseInt(ll[1]);
            while (!r.readLine().startsWith("VERTICES")) ;
            nodes = new Node[nnodes_];
            if (DEBUG) System.out.println(nodes.length + " nodes to be read....");
            for (int i = 0; i < nodes.length; i++) {
                line_ = r.readLine();
                ll = null;
                if (line_ == null) throw new RuntimeException("Expect " + nodes.length + " nodes but got only " + i);
                ll = pattern_.split(line_);
                double x = Double.valueOf(ll[2]).doubleValue();
                double y = Double.valueOf(ll[3]).doubleValue();
                nodes[i] = new Node(i, x, y);
            }
            while (!r.readLine().startsWith("EDGES")) ;
            links = new Link[nlinks_];
            if (DEBUG) System.out.println(links.length + " links to be read....");
            for (int i = 0; i < links.length; i++) {
                line_ = r.readLine();
                ll = null;
                if (line_ == null) throw new RuntimeException("Expect " + links.length + " links but got only " + i);
                ll = pattern_.split(line_);
                int i1 = Integer.parseInt(ll[0]);
                int i2 = Integer.parseInt(ll[1]);
                links[i] = new Link(i, nodes[i1], nodes[i2]);
            }
        } catch (Exception e_) {
            e_.printStackTrace();
            System.out.println(line_);
            if (ll != null) for (int i = 0; i < ll.length; i++) System.out.println("Term " + i + ": " + ll[i]);
            System.exit(1);
        }
    }
}
