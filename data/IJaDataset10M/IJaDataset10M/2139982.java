package org.skycastle.scratchpad.language;

import org.skycastle.scratchpad.language.block.Block;
import org.skycastle.scratchpad.language.block.BlockReference;
import java.io.Serializable;
import java.util.Collection;

/**
 * Somethign that can contain blocks.
 * <p/>
 * May contain one or more blocks for communicating with the outside of the container.
 *
 * @author Hans Haggstrom
 */
public interface BlockContainer extends Serializable {

    /**
     * Adds the reference to the specified Block.
     *
     * @param block should not be null or already added.
     */
    void addBlock(BlockReference block);

    /**
     * Removes the reference to the specified Block.
     *
     * @param block should not be null, and should be present.
     */
    void removeBlock(BlockReference block);

    Collection<BlockReference> getBlocks();

    /**
     * @return the host {@link Block}s internal interface.
     */
    Block getHostInternalBlock();
}
