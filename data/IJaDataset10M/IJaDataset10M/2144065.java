package de.uniba.wiai.kinf.pw.projects.lillytab.reasoner.abox.completer;

import de.uniba.wiai.kinf.pw.projects.lillytab.abox.IABox;
import de.uniba.wiai.kinf.pw.projects.lillytab.abox.IABoxNode;
import de.uniba.wiai.kinf.pw.projects.lillytab.abox.IRBox;
import de.uniba.wiai.kinf.pw.projects.lillytab.abox.NodeID;
import de.uniba.wiai.kinf.pw.projects.lillytab.abox.RoleProperty;
import de.uniba.wiai.kinf.pw.projects.lillytab.reasoner.abox.Branch;
import de.uniba.wiai.kinf.pw.projects.lillytab.reasoner.abox.INodeConsistencyChecker;
import de.uniba.wiai.kinf.pw.projects.lillytab.reasoner.abox.ReasonerException;
import java.util.Collection;

public class TransitiveRoleCompleter<Name extends Comparable<? super Name>, Klass extends Comparable<? super Klass>, Role extends Comparable<? super Role>> extends AbstractCompleter<Name, Klass, Role> implements ICompleter<Name, Klass, Role> {

    public TransitiveRoleCompleter(final INodeConsistencyChecker<Name, Klass, Role> cChecker) {
        super(cChecker);
    }

    public TransitiveRoleCompleter(final INodeConsistencyChecker<Name, Klass, Role> cChecker, boolean trace) {
        super(cChecker, trace);
    }

    public boolean completeNode(IABoxNode<Name, Klass, Role> node, Branch<Name, Klass, Role> branch, Collection<Branch<Name, Klass, Role>> branchQueue) throws ReasonerException {
        final IABox<Name, Klass, Role> abox = node.getABox();
        final IRBox<Name, Klass, Role> rbox = abox.getTBox().getRBox();
        boolean wasModified = false;
        for (Role role : node.getPredecessors().keySet()) {
            if (rbox.hasRoleProperty(role, RoleProperty.TRANSITIVE) && node.getPredecessors().containsKey(role)) {
                for (NodeID predID : node.getPredecessors().get(role)) {
                    Collection<NodeID> succIDs = node.getSuccessors().get(role);
                    if (succIDs != null) {
                        for (NodeID succID : succIDs) {
                            final IABoxNode<Name, Klass, Role> pred = abox.getNode(predID);
                            if (!pred.getSuccessors().containsValue(role, succID)) {
                                pred.getSuccessors().put(role, succID);
                                branch.touch(predID);
                                branch.touch(succID);
                                wasModified = true;
                                if (isTracing()) {
                                    logFiner("Added transitive %s-link from %s to %s", role, predID, succID);
                                }
                            }
                        }
                    }
                }
            }
        }
        return !wasModified;
    }
}
