package net.sf.openforge.optimize.io;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.sf.openforge.app.EngineThread;
import net.sf.openforge.app.OptionRegistry;
import net.sf.openforge.lim.*;
import net.sf.openforge.lim.io.*;
import net.sf.openforge.lim.memory.*;
import net.sf.openforge.optimize.*;
import net.sf.openforge.optimize.memory.ObjectResolver;

/**
 * BlockElementRemover analyzes each Allocation in all memories of a
 * Design, and determines from their access characteristics and their
 * association with a given {@link BlockElement} whether that
 * BlockElement can be eliminated from either the input block IO
 * interface or the output block IO interface.  If the BlockElement
 * can be eliminated from the interface, then the LValue access(es) in
 * the block IO wrapper functionality (around the entry method)
 * associated with either loading or unloading the Allocation(s) are
 * deleted, effectively removing the element from the interface
 * (removal of the LValue will leave the fifo read/write dangling and
 * dead component removal will remove it).  Further, the BlockElement
 * will be modified so that its stream format is empty, effectively
 * removing it from the stream while still maintaining the information
 * needed for generation of an ATB.
 *
 * <p><b>Note</b> in the case of input parameters which are write-only
 * in the algorithmic portion of the design, we do not currently do
 * enough analysis to determine that the parameter can be eliminated
 * from the transfer.  Take for example a function which is modifying
 * only half of an array.  If we do not accept that array as input
 * data, then the returned array will only be 1/2 correct.  ie, it is
 * necessary for us to preserve the data in any non-written elements.
 * Consequently the elimination of write-only parameters is now
 * qualified by a user preference (command line arg woparamopt) which
 * defaults to not removing these parameters.  Forge should be
 * modified so that we do a more comprehensive analysis, and if we can
 * determine that the param is write-first (or write-only) to EVERY
 * byte of the parameter, then we can automatically eliminate it from
 * the stream.  Until then, the command line arg defaults to false and
 * we will leave all write only params in the input stream to ensure
 * correctness in all cases.
 *
 * <p>Created: Thu Feb 26 07:28:05 2004
 *
 * @author imiller, last modified by $Author: imiller $
 * @version $Id: BlockElementRemover.java 2 2005-06-09 20:00:48Z imiller $
 */
public class BlockElementRemover implements Optimization {

    private static final String _RCS_ = "$Rev: 2 $";

    /** A List of BlockElements that have been deleted ON THE CURRENT
     * RUN, this list will be cleared out by the 'clear' method. */
    private List deletedBlockElements = new ArrayList();

    /**
     * Create a new instance of this Optimization.
     */
    public BlockElementRemover() {
    }

    /**
     * Runs this Optimization on the specified {@link Visitable}
     * target, which must be a design.  Does nothing if it is not a
     * design. 
     *
     * @param visitable a <code>Visitable</code> value
     */
    public void run(Visitable visitable) {
        if (!(visitable instanceof Design)) {
            return;
        }
        final Design design = (Design) visitable;
        ObjectResolver.resolve(design);
        final Set blockElements = buildSets(design);
        final Set allLValues = new HashSet();
        final Set deletedLValues = new HashSet();
        for (Iterator setIter = blockElements.iterator(); setIter.hasNext(); ) {
            final BlockAllocationSet set = (BlockAllocationSet) setIter.next();
            final BlockElement element = set.getBlockElement();
            final boolean isInputIF = element.getBlockDescriptor().isSlave();
            final Set accessingLValues = set.getAccessingLValues();
            allLValues.addAll(accessingLValues);
            if (isInputIF) {
                if (set.isInternalReadOnly()) {
                    for (Iterator lvalueIter = accessingLValues.iterator(); lvalueIter.hasNext(); ) {
                        LValue lvalue = (LValue) lvalueIter.next();
                        if (lvalue.isWrite()) {
                            continue;
                        }
                        if (lvalue.getBlockElement() != null) {
                            assert lvalue.getBlockElement() != element;
                            delete(lvalue);
                            deletedLValues.add(lvalue);
                        }
                    }
                }
            } else {
                if (EngineThread.getGenericJob().getUnscopedBooleanOptionValue(OptionRegistry.WRITE_ONLY_INPUT_PARAM_OPT)) {
                    if (set.isInternalWriteOnly() || set.isInternalWriteFirst()) {
                        for (Iterator lvalueIter = accessingLValues.iterator(); lvalueIter.hasNext(); ) {
                            LValue lvalue = (LValue) lvalueIter.next();
                            if (!lvalue.isWrite()) {
                                continue;
                            }
                            if (lvalue.getBlockElement() != null) {
                                assert lvalue.getBlockElement() != element;
                                delete(lvalue);
                                deletedLValues.add(lvalue);
                            }
                        }
                    }
                }
            }
        }
        allLValues.removeAll(deletedLValues);
        final Set blockElementsWithLValues = new HashSet();
        for (Iterator iter = allLValues.iterator(); iter.hasNext(); ) {
            LValue lvalue = (LValue) iter.next();
            if (lvalue.getBlockElement() != null) {
                blockElementsWithLValues.add(lvalue.getBlockElement());
            }
        }
        for (Iterator iter = blockElements.iterator(); iter.hasNext(); ) {
            BlockAllocationSet baSet = (BlockAllocationSet) iter.next();
            if (!blockElementsWithLValues.contains(baSet.getBlockElement())) {
                baSet.deleteElement();
                this.deletedBlockElements.add(baSet.getBlockElement());
            }
        }
    }

    /**
     * Builds a Set of BlockAllocationSet objects for the memories in
     * the design.
     *
     * @param design a value of type 'Design'
     * @return a Set of BlockAllocationSet objects.
     */
    private Set buildSets(Design design) {
        final Map blockElements = new HashMap();
        for (Iterator memIter = design.getLogicalMemories().iterator(); memIter.hasNext(); ) {
            final LogicalMemory mem = (LogicalMemory) memIter.next();
            for (Iterator allocIter = mem.getAllocations().iterator(); allocIter.hasNext(); ) {
                Allocation alloc = (Allocation) allocIter.next();
                for (Iterator elemIter = alloc.getBlockElements().iterator(); elemIter.hasNext(); ) {
                    BlockElement element = (BlockElement) elemIter.next();
                    BlockAllocationSet set = (BlockAllocationSet) blockElements.get(element);
                    if (set == null) {
                        set = new BlockAllocationSet(element);
                        blockElements.put(element, set);
                    }
                    set.add(alloc);
                }
            }
        }
        return new HashSet(blockElements.values());
    }

    /**
     * Utility method for deleting an LValue and taking care of all
     * the details.
     *
     * @param lvalue a value of type 'LValue'
     */
    private void delete(LValue lvalue) {
        assert lvalue instanceof MemoryAccessBlock;
        assert lvalue.getBlockElement() != null;
        MemoryAccessBlock mab = (MemoryAccessBlock) lvalue;
        try {
            if (mab.getOwner().getOwner().getOwner() instanceof Loop) {
                ((Loop) mab.getOwner().getOwner().getOwner()).setForceUnroll(true);
            }
        } catch (NullPointerException e) {
            EngineThread.getGenericJob().warn("Unexpected internal state while deleting unused memory access.  Recovered normally.  ");
        }
        for (Iterator doneBusIter = mab.getExit(Exit.DONE).getDoneBus().getLogicalDependents().iterator(); doneBusIter.hasNext(); ) {
            ((Dependency) doneBusIter.next()).zap();
        }
        ComponentSwapVisitor.wireControlThrough(mab);
        ComponentSwapVisitor.removeComp(mab);
        mab.removeFromMemory();
    }

    /**
     * Clears the status and internal state of this Optimization.
     */
    public void clear() {
        this.deletedBlockElements.clear();
    }

    /**
     * Reports, via the {@link Job} reporting mechanisms any relevent
     * information prior to running this optimization.
     */
    public void preStatus() {
        EngineThread.getGenericJob().info("Optimizing interface streams...");
    }

    /**
     * Reports, via the {@link Job} reporting mechanisms any relevent
     * information subsequent to running this optimization.
     */
    public void postStatus() {
        for (Iterator iter = this.deletedBlockElements.iterator(); iter.hasNext(); ) {
            BlockElement element = (BlockElement) iter.next();
            EngineThread.getGenericJob().verbose("Deleted '" + element.getFormalName() + "' from " + (element.getBlockDescriptor().isSlave() ? "input" : "output") + " stream");
        }
    }

    /**
     * Returns true if the current run of this optimization made any
     * change to the LIM.  That is, returns true if any LValue
     * accesses were deleted.
     *
     * @return true if the LIM structure was modified by this
     * optimization 
     */
    public boolean didModify() {
        return this.deletedBlockElements.size() > 0;
    }
}
