package net.sf.openforge.schedule.block;

import java.util.*;
import net.sf.openforge.lim.*;
import net.sf.openforge.lim.io.*;
import net.sf.openforge.lim.memory.*;

/**
 * ProcessIdentifier is a helper class used to generate a collection
 * of processes that need to be scheduled against.  This class
 * identifies processes by analyzing the sequence of accesses to state
 * bearing resources such as memories and registers.  The process
 * endpoints are defined to be the first and last access to each
 * resource. 
 *
 * <p>Created: Wed Sep  1 08:25:22 2004
 *
 * @author imiller, last modified by $Author: imiller $
 * @version $Id: ProcessIdentifier.java 122 2006-03-30 18:05:17Z imiller $
 */
public class ProcessIdentifier extends DataFlowVisitor {

    private static final String _RCS_ = "$Rev: 122 $";

    private ProcessIdentifier() {
        super();
        setRunForward(true);
    }

    /**
     * Generates a map of StateHolder key to MemProcess value.  The
     * StateHolder is the 'reason' for the process and the MemProcess
     * identifies the endpoints of the process (first and last
     * accesses) and the critical context for these endpoints.
     *
     * @param vis a value of type 'Visitable'
     * @return a Map of {@link StateHolder} to {@link MemProcess} 
     */
    public static Map generateProcesses(Visitable vis) {
        ProcessIdentifier pid = new ProcessIdentifier();
        vis.accept(pid);
        Map processMap = new HashMap();
        Set keys = pid.getProcessKeys();
        for (Iterator iter = keys.iterator(); iter.hasNext(); ) {
            Object key = iter.next();
            Set start = (Set) pid.stateToFirst.get(key);
            Set end = (Set) pid.stateToLast.get(key);
            assert start != null && start.size() > 0 : "No valid start point found for process";
            assert end != null && end.size() > 0 : "No valid end point found for process";
            if (!start.equals(end)) {
                MemProcess process = new MemProcess(start, end);
                if (_block.db) _block.ln("Process " + process.debug());
                processMap.put(key, process);
            }
        }
        return processMap;
    }

    private Map stateToFirst = new HashMap();

    private Map stateToLast = new HashMap();

    /** A set of StateHolder objects that have been identified such
     * that as we reach new accesses the 'firsts' should not be
     * aggregated.  That is, any StateHolder identified here has only
     * 1 call to the 'register' method to register first accesses.  If
     * the stateID is not in here, then each call to register simply
     * adds more potential first accesses to the set of
     * possibles. This is to handle register reads which have no inter
     * dependencies. */
    private Set nonAggregating = new HashSet();

    /**
     * Registers the access as the first access iff no other access to
     * the given target has been registered, and registers the access
     * as the last access, always overwriting any prior last access.
     *
     * @param acc a non-null StateAccessor
     */
    private void register(StateAccessor acc) {
        StateHolder stateID = acc.getStateHolder();
        register(stateID, Collections.singleton(acc));
    }

    private void register(StateHolder stateID, Collection accessors) {
        if (_block.db) _block.ln("Identified " + stateID + " <= " + accessors);
        final boolean aggregateFirsts = !nonAggregating.contains(stateID);
        if (!aggregateFirsts) {
            if (!stateToFirst.containsKey(stateID)) {
                stateToFirst.put(stateID, accessors);
            }
        } else {
            Collection firsts = (Collection) stateToFirst.get(stateID);
            if (firsts == null) {
                firsts = new HashSet();
            }
            firsts.addAll(accessors);
            stateToFirst.put(stateID, firsts);
        }
        stateToLast.put(stateID, accessors);
    }

    private Set getProcessKeys() {
        Set keys = new HashSet();
        keys.addAll(stateToFirst.keySet());
        keys.addAll(stateToLast.keySet());
        return keys;
    }

    public void visit(TaskCall mod) {
        ProcessIdentifier pid = new ProcessIdentifier();
        mod.getTarget().accept(pid);
        for (Iterator iter = pid.getProcessKeys().iterator(); iter.hasNext(); ) {
            Object key = iter.next();
            Set targets = new HashSet();
            if (pid.stateToFirst.containsKey(key)) targets.addAll((Collection) pid.stateToFirst.get(key));
            if (pid.stateToLast.containsKey(key)) targets.addAll((Collection) pid.stateToLast.get(key));
            register((StateHolder) key, Collections.unmodifiableSet(targets));
        }
        this.nonAggregating.addAll(pid.nonAggregating);
    }

    public void visit(SimplePinAccess point) {
        nonAggregating.add(point.getStateHolder());
        register(point);
    }

    public void visit(FifoAccess point) {
        nonAggregating.add(point.getStateHolder());
        register(point);
    }

    public void visit(FifoRead point) {
        nonAggregating.add(point.getStateHolder());
        register(point);
    }

    public void visit(FifoWrite point) {
        nonAggregating.add(point.getStateHolder());
        register(point);
    }

    public void visit(MemoryRead point) {
        nonAggregating.add(point.getStateHolder());
        register(point);
    }

    public void visit(MemoryWrite point) {
        nonAggregating.add(point.getStateHolder());
        register(point);
    }

    /**
     * Any register read which comes before a register write is
     * potentially a 'first' access because they do not depend on one
     * another.  So, we will continue to 'aggregate' them until we hit
     * a register write (to the same register).
     * <p>NOTE:  This will work so long as we hit all the register
     * reads before descending into a sub-module which contains a
     * register write.  I fear that our data flow visitor may not
     * uphold this contract for us.  If it does not (ie if it descends
     * into a sub-module and then visits another register read
     * afterwards where that next register read does not depend on the
     * traversed module) then we need to modify the data flow visitor
     * to ensure that it traverses ALL components with satisfied
     * dependencies before it traverses any contained modules.
     *
     * @param point a value of type 'RegisterRead'
     */
    public void visit(RegisterRead point) {
        register(point);
    }

    public void visit(RegisterWrite point) {
        nonAggregating.add(point.getStateHolder());
        register(point);
    }

    /**
     * Visit the true and false branches.  Register any targetted
     * resource found in either branch as joined.  Thus the last found
     * will be a set of the lasts from each branch.
     *
     * @param branch a value of type 'Branch'
     */
    public void visit(Branch branch) {
        Set components = new HashSet(branch.getComponents());
        components.remove(branch.getDecision());
        components.remove(branch.getTrueBranch());
        components.remove(branch.getFalseBranch());
        components.remove(branch.getInBuf());
        branch.getInBuf().accept(this);
        branch.getDecision().accept(this);
        ProcessIdentifier trueIdentifier = new ProcessIdentifier();
        branch.getTrueBranch().accept(trueIdentifier);
        ProcessIdentifier falseIdentifier = new ProcessIdentifier();
        branch.getFalseBranch().accept(falseIdentifier);
        Set keys = new HashSet();
        keys.addAll(trueIdentifier.getProcessKeys());
        keys.addAll(falseIdentifier.getProcessKeys());
        for (Iterator iter = keys.iterator(); iter.hasNext(); ) {
            Object key = iter.next();
            Set targets = new HashSet();
            if (trueIdentifier.stateToFirst.containsKey(key)) targets.addAll((Collection) trueIdentifier.stateToFirst.get(key));
            if (falseIdentifier.stateToFirst.containsKey(key)) targets.addAll((Collection) falseIdentifier.stateToFirst.get(key));
            if (trueIdentifier.stateToLast.containsKey(key)) targets.addAll((Collection) trueIdentifier.stateToLast.get(key));
            if (falseIdentifier.stateToLast.containsKey(key)) targets.addAll((Collection) falseIdentifier.stateToLast.get(key));
            register((StateHolder) key, Collections.unmodifiableSet(targets));
        }
        this.nonAggregating.addAll(trueIdentifier.nonAggregating);
        this.nonAggregating.addAll(falseIdentifier.nonAggregating);
    }

    public String toString() {
        String ret = super.toString();
        return ret.substring(ret.lastIndexOf(".") + 1);
    }
}
