package org.loon.framework.javase.game.media.flv;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * 
 * Copyright 2008 - 2009
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 * @project loonframework
 * @author chenpeng
 * @email：ceponline@yahoo.com.cn
 * @version 0.1
 */
public class ScreenVideoData implements DataWritter {

    protected static final int block = 16, blockWidth = 16, blockHeight = 16;

    private int imageWidth, imageHeight;

    private int blocksX, blocksY;

    private DataWritter[] imageBlocks;

    public ScreenVideoData(BufferedImage image, int compression) {
        imageWidth = image.getWidth();
        imageHeight = image.getHeight();
        createImageBlocks(image);
    }

    /**
	 * 输出图像数据块到FLV
	 * 
	 */
    public void write(FLVDataOutputStream out) throws IOException {
        out.writeShort(((blockWidth / block) - 1) << 12 | imageWidth);
        out.writeShort(((blockHeight / block) - 1) << 12 | imageHeight);
        int length = imageBlocks.length;
        for (int i = 0; i < length; i++) {
            imageBlocks[i].write(out);
        }
    }

    /**
	 * 创建一组图像数据块
	 * 
	 * @param image
	 */
    private void createImageBlocks(BufferedImage image) {
        blocksX = (int) Math.ceil(1.0d * imageWidth / blockWidth);
        blocksY = (int) Math.ceil(1.0d * imageHeight / blockHeight);
        imageBlocks = new ScreenVideoDataImage[blocksX * blocksY];
        int length = imageBlocks.length;
        for (int i = 0; i < length; i++) {
            imageBlocks[i] = addBlock(i, image);
        }
    }

    /**
	 * 添加一组图像数据块
	 * 
	 * @param block
	 * @param image
	 * @return
	 */
    public ScreenVideoDataImage addBlock(int block, BufferedImage image) {
        int x = block % blocksX;
        int y = block / blocksX;
        int x1 = x * blockWidth;
        int y1 = imageHeight - y * blockHeight - blockHeight;
        byte[] data = new byte[blockHeight * blockWidth * 3];
        int pos = 0, rgb = 0, blength = blockHeight - 1;
        for (int offsetY = 0; offsetY < blockHeight; offsetY++) {
            for (int offsetX = 0; offsetX < blockWidth; offsetX++) {
                rgb = image.getRGB(offsetX + x1, (blength - offsetY) + y1);
                data[pos++] = (byte) (rgb >> 0 & 0xff);
                data[pos++] = (byte) (rgb >> 8 & 0xff);
                data[pos++] = (byte) (rgb >> 16 & 0xff);
            }
        }
        return new ScreenVideoDataImage(data);
    }
}
