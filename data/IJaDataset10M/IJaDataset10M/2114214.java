package ec.gp.breed;

import ec.*;
import ec.util.*;
import ec.gp.*;

/**
 * MutateDemotePipeline works very similarly to the DemoteNode algorithm
 * described in  Kumar Chellapilla,
 * "A Preliminary Investigation into Evolving Modular Programs without Subtree
 * Crossover", GP98, and is also similar to the "insertion" operator found in
 * Una-May O'Reilly's thesis,
 * <a href="http://www.ai.mit.edu/people/unamay/thesis.html">
 * "An Analysis of Genetic Programming"</a>.
 *
 * <p>MutateDemotePipeline tries picks a random tree, then picks
 * randomly from all the demotable nodes in the tree, and demotes one.  
 * If its chosen tree has no demotable nodes, or demoting
 * its chosen demotable node would make the tree too deep, it repeats
 * the choose-tree-then-choose-node process.  If after <i>tries</i> times
 * it has failed to find a valid tree and demotable node, it gives up and simply
 * copies the individual.
 *
 * <p>"Demotion" means to take a node <i>n</i> and insert a new node <i>m</i>
 * between <i>n</i> and <i>n</i>'s parent.  <i>n</i> becomes a child of
 * <i>m</i>; the place where it becomes a child is determined at random
 * from all the type-compatible slots of <i>m</i>.  The other child slots
 * of <i>m</i> are filled with randomly-generated terminals.  
 * Chellapilla's version of the algorithm always
 * places <i>n</i> in child slot 0 of <i>m</i>.  Because this would be
 * unneccessarily restrictive on strong typing, MutateDemotePipeline instead
 * picks the slot at random from all available valid choices.
 *
 * <p>A "Demotable" node means a node which is capable of demotion
 * given the existing function set.  In general to demote a node <i>foo</i>,
 * there must exist in the function set a nonterminal whose return type
 * is type-compatible with the child slot <i>foo</i> holds in its parent;
 * this nonterminal must also have a child slot which is type-compatible
 * with <i>foo</i>'s return type.
 *
 * <p>This method is very expensive in searching nodes for
 * "demotability".  However, if the number of types is 1 (the
 * GP run is typeless) then the type-constraint-checking
 * code is bypassed and the method runs a little faster.
 *

 <p><b>Typical Number of Individuals Produced Per <tt>produce(...)</tt> call</b><br>
 ...as many as the source produces

 <p><b>Number of Sources</b><br>
 1

 <p><b>Parameters</b><br>
 <table>
 <tr><td valign=top><i>base</i>.<tt>tries</tt><br>
 <font size=-1>int &gt;= 1</font></td>
 <td valign=top>(number of times to try finding valid pairs of nodes)</td></tr>

 <tr><td valign=top><i>base</i>.<tt>maxdepth</tt><br>
 <font size=-1>int &gt;= 1</font></td>
 <td valign=top>(maximum valid depth of a mutated tree)</td></tr>

 <tr><td valign=top><i>base</i>.<tt>tree.0</tt><br>
 <font size=-1>0 &lt; int &lt; (num trees in individuals), if exists</font></td>
 <td valign=top>(tree chosen for mutation; if parameter doesn't exist, tree is picked at random)</td></tr>

 </table>

 <p><b>Default Base</b><br>
 gp.breed.mutate-demote


 * @author Sean Luke
 * @version 1.0 
 */
public class MutateDemotePipeline extends GPBreedingPipeline {

    public static final String P_MUTATEDEMOTE = "mutate-demote";

    public static final String P_NUM_TRIES = "tries";

    public static final String P_MAXDEPTH = "maxdepth";

    public static final int NUM_SOURCES = 1;

    /** The number of times the pipeline tries to build a valid mutated
        tree before it gives up and just passes on the original */
    int numTries;

    /** The maximum depth of a mutated tree */
    int maxDepth;

    /** Is our tree fixed?  If not, this is -1 */
    int tree;

    /** Temporary Node Gatherer */
    private GPNodeGatherer gatherer;

    public MutateDemotePipeline() {
        gatherer = new GPNodeGatherer();
    }

    public Parameter defaultBase() {
        return GPBreedDefaults.base().push(P_MUTATEDEMOTE);
    }

    public int numSources() {
        return NUM_SOURCES;
    }

    public void setup(final EvolutionState state, final Parameter base) {
        super.setup(state, base);
        Parameter def = defaultBase();
        numTries = state.parameters.getInt(base.push(P_NUM_TRIES), def.push(P_NUM_TRIES), 1);
        if (numTries == 0) state.output.fatal("MutateDemotePipeline has an invalid number of tries (it must be >= 1).", base.push(P_NUM_TRIES), def.push(P_NUM_TRIES));
        maxDepth = state.parameters.getInt(base.push(P_MAXDEPTH), def.push(P_MAXDEPTH), 1);
        if (maxDepth == 0) state.output.fatal("The MutateDemotePipeline " + base + "has an invalid maximum depth (it must be >= 1).", base.push(P_MAXDEPTH), def.push(P_MAXDEPTH));
        tree = TREE_UNFIXED;
        if (state.parameters.exists(base.push(P_TREE).push("" + 0), def.push(P_TREE).push("" + 0))) {
            tree = state.parameters.getInt(base.push(P_TREE).push("" + 0), def.push(P_TREE).push("" + 0), 0);
            if (tree == -1) state.output.fatal("Tree fixed value, if defined, must be >= 0");
        }
    }

    public Object clone() {
        MutateDemotePipeline obj = (MutateDemotePipeline) (super.clone());
        obj.gatherer = new GPNodeGatherer();
        return obj;
    }

    private boolean demotable(final GPInitializer initializer, final GPNode node, final GPFunctionSet set) {
        GPType t;
        if (node.parent instanceof GPNode) t = ((GPNode) (node.parent)).constraints(initializer).childtypes[node.argposition]; else t = ((GPTree) (node.parent)).constraints(initializer).treetype;
        for (int x = 0; x < set.nonterminals[t.type].length; x++) for (int y = 0; y < set.nonterminals[t.type][x].constraints(initializer).childtypes.length; y++) if (set.nonterminals[t.type][x].constraints(initializer).childtypes[y].compatibleWith(initializer, node.constraints(initializer).returntype)) return true;
        return false;
    }

    private void demoteSomething(final GPNode node, final EvolutionState state, final int thread, final GPFunctionSet set) {
        if (((GPInitializer) state.initializer).numAtomicTypes + ((GPInitializer) state.initializer).numSetTypes == 1) _demoteSomethingTypeless(node, state, thread, set); else _demoteSomething(node, state, thread, set);
    }

    private void _demoteSomething(final GPNode node, final EvolutionState state, final int thread, final GPFunctionSet set) {
        int numDemotable = 0;
        GPType t;
        GPInitializer initializer = ((GPInitializer) state.initializer);
        if (node.parent instanceof GPNode) t = ((GPNode) (node.parent)).constraints(initializer).childtypes[node.argposition]; else t = ((GPTree) (node.parent)).constraints(initializer).treetype;
        for (int x = 0; x < set.nonterminals[t.type].length; x++) for (int y = 0; y < set.nonterminals[t.type][x].constraints(initializer).childtypes.length; y++) if (set.nonterminals[t.type][x].constraints(initializer).childtypes[y].compatibleWith(initializer, node.constraints(initializer).returntype)) {
            numDemotable++;
            break;
        }
        int demoteItem = state.random[thread].nextInt(numDemotable);
        numDemotable = 0;
        for (int x = 0; x < set.nonterminals[t.type].length; x++) for (int y = 0; y < set.nonterminals[t.type][x].constraints(initializer).childtypes.length; y++) if (set.nonterminals[t.type][x].constraints(initializer).childtypes[y].compatibleWith(initializer, node.constraints(initializer).returntype)) {
            if (numDemotable == demoteItem) {
                GPNode cnode = (GPNode) (set.nonterminals[t.type][x].lightClone());
                int numSpots = 0;
                GPType retyp = node.constraints(initializer).returntype;
                GPType[] chityp = cnode.constraints(initializer).childtypes;
                for (int z = 0; z < cnode.children.length; z++) if (chityp[z].compatibleWith(initializer, retyp)) numSpots++;
                int choice = state.random[thread].nextInt(numSpots);
                numSpots = 0;
                for (int z = 0; z < cnode.children.length; z++) if (chityp[z].compatibleWith(initializer, retyp)) {
                    if (numSpots == choice) {
                        cnode.parent = node.parent;
                        cnode.argposition = node.argposition;
                        cnode.children[z] = node;
                        node.parent = cnode;
                        node.argposition = (byte) z;
                        if (cnode.parent instanceof GPNode) ((GPNode) (cnode.parent)).children[cnode.argposition] = cnode; else ((GPTree) (cnode.parent)).child = cnode;
                        numSpots++;
                    } else {
                        GPNode term = (GPNode) (set.terminals[chityp[z].type][state.random[thread].nextInt(set.terminals[chityp[z].type].length)].lightClone());
                        cnode.children[z] = term;
                        term.parent = cnode;
                        term.argposition = (byte) z;
                        term.resetNode(state, thread);
                        numSpots++;
                    }
                } else {
                    GPNode term = (GPNode) (set.terminals[chityp[z].type][state.random[thread].nextInt(set.terminals[chityp[z].type].length)].lightClone());
                    cnode.children[z] = term;
                    term.parent = cnode;
                    term.argposition = (byte) z;
                    term.resetNode(state, thread);
                }
                return;
            } else {
                numDemotable++;
                break;
            }
        }
        throw new InternalError("Bug in demoteSomething -- should never be able to reach the end of the function");
    }

    private void _demoteSomethingTypeless(final GPNode node, final EvolutionState state, final int thread, final GPFunctionSet set) {
        int numDemotable = 0;
        numDemotable = set.nonterminals[0].length;
        int demoteItem = state.random[thread].nextInt(numDemotable);
        numDemotable = 0;
        GPNode cnode = (GPNode) (set.nonterminals[0][demoteItem].lightClone());
        GPType[] chityp = cnode.constraints(((GPInitializer) state.initializer)).childtypes;
        int choice = state.random[thread].nextInt(cnode.children.length);
        for (int z = 0; z < cnode.children.length; z++) if (z == choice) {
            cnode.parent = node.parent;
            cnode.argposition = node.argposition;
            cnode.children[z] = node;
            node.parent = cnode;
            node.argposition = (byte) z;
            if (cnode.parent instanceof GPNode) ((GPNode) (cnode.parent)).children[cnode.argposition] = cnode; else ((GPTree) (cnode.parent)).child = cnode;
        } else {
            GPNode term = (GPNode) (set.terminals[chityp[z].type][state.random[thread].nextInt(set.terminals[chityp[z].type].length)].lightClone());
            cnode.children[z] = term;
            term.parent = cnode;
            term.argposition = (byte) z;
            term.resetNode(state, thread);
        }
    }

    private int numDemotableNodes(final GPInitializer initializer, final GPNode root, int soFar, final GPFunctionSet set) {
        if (initializer.numAtomicTypes + initializer.numSetTypes == 1) return root.numNodes(GPNode.NODESEARCH_ALL); else return _numDemotableNodes(initializer, root, soFar, set);
    }

    private int _numDemotableNodes(final GPInitializer initializer, final GPNode root, int soFar, final GPFunctionSet set) {
        if (demotable(initializer, root, set)) soFar++;
        for (int x = 0; x < root.children.length; x++) soFar = _numDemotableNodes(initializer, root.children[x], soFar, set);
        return soFar;
    }

    private GPNode demotableNode;

    private int pickDemotableNode(final GPInitializer initializer, final GPNode root, int num, final GPFunctionSet set) {
        if (initializer.numAtomicTypes + initializer.numSetTypes == 1) {
            gatherer.node = null;
            root.nodeInPosition(num, gatherer, GPNode.NODESEARCH_ALL);
            if (gatherer.node == null) throw new InternalError("Internal error in pickDemotableNode, nodeInPosition didn't find a node!");
            demotableNode = gatherer.node;
            return -1;
        } else return _pickDemotableNode(initializer, root, num, set);
    }

    private int _pickDemotableNode(final GPInitializer initializer, final GPNode root, int num, final GPFunctionSet set) {
        if (demotable(initializer, root, set)) {
            num--;
            if (num == -1) {
                demotableNode = root;
                return num;
            }
        }
        for (int x = 0; x < root.children.length; x++) {
            num = _pickDemotableNode(initializer, root.children[x], num, set);
            if (num == -1) break;
        }
        return num;
    }

    /** Returns true if inner1's depth + atdepth +1 is within the depth bounds */
    private boolean verifyPoint(GPNode inner1) {
        if (inner1.depth() + inner1.atDepth() + 1 > maxDepth) return false;
        return true;
    }

    public int produce(final int min, final int max, final int start, final int subpopulation, final Individual[] inds, final EvolutionState state, final int thread) {
        int n = sources[0].produce(min, max, start, subpopulation, inds, state, thread);
        if (!state.random[thread].nextBoolean(likelihood)) return reproduce(n, start, subpopulation, inds, state, thread, false);
        GPInitializer initializer = ((GPInitializer) state.initializer);
        for (int q = start; q < n + start; q++) {
            GPIndividual i = (GPIndividual) inds[q];
            if (tree != TREE_UNFIXED && (tree < 0 || tree >= i.trees.length)) state.output.fatal("MutateDemotePipeline attempted to fix tree.0 to a value which was out of bounds of the array of the individual's trees.  Check the pipeline's fixed tree values -- they may be negative or greater than the number of trees in an individual");
            GPIndividual j;
            if (sources[0] instanceof BreedingPipeline) {
                j = i;
            } else {
                j = (GPIndividual) (i.lightClone());
                j.trees = new GPTree[i.trees.length];
                for (int x = 0; x < j.trees.length; x++) {
                    j.trees[x] = (GPTree) (i.trees[x].lightClone());
                    j.trees[x].owner = j;
                    j.trees[x].child = (GPNode) (i.trees[x].child.clone());
                    j.trees[x].child.parent = j.trees[x];
                    j.trees[x].child.argposition = 0;
                }
            }
            for (int x = 0; x < numTries; x++) {
                int t;
                if (tree == TREE_UNFIXED) if (i.trees.length > 1) t = state.random[thread].nextInt(i.trees.length); else t = 0; else t = tree;
                int numdemote = numDemotableNodes(initializer, j.trees[t].child, 0, j.trees[t].constraints(initializer).functionset);
                if (numdemote == 0) continue;
                pickDemotableNode(initializer, j.trees[t].child, state.random[thread].nextInt(numdemote), j.trees[t].constraints(initializer).functionset);
                if (!verifyPoint(demotableNode)) continue;
                demoteSomething(demotableNode, state, thread, j.trees[t].constraints(initializer).functionset);
                j.evaluated = false;
                break;
            }
            inds[q] = j;
        }
        return n;
    }
}
