package org.mmtk.plan.sapphire;

import org.mmtk.policy.CopyLocal;
import org.mmtk.policy.ReplicatingSpace;
import org.mmtk.policy.Space;
import org.mmtk.plan.*;
import org.mmtk.plan.concurrent.Concurrent;
import org.mmtk.plan.sapphire.sanityChecking.*;
import org.mmtk.utility.Log;
import org.mmtk.utility.heap.VMRequest;
import org.mmtk.utility.options.Options;
import org.mmtk.utility.statistics.Stats;
import org.mmtk.vm.Lock;
import org.mmtk.vm.VM;
import org.vmmagic.pragma.*;
import org.vmmagic.unboxed.*;

@Uninterruptible
public class Sapphire extends Concurrent {

    /** True if allocating into the "higher" semispace */
    public static volatile boolean low = true;

    public static boolean iuTerminationMustCheckRoots = true;

    /** One of the two semi spaces that alternate roles at each collection */
    public static final ReplicatingSpace repSpace0 = new ReplicatingSpace("rep-ss0", VMRequest.create());

    public static final int SS0 = repSpace0.getDescriptor();

    /** One of the two semi spaces that alternate roles at each collection */
    public static final ReplicatingSpace repSpace1 = new ReplicatingSpace("rep-ss1", VMRequest.create());

    public static final int SS1 = repSpace1.getDescriptor();

    public final Trace globalFirstTrace;

    public final Trace globalSecondTrace;

    public static volatile int currentTrace = 0;

    protected static CopyLocal deadFromSpaceBumpPointers = new CopyLocal();

    protected static CopyLocal deadToSpaceBumpPointers = new CopyLocal();

    protected static Lock deadBumpPointersLock = VM.newLock("tackOnLock");

    static {
        fromSpace().prepare(true);
        toSpace().prepare(false);
        deadFromSpaceBumpPointers.rebind(fromSpace());
        deadToSpaceBumpPointers.rebind(toSpace());
    }

    static final PreFirstPhaseFromSpaceLinearSanityScan preFirstPhaseFromSpaceLinearSanityScan = new PreFirstPhaseFromSpaceLinearSanityScan();

    static final PreFirstPhaseToSpaceLinearSanityScan preFirstPhaseToSpaceLinearSanityScan = new PreFirstPhaseToSpaceLinearSanityScan();

    static final PostFirstPhaseFromSpaceLinearSanityScan postFirstPhaseFromSpaceLinearSanityScan = new PostFirstPhaseFromSpaceLinearSanityScan();

    static final PostFirstPhaseToSpaceLinearSanityScan postFirstPhaseToSpaceLinearSanityScan = new PostFirstPhaseToSpaceLinearSanityScan();

    static final PreSecondPhaseFromSpaceLinearSanityScan preSecondPhaseFromSpaceLinearSanityScan = new PreSecondPhaseFromSpaceLinearSanityScan();

    static final PreSecondPhaseToSpaceLinearSanityScan preSecondPhaseToSpaceLinearSanityScan = new PreSecondPhaseToSpaceLinearSanityScan();

    static final PostSecondPhaseFromSpaceLinearSanityScan postSecondPhaseFromSpaceLinearSanityScan = new PostSecondPhaseFromSpaceLinearSanityScan();

    static final PostSecondPhaseToSpaceLinearSanityScan postSecondPhaseToSpaceLinearSanityScan = new PostSecondPhaseToSpaceLinearSanityScan();

    /**
   * Class variables
   */
    protected static final int ALLOC_REPLICATING = Plan.ALLOC_DEFAULT;

    public static final int FIRST_SCAN_SS = 0;

    public static final int SECOND_SCAN_SS = 0;

    public static volatile boolean mutatorsEnabled = true;

    /**
   * Constructor
   */
    public Sapphire() {
        globalFirstTrace = new Trace(metaDataSpace1);
        globalSecondTrace = new Trace(metaDataSpace2);
        collection = Phase.createComplex("collection", null, Phase.schedulePlaceholder(PRE_SANITY_PLACEHOLDER), Phase.scheduleComplex(initPhase), Phase.scheduleGlobal(SAPPHIRE_PREPARE_FIRST_TRACE), Phase.scheduleOnTheFlyMutator(FLUSH_MUTATOR), Phase.scheduleComplex(transitiveClosure), Phase.scheduleGlobal(INSERTION_BARRIER_TERMINATION_CONDITION), Phase.scheduleSpecial(DISABLE_MUTATORS), Phase.scheduleComplex(completeClosurePhase), Phase.schedulePlaceholder(POST_SANITY_PLACEHOLDER), Phase.scheduleSTWmutator(PREPARE), Phase.scheduleGlobal(PREPARE), Phase.scheduleCollector(PREPARE), Phase.scheduleGlobal(SAPPHIRE_PREPARE_SECOND_TRACE), Phase.schedulePlaceholder(PRE_SANITY_PLACEHOLDER), Phase.scheduleComplex(transitiveClosure), Phase.scheduleComplex(forwardPhase), Phase.scheduleComplex(completeClosurePhase), Phase.schedulePlaceholder(POST_SANITY_PLACEHOLDER), Phase.scheduleComplex(finishPhase), Phase.scheduleGlobal(SAPPHIRE_PREPARE_ZERO_TRACE), Phase.scheduleSpecial(ENABLE_MUTATORS));
    }

    protected static final short INSERTION_BARRIER_TERMINATION_CONDITION = Phase.createSimple("insertionBarrierTerminationCondition");

    protected static final short transitiveClosure = Phase.createComplex("transitiveClosure", Phase.scheduleComplex(rootClosurePhase), Phase.scheduleComplex(refTypeClosurePhase));

    /**
   * Perform a (global) collection phase.
   *
   * @param phaseId Collection phase
   */
    @Inline
    public void collectionPhase(short phaseId) {
        if (phaseId == SAPPHIRE_PREPARE_FIRST_TRACE) {
            if (VM.VERIFY_ASSERTIONS) {
                VM.assertions._assert(Sapphire.currentTrace == 0);
                VM.assertions._assert(!globalFirstTrace.hasWork());
                VM.assertions._assert(!globalSecondTrace.hasWork());
            }
            if (Options.verbose.getValue() >= 8) Log.writeln("Switching to 1st trace");
            Sapphire.currentTrace = 1;
            iuTerminationMustCheckRoots = true;
            ;
            MutatorContext.globalViewInsertionBarrier = true;
            MutatorContext.globalViewMutatorMustDoubleAllocate = false;
            MutatorContext.globalViewMutatorMustReplicate = false;
            if (Options.verbose.getValue() >= 8) Log.writeln("Global set insertion barrier about to request handshake");
            VM.collection.requestUpdateBarriers();
            MutatorContext.globalViewMutatorMustDoubleAllocate = true;
            if (Options.verbose.getValue() >= 8) Log.writeln("Global set double allocation barrier about to request handshake");
            VM.collection.requestUpdateBarriers();
            return;
        }
        if (phaseId == INSERTION_BARRIER_TERMINATION_CONDITION) {
            if (Options.verbose.getValue() >= 8) {
                Log.writeln("INSERTION_BARRIER_TERMINATION_CONDITION checking for work");
                Space.printVMMap();
            }
            if (globalFirstTrace.hasWork()) {
                if (Options.verbose.getValue() >= 8) Log.writeln("INSERTION_BARRIER_TERMINATION_CONDITION globalFirstTrace already contains work, mustCheckRoots will be set true");
                Phase.pushScheduledPhase(Phase.scheduleGlobal(INSERTION_BARRIER_TERMINATION_CONDITION));
                Phase.pushScheduledPhase(Phase.scheduleComplex(transitiveClosure));
                iuTerminationMustCheckRoots = true;
                return;
            }
            if (iuTerminationMustCheckRoots) {
                if (Options.verbose.getValue() >= 8) Log.writeln("INSERTION_BARRIER_TERMINATION_CONDITION mustCheckRoots true, will scan roots");
                Phase.pushScheduledPhase(Phase.scheduleGlobal(INSERTION_BARRIER_TERMINATION_CONDITION));
                Phase.pushScheduledPhase(Phase.scheduleCollector(FLUSH_COLLECTOR));
                Phase.pushScheduledPhase(Phase.scheduleOnTheFlyMutator(FLUSH_MUTATOR));
                Phase.pushScheduledPhase(Phase.scheduleComplex(rootScanPhase));
                iuTerminationMustCheckRoots = false;
                return;
            }
            if (Options.verbose.getValue() >= 8) Log.writeln("INSERTION_BARRIER_TERMINATION_CONDITION found no work and did not have to scan roots, done");
            return;
        }
        if (phaseId == PRE_TRACE_LINEAR_SCAN) {
            if (currentTrace == 0) {
                Log.writeln("Global running preFirstPhaseFromSpaceLinearSanityScan and preFirstPhaseToSpaceLinearSanityScan");
                deadFromSpaceBumpPointers.linearScan(Sapphire.preFirstPhaseFromSpaceLinearSanityScan);
                deadToSpaceBumpPointers.linearScan(Sapphire.preFirstPhaseToSpaceLinearSanityScan);
                return;
            }
            if (currentTrace == 2) {
                Log.writeln("Global running preSecondPhaseFromSpaceLinearSanityScan and preSecondPhaseToSpaceLinearSanityScan");
                deadFromSpaceBumpPointers.linearScan(Sapphire.preSecondPhaseFromSpaceLinearSanityScan);
                deadToSpaceBumpPointers.linearScan(Sapphire.preSecondPhaseToSpaceLinearSanityScan);
                return;
            }
        }
        if (phaseId == POST_TRACE_LINEAR_SCAN) {
            if (currentTrace == 1) {
                Log.writeln("Global running postFirstPhaseFromSpaceLinearSanityScan and postFirstPhaseToSpaceLinearSanityScan");
                deadFromSpaceBumpPointers.linearScan(Sapphire.postFirstPhaseFromSpaceLinearSanityScan);
                deadToSpaceBumpPointers.linearScan(Sapphire.postFirstPhaseToSpaceLinearSanityScan);
                return;
            }
            if (currentTrace == 2) {
                Log.writeln("Global running postSecondPhaseFromSpaceLinearSanityScan and postSecondPhaseToSpaceLinearSanityScan");
                deadFromSpaceBumpPointers.linearScan(Sapphire.postSecondPhaseFromSpaceLinearSanityScan);
                deadToSpaceBumpPointers.linearScan(Sapphire.postSecondPhaseToSpaceLinearSanityScan);
                return;
            }
        }
        if (phaseId == Sapphire.PREPARE) {
            if (currentTrace == 0) {
                fromSpace().prepare(false);
                globalFirstTrace.prepareNonBlocking();
            } else if (currentTrace == 1) {
                fromSpace().prepare(true);
                globalSecondTrace.prepareNonBlocking();
            } else {
                VM.assertions.fail("Unknown currentTrace value");
            }
            super.collectionPhase(phaseId);
            return;
        }
        if (phaseId == CLOSURE) {
            return;
        }
        if (phaseId == SAPPHIRE_PREPARE_SECOND_TRACE) {
            if (VM.VERIFY_ASSERTIONS) {
                VM.assertions._assert(Sapphire.currentTrace == 1);
                VM.assertions._assert(!globalFirstTrace.hasWork());
                VM.assertions._assert(!globalSecondTrace.hasWork());
            }
            if (Options.verbose.getValue() >= 8) Log.writeln("Switching to 2nd trace");
            currentTrace = 2;
            return;
        }
        if (phaseId == Sapphire.RELEASE) {
            if (currentTrace == 1) {
                if (Options.verbose.getValue() >= 8) {
                    Space.printVMMap();
                    Space.printUsageMB();
                    Space.printUsagePages();
                }
            } else if (currentTrace == 2) {
                low = !low;
                toSpace().release();
                Sapphire.deadBumpPointersLock.acquire();
                deadFromSpaceBumpPointers.rebind(fromSpace());
                CopyLocal tmp = deadFromSpaceBumpPointers;
                deadFromSpaceBumpPointers = deadToSpaceBumpPointers;
                deadToSpaceBumpPointers = tmp;
                deadToSpaceBumpPointers.rebind(toSpace());
                Sapphire.deadBumpPointersLock.release();
            } else {
                VM.assertions.fail("Unknown currentTrace value");
            }
            super.collectionPhase(phaseId);
            return;
        }
        if (phaseId == Sapphire.COMPLETE) {
            fromSpace().prepare(true);
        }
        if (phaseId == SAPPHIRE_PREPARE_ZERO_TRACE) {
            VM.assertions._assert(Sapphire.currentTrace == 2);
            if (Options.verbose.getValue() >= 8) Log.writeln("Switching to 0th trace");
            Sapphire.currentTrace = 0;
            return;
        }
        if (phaseId == Simple.PREPARE_STACKS) {
            stacksPrepared = false;
            return;
        }
        super.collectionPhase(phaseId);
    }

    /**
   * Return the number of pages reserved for copying.
   *
   * @return The number of pages reserved given the pending
   * allocation, including space reserved for copying.
   */
    public final int getCollectionReserve() {
        return (fromSpace().reservedPages() - toSpace().reservedPages()) + super.getCollectionReserve();
    }

    /**
   * Return the number of pages reserved for use given the pending
   * allocation.  This is <i>exclusive of</i> space reserved for
   * copying.
   *
   * @return The number of pages reserved given the pending
   * allocation, excluding space reserved for copying.
   */
    public int getPagesUsed() {
        return super.getPagesUsed() + toSpace().reservedPages() + fromSpace().reservedPages();
    }

    /**
   * This method controls the triggering of a GC. It is called periodically
   * during allocation. Returns true to trigger a collection.
   *
   * @param spaceFull Space request failed, must recover pages within 'space'.
   * @param space TODO
   * @return True if a collection is requested by the plan.
   */
    protected boolean collectionRequired(boolean spaceFull, Space space) {
        if (space == Sapphire.toSpace()) {
            logPoll(space, "To-space collection requested - ignoring request");
            return false;
        }
        return super.collectionRequired(spaceFull, space);
    }

    /**
   * This method controls the triggering of an atomic phase of a concurrent collection. It is called periodically during allocation.
   * @return True if a collection is requested by the plan.
   */
    @Override
    protected boolean concurrentCollectionRequired(Space space) {
        if (space == Sapphire.toSpace()) {
            logPoll(space, "To-space collection requested - ignoring request");
            return false;
        }
        return !Phase.concurrentPhaseActive() && ((getPagesReserved() * 100) / getTotalPages()) > Options.concurrentTrigger.getValue();
    }

    /**
   * @see org.mmtk.plan.Plan#willNeverMove
   *
   * @param object Object in question
   * @return True if the object will never move
   */
    @Override
    public boolean willNeverMove(ObjectReference object) {
        if (Space.isInSpace(SS0, object) || Space.isInSpace(SS1, object)) return false;
        return super.willNeverMove(object);
    }

    /**
   * Register specialized methods.
   */
    @Interruptible
    protected void registerSpecializedMethods() {
        TransitiveClosure.registerSpecializedScan(FIRST_SCAN_SS, SapphireTraceLocalFirst.class);
        TransitiveClosure.registerSpecializedScan(SECOND_SCAN_SS, SapphireTraceLocalSecond.class);
        super.registerSpecializedMethods();
    }

    /**
   * @return The to space for the current collection.
   */
    @Inline
    public static ReplicatingSpace toSpace() {
        return low ? repSpace1 : repSpace0;
    }

    /**
   * @return The from space for the current collection.
   */
    @Inline
    public static ReplicatingSpace fromSpace() {
        return low ? repSpace0 : repSpace1;
    }

    public static boolean inFromSpace(ObjectReference obj) {
        return inFromSpace(VM.objectModel.refToAddress(obj));
    }

    public static boolean inToSpace(ObjectReference obj) {
        return inToSpace(VM.objectModel.refToAddress(obj));
    }

    public static boolean inFromSpace(Address slot) {
        return Space.isInSpace(fromSpace().getDescriptor(), slot);
    }

    public static boolean inToSpace(Address slot) {
        return Space.isInSpace(toSpace().getDescriptor(), slot);
    }

    /** @return the current trace object. */
    public Trace getCurrentTrace() {
        if (currentTrace == 1) return globalFirstTrace; else if (currentTrace == 2) return globalSecondTrace; else {
            VM.assertions.fail("Unknown trace count");
            return null;
        }
    }

    protected static final short preSanityPhase = Phase.createComplex("pre-sanity", null, Phase.scheduleSpecial(STOP_MUTATORS), Phase.scheduleGlobal(SANITY_SET_PREGC), Phase.scheduleComplex(sanityBuildPhase), Phase.scheduleComplex(sanityCheckPhase), Phase.scheduleSpecial(RESTART_MUTATORS), Phase.scheduleOnTheFlyMutator(PRE_TRACE_LINEAR_SCAN), Phase.scheduleCollector(PRE_TRACE_LINEAR_SCAN), Phase.scheduleGlobal(PRE_TRACE_LINEAR_SCAN));

    /** Build and validate a sanity table */
    protected static final short postSanityPhase = Phase.createComplex("post-sanity", null, Phase.scheduleSpecial(STOP_MUTATORS), Phase.scheduleGlobal(SANITY_SET_POSTGC), Phase.scheduleComplex(sanityBuildPhase), Phase.scheduleComplex(sanityCheckPhase), Phase.scheduleSpecial(RESTART_MUTATORS), Phase.scheduleSTWmutator(POST_TRACE_LINEAR_SCAN), Phase.scheduleCollector(POST_TRACE_LINEAR_SCAN), Phase.scheduleGlobal(POST_TRACE_LINEAR_SCAN));

    /**
   * The processOptions method is called by the runtime immediately after command-line arguments are available. Allocation must be
   * supported prior to this point because the runtime infrastructure may require allocation in order to parse the command line
   * arguments. For this reason all plans should operate gracefully on the default minimum heap size until the point that
   * processOptions is called.
   */
    @Interruptible
    public void processOptions() {
        VM.statistics.perfEventInit(Options.perfEvents.getValue());
        if (Options.verbose.getValue() > 2) Space.printVMMap();
        if (Options.verbose.getValue() > 3) VM.config.printConfig();
        if (Options.verbose.getValue() > 0) Stats.startAll();
        if (Options.eagerMmapSpaces.getValue()) Space.eagerlyMmapMMTkSpaces();
        replacePhase(Phase.scheduleCollector(CLOSURE), Phase.scheduleComplex(concurrentClosure));
    }
}
