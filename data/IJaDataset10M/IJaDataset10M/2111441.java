package at.ofai.aaa.agent.jam;

import java.util.Vector;

/**
 * Represents conditional branching plan components.
 * @author Marc Huber
 * @author Jaeho Lee
 */
class PlanBranchConstruct implements PlanConstruct {

    /** Enum for the types of branching constructs in plans, maybe we can get rid of this. */
    static enum BranchType {

        NO_BRANCH, AND_BRANCH, OR_BRANCH
    }

    private BranchType branchType;

    private Vector<PlanSequenceConstruct> branches;

    /**  */
    PlanBranchConstruct() {
        this.branchType = BranchType.NO_BRANCH;
        this.branches = new Vector<PlanSequenceConstruct>(1, 1);
    }

    /**  */
    PlanBranchConstruct(final PlanSequenceConstruct s, final BranchType pBranchType) {
        this.branchType = pBranchType;
        this.branches = new Vector<PlanSequenceConstruct>(1, 1);
        addBranch(s);
    }

    int getNumBranches() {
        return this.branches.size();
    }

    BranchType getBranchType() {
        return this.branchType;
    }

    /**  */
    public PlanRuntimeState newRuntimeState() {
        return new PlanRuntimeBranchState(this);
    }

    /**  */
    void setBranchType(final BranchType bt) {
        this.branchType = bt;
    }

    /**  */
    PlanSequenceConstruct getBranch(final int branchnum) {
        return (branchnum >= 0 && branchnum < this.branches.size()) ? this.branches.elementAt(branchnum) : null;
    }

    /**  */
    void addBranch(final PlanConstruct be) {
        if (be != null) {
            if (be instanceof PlanSequenceConstruct) {
                this.branches.addElement((PlanSequenceConstruct) be);
            } else {
                PlanSequenceConstruct ns;
                ns = new PlanSequenceConstruct(be);
                this.branches.addElement(ns);
            }
        }
    }
}
