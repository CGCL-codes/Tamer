package x10me.opt.controlflow;

import java.util.Enumeration;
import x10me.opt.ir.IR;
import x10me.opt.util.Tree;
import x10me.opt.util.TreeNode;
import x10me.util.BitVector;

/**
 * This class provides the abstraction of a dominator tree
 *
 * TODO: we do not support IRs with exception handlers.
 */
public class DominatorTree extends Tree {

    /**
   * True if we are computing regular dominators, false for post-dominators
   */
    private final boolean forward;

    /**
   * The governing IR
   */
    private final IR ir;

    /**
   * A structure used to quickly index into the DominatorVertex tree
   */
    private DominatorTreeNode[] dominatorInfoMap;

    /**
   * Build a dominator tree from an IR. NOTE: the dominator
   * information MUST be computed BEFORE calling this method!
   * We assume the scratch object of each basic block contains
   * the LTDominatorInfo object holding this information.
   *
   * @param ir the governing IR
   * @param forward are we building regular dominators or post-dominators?
   */
    public static void perform(IR ir, boolean forward) {
        final boolean DEBUG = false;
        if (forward) {
            ir.dominatorTree = new DominatorTree(ir, forward);
            if (ir.options.PRINT_DOMINATORS) {
                ir.dumpFile.current.println("Here is the CFG for method " + ir.method.getName() + "\n" + ir.cfg);
                ir.dumpFile.current.println("Here is the Dominator Tree for method " + ir.method.getName() + "\n" + ir.dominatorTree);
            }
        } else {
            ir.postDominatorTree = new DominatorTree(ir, forward);
            if (ir.options.PRINT_POST_DOMINATORS) {
                ir.dumpFile.current.println("Here is the CFG for method " + ir.method.getName() + "\n" + ir.cfg);
                ir.dumpFile.current.println("Here is the Post-Dominator Tree for method " + ir.method.getName() + "\n" + ir.postDominatorTree);
            }
        }
    }

    /**
   * Build a dominator tree from an IR. NOTE: the dominator
   * information MUST be computed BEFORE calling this
   * constructor!
   *
   * @param ir the governing IR
   * @param forward are we building regular dominators or post-dominators?
   */
    public DominatorTree(IR ir, boolean forward) {
        this.ir = ir;
        this.forward = forward;
        ir.resetBasicBlockMap();
        dominatorInfoMap = new DominatorTreeNode[ir.getMaxBasicBlockNumber() + 1];
        for (Enumeration<BasicBlock> bbEnum = ir.cfg.basicBlocks(); bbEnum.hasMoreElements(); ) {
            BasicBlock block = bbEnum.nextElement();
            if (!forward || !block.isExit()) {
                addNode(block);
            }
        }
        setRoot(dominatorInfoMap[getFirstNode().getNumber()]);
        for (Enumeration<BasicBlock> bbEnum = ir.cfg.basicBlocks(); bbEnum.hasMoreElements(); ) {
            BasicBlock block = bbEnum.nextElement();
            if (forward && block.isExit()) {
                continue;
            }
            DominatorTreeNode blockNode = dominatorInfoMap[block.getNumber()];
            if (LTDominatorInfo.getInfo(block) == null) {
                if (ir.dumpFile.current != null) ir.dumpFile.current.println("info field is null for block: " + block);
            }
            BasicBlock parent = LTDominatorInfo.getInfo(block).getDominator();
            if (parent != null) {
                DominatorTreeNode parentNode = dominatorInfoMap[parent.getNumber()];
                parentNode.addChild(blockNode);
            }
        }
    }

    /**
   * Get the first node, either entry or exit
   * depending on which way we are viewing the graph
   * @return the entry node or exit node
   */
    private BasicBlock getFirstNode() {
        if (forward) {
            return ir.cfg.entry();
        } else {
            return ir.cfg.exit();
        }
    }

    /**
   * Enumerate the children of the vertex corresponding to a basic
   * block
   *
   * @param bb the basic block
   * @return an Enumeration of bb's children
   */
    public Enumeration<TreeNode> getChildren(BasicBlock bb) {
        DominatorTreeNode node = dominatorInfoMap[bb.getNumber()];
        return node.getChildren();
    }

    /**
   * Return the parent of the vertex corresponding to a basic
   * block
   *
   * @param bb the basic block
   * @return bb's parent
   */
    public BasicBlock getParent(BasicBlock bb) {
        DominatorTreeNode node = dominatorInfoMap[bb.getNumber()];
        return ((DominatorTreeNode) node.getParent()).getBlock();
    }

    /**
   * Return the (already calculated) dominance frontier for
   * a basic block
   *
   * @param bb the basic block
   * @return a BitVector representing the dominance frontier
   */
    public BitVector getDominanceFrontier(BasicBlock bb) {
        DominatorTreeNode info = dominatorInfoMap[bb.getNumber()];
        return info.getDominanceFrontier();
    }

    /**
   * Return the (already calculated) dominance frontier for
   * a basic block
   *
   * @param number the number of the basic block
   * @return a BitVector representing the dominance frontier
   */
    public BitVector getDominanceFrontier(int number) {
        return getDominanceFrontier(ir.getBasicBlock(number));
    }

    /**
   * Does basic block number b dominate all basic blocks in a set?
   *
   * @param b the number of the basic block to test
   * @param bits BitVector representation of the set of basic blocks to test
   * @return true or false
   */
    public boolean dominates(int b, BitVector bits) {
        for (int i = 0; i < bits.length(); i++) {
            if (!bits.get(i)) {
                continue;
            }
            if (!dominates(b, i)) {
                return false;
            }
        }
        return true;
    }

    /**
   * Does basic block number "master" dominate basic block number "slave"?
   *
   * @param master the number of the proposed "master" basic block
   * @param slave  the number of the proposed "slave block
   * @return "master dominates slave"
   */
    public boolean dominates(int master, int slave) {
        BasicBlock masterBlock = ir.getBasicBlock(master);
        BasicBlock slaveBlock = ir.getBasicBlock(slave);
        DominatorTreeNode masterNode = dominatorInfoMap[masterBlock.getNumber()];
        DominatorTreeNode slaveNode = dominatorInfoMap[slaveBlock.getNumber()];
        return slaveNode.isDominatedBy(masterNode);
    }

    /**
   * Does basic block number "master" dominate basic block number "slave"?
   *
   * @param master the number of the proposed "master" basic block
   * @param slave  the number of the proposed "slave block
   * @return "master dominates slave"
   */
    public boolean dominates(BasicBlock master, BasicBlock slave) {
        DominatorTreeNode masterNode = dominatorInfoMap[master.getNumber()];
        DominatorTreeNode slaveNode = dominatorInfoMap[slave.getNumber()];
        return slaveNode.isDominatedBy(masterNode);
    }

    /**
   * Creates domniator tree nodes for the passed block and adds them to the
   * map.
   * @param b the basic block
   */
    private void addNode(BasicBlock b) {
        DominatorTreeNode node = new DominatorTreeNode(b);
        dominatorInfoMap[b.getNumber()] = node;
    }

    /**
   * Return the distance from the root of the dominator tree to a given
   * basic block
   *
   * @param b the basic block in question
   * @return <code>b</code>'s depth
   */
    public int depth(BasicBlock b) {
        return dominatorInfoMap[b.getNumber()].getDepth();
    }

    /**
   * Return the deepest common dominance ancestor of blocks <code>a</code> and <code>b</code>
   *
   * @param a The first basic block
   * @param b The second basic block
   * @return the deepest common dominance ancestor of blocks <code>a</code>
   *        and <code>b</code>
   */
    public BasicBlock deepestCommonAncestor(BasicBlock a, BasicBlock b) {
        DominatorTreeNode n_a = dominatorInfoMap[a.getNumber()];
        DominatorTreeNode n_b = dominatorInfoMap[b.getNumber()];
        while (n_a != n_b) {
            if (n_a.getDepth() > n_b.getDepth()) {
                n_a = (DominatorTreeNode) n_a.getParent();
            } else {
                n_b = (DominatorTreeNode) n_b.getParent();
            }
        }
        return n_a.getBlock();
    }
}
