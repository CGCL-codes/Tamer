package x10me.opt.controlflow;

import java.util.Enumeration;
import x10me.opt.controlflow.BasicBlockEnumeration;
import x10me.opt.ir.IR;
import x10me.opt.util.Stack;

/**
 * Calculate dominators using Langauer and Tarjan's fastest algorithm.
 * TOPLAS 1(1), July 1979.  This implementation uses path compression and
 * results in a O(e * alpha(e,n)) complexity, where e is the number of
 * edges in the CFG and n is the number of nodes.
 *
 * Sources: TOPLAS article, Muchnick book
 *
 * The current implementation (4/25/00) does not include the EXIT node
 * in any solution despite the fact that it is part of the CFG (it has
 * incoming edges).  This is to be compatible with the old code.
 */
public class LTDominators extends Stack<BasicBlock> {

    static final boolean DEBUG = false;

    /**
   * Indicates whether we perform the algorithm over the CFG or
   *  the reverse CFG, i.e., whether we are computing dominators or
   *  post-dominators.
   */
    private final boolean forward;

    /**
   * The ir.
   */
    private IR ir;

    /**
   * a counter for assigning DFS numbers
   */
    protected int DFSCounter;

    /**
   * a mapping from DFS number to their basic blocks
   */
    private BasicBlock[] vertex;

    /**
   * a convenient place to locate the cfg to avoid passing it internally
   */
    private final ControlFlowGraph cfg;

    /**
   * The constructor, called by the perform method
   * @param ir
   * @param forward Should we compute regular dominators, or post-dominators?
   */
    LTDominators(IR ir, boolean forward) {
        cfg = ir.cfg;
        this.forward = forward;
    }

    /**
   * The entry point for this phase
   * @param ir the IR
   * @param forward Should we compute regular dominators, or post-dominators?
   * @param unfactor Should we unfactor the CFG?
   */
    public static void perform(IR ir, boolean forward, boolean unfactor) {
        if (ir.hasReachableExceptionHandlers()) {
            if (unfactor) {
                ir.unfactor();
            } else {
                throw new Error("IR with exception handlers");
            }
        }
        LTDominators dom = new LTDominators(ir, forward);
        dom.analyze(ir);
    }

    /**
   * Compute approximate dominator/post dominator without unfactoring
   * exception handlers.  Can only be used if what the client wants is
   * approximate domination (ie, if it doesn't actually have to be correct...)
   * @param ir the IR
   * @param forward Should we compute regular dominators, or post-dominators?
   */
    public static void approximate(IR ir, boolean forward) {
        LTDominators dom = new LTDominators(ir, forward);
        dom.analyze(ir);
    }

    /**
   * analyze dominators
   */
    protected void analyze(IR ir) {
        if (DEBUG) {
            ir.dumpFile.current.println("   Here's the CFG for method: " + ir.method.getName() + "\n" + ir.cfg);
        }
        step1();
        checkReachability(ir);
        step2();
        step3();
        if (DEBUG) {
            printResults(ir);
        }
    }

    /**
   * Check to make sure all nodes were reached
   */
    private void checkReachability(IR ir) {
        if (!forward) {
            if (DFSCounter != cfg.numberOfNodes()) {
                System.err.println(" *** Warning ***\n CFG for method " + ir.method.getName() + " in class " + ir.method.getDeclaringClass() + " has unreachable nodes.");
                System.err.println(" Assuming pessimistic results in dominators computation\n" + " for unreachable nodes.");
            }
        }
    }

    /**
   *  The goal of this step is to perform a DFS numbering on the CFG,
   *  starting at the root.  The exit node is not included.
   */
    private void step1() {
        vertex = new BasicBlock[cfg.numberOfNodes() + 1];
        DFSCounter = 0;
        if (DEBUG) {
            ir.dumpFile.current.println("Initializing blocks:");
        }
        for (Enumeration<BasicBlock> bbEnum = cfg.basicBlocks(); bbEnum.hasMoreElements(); ) {
            BasicBlock block = bbEnum.nextElement();
            if (!forward || !block.isExit()) {
                block.scratchObject = new LTDominatorInfo(block);
                if (DEBUG) {
                    printNextNodes(block);
                }
            }
        }
        DFS();
        if (DEBUG) {
            ir.dumpFile.current.println("DFSCounter: " + DFSCounter + ", CFG Nodes: " + cfg.numberOfNodes());
            printDFSNumbers();
        }
    }

    private void DFS() {
        DFS(getFirstNode());
    }

    /**
   * Get the first node, either entry or exit
   * depending on which way we are viewing the graph
   * @return the entry node or exit node
   */
    private BasicBlock getFirstNode() {
        if (forward) {
            return cfg.entry();
        } else {
            return cfg.exit();
        }
    }

    /**
   * Print the "next" nodes (either out or in) for the passed block
   * depending on which way we are viewing the graph
   * @param block the basic block of interest
   */
    private void printNextNodes(BasicBlock block) {
        if (forward) {
            ir.dumpFile.current.print(block + " Succs:");
        } else {
            ir.dumpFile.current.print(block + " Preds:");
        }
        BasicBlockEnumeration e = getNextNodes(block);
        while (e.hasMoreElements()) {
            ir.dumpFile.current.print(" ");
            ir.dumpFile.current.print(e.next());
        }
        ir.dumpFile.current.println();
    }

    /**
   * Returns an enumeration of the "next" nodes (either out or in) for the
   * passed block depending on which way we are viewing the graph
   * @param block the basic block of interest
   */
    private BasicBlockEnumeration getNextNodes(BasicBlock block) {
        BasicBlockEnumeration bbEnum;
        if (forward) {
            bbEnum = block.getOut();
        } else {
            bbEnum = block.getIn();
        }
        return bbEnum;
    }

    /**
   * Returns an enumeration of the "prev" nodes (either in or out) for the
   * passed block depending on which way we are viewing the graph
   * @param block the basic block of interest
   */
    private BasicBlockEnumeration getPrevNodes(BasicBlock block) {
        BasicBlockEnumeration bbEnum;
        if (forward) {
            bbEnum = block.getIn();
        } else {
            bbEnum = block.getOut();
        }
        return bbEnum;
    }

    /**
   * The non-recursive depth-first numbering code called from Step 1.
   * The recursive version was too costly on the toba benchmark on Linux/IA32.
   * @param block the basic block to process
   */
    protected void DFS(BasicBlock block) {
        push(block);
        recurse: while (!empty()) {
            block = peek();
            if (DEBUG) {
                ir.dumpFile.current.println(" Processing (peek)" + block);
            }
            if (block == null) {
                if (DEBUG) {
                    ir.dumpFile.current.println(" Popping");
                }
                pop();
                continue;
            }
            if (forward && block == cfg.exit()) {
                if (DEBUG) {
                    ir.dumpFile.current.println(" Popping");
                }
                pop();
                continue;
            }
            BasicBlockEnumeration e;
            e = LTDominatorInfo.getInfo(block).getEnum();
            if (e == null) {
                if (DEBUG) {
                    ir.dumpFile.current.println(" Initial processing of " + block);
                }
                DFSCounter++;
                LTDominatorInfo.getInfo(block).setSemiDominator(DFSCounter);
                vertex[DFSCounter] = block;
                e = getNextNodes(block);
            } else {
                if (DEBUG) {
                    ir.dumpFile.current.println(" Resuming processing of " + block);
                }
            }
            while (e.hasMoreElements()) {
                BasicBlock next = e.next();
                if (DEBUG) {
                    ir.dumpFile.current.println("    Inspecting next node: " + next);
                }
                if (forward && next.isExit()) {
                    continue;
                }
                if (getSemi(next) == 0) {
                    LTDominatorInfo.getInfo(next).setParent(block);
                    LTDominatorInfo.getInfo(block).setEnum(e);
                    if (DEBUG) {
                        ir.dumpFile.current.println(" Pushing" + next);
                    }
                    push(next);
                    continue recurse;
                }
            }
            if (DEBUG) {
                ir.dumpFile.current.println(" Popping");
            }
            pop();
        }
    }

    /**
   *  This is the heart of the algorithm.  See sources for details.
   */
    private void step2() {
        if (DEBUG) {
            ir.dumpFile.current.println(" ******* Beginning STEP 2 *******\n");
        }
        for (int i = DFSCounter; i > 1; i--) {
            BasicBlock block = vertex[i];
            LTDominatorInfo blockInfo = LTDominatorInfo.getInfo(block);
            if (DEBUG) {
                ir.dumpFile.current.println(" Processing: " + block + "\n");
            }
            BasicBlockEnumeration e = getPrevNodes(block);
            while (e.hasMoreElements()) {
                BasicBlock prev = e.next();
                if (DEBUG) {
                    ir.dumpFile.current.println("    Inspecting prev: " + prev);
                }
                BasicBlock u = EVAL(prev);
                if (getSemi(u) != 0 && getSemi(u) < getSemi(block)) {
                    blockInfo.setSemiDominator(getSemi(u));
                }
            }
            LTDominatorInfo.getInfo(vertex[blockInfo.getSemiDominator()]).addToBucket(block);
            LINK(blockInfo.getParent(), block);
            java.util.Iterator<BasicBlock> bucketEnum = LTDominatorInfo.getInfo(getParent(block)).getBucketIterator();
            while (bucketEnum.hasNext()) {
                BasicBlock block2 = bucketEnum.next();
                BasicBlock u = EVAL(block2);
                if (getSemi(u) < getSemi(block2)) {
                    LTDominatorInfo.getInfo(block2).setDominator(u);
                } else {
                    LTDominatorInfo.getInfo(block2).setDominator(getParent(block));
                }
            }
        }
    }

    /**
   * This method inspects the passed block and returns the following:
   *    block,                       if block is a root of a tree in the forest
   *
   *    any vertex, u != r such that                        otherwise
   *      r is the root of the tree containing block and
   *                                        *
   *      semi(u) is minimum on the path  r -> v
   *
   * See TOPLAS 1(1), July 1979, p 128 for details.
   *
   * @param block the block to evaluate
   * @return the block as described above
   */
    private BasicBlock EVAL(BasicBlock block) {
        if (DEBUG) {
            ir.dumpFile.current.println("  Evaling " + block);
        }
        if (getAncestor(block) == null) {
            return getLabel(block);
        } else {
            compress(block);
            if (getSemi(getLabel(getAncestor(block))) >= getSemi(getLabel(block))) {
                return getLabel(block);
            } else {
                return getLabel(getAncestor(block));
            }
        }
    }

    /**
   *  This recursive method performs the path compression
   *  @param block the block of interest
   */
    private void compress(BasicBlock block) {
        if (getAncestor(getAncestor(block)) != null) {
            compress(getAncestor(block));
            LTDominatorInfo blockInfo = LTDominatorInfo.getInfo(block);
            if (getSemi(getLabel(getAncestor(block))) < getSemi(getLabel(block))) {
                blockInfo.setLabel(getLabel(getAncestor(block)));
            }
            blockInfo.setAncestor(getAncestor(getAncestor(block)));
        }
    }

    /**
   *  Adds edge (block1, block2) to the forest maintained as an auxiliary
   *  data structure.  This implementation uses path compression and
   *  results in a O(e * alpha(e,n)) complexity, where e is the number of
   *  edges in the CFG and n is the number of nodes.
   *
   *  @param block1 a basic block corresponding to the source of the new edge
   *  @param block2 a basic block corresponding to the source of the new edge
   */
    private void LINK(BasicBlock block1, BasicBlock block2) {
        if (DEBUG) {
            ir.dumpFile.current.println("  Linking " + block1 + " with " + block2);
        }
        BasicBlock s = block2;
        while (getSemi(getLabel(block2)) < getSemi(getLabel(getChild(s)))) {
            if (getSize(s) + getSize(getChild(getChild(s))) >= 2 * getSize(getChild(s))) {
                LTDominatorInfo.getInfo(getChild(s)).setAncestor(s);
                LTDominatorInfo.getInfo(s).setChild(getChild(getChild(s)));
            } else {
                LTDominatorInfo.getInfo(getChild(s)).setSize(getSize(s));
                LTDominatorInfo.getInfo(s).setAncestor(getChild(s));
                s = getChild(s);
            }
        }
        LTDominatorInfo.getInfo(s).setLabel(getLabel(block2));
        LTDominatorInfo.getInfo(block1).setSize(getSize(block1) + getSize(block2));
        if (getSize(block1) < 2 * getSize(block2)) {
            BasicBlock tmp = s;
            s = getChild(block1);
            LTDominatorInfo.getInfo(block1).setChild(tmp);
        }
        while (s != null) {
            LTDominatorInfo.getInfo(s).setAncestor(block1);
            s = getChild(s);
        }
        if (DEBUG) {
            ir.dumpFile.current.println("  .... done");
        }
    }

    /**
   *  This final step sets the final dominator information.
   */
    private void step3() {
        for (int i = 2; i <= DFSCounter; i++) {
            BasicBlock block = vertex[i];
            if (getDom(block) != vertex[getSemi(block)]) {
                LTDominatorInfo.getInfo(block).setDominator(getDom(getDom(block)));
            }
        }
    }

    /**
   * Returns the current dominator for the passed block
   * @param block
   * @return the domiator for the passed block
   */
    private BasicBlock getDom(BasicBlock block) {
        return LTDominatorInfo.getInfo(block).getDominator();
    }

    /**
   * Returns the parent for the passed block
   * @param block
   * @return the parent for the passed block
   */
    private BasicBlock getParent(BasicBlock block) {
        return LTDominatorInfo.getInfo(block).getParent();
    }

    /**
   * Returns the ancestor for the passed block
   * @param block
   * @return the ancestor for the passed block
   */
    private BasicBlock getAncestor(BasicBlock block) {
        return LTDominatorInfo.getInfo(block).getAncestor();
    }

    /**
   * returns the label for the passed block or null if the block is null
   * @param block
   * @return the label for the passed block or null if the block is null
   */
    private BasicBlock getLabel(BasicBlock block) {
        if (block == null) {
            return null;
        }
        return LTDominatorInfo.getInfo(block).getLabel();
    }

    /**
   * Returns the current semidominator for the passed block
   * @param block
   * @return the semidominator for the passed block
   */
    private int getSemi(BasicBlock block) {
        if (block == null) {
            return 0;
        }
        return LTDominatorInfo.getInfo(block).getSemiDominator();
    }

    /**
   * returns the size associated with the block
   * @param block
   * @return the size of the block or 0 if the block is null
   */
    private int getSize(BasicBlock block) {
        if (block == null) {
            return 0;
        }
        return LTDominatorInfo.getInfo(block).getSize();
    }

    /**
   * Get the child node for this block
   * @param block
   * @return the child node
   */
    private BasicBlock getChild(BasicBlock block) {
        return LTDominatorInfo.getInfo(block).getChild();
    }

    /**
   * Print the nodes that dominate each basic block
   * @param ir the IR
   */
    private void printResults(IR ir) {
        if (forward) {
            ir.dumpFile.current.println("Results of dominators computation for method " + ir.method.getName() + "\n");
            ir.dumpFile.current.println("   Here's the CFG:");
            ir.dumpFile.current.println(ir.cfg);
            ir.dumpFile.current.println("\n\n  Here's the Dominator Info:");
        } else {
            ir.dumpFile.current.println("Results of Post-Dominators computation for method " + ir.method.getName() + "\n");
            ir.dumpFile.current.println("   Here's the CFG:");
            ir.dumpFile.current.println(ir.cfg);
            ir.dumpFile.current.println("\n\n  Here's the Post-Dominator Info:");
        }
        for (Enumeration<BasicBlock> bbEnum = cfg.basicBlocks(); bbEnum.hasMoreElements(); ) {
            BasicBlock block = bbEnum.nextElement();
            if (!forward || !block.isExit()) {
                ir.dumpFile.current.println("Dominators of " + block + ":" + LTDominatorInfo.getInfo(block).dominators(block, ir));
            }
        }
        ir.dumpFile.current.println("\n");
    }

    /**
   *  Print the result of the DFS numbering performed in Step 1
   */
    private void printDFSNumbers() {
        for (Enumeration<BasicBlock> bbEnum = cfg.basicBlocks(); bbEnum.hasMoreElements(); ) {
            BasicBlock block = bbEnum.nextElement();
            if (forward && block.isExit()) {
                continue;
            }
            LTDominatorInfo info = (LTDominatorInfo) block.scratchObject;
            ir.dumpFile.current.println(" " + block + " " + info);
        }
        for (int i = 1; i <= DFSCounter; i++) {
            ir.dumpFile.current.println(" Vertex: " + i + " " + vertex[i]);
        }
    }
}
