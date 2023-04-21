package brainlink.core.model.events;

import brainlink.core.model.Block;
import brainlink.core.model.ModelListener;

/**
 * Event notifier for block deletion.
 * @author Iain McGinniss
 */
public class BlockDeletedNotifier implements EventNotifier {

    private Block deletedBlock;

    public BlockDeletedNotifier(Block deletedBlock) {
        this.deletedBlock = deletedBlock;
    }

    public void executeEvent(ModelListener listener) {
        listener.blockDeleted(deletedBlock);
    }
}
