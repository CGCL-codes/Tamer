package com.akira.maltese.message.config;

import java.util.ArrayList;
import java.util.List;

public class MessageConfig {

    private List<BlockConfig> block = new ArrayList<BlockConfig>();

    private int blockIndex = 0;

    public BlockConfig getNextBlock() {
        BlockConfig curBlockIndex = block.get(blockIndex);
        blockIndex++;
        return curBlockIndex;
    }

    public List<BlockConfig> getBlock() {
        return block;
    }

    public void setBlock(List<BlockConfig> block) {
        this.block = block;
    }
}
