package org.authorsite.vocab.gui;

import org.authorsite.vocab.core.*;
import org.authorsite.vocab.core.dto.VocabNodeDTO;
import org.authorsite.vocab.exceptions.VocabException;

/**
 * 
 * @author jejking
 * @version $Revision: 1.1 $
 */
public final class DeleteVocabNodeCommand implements VocabCommand {

    private VocabSet vocabSet;

    private VocabNodeDTO nodeDTO;

    public DeleteVocabNodeCommand(VocabNodeDTO nodeDTO) {
        this.nodeDTO = nodeDTO;
    }

    /**
	 * @see org.authorsite.vocab.gui.VocabCommand#execute(org.authorsite.vocab.gui.VocabMediator)
	 */
    public void execute(VocabMediator mediator) throws VocabException {
        vocabSet = mediator.getVocabSet();
        VocabNode nodeToDelete = vocabSet.findVocabNodeById(nodeDTO.getId());
        vocabSet.remove(nodeToDelete);
        mediator.notifyListeners(this, nodeToDelete);
    }

    /**
	 * @see org.authorsite.vocab.gui.VocabCommand#getEventType()
	 */
    public VocabEventType getEventType() {
        return VocabEventType.NODE_DELETED;
    }
}
