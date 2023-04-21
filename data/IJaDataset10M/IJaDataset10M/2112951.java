package gov.nasa.ltl.trans;

import gov.nasa.ltl.graph.*;
import java.util.*;

/**
 * DOCUMENT ME!
 */
class Transition {

    private TreeSet<Formula> propositions;

    private int pointsTo;

    private BitSet accepting;

    private boolean safe_accepting;

    public Transition(TreeSet<Formula> prop, int nd_id, BitSet acc, boolean safety) {
        propositions = prop;
        pointsTo = nd_id;
        accepting = new BitSet(Node.getAcceptingConds());
        accepting.or(acc);
        safe_accepting = safety;
    }

    public void FSPoutput() {
        if (propositions.isEmpty()) {
            System.out.print("TRUE{");
        } else {
            Iterator<Formula> it = propositions.iterator();
            Formula nextForm = null;
            StringBuilder act = new StringBuilder();
            char cont;
            boolean need_AND = false;
            while (it.hasNext()) {
                nextForm = it.next();
                cont = nextForm.getContent();
                if (need_AND) {
                    act.append("_AND_");
                }
                need_AND = true;
                switch(cont) {
                    case 'N':
                        act.append('N');
                        act.append(nextForm.getSub1().getName());
                        break;
                    case 't':
                        act.append("TRUE");
                        break;
                    default:
                        act.append(nextForm.getName());
                        break;
                }
            }
            act.append('{');
            System.out.print(act);
        }
        if (Node.accepting_conds == 0) {
            if (safe_accepting == true) {
                System.out.print("0");
            }
        } else {
            for (int i = 0; i < Node.accepting_conds; i++) {
                if (!accepting.get(i)) {
                    System.out.print(i);
                }
            }
        }
        System.out.print("} -> S" + pointsTo + " ");
    }

    public void SMoutput(gov.nasa.ltl.graph.Node[] nodes, gov.nasa.ltl.graph.Node node) {
        String guard = "-";
        String action = "-";
        if (!propositions.isEmpty()) {
            Iterator<Formula> it = propositions.iterator();
            Formula nextForm = null;
            StringBuilder sb = new StringBuilder();
            char cont;
            boolean need_AND = false;
            while (it.hasNext()) {
                nextForm = it.next();
                cont = nextForm.getContent();
                if (need_AND) {
                    sb.append('&');
                }
                need_AND = true;
                switch(cont) {
                    case 'N':
                        sb.append('!');
                        sb.append(nextForm.getSub1().getName());
                        break;
                    case 't':
                        sb.append("true");
                        break;
                    default:
                        sb.append(nextForm.getName());
                        break;
                }
            }
            guard = sb.toString();
        }
        Edge e = new Edge(node, nodes[pointsTo], guard, action);
        if (Node.accepting_conds == 0) {
            e.setBooleanAttribute("acc0", true);
        } else {
            for (int i = 0; i < Node.accepting_conds; i++) {
                if (!accepting.get(i)) {
                    e.setBooleanAttribute("acc" + i, true);
                }
            }
        }
    }

    public boolean enabled(Hashtable<String, Boolean> ProgramState) {
        Iterator<Formula> mustHold = propositions.iterator();
        Formula form = null;
        Boolean value;
        while (mustHold.hasNext()) {
            form = mustHold.next();
            switch(form.getContent()) {
                case 'N':
                    value = ProgramState.get(form.getSub1().getName());
                    if (value == null) {
                        return false;
                    } else if (value.booleanValue()) {
                        return false;
                    }
                    break;
                case 't':
                    break;
                case 'p':
                    value = ProgramState.get(form.getName());
                    if (value == null) {
                        return false;
                    } else if (!value.booleanValue()) {
                        return false;
                    }
                    break;
            }
        }
        return true;
    }

    public int goesTo() {
        return pointsTo;
    }
}
