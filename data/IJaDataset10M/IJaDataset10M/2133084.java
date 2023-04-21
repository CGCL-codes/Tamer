package imp.com;

import imp.data.*;
import imp.util.Trace;

/**
 * A Command that can be used to drag a note from one slot to 
 * another slot, it can be specified as undoable or not undoable.
 * @see         Command
 * @see         SetNoteCommand
 * @see         CommandManager
 * @see         MelodyPart
 * @see         Note
 * @author      Stephen Jones
 */
public class DragNoteCommand implements Command {

    /**
     * the MelodyPart that contains the Note to drag
     */
    private MelodyPart part;

    /**
     * the Note to drag
     */
    private Note note;

    /**
     * the slot to drag from
     */
    private int oldSlot;

    /**
     * the slot to drag to
     */
    private int newSlot;

    /**
     * the Command used to set the Note at the new spot
     */
    private Command setNote;

    /**
     * this Command is undoable, by default
     */
    private boolean undoable = true;

    /**
     * Creates a new Command that can drag a note
     * @param part      the MelodyPart containing the note to drag
     * @param oldSlot   the slot to drag from
     * @param newSlot   the slot to drag to
     */
    public DragNoteCommand(MelodyPart part, int oldSlot, int newSlot) {
        this.part = part;
        this.oldSlot = oldSlot;
        this.newSlot = newSlot;
    }

    /**
     * Creates a new Command that can drag a note 
     * @param part      the MelodyPart containing the note to drag
     * @param oldSlot   the slot to drag from
     * @param newSlot   the slot to drag to
     * @param undoable  a boolean that sets this Command as undoable or not
     */
    public DragNoteCommand(MelodyPart part, int oldSlot, int newSlot, boolean undoable) {
        this(part, oldSlot, newSlot);
        this.undoable = undoable;
    }

    /**
     * Executes the drag.
     */
    public void execute() {
        Trace.log(2, "executing DragNoteCommand");
        note = part.getNote(oldSlot);
        part.delUnit(oldSlot);
        setNote = new SetNoteCommand(newSlot, note, part);
        setNote.execute();
    }

    /**
     * Undoes the drag.
     */
    public void undo() {
        setNote.undo();
        part.setNote(oldSlot, note);
    }

    /**
     * Redoes the drag.
     */
    public void redo() {
        part.delUnit(oldSlot);
        setNote.redo();
    }

    public boolean isUndoable() {
        return undoable;
    }
}
