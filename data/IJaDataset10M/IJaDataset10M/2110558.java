package cc.mallet.grmm.inference;

import gnu.trove.THashSet;
import java.util.Iterator;
import cc.mallet.grmm.types.Factor;
import cc.mallet.grmm.types.FactorGraph;
import cc.mallet.grmm.types.Variable;

/**
 * Implements the tree-based schedule of belief propagation for exact inference
 *  in trees.  Can be used either for sum-product or max-product.
 * <p>
 * <p>
 * Do not use the
 * Created: Feb 1, 2006
 *
 * @author <A HREF="mailto:casutton@cs.umass.edu>casutton@cs.umass.edu</A>
 * @version $Id: TreeBP.java,v 1.1 2007/10/22 21:37:49 mccallum Exp $
 */
public class TreeBP extends AbstractBeliefPropagation {

    private transient THashSet marked;

    private transient Variable root;

    public static TreeBP createForMaxProduct() {
        return (TreeBP) new TreeBP().setMessager(new MaxProductMessageStrategy());
    }

    public void computeMarginals(FactorGraph fg) {
        initForGraph(fg);
        marked = new THashSet();
        lambdaPropagation(fg, null, root);
        marked = new THashSet();
        piPropagation(fg, root);
    }

    protected void initForGraph(FactorGraph fg) {
        super.initForGraph(fg);
        root = (Variable) fg.variablesIterator().next();
    }

    private void lambdaPropagation(FactorGraph mdl, Factor parent, Variable child) {
        logger.finer("lambda propagation " + parent + " , " + child);
        marked.add(child);
        for (Iterator it = mdl.allFactorsContaining(child).iterator(); it.hasNext(); ) {
            Factor gchild = (Factor) it.next();
            if (!marked.contains(gchild)) {
                lambdaPropagation(mdl, child, gchild);
            }
        }
        if (parent != null) {
            sendMessage(mdl, child, parent);
        }
    }

    private void lambdaPropagation(FactorGraph mdl, Variable parent, Factor child) {
        logger.finer("lambda propagation " + parent + " , " + child);
        marked.add(child);
        for (Iterator it = child.varSet().iterator(); it.hasNext(); ) {
            Variable gchild = (Variable) it.next();
            if (!marked.contains(gchild)) {
                lambdaPropagation(mdl, child, gchild);
            }
        }
        if (parent != null) {
            sendMessage(mdl, child, parent);
        }
    }

    private void piPropagation(FactorGraph mdl, Variable var) {
        logger.finer("Pi propagation from " + var);
        marked.add(var);
        for (Iterator it = mdl.allFactorsContaining(var).iterator(); it.hasNext(); ) {
            Factor child = (Factor) it.next();
            if (!marked.contains(child)) {
                sendMessage(mdl, var, child);
                piPropagation(mdl, child);
            }
        }
    }

    private void piPropagation(FactorGraph mdl, Factor factor) {
        logger.finer("Pi propagation from " + factor);
        marked.add(factor);
        for (Iterator it = factor.varSet().iterator(); it.hasNext(); ) {
            Variable child = (Variable) it.next();
            if (!marked.contains(child)) {
                sendMessage(mdl, factor, child);
                piPropagation(mdl, child);
            }
        }
    }
}
