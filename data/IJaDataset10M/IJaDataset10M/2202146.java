package com.flagstone.transform;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * The FSScreenVideoPacket class is used to encode or decode a frame of video data using 
 * Macromedia's ScreenVideo format.
 * 
 */
public final class FSScreenVideoPacket implements Cloneable {

    private boolean keyFrame;

    private int codec;

    private int blockWidth;

    private int blockHeight;

    private int imageWidth;

    private int imageHeight;

    private ArrayList imageBlocks;

    public FSScreenVideoPacket(byte[] data) {
        decode(data);
    }

    public FSScreenVideoPacket(boolean key, int codec, int imageWidth, int imageHeight, int blockWidth, int blockHeight, ArrayList blocks) {
        setKeyFrame(key);
        setCodec(codec);
        setImageWidth(imageWidth);
        setImageHeight(imageHeight);
        setBlockWidth(blockWidth);
        setBlockHeight(blockHeight);
        setImageBlocks(blocks);
    }

    public void add(FSImageBlock block) {
        imageBlocks.add(block);
    }

    public boolean getKeyFrame() {
        return keyFrame;
    }

    public void setKeyFrame(boolean key) {
        keyFrame = key;
    }

    public int getCodec() {
        return codec;
    }

    public void setCodec(int codec) {
        this.codec = codec;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(int width) {
        imageWidth = width;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(int height) {
        imageHeight = height;
    }

    public int getBlockWidth() {
        return blockWidth;
    }

    public void setBlockWidth(int width) {
        blockWidth = width;
    }

    public int getBlockHeight() {
        return blockHeight;
    }

    public void setBlockHeight(int height) {
        blockHeight = height;
    }

    public ArrayList getImageBlocks() {
        return imageBlocks;
    }

    public void setImageBlocks(ArrayList blocks) {
        imageBlocks = new ArrayList(blocks);
    }

    public Object clone() {
        FSScreenVideoPacket anObject = null;
        try {
            anObject = (FSScreenVideoPacket) super.clone();
            anObject.imageBlocks = new ArrayList();
            for (Iterator i = imageBlocks.iterator(); i.hasNext(); ) anObject.imageBlocks.add(((FSImageBlock) i.next()).clone());
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
        return anObject;
    }

    public boolean equals(Object anObject) {
        boolean result = false;
        if (super.equals(anObject)) {
            FSScreenVideoPacket typedObject = (FSScreenVideoPacket) anObject;
            result = keyFrame == typedObject.keyFrame;
            result = result && codec == typedObject.codec;
            result = result && imageWidth == typedObject.imageWidth;
            result = result && imageHeight == typedObject.imageHeight;
            result = result && blockHeight == typedObject.blockHeight;
            result = result && blockWidth == typedObject.blockWidth;
            result = result && imageBlocks.equals(typedObject.imageBlocks);
        }
        return result;
    }

    private int length() {
        int length = 5;
        FSImageBlock block;
        for (Iterator i = imageBlocks.iterator(); i.hasNext(); ) {
            block = (FSImageBlock) i.next();
            length += 2;
            if (!block.isEmpty()) {
                length += block.getData().length;
            }
        }
        return length;
    }

    public byte[] encode() {
        byte[] data = new byte[length()];
        FSCoder coder = new FSCoder(FSCoder.LITTLE_ENDIAN, data);
        coder.writeBits(keyFrame ? FSVideo.KeyFrame : FSVideo.Frame, 4);
        coder.writeBits(codec, 4);
        coder.writeBits((blockWidth / 16) - 1, 4);
        coder.writeBits(imageWidth, 12);
        coder.writeBits((blockHeight / 16) - 1, 4);
        coder.writeBits(imageHeight, 12);
        FSImageBlock block;
        byte[] blockData;
        for (Iterator i = imageBlocks.iterator(); i.hasNext(); ) {
            block = (FSImageBlock) i.next();
            if (block.isEmpty()) {
                coder.writeWord(0, 2);
            } else {
                blockData = block.getData();
                coder.writeBits(blockData.length, 16);
                coder.writeBytes(blockData);
            }
        }
        return coder.getData();
    }

    public void decode(byte[] data) {
        FSCoder coder = new FSCoder(FSCoder.LITTLE_ENDIAN, data);
        keyFrame = coder.readBits(4, false) == 1 ? true : false;
        codec = coder.readBits(4, false);
        blockWidth = (coder.readBits(4, false) + 1) * 16;
        imageWidth = coder.readBits(12, false);
        blockHeight = (coder.readBits(4, false) + 1) * 16;
        imageHeight = coder.readBits(12, false);
        int columns = imageWidth / blockWidth + ((imageWidth % blockWidth > 0) ? 1 : 0);
        int rows = imageHeight / blockHeight + ((imageHeight % blockHeight > 0) ? 1 : 0);
        int height = imageHeight;
        int width = imageWidth;
        imageBlocks = new ArrayList(rows * columns);
        FSImageBlock block;
        for (int i = 0; i < rows; i++, height -= blockHeight) {
            for (int j = 0; j < columns; j++, width -= blockWidth) {
                int length = coder.readBits(16, false);
                if (length != 0) {
                    byte[] blockData = new byte[length];
                    coder.readBytes(blockData);
                    int dataHeight = (height < blockHeight) ? height : blockHeight;
                    int dataWidth = (width < blockWidth) ? width : blockWidth;
                    block = new FSImageBlock(dataHeight, dataWidth, blockData);
                } else {
                    block = new FSImageBlock(0, 0, null);
                }
                imageBlocks.add(block);
            }
        }
    }
}
