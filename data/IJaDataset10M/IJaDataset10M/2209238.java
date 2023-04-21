package _api.alienfactory.javamappy;

import _api.alienfactory.javamappy.util.ParameterChecker;

public class Layer {

    private final MapHeader mapHeader;

    private final short[][] layerData;

    private final Block blocks[];

    private final AnimBlock animBlocks[];

    private final int widthInBlocks, heightInBlocks;

    private final int widthInPixels, heightInPixels;

    /**
	 * @deprecated use @link Layer#Layer(MapHeader, short[][], Block[], AnimBlock[]) instead
	 */
    public Layer(Map map, short[][] layerData) {
        this(map.getMapHeader(), layerData, map.getBlocks(), map.getAnimBlocks());
    }

    public Layer(MapHeader mapHeader, short[][] layerData, Block[] blocks, AnimBlock[] animBlocks) {
        ParameterChecker.checkNotNull(mapHeader, "mapHeader");
        ParameterChecker.checkNotNull(layerData, "layerData");
        ParameterChecker.checkNotNull(blocks, "blocks");
        ParameterChecker.checkNotNull(animBlocks, "animBlocks");
        this.mapHeader = mapHeader;
        this.layerData = layerData;
        this.blocks = blocks;
        this.animBlocks = animBlocks;
        if (layerData.length > 0) widthInBlocks = layerData[0].length; else widthInBlocks = 0;
        heightInBlocks = layerData.length;
        widthInPixels = mapHeader.getMapPixelWidth();
        heightInPixels = mapHeader.getMapPixelHeight();
    }

    /**
	 * Returns the {@link MapHeader} associated with this Layer. This method 
	 * should not return <code>null</code>. <br>
	 * @return the MapHeader associated with this Viewer
	 */
    public MapHeader getMapHeader() {
        return mapHeader;
    }

    /**
	 * Returns the width of the layer, measured in blocks. <br>
	 */
    public int getWidthInBlocks() {
        return widthInBlocks;
    }

    /**
	 * Returns the height of the layer, measured in blocks. <br>
	 */
    public int getHeightInBlocks() {
        return heightInBlocks;
    }

    /**
	 * Returns the width of the layer, measured in pixels. <br>
	 */
    public int getWidthInPixels() {
        return widthInPixels;
    }

    /**
	 * Returns the height of the layer, measured in pixels. <br>
	 */
    public int getHeightInPixels() {
        return heightInPixels;
    }

    /**
	 * Returns the raw layer data as passed into the constructor. <br>
	 */
    public short[][] getLayerData() {
        return layerData;
    }

    /**
	 * Returns the Blocks used by this Layer, as passed into the constructor. <br>
	 */
    public Block[] getBlocks() {
        return blocks;
    }

    /**
	 * Returns the AnimBlocks used by this Layer, as passed into the constructor. <br>
	 */
    public AnimBlock[] getAnimBlocks() {
        return animBlocks;
    }

    /**
	 * Returns the <code>Block</code> index at the given coordinates. Animation 
	 * Blocks have negative values. <br>
	 */
    public int getBlockIndex(int blockX, int blockY) throws IllegalArgumentException {
        ParameterChecker.checkMinMax(blockX, "blockX", 0, widthInBlocks - 1);
        ParameterChecker.checkMinMax(blockY, "blockY", 0, heightInBlocks - 1);
        return layerData[blockY][blockX];
    }

    /**
	 * Replaces the block at the given coordinates. Use this method for 
	 * dynamically updating and changing the map.  
	 */
    public void setBlockIndex(int blockX, int blockY, int blockIndex) throws IllegalArgumentException {
        ParameterChecker.checkMinMax(blockX, "blockX", 0, widthInBlocks - 1);
        ParameterChecker.checkMinMax(blockY, "blockY", 0, heightInBlocks - 1);
        ParameterChecker.checkMinMax(blockIndex, "blockIndex", -animBlocks.length, blocks.length - 1);
        layerData[blockY][blockX] = (short) blockIndex;
    }

    /**
	 * Returns the <code>Block</code> at the given block coordinates. <br>
	 * &nbsp;<br>
	 * If the block is an animation block then the current block in the anim 
	 * sequence is returned. <br>
	 */
    public Block getBlock(int blockX, int blockY) throws IllegalArgumentException {
        ParameterChecker.checkMinMax(blockX, "blockX", 0, widthInBlocks - 1);
        ParameterChecker.checkMinMax(blockY, "blockY", 0, heightInBlocks - 1);
        int blockIndex = layerData[blockY][blockX];
        if (blockIndex < 0) {
            AnimBlock animBlock = animBlocks[-blockIndex - 1];
            blockIndex = animBlock.getCurrentFrame();
        }
        ParameterChecker.checkMinMax(blockIndex, "blockIndex", 0, blocks.length - 1);
        return blocks[blockIndex];
    }

    /**
	 * Returns the <code>AnimBlock</code> at the given block coordinates. <br>
	 * &nbsp;<br>
	 * If the block is not an <code>AnimBlock</code> then an 
	 * <code>IllegalArgumentException</code> is thrown. <br>
	 */
    public AnimBlock getAnimBlock(int blockX, int blockY) throws IllegalArgumentException {
        int blockIndex = getBlockIndex(blockX, blockY);
        if (blockIndex >= 0) throw new IllegalArgumentException("The block at coors [" + blockX + "][" + blockY + "] is not an AnimBlock");
        return animBlocks[-blockIndex - 1];
    }

    /**
	 * A helper method that checks if the block at the given coordinates is an 
	 * <code>AnimBlock</code>. 
	 */
    public boolean isAnimBlock(int blockX, int blockY) throws IllegalArgumentException {
        ParameterChecker.checkMinMax(blockX, "blockX", 0, widthInBlocks - 1);
        ParameterChecker.checkMinMax(blockY, "blockY", 0, heightInBlocks - 1);
        int blockIndex = layerData[blockY][blockX];
        return (blockIndex < 0);
    }

    /**
	 * Performs a collision detection test at the given pixel coordinates
	 * using the <code>Block</code>'s collision flags. <br>
	 * @param	x	the X coordinate
	 * @param	y	the Y coordinate
	 * @return 	<code>true</code> if there is a collision flag set at the given 
	 * 			coordinates
	 * @see		Block#setCollisionFlag(int, boolean)
	 * @see		Block#getCollisionFlag(int)
	 **/
    public boolean isCollisionAt(int x, int y) {
        return (getCollisionAt(x, y) != -1);
    }

    /**
	 * Performs a collision detection test at the given pixel coordinates
	 * using the <code>Block</code>'s collision flags. This mehtod returns a 
	 * number representing which corner of the Block the coordinates collided 
	 * with, or -1 if no collision occured. <br>
	 * @param	x	the X coordinate
	 * @param	y	the Y coordinate
	 * @return 	a combination of {@link Block#LEFT}, {@link Block#RIGHT}, 
	 * 			{@link Block#TOP} and {@link Block#BOTTOM} or <code>-1</code> 
	 * 			if no collision occured
	 * @see		Block#setCollisionFlag(int, boolean)
	 * @see		Block#getCollisionFlag(int)
	 **/
    public int getCollisionAt(int x, int y) {
        int pixelX, pixelY;
        int blockX, blockY;
        int blockWidth, blockHeight;
        int collisionFlag = 0;
        Block block;
        blockWidth = mapHeader.getBlockWidth();
        blockHeight = mapHeader.getBlockHeight();
        blockX = x / blockWidth;
        blockY = y / blockHeight;
        pixelX = x % blockWidth;
        pixelY = y % blockHeight;
        block = getBlock(blockX, blockY);
        if (pixelX >= (blockWidth / 2)) collisionFlag |= Block.RIGHT; else collisionFlag |= Block.LEFT;
        if (pixelY >= (blockHeight / 2)) collisionFlag |= Block.BOTTOM; else collisionFlag |= Block.TOP;
        if (!block.getCollisionFlag(collisionFlag)) collisionFlag = -1;
        return collisionFlag;
    }

    /**
	 * Starts at the bottom of the given rectangle and works its way up,
	 * checking the status of the collision flags on the way. Use this method in
	 * platform games when your hero is jumping and you want to know the coor of
	 * the ceiling she's just hit her head on, if any. <br>
	 * 
	 * @return the Y coordinate of the collision or -1 if no collision occured
	 */
    public int getTopCollisionCoor(int pixelX, int pixelY, int width, int height) {
        ParameterChecker.checkMinMax(pixelX, "pixelX", 0, widthInPixels - width);
        ParameterChecker.checkMinMax(pixelY, "pixelY", 0, heightInPixels - height);
        ParameterChecker.checkMin(width, "width", 1);
        ParameterChecker.checkMin(height, "height", 1);
        int blockWidth = mapHeader.getBlockWidth();
        int blockHeight = mapHeader.getBlockHeight();
        int halfBlockWidth = blockWidth / 2;
        int halfBlockHeight = blockHeight / 2;
        int pixelStartX = pixelX;
        int pixelStartY = pixelY;
        int pixelEndX = pixelX + width;
        int pixelEndY = pixelY + height;
        int blockStartX = pixelStartX / blockWidth;
        int blockStartY = pixelStartY / blockHeight;
        int blockEndX = pixelEndX / blockWidth;
        int blockEndY = pixelEndY / blockHeight;
        pixelX = (blockStartX * blockWidth) + halfBlockWidth;
        pixelY = (blockEndY * blockHeight) + halfBlockHeight;
        Block block;
        int blockIndex, coorCache;
        for (int blockY = blockEndY; blockY >= blockStartY; blockY--) {
            coorCache = -1;
            pixelX = (blockStartX * blockWidth) + halfBlockWidth;
            for (int blockX = blockStartX; blockX <= blockEndX; blockX++) {
                blockIndex = layerData[blockY][blockX];
                if (blockIndex < 0) blockIndex = animBlocks[-blockIndex - 1].getCurrentFrame();
                block = blocks[blockIndex];
                if (pixelEndY > pixelY) {
                    if (pixelStartX < pixelX) if (block.getCollisionFlag(Block.BOTTOM | Block.LEFT)) return (pixelY + halfBlockHeight);
                    if (pixelEndX > pixelX) if (block.getCollisionFlag(Block.BOTTOM | Block.RIGHT)) return (pixelY + halfBlockHeight);
                }
                if (coorCache == -1) if (pixelStartY < pixelY) {
                    if (pixelStartX < pixelX) if (block.getCollisionFlag(Block.TOP | Block.LEFT)) coorCache = pixelY;
                    if (pixelEndX > pixelX) if (block.getCollisionFlag(Block.TOP | Block.RIGHT)) coorCache = pixelY;
                }
                pixelX += blockWidth;
            }
            if (coorCache != -1) return coorCache;
            pixelY -= blockHeight;
        }
        return -1;
    }

    /**
	 * Starts at the top of the given rectangle and works its way down, checking
	 * the status of the collision flags on the way. Use this method in
	 * a platform game when your hero is falling and you want to know the coor
	 * of the floor she's just landed on, if any. <br>
	 * 
	 * @return the Y coordinate of the collision or -1 if no collision occured
	 */
    public int getBottomCollisionCoor(int pixelX, int pixelY, int width, int height) {
        ParameterChecker.checkMinMax(pixelX, "pixelX", 0, widthInPixels - width);
        ParameterChecker.checkMinMax(pixelY, "pixelY", 0, heightInPixels - height);
        ParameterChecker.checkMin(width, "width", 1);
        ParameterChecker.checkMin(height, "height", 1);
        int blockWidth = mapHeader.getBlockWidth();
        int blockHeight = mapHeader.getBlockHeight();
        int halfBlockWidth = blockWidth / 2;
        int halfBlockHeight = blockHeight / 2;
        int pixelStartX = pixelX;
        int pixelStartY = pixelY;
        int pixelEndX = pixelX + width;
        int pixelEndY = pixelY + height;
        int blockStartX = pixelStartX / blockWidth;
        int blockStartY = pixelStartY / blockHeight;
        int blockEndX = pixelEndX / blockWidth;
        int blockEndY = pixelEndY / blockHeight;
        pixelX = (blockStartX * blockWidth) + halfBlockWidth;
        pixelY = (blockStartY * blockHeight) + halfBlockHeight;
        Block block;
        int blockIndex, coorCache;
        for (int blockY = blockStartY; blockY <= blockEndY; blockY++) {
            coorCache = -1;
            pixelX = (blockStartX * blockWidth) + halfBlockWidth;
            for (int blockX = blockStartX; blockX <= blockEndX; blockX++) {
                blockIndex = layerData[blockY][blockX];
                if (blockIndex < 0) blockIndex = animBlocks[-blockIndex - 1].getCurrentFrame();
                block = blocks[blockIndex];
                if (pixelStartY < pixelY) {
                    if (pixelStartX < pixelX) if (block.getCollisionFlag(Block.TOP | Block.LEFT)) return (pixelY - halfBlockHeight);
                    if (pixelEndX > pixelX) if (block.getCollisionFlag(Block.TOP | Block.RIGHT)) return (pixelY - halfBlockHeight);
                }
                if (coorCache == -1) if (pixelEndY > pixelY) {
                    if (pixelStartX < pixelX) if (block.getCollisionFlag(Block.BOTTOM | Block.LEFT)) coorCache = pixelY;
                    if (pixelEndX > pixelX) if (block.getCollisionFlag(Block.BOTTOM | Block.RIGHT)) coorCache = pixelY;
                }
                pixelX += blockWidth;
            }
            if (coorCache != -1) return coorCache;
            pixelY += blockHeight;
        }
        return -1;
    }

    /**
	 * Starts at the right of the given rectangle and works its way to the left,
	 * checking the status of the collision flags on the way. Use this method in
	 * a platform game when your hero is moving and you want to know the coor
	 * of a wall she's just hit, if any. <br>
	 * 
	 * @return the X coordinate of the collision or -1 if no collision occured
	 */
    public int getLeftCollisionCoor(int pixelX, int pixelY, int width, int height) {
        ParameterChecker.checkMinMax(pixelX, "pixelX", 0, widthInPixels - width);
        ParameterChecker.checkMinMax(pixelY, "pixelY", 0, heightInPixels - height);
        ParameterChecker.checkMin(width, "width", 1);
        ParameterChecker.checkMin(height, "height", 1);
        int blockWidth = mapHeader.getBlockWidth();
        int blockHeight = mapHeader.getBlockHeight();
        int halfBlockWidth = blockWidth / 2;
        int halfBlockHeight = blockHeight / 2;
        int pixelStartX = pixelX;
        int pixelStartY = pixelY;
        int pixelEndX = pixelX + width;
        int pixelEndY = pixelY + height;
        int blockStartX = pixelStartX / blockWidth;
        int blockStartY = pixelStartY / blockHeight;
        int blockEndX = pixelEndX / blockWidth;
        int blockEndY = pixelEndY / blockHeight;
        pixelX = (blockEndX * blockWidth) + halfBlockWidth;
        pixelY = (blockStartY * blockHeight) + halfBlockHeight;
        Block block;
        int blockIndex, coorCache;
        for (int blockX = blockEndX; blockX >= blockStartX; blockX--) {
            coorCache = -1;
            pixelY = (blockStartY * blockHeight) + halfBlockHeight;
            for (int blockY = blockStartY; blockY <= blockEndY; blockY++) {
                blockIndex = layerData[blockY][blockX];
                if (blockIndex < 0) blockIndex = animBlocks[-blockIndex - 1].getCurrentFrame();
                block = blocks[blockIndex];
                if (pixelEndX > pixelX) {
                    if (pixelStartY < pixelY) if (block.getCollisionFlag(Block.TOP | Block.RIGHT)) return (pixelX + halfBlockWidth);
                    if (pixelEndY > pixelY) if (block.getCollisionFlag(Block.BOTTOM | Block.RIGHT)) return (pixelX + halfBlockWidth);
                }
                if (coorCache == -1) if (pixelStartX < pixelX) {
                    if (pixelStartY < pixelY) if (block.getCollisionFlag(Block.TOP | Block.LEFT)) coorCache = pixelX;
                    if (pixelEndY > pixelY) if (block.getCollisionFlag(Block.BOTTOM | Block.LEFT)) coorCache = pixelX;
                }
                pixelY += blockHeight;
            }
            if (coorCache != -1) return coorCache;
            pixelX -= blockWidth;
        }
        return -1;
    }

    /**
	 * Starts at the left of the given rectangle and works its way to the right,
	 * checking the status of the collision flags on the way. Use this method in
	 * a platform game when your hero is moving and you want to know the coor
	 * of a wall she's just hit, if any. <br>
	 * 
	 * @return the X coordinate of the collision or -1 if no collision occured
	 */
    public int getRightCollisionCoor(int pixelX, int pixelY, int width, int height) {
        ParameterChecker.checkMinMax(pixelX, "pixelX", 0, widthInPixels - width);
        ParameterChecker.checkMinMax(pixelY, "pixelY", 0, heightInPixels - height);
        ParameterChecker.checkMin(width, "width", 1);
        ParameterChecker.checkMin(height, "height", 1);
        int blockWidth = mapHeader.getBlockWidth();
        int blockHeight = mapHeader.getBlockHeight();
        int halfBlockWidth = blockWidth / 2;
        int halfBlockHeight = blockHeight / 2;
        int pixelStartX = pixelX;
        int pixelStartY = pixelY;
        int pixelEndX = pixelX + width;
        int pixelEndY = pixelY + height;
        int blockStartX = pixelStartX / blockWidth;
        int blockStartY = pixelStartY / blockHeight;
        int blockEndX = pixelEndX / blockWidth;
        int blockEndY = pixelEndY / blockHeight;
        pixelX = (blockStartX * blockWidth) + halfBlockWidth;
        pixelY = (blockStartY * blockHeight) + halfBlockHeight;
        Block block;
        int blockIndex, coorCache;
        for (int blockX = blockStartX; blockX <= blockEndX; blockX++) {
            coorCache = -1;
            pixelY = (blockStartY * blockHeight) + halfBlockHeight;
            for (int blockY = blockStartY; blockY <= blockEndY; blockY++) {
                blockIndex = layerData[blockY][blockX];
                if (blockIndex < 0) blockIndex = animBlocks[-blockIndex - 1].getCurrentFrame();
                block = blocks[blockIndex];
                if (pixelStartX < pixelX) {
                    if (pixelStartY < pixelY) if (block.getCollisionFlag(Block.TOP | Block.LEFT)) return (pixelX - halfBlockHeight);
                    if (pixelEndY > pixelY) if (block.getCollisionFlag(Block.BOTTOM | Block.LEFT)) return (pixelX - halfBlockHeight);
                }
                if (coorCache == -1) if (pixelEndX > pixelX) {
                    if (pixelStartY < pixelY) if (block.getCollisionFlag(Block.TOP | Block.RIGHT)) coorCache = pixelX;
                    if (pixelEndY > pixelY) if (block.getCollisionFlag(Block.BOTTOM | Block.RIGHT)) coorCache = pixelX;
                }
                pixelY += blockHeight;
            }
            if (coorCache != -1) return coorCache;
            pixelX += blockWidth;
        }
        return -1;
    }
}
