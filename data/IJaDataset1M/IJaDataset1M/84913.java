package tx.core;

import tx.development.Development;

/**
 * The I tetraminoe. It is composed by four blocks in a straight line. 
 * @author Guilherme Mauro Germoglio Barbosa - germoglio@gmail.com
 */
public class ITetraminoe extends Tetraminoe {

    /**
	 * If its state is horizontal or vertical.
	 */
    private boolean isHorizontal;

    private ITetraminoe(Block[] blocks) {
        super(blocks);
    }

    /**
	 * Creates the ITetranaminoe with the corresponding color.
	 * @param color The color. It can be found in tx.core.BlockColors constants.
	 * @return An ITetranominoe with the corresponding color. It will be created on the horizontal orientation.
	 */
    public static Tetraminoe createTetraminoe(int color) {
        Block[] blocks = new Block[NUMBER_OF_BLOCKS];
        for (int k = 0; k < NUMBER_OF_BLOCKS; k++) {
            blocks[k] = Block.createBlock(color);
            blocks[k].setX(k * blocks[k].getWidth());
        }
        ITetraminoe tetraminoe = new ITetraminoe(blocks);
        tetraminoe.isHorizontal = true;
        return tetraminoe;
    }

    public void rotate() {
        if (Development.DEBUG_ENABLED) System.out.println(Development.DEBUG_LABEL + "ITetraminoe.rotate()");
        Block[] blocks = this.getBlocks();
        int h = blocks[0].getHeight();
        int w = blocks[0].getWidth();
        float x = blocks[0].getX();
        float y = blocks[0].getY();
        if (isHorizontal) {
            isHorizontal = false;
            this.setX(x + w);
            this.setY(y - h);
        } else {
            isHorizontal = true;
            this.setX(x - w);
            this.setY(y + (1 * h));
        }
    }

    public void setX(float x) {
        Block[] blocks = this.getBlocks();
        if (isHorizontal) {
            int width = blocks[0].getWidth();
            for (int k = 0; k < NUMBER_OF_BLOCKS; k++) {
                blocks[k].setX(x + (k * width));
            }
        } else {
            for (int k = 0; k < NUMBER_OF_BLOCKS; k++) {
                blocks[k].setX(x);
            }
        }
    }

    public void setY(float y) {
        Block[] blocks = this.getBlocks();
        if (isHorizontal) {
            for (int k = 0; k < NUMBER_OF_BLOCKS; k++) {
                blocks[k].setY(y);
            }
        } else {
            int height = blocks[0].getHeight();
            for (int k = 0; k < NUMBER_OF_BLOCKS; k++) {
                blocks[k].setY(y + (k * height));
            }
        }
    }
}
