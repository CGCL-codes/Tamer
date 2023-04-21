package org.tigr.seq.seqdata.edit;

import org.tigr.seq.seqdata.display.*;

public interface ISequenceSelectionEdit extends IEdit {

    public ISequenceSelectionEditState getBeforeState();

    public ISequenceSelectionEditState getAfterState();

    public IEditableAssemblySequence getReferenceData();

    public Integer getEditType();

    public IEditableAssemblyData getEditableAssemblyData();

    public IAssemblySelectionManager getAssemblySelectionManager();
}
