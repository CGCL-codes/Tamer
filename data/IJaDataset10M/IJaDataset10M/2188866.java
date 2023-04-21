package imp.com;

import imp.*;
import imp.data.*;
import imp.util.Trace;

/**
 * A Command that splits a unit in a Part into two units at the specified slotIndex if possible
 * @see         Command
 * @see         CommandManager
 * @see         Part
 * @see         Unit
 * @author      Martin Hunt
 */
public class SplitUnitCommand implements Command, Constants {

    /**
     * the part are we working with
     */
    private Part part;

    /**
     * the slot to split at
     */
    private int splitAtSlotIndex;

    /**
     * the unit to split
     */
    private Unit origSplitUnit;

    /**
     * the original unit's rhythmValue
     */
    private int origRhythmValue;

    /**
     * the new unit created from the split
     */
    private Unit newSplitUnit;

    /**
     * true since this can be undone
     */
    private boolean undoable = true;

    /**
     * Creates a new Command that can delete a section of a Part.
     * @param part         the Part to insert into
     * @param slotIndex    the slot to insert at
     * @param insertedPart the Part to insert
     */
    public SplitUnitCommand(Part part, int splitAtSlotIndex) {
        this.part = part;
        this.splitAtSlotIndex = splitAtSlotIndex;
    }

    /**
     * Deletes the section of the Part, saving the deleted units for
     * undoing.
     */
    public void execute() {
        Trace.log(2, "executing SplitUnitCommand");
        origSplitUnit = part.getUnit(splitAtSlotIndex);
        if (origSplitUnit != null) {
            undoable = false;
            return;
        }
        int prevIndex = part.getPrevIndex(splitAtSlotIndex);
        origSplitUnit = part.getUnit(prevIndex);
        origRhythmValue = origSplitUnit.getRhythmValue();
        if (prevIndex + origRhythmValue >= splitAtSlotIndex) {
            System.out.println(part);
            Unit splitUnit = origSplitUnit.copy();
            splitUnit.setRhythmValue(origRhythmValue + prevIndex - splitAtSlotIndex);
            part.setUnit(splitAtSlotIndex, splitUnit);
        } else {
            Trace.log(0, "Error: SplitUnitCommand found inconsistencies with the Part");
        }
    }

    /**
     * Undoes the deleting.
     */
    public void undo() {
        Trace.log(2, "undo SplitUnitCommand");
        part.delUnit(splitAtSlotIndex);
    }

    /**
     * Redoes the deleting.
     */
    public void redo() {
        execute();
    }

    public boolean isUndoable() {
        return undoable;
    }
}
