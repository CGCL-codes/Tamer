package flow;

import java.util.*;

public class DominatingSet {

    FlowGraph g;

    Map<BasicBlock, Set<BasicBlock>> dominating = new HashMap<BasicBlock, Set<BasicBlock>>();

    public DominatingSet(FlowGraph g) {
        this.g = g;
        analysis();
    }

    public Set<BasicBlock> get(BasicBlock b) {
        return dominating.get(b);
    }

    private void explore(BasicBlock b, Set<BasicBlock> visited) {
        if (visited.contains(b)) return;
        visited.add(b);
        for (BasicBlock s : g.succ(b)) explore(s, visited);
    }

    private void analysis() {
        if (g.entry == null) return;
        Map<BasicBlock, Set<BasicBlock>> dominated = new HashMap<BasicBlock, Set<BasicBlock>>();
        Set<BasicBlock> full = new HashSet<BasicBlock>();
        explore(g.entry, full);
        for (BasicBlock b : g.nodes()) {
            dominating.put(b, new HashSet<BasicBlock>());
            if (b != g.entry) dominated.put(b, new HashSet<BasicBlock>(full)); else {
                dominated.put(b, new HashSet<BasicBlock>());
                dominated.get(b).add(b);
            }
        }
        boolean change = false;
        do {
            change = false;
            for (BasicBlock b : g.nodes()) {
                Set<BasicBlock> t = new HashSet<BasicBlock>(dominated.get(b));
                for (BasicBlock p : g.pred(b)) t.retainAll(dominated.get(p));
                t.add(b);
                if (!t.equals(dominated.get(b))) {
                    change = true;
                    dominated.put(b, t);
                }
            }
        } while (change);
        for (BasicBlock b : g.nodes()) {
            for (BasicBlock d : dominated.get(b)) dominating.get(d).add(b);
        }
    }
}
