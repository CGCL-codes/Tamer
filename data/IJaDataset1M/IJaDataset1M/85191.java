package de.uniba.wiai.kinf.pw.projects.lillytab.reasoner.abox.completer;

import de.uniba.wiai.kinf.pw.projects.lillytab.abox.IABoxNode;
import de.uniba.wiai.kinf.pw.projects.lillytab.reasoner.abox.*;
import de.uniba.wiai.kinf.pw.projects.lillytab.util.LoggingClass;
import java.util.Collection;
import java.util.List;

/**
 *
 * @param <Name>
 * @param <Klass>
 * @author Peter Wullinger <peter.wullinger@uni-bamberg.de>
 */
public abstract class AbstractCompleter<Name extends Comparable<? super Name>, Klass extends Comparable<? super Klass>, Role extends Comparable<? super Role>> extends LoggingClass implements ICompleter<Name, Klass, Role> {

    private final INodeConsistencyChecker<Name, Klass, Role> _cChecker;

    private final boolean _trace;

    public AbstractCompleter(final INodeConsistencyChecker<Name, Klass, Role> cChecker, final boolean trace) {
        _cChecker = cChecker;
        _trace = trace;
    }

    public AbstractCompleter(final INodeConsistencyChecker<Name, Klass, Role> cChecker) {
        this(cChecker, false);
    }

    public INodeConsistencyChecker<Name, Klass, Role> getNodeConsistencyChecker() {
        return _cChecker;
    }

    protected boolean isTracing() {
        return _trace;
    }

    /**
	 * <p>
	 * Traverse the list of {@link BranchCreationInfo}s and update
	 * the branch queue, where necessary.
	 * </p><p>
	 * Also validate, if the modifications to the current branch
	 * require rechecking the node queue.
	 * </p>
	 * @param branchCreationInfos A list of recent {@link BranchCreationInfo}s.
	 * @param currentBranch The current (base) branch.
	 * @param node The current node.
	 * @param branchQueue The branch queue.
	 * @return {@literal true}, if processing can continue uninterruptedly on the current branch.
	 *	{@literal false}, if the node queue needs to be rechecked before further processing.
	 *
	 */
    protected boolean handleBranchCreation(final List<BranchCreationInfo<Name, Klass, Role>> branchCreationInfos, Branch<Name, Klass, Role> currentBranch, IABoxNode<Name, Klass, Role> node, Collection<Branch<Name, Klass, Role>> branchQueue) {
        boolean canContinue = true;
        for (BranchCreationInfo<Name, Klass, Role> bci : branchCreationInfos) {
            if (currentBranch == bci.getTargetBranch()) {
                if (bci.getMergeInfo().getMergedNodes().contains(node)) {
                    canContinue = false;
                }
            } else {
                branchQueue.add(bci.getTargetBranch());
            }
            if (!canContinue) return true;
        }
        return false;
    }
}
