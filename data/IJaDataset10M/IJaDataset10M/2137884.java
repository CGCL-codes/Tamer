package de.fuh.xpairtise.common.replication.elements;

import de.fuh.xpairtise.common.replication.ReplicatedListElement;

/**
 * a replicated list element representing the caret offset of an editor window
 */
public class ReplicatedEditorCaretChange extends AbstractReplicatedEditorCommand {

    private static final long serialVersionUID = 8967378649406413760L;

    private int offset;

    /**
   * creates a new ReplicatedCaretChange element representing the given offset
   * 
   * @param originatorId
   *          the originatorId of the sender
   * @param offset
   *          the caret offset
   */
    public ReplicatedEditorCaretChange(String originatorId, int offset) {
        super(originatorId);
        this.offset = offset;
    }

    /**
   * gets the caret offset represented by this element
   * 
   * @return the caret offset represented by this element
   */
    public int getOffset() {
        return offset;
    }

    /**
   * sets the caret offset represented by this element
   * 
   * @param offset
   *          the offset to set
   */
    public void setOffset(int offset) {
        this.offset = offset;
    }

    /**
   * @see de.fuh.xpairtise.common.replication.ReplicatedListElement#performCopyDataFromValidatedElement(de.fuh.xpairtise.common.replication.ReplicatedListElement)
   */
    @Override
    protected void performCopyDataFromValidatedElement(ReplicatedListElement element) {
        super.performCopyDataFromValidatedElement(element);
        ReplicatedEditorCaretChange other = (ReplicatedEditorCaretChange) element;
        offset = other.getOffset();
    }
}
