package destal.shared.entity.block;

import destal.shared.entity.data.Values;

public class Tree extends Block {

    /**
	 * 
	 */
    private static final long serialVersionUID = 3105775316331966923L;

    @Override
    public int getDataValue() {
        return Values.BLOCK_TREE;
    }

    @Override
    public double getBlockChangePossibility() {
        return 0.5;
    }

    @Override
    public Block getNeighbourBlock() {
        return Block.create(Values.BLOCK_DIRT);
    }
}
