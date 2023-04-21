package brainlink.core.model.events;

import brainlink.core.model.Block;
import brainlink.core.model.ModelListener;

/**
 * Event notifier for block properties modification.
 * @author Iain McGinniss
 */
public class BlockPropertiesModifiedNotifier implements EventNotifier {

    private Block modifiedBlock;

    public BlockPropertiesModifiedNotifier(Block modifiedBlock) {
        this.modifiedBlock = modifiedBlock;
    }

    public void executeEvent(ModelListener listener) {
        listener.blockPropertiesModified(modifiedBlock);
    }
}
