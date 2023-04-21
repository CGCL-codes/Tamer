package org.tigr.seq.seqdata.edit;

import java.util.*;
import javax.swing.undo.*;
import org.tigr.seq.log.*;
import org.tigr.seq.seqdata.*;

public class SequenceDeleteEdit extends AbstractUndoableEdit implements IEdit {

    /**
     * 
     */
    private static final long serialVersionUID = -2857399287598231897L;

    private ISequenceDeleteEditState afterState;

    private ISequenceAddEditState beforeState;

    public SequenceDeleteEdit(IEditableAssembly pReferenceAssembly, IBaseAssemblySequence pSequence) throws SeqdataException {
        System.out.println("The sequence to be deleted has id=" + pSequence.getDelegateSequence().getSequenceID());
        this.afterState = new SequenceDeleteEditState(pReferenceAssembly, pSequence.getDelegateSequence().getSequenceName());
        ArrayList gaps = new ArrayList();
        char[] sequence = pSequence.getGappedSequenceData().toArray();
        for (int c = 0; c < sequence.length; c++) {
            if (sequence[c] == '-') {
                gaps.add(new Integer(c));
            }
        }
        int[] igaps = new int[gaps.size()];
        for (int i = 0; i < igaps.length; i++) {
            igaps[i] = ((Integer) gaps.get(i)).intValue();
        }
        this.beforeState = new SequenceAddEditState(pReferenceAssembly, pSequence.getDelegateSequence().getSequenceName(), pSequence.isComplemented(), igaps, pSequence.getStartOffset(), pSequence.getSequenceStartOffset(), pSequence.getAlignmentDataLength(), pSequence.getSequenceVectorFreeStartOffset(), pSequence.getSequenceVectorFreeLength());
    }

    public void undo() {
        try {
            this.beforeState.apply();
        } catch (SeqdataException sx) {
            Log.log(Log.ERROR, new Throwable(), sx, ResourceUtil.getMessage(SequenceDeleteEdit.class, "caught_seqdata_exception"));
        }
    }

    public void redo() {
        try {
            this.afterState.apply();
        } catch (SeqdataException sx) {
            Log.log(Log.ERROR, new Throwable(), sx, ResourceUtil.getMessage(SequenceDeleteEdit.class, "caught_seqdata_exception"));
        }
    }
}
