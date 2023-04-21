package com.frinika.sequencer.model;

/**
 * EditHistoryEntry represents one entry in the edit history - being a single event.
 * @author Peter Johan Salomonsen
 */
public class EditHistoryEntry {

    private int editHistoryType;

    /**
     * Either a MultiEvent or MidiEvent
     */
    EditHistoryRecordable recordable;

    EditHistoryRecordable recordableClone = null;

    EditHistoryContainer editHistoryContainer;

    EditHistoryRecorder editHistoryRecorder;

    public static final int EDIT_HISTORY_TYPE_ADD = 0;

    public static final int EDIT_HISTORY_TYPE_REMOVE = 1;

    /**
     * 
     * @param editHistoryContainer
     * @param recorder 
     * @param editHistoryType
     * @param recordable - Either a MultiEvent or MidiEvent
     */
    public EditHistoryEntry(EditHistoryContainer editHistoryContainer, EditHistoryRecorder recorder, int editHistoryType, EditHistoryRecordable recordable) {
        this.editHistoryRecorder = recorder;
        this.editHistoryContainer = editHistoryContainer;
        this.editHistoryType = editHistoryType;
        this.recordable = recordable;
        try {
            recordableClone = (EditHistoryRecordable) recordable.clone();
        } catch (Exception e) {
        }
    }

    /**
     * @return Returns the editHistoryType.
     */
    public int getEditHistoryType() {
        return editHistoryType;
    }

    /**
     * @return Returns the event (Either a MultiEvent or a MidiEvent) affected by this entry
     */
    public EditHistoryRecordable getRecordable() {
        return recordable;
    }

    /**
     * This method should be called by the EditHistory container
     *
     */
    synchronized void reverse() {
        editHistoryContainer.disableRecording();
        if (editHistoryType == EDIT_HISTORY_TYPE_ADD) {
            editHistoryRecorder.remove(recordable);
        } else if (editHistoryType == EDIT_HISTORY_TYPE_REMOVE) {
            if (recordableClone != null) {
                try {
                    EditHistoryRecordable redoClone = (EditHistoryRecordable) recordable.clone();
                    recordable.restoreFromClone(recordableClone);
                    recordableClone = redoClone;
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            }
            editHistoryRecorder.add(recordable);
        }
        editHistoryContainer.enableRecording();
    }

    /**
     * This method should be called by the EditHistory container
     *
     */
    synchronized void redo() {
        editHistoryContainer.disableRecording();
        if (editHistoryType == EDIT_HISTORY_TYPE_REMOVE) {
            if (recordableClone != null) {
                try {
                    EditHistoryRecordable undoClone = (EditHistoryRecordable) recordable.clone();
                    recordable.restoreFromClone(recordableClone);
                    recordableClone = undoClone;
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            }
            editHistoryRecorder.remove(recordable);
        } else if (editHistoryType == EDIT_HISTORY_TYPE_ADD) {
            editHistoryRecorder.add(recordable);
        }
        editHistoryContainer.enableRecording();
    }

    @Override
    public String toString() {
        String editHistoryTypeString = (editHistoryType == EDIT_HISTORY_TYPE_ADD) ? "Add" : "Remove";
        return "EditHistoryEntry: " + editHistoryTypeString + " " + recordable;
    }

    /**
     * Return a cloned EditHistoryEntry with the opposite editHistoryType. Used to notify listeners when undoing in order to indicate that
     * the previous action was reversed
     * @return
     */
    public EditHistoryEntry getInvertedClone() {
        return new EditHistoryEntry(editHistoryContainer, editHistoryRecorder, (~editHistoryType) & 0x01, recordable);
    }
}
