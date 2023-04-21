package org.tigr.cloe.model.edit.sequenceEdit;

import org.tigr.cloe.model.facade.datastoreFacade.IEditableSequence;

public class SequenceLengthEditCommand extends SequenceEditCommand<Integer, Boolean> {

    public SequenceLengthEditCommand(IEditableSequence sequence, Integer newValue) {
        super(sequence, newValue);
    }

    @Override
    public Boolean getDirty() {
        return sequence.isLengthDirty();
    }

    @Override
    public Integer getOldValue() {
        return new Integer(sequence.getLength());
    }

    @Override
    public void setNewDirty(Boolean newDirty) {
        sequence.setLengthDirty(newDirty);
    }

    @Override
    public void setNewValue(Integer newValue) {
        sequence.setLength(new Integer(newValue));
    }

    @Override
    public Boolean createDirtyObject() {
        return Boolean.TRUE;
    }
}
