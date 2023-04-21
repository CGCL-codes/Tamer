package fluid.version;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Vector;
import fluid.ir.DefaultSlotStorage;
import fluid.ir.IRCompound;
import fluid.ir.IRCompoundType;
import fluid.ir.IRInput;
import fluid.ir.IROutput;
import fluid.ir.IRType;
import fluid.ir.Slot;
import fluid.ir.SlotUndefinedException;
import fluid.ir.SlotUnknownException;
import fluid.util.IntegerTable;
import fluid.util.ThreadGlobal;

public abstract class VersionedSlot extends DefaultSlotStorage {

    /**
   * Log4j logger for this class
   */
    protected static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger("IR.version");

    private static final ThreadGlobal eraVar = new ThreadGlobal(null);

    static Era getEra() {
        return (Era) eraVar.getValue();
    }

    static void setEra(Era era) {
        eraVar.setValue(era);
    }

    public static void pushEra(Era era) {
        eraVar.pushValue(era);
    }

    static void popEra() {
        eraVar.popValue();
    }

    protected static Object undefinedValue = new Object();

    private static Vector slotList = new Vector();

    static boolean verboseDebug = false;

    /** Remove a slot from the list of all slots
   * because it is being retired, no use for debugging.
   * (NB This is only used for debugging, and has no effect otherwise.)
   */
    protected void retire() {
        slotList.removeElement(this);
    }

    /** Create a versioned slot. */
    public VersionedSlot() {
        super();
        if (verboseDebug) slotList.addElement(this);
    }

    /** The number of version-value pairs in this versioned slot.
   * Used primarily for debugging and statistics.
   */
    public abstract int size();

    /** Turn on debugging.
   * Currently this only enables the stashing of all versioned slots.
   * @see #listing
   */
    public static synchronized void debugOn() {
        verboseDebug = true;
    }

    public static synchronized void debugOff() {
        verboseDebug = false;
    }

    public static Object debugTogglePrintMe = new Object() {

        public String toString() {
            verboseDebug = !verboseDebug;
            return verboseDebug ? "debugging turned on" : "debugging turned off";
        }
    };

    /** Print out a description of slots created while
   * debugging is on.  It prints the total count, the first maxPrint
   * of the slots and then a description of how many (of all the slots)
   * have the given number of version-value pairs.
   */
    public static void listing(int maxPrint) {
        int totalSlots = slotList.size();
        System.out.println(totalSlots + " slots in all.");
        Vector histogram = new Vector();
        for (int i = 0; i < totalSlots; ++i) {
            VersionedSlot s = (VersionedSlot) slotList.elementAt(i);
            if (i < maxPrint) {
                s.describe(System.out);
                System.out.println();
            }
            int n = s.size();
            if (histogram.size() <= n) histogram.setSize(n + 1);
            Integer count = (Integer) histogram.elementAt(n);
            if (count == null) {
                count = IntegerTable.newInteger(1);
            } else {
                count = IntegerTable.newInteger(count.intValue() + 1);
            }
            histogram.setElementAt(count, n);
        }
        System.out.println("Number of slots of the following sizes:");
        int hsize = histogram.size();
        for (int i = 0; i < hsize; ++i) {
            Integer count = (Integer) histogram.elementAt(i);
            if (count != null) {
                System.out.println("  " + i + ": " + count);
            }
        }
    }

    public static Object debugDumpPrintMe = new Object() {

        public String toString() {
            listing(100);
            return "Done.";
        }
    };

    public void describe(PrintStream out) {
    }

    /** Return the value of this slot at the current version.
   * @see Version#getVersion()
   * @see #getValue(Version)
   * @throws SlotUndefinedException if the slot does not have
   * a value.
   * @throws SlotUnknownException if we do not know whetehr the slot
   * has a value.
   */
    public Object getValue() throws SlotUndefinedException, SlotUnknownException {
        return getValue(Version.getVersionLocal());
    }

    /** Return the value of this slot at a particular version.
   * @throws SlotUndefinedException if the slot does not have
   * a value.
   * @throws SlotUnknownException if we do not know whetehr the slot
   * has a value.
   */
    public abstract Object getValue(Version v) throws SlotUndefinedException, SlotUnknownException;

    /** Return the version at which this slot was assigned the value given.
   */
    public abstract Version getLatestChange(Version v);

    /** Set the value of this slot for a new child of the current version.
   */
    public Slot setValue(Object newValue) {
        Version.bumpVersion();
        return setValue(Version.getVersionLocal(), newValue);
    }

    /** Set the value of this slot for a particular version.
   * This method is protected because used incorrectly it can upset the
   * property that a versioned slot never changes for a version.
   */
    protected abstract VersionedSlot setValue(Version v, Object newValue);

    public boolean isValid() {
        getEra();
        return isValid(Version.getVersionLocal());
    }

    /** Does the slot have a value at the given version?
   */
    public abstract boolean isValid(Version v);

    public boolean isChanged() {
        Era era = getEra();
        if (era != null) return isChanged(era);
        return true;
    }

    /** Return true if this version slot has been changed somewhere
   * in the given version region.
   */
    public abstract boolean isChanged(Era era);

    public void writeValue(IRType ty, IROutput out) throws IOException {
        Era era = getEra();
        if (era != null) {
            out.debugBegin("vl");
            writeValues(ty, out, era);
            out.debugMark("sentinel");
            Version.writeRootVersion(out);
            out.debugEnd("vl");
        } else {
            Object val = getValue();
            if (ty instanceof IRCompoundType) {
                Version v0 = Version.getInitialVersion();
                Version v = Version.getVersionLocal();
                if (v != v0) {
                    v = getLatestChange(Version.getVersionLocal());
                    out.debugMark("compound_date");
                    IRVersionType.prototype.writeValue(v, out);
                }
            }
            ty.writeValue(val, out);
        }
    }

    /** Write all values of this slot for the given era,
   * in the form version,value,version,value etc.
   * The version will never be null.
   */
    protected abstract void writeValues(IRType ty, IROutput out, Era e) throws IOException;

    /** Write out one version,value pair.
   * Special case work is done for compounds.
   */
    protected void writeVersionValue(IRType ty, Version v, Object val, IROutput out) throws IOException {
        out.debugBegin("vv");
        if (out.debug()) {
            System.out.println("  " + v + " : " + val);
        }
        out.debugBegin("v");
        v.write(out);
        out.debugEnd("v");
        if (ty instanceof IRCompoundType && val != null) {
            Version.saveVersion();
            pushEra(null);
            try {
                Version.setVersion(Version.getInitialVersion());
                ty.writeValue(val, out);
            } finally {
                popEra();
                Version.restoreVersion();
            }
            if (val != null) {
                out.debugBegin("changed");
                ((IRCompound) val).writeChangedContents((IRCompoundType) ty, out);
                out.debugEnd("changed");
            }
        } else {
            ty.writeValue(val, out);
        }
        out.debugEnd("vv");
    }

    public synchronized Slot readValue(IRType ty, IRInput in) throws IOException {
        Era era = getEra();
        if (era == null) {
            Version v = Version.getVersionLocal();
            Object val;
            if (in.getRevision() >= 4 && ty instanceof IRCompoundType) {
                if (v != Version.getInitialVersion()) {
                    in.debugMark("compound_date");
                    v = (Version) IRVersionType.prototype.readValue(in);
                    if (isValid(v) && (val = getValue(v)) != null) {
                        if (val != ((IRCompoundType) ty).readValue(in, val)) {
                            throw new IOException("changed value of compound");
                        }
                        return this;
                    }
                }
            }
            val = ty.readValue(in);
            if (in.debug()) {
                System.out.println("  " + v + " : " + val);
            }
            if (in.getRevision() < 4 && ty instanceof IRCompoundType) v = Version.getInitialVersion();
            return setValue(v, val);
        }
        in.debugBegin("vl");
        Version root = era.getRoot();
        Version v;
        VersionedSlot s = this;
        in.debugBegin("vv");
        in.debugBegin("v");
        while (!(v = Version.read(in, era)).equals(root)) {
            in.debugEnd("v");
            Object val;
            if (in.getRevision() >= 4 && ty instanceof IRCompoundType) {
                Version.saveVersion();
                pushEra(null);
                try {
                    Version.setVersion(Version.getInitialVersion());
                    val = ty.readValue(in);
                } finally {
                    popEra();
                    Version.restoreVersion();
                }
                if (val != null) {
                    in.debugBegin("changed");
                    ((IRCompound) val).readChangedContents((IRCompoundType) ty, in);
                    in.debugEnd("changed");
                }
            } else {
                val = ty.readValue(in);
            }
            if (in.debug()) {
                System.out.println("  " + v + " : " + val);
            }
            in.debugEnd("vv");
            s = s.setValue(v, val);
            in.debugBegin("vv");
            in.debugBegin("v");
        }
        in.debugEnd("v");
        in.debugEnd("vv");
        in.debugEnd("vl");
        return s;
    }
}
