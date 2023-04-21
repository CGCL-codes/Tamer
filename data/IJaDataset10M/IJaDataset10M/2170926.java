package regAlloc;

import flowGraph.FlowGraph;
import graph.Node;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import temp.Temp;

public class Liveness extends InterferenceGraph {

    Hashtable<temp.Temp, Node> mapTN = new Hashtable<temp.Temp, Node>();

    Hashtable<Node, temp.Temp> mapNT = new Hashtable<Node, temp.Temp>();

    MoveList moves = null, tail = null;

    private Node getNode(temp.Temp t, boolean autoAdd) {
        Node n;
        if ((n = mapTN.get(t)) == null) {
            if (autoAdd) {
                n = this.newNode();
                mapTN.put(t, n);
                mapNT.put(n, t);
                return n;
            } else return null;
        } else return n;
    }

    public Liveness(FlowGraph flow) {
        Hashtable<Node, HashSet<temp.Temp>> def, use, in, out;
        in = new Hashtable<Node, HashSet<temp.Temp>>();
        out = new Hashtable<Node, HashSet<temp.Temp>>();
        def = new Hashtable<Node, HashSet<temp.Temp>>();
        use = new Hashtable<Node, HashSet<temp.Temp>>();
        for (graph.NodeList i = flow.nodes(); i != null; i = i.tail) {
            in.put(i.head, new HashSet<temp.Temp>());
            out.put(i.head, new HashSet<temp.Temp>());
            HashSet<temp.Temp> tmpset = new HashSet<temp.Temp>();
            for (temp.TempList j = flow.def(i.head); j != null; j = j.tail) tmpset.add(j.head);
            def.put(i.head, tmpset);
            tmpset = new HashSet<temp.Temp>();
            for (temp.TempList j = flow.use(i.head); j != null; j = j.tail) tmpset.add(j.head);
            use.put(i.head, tmpset);
        }
        boolean changed;
        do {
            changed = false;
            int cnt = 0;
            for (graph.NodeList i = flow.nodes(); i != null; i = i.tail) {
                int lic = in.get(i.head).size(), loc = out.get(i.head).size();
                HashSet<temp.Temp> tmp = new HashSet<temp.Temp>();
                for (graph.NodeList j = i.head.succ(); j != null; j = j.tail) tmp.addAll(in.get(j.head));
                out.put(i.head, tmp);
                if (tmp.size() != loc) changed = true;
                tmp = new HashSet<temp.Temp>(out.get(i.head));
                tmp.removeAll(def.get(i.head));
                tmp.addAll(use.get(i.head));
                in.put(i.head, tmp);
                cnt++;
                if (tmp.size() != lic) changed = true;
            }
        } while (changed);
        int count = 0;
        for (graph.NodeList i = flow.nodes(); i != null; i = i.tail) {
            boolean moveInstr = flow.isMove(i.head);
            HashSet<temp.Temp> ts = out.get(i.head);
            for (temp.TempList j = flow.def(i.head); j != null; j = j.tail) if (!moveInstr || j.head != ((assem.MOVE) ((flowGraph.AssemFlowGraph) flow).instr(i.head)).use().head) {
                Node nj = getNode(j.head, true);
                Iterator<temp.Temp> itr = ts.iterator();
                while (itr.hasNext()) {
                    Temp p = itr.next();
                    this.addEdge(nj, getNode(p, true));
                    this.addEdge(getNode(p, true), nj);
                }
            }
            if (moveInstr) {
                if (moves == null) moves = tail = new MoveList(getNode(flow.use(i.head).head, true), getNode(flow.def(i.head).head, true), null); else tail = tail.tail = new MoveList(getNode(flow.use(i.head).head, true), getNode(flow.def(i.head).head, true), null);
            }
        }
    }

    public Temp gtemp(Node node) {
        return mapNT.get(node);
    }

    public MoveList moves() {
        return moves;
    }

    public Node tnode(Temp temp) {
        return getNode(temp, false);
    }
}
