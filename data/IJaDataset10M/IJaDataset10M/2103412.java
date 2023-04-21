package gnagck.game;

import gnagck.block.BlockType;
import gnagck.level.LevelType;
import java.util.*;

public class GameType {

    /** The level blocks used in this game. */
    protected List<BlockType> m_blockSet;

    /** The levels in this game. */
    protected List<LevelType> m_levelSet;

    protected BlockType currentBlock;

    protected LevelType currentLevel;

    /** Default constructor. */
    public GameType() {
        LevelType tempLevel = new LevelType();
        BlockType tempBlock = new BlockType();
        tempBlock.Setname("Blank Block");
        currentBlock = tempBlock;
        AddBlock(tempBlock);
        tempLevel.SetAll(tempBlock);
        currentLevel = tempLevel;
        AddLevel(tempLevel);
    }

    /** Adds the specified block to the list of blocks used in this game.
     * @param theBlock The block to add to the game.
     */
    public void AddBlock(BlockType theBlock) {
        m_blockSet.add(theBlock);
    }

    /** Adds a new level to the list of levels in this game.
     * @param theLevel The level to add to this game.
     */
    public void AddLevel(LevelType theLevel) {
        m_levelSet.add(theLevel);
    }
}
