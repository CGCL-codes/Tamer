package algorithms.centralityAlgorithms.rbcRational;

import algorithms.centralityAlgorithms.betweenness.brandes.preprocessing.DataWorkshop;
import common.Pair;
import common.RationalGCD;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import topology.EdgeInterface;
import topology.GraphInterface;
import javolution.util.FastList;
import javolution.util.Index;

/**
 *
 * @author Omer zohar
 */
public class GRBCAlgorithm extends AbsBetweenessAlgorithm<FastList<Index>> {

    public GRBCAlgorithm(GraphInterface<Index> G, AbsRoutingFunction routingFunction, DataWorkshop dw) {
        super(G, routingFunction, dw);
    }

    public GRBCAlgorithm(GraphInterface<Index> G, DataWorkshop dw) {
        super(G, dw);
    }

    @Override
    public void getDelta(Index s, FastList<Index> group, Index t, RationalGCD groupDelta) {
        groupDelta.Zero();
        if (group == null || s == null || t == null) {
            return;
        }
        for (int i = 0; i < group.size(); i++) {
            if (group.get(i).intValue() == s.intValue() || group.get(i).intValue() == t.intValue()) {
                groupDelta.set(1, 1);
                return;
            }
        }
        LinkedList<Index> Q = new LinkedList<Index>();
        Index v;
        Q.add(s);
        HashMap<Index, RationalGCD> delta = new HashMap<Index, RationalGCD>();
        delta.put(s, RationalGCD.makeOne());
        while (Q.size() > 0) {
            v = Q.pop();
            Iterator<EdgeInterface<Index>> succ = G.getOutgoingEdges(v);
            LinkedList<Pair<Index, RationalGCD>> RFCalculation = new LinkedList<Pair<Index, RationalGCD>>();
            while (succ.hasNext()) {
                Index current = G.getNeighbor(succ.next(), v);
                RationalGCD calc = RationalGCD.NaN();
                this.RF.calculate(s.intValue(), v.intValue(), current.intValue(), t.intValue(), calc);
                if (calc.toDouble() > 0) RFCalculation.add(new Pair<Index, RationalGCD>(current, calc));
            }
            for (int i = 0; i < RFCalculation.size(); i++) {
                Index u = RFCalculation.get(i).getValue1();
                RationalGCD pu = RFCalculation.get(i).getValue2();
                if (pu == null) pu.Zero();
                RationalGCD dv = delta.get(v);
                if (dv == null) dv.Zero();
                if (group.contains(u)) {
                    pu.times(dv);
                    groupDelta.add(pu);
                } else {
                    pu.times(dv);
                    delta.put(u, pu);
                    Q.addLast(u);
                }
            }
        }
        assert !(groupDelta.toDouble() > 1.0);
    }
}
