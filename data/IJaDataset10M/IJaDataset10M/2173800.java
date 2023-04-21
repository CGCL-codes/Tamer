package org.kumenya.compiler.phases;

import org.kumenya.api.compiler.*;
import org.kumenya.compiler.Nodes;
import java.util.*;

/**
 * TODO: skip the traversal of functions for performance reason.
 * <p/>
 * TODO: this optimization can actually produces wrong translation.
 * By example, we need to consider associatity rules of outer join.
 *
 * @author Jean Morissette
 */
class PredicatePushdown extends NodeTranslator implements Phase {

    public Node perform(Node node, DiagnosticListener logger) {
        node.accept(this, null);
        return node;
    }

    private static class Entry {

        List<Node> predicates;

        List<List<String>> vars;

        private Entry(List<Node> predicates, List<List<String>> vars) {
            this.predicates = predicates;
            this.vars = vars;
        }
    }

    private Map<Node.Filter, Entry> candidates = new HashMap();

    private NodeFactory factory;

    public Node visitFilter(Node.Filter node, Object o) {
        List<Node> predicates = new ArrayList();
        unfoldPredicate(node.predicate, predicates);
        List<List<String>> vars = new LinkedList();
        for (Node n : predicates) {
            vars.add(Nodes.getFreeVariables(n));
        }
        candidates.put(node, new Entry(predicates, vars));
        super.visitFilter(node, o);
        Entry e = candidates.remove(node);
        if (e.predicates.isEmpty()) {
            return node.operand;
        } else {
            node.predicate = foldPredicates(e.predicates);
            return node;
        }
    }

    public Node visitJoin(Node.Join node, Object o) {
        super.visitJoin(node, o);
        for (Entry e : candidates.values()) {
            for (int i = e.predicates.size() - 1; i >= 0; i--) {
                org.kumenya.api.compiler.Node p = e.predicates.get(i);
                List<String> vars = e.vars.get(i);
                if (node.type.getComponentType().getMemberLabels().containsAll(vars)) {
                    if (node.predicate == null) {
                        node.predicate = p;
                    } else {
                        node.predicate = factory.createBinaryLogical(Node.AND, node.predicate, p);
                    }
                    e.predicates.remove(i);
                    e.vars.remove(i);
                }
            }
        }
        return node;
    }

    /**
     * Returns the list of predicate composing the given predicate.
     */
    private void unfoldPredicate(Node node, List result) {
        if (node instanceof Node.BinaryLogical) {
            Node.BinaryLogical b = (Node.BinaryLogical) node;
            if (b.operator == Node.AND) {
                unfoldPredicate(b.lhs, result);
                unfoldPredicate(b.rhs, result);
            } else {
                result.add(node);
            }
        } else {
            result.add(node);
        }
    }

    private Node foldPredicates(List<Node> predicates) {
        assert (predicates.size() > 0);
        Iterator<Node> it = predicates.iterator();
        Node p = it.next();
        while (it.hasNext()) {
            p = factory.createBinaryLogical(Node.AND, p, it.next());
        }
        return p;
    }
}
