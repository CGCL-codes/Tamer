package org.loon.framework.game.simple.media.flv;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import org.loon.framework.game.simple.utils.FileUtils;
import org.loon.framework.game.simple.utils.GraphicsUtils;

/**
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
public class FLVEngine {

    private List cacheFrame = new ArrayList(3000);

    private Dimension size;

    private long timer;

    private OutputStream out;

    private FLVContainer container;

    public FLVEngine(int width, int height) {
        this.size = FLVEngine.matchSize(width, height);
    }

    public Dimension getSize() {
        return size;
    }

    public synchronized void open(String fileName) throws IOException {
        open(new File(fileName));
    }

    public synchronized void open(File file) throws IOException {
        FileUtils.makedirs(file);
        out = new FileOutputStream(file);
        container = FLVEngine.getFLVContainer(out, size);
    }

    private class Animation {

        BufferedImage image;

        long endTimer;

        public Animation(BufferedImage image, long endTimer) {
            this.image = image;
            this.endTimer = endTimer;
        }
    }

    public void addCacheFrame(BufferedImage image) {
        addFrame(image, 220);
    }

    public void addCacheFrame(BufferedImage image, long elapsedTime) {
        cacheFrame.add(new Animation(image, timer += elapsedTime));
    }

    public synchronized void addFrame(BufferedImage image) {
        addFrame(image, 220);
    }

    public synchronized void addFrame(BufferedImage image, long elapsedTime) {
        try {
            container.writeFrame(GraphicsUtils.getResize(image, size), timer += elapsedTime);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void close() {
        try {
            int size = cacheFrame.size();
            if (size > 0) {
                for (int i = 0; i < size; i++) {
                    Animation animation = (Animation) cacheFrame.get(i);
                    container.writeFrame(animation.image, animation.endTimer);
                }
            }
            cacheFrame.clear();
            out.flush();
            out.close();
            out = null;
            container = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
	 * 在指定位置，以指定图像数组，指定速度导出FLV文件。
	 * 
	 * @param fileName
	 * @param images
	 * @param sleep
	 */
    public static void outputFLV(String fileName, BufferedImage[] images, int sleep) {
        if (images == null || images.length == 0) {
            throw new RuntimeException("");
        }
        try {
            int width = images[0].getWidth();
            int height = images[0].getHeight();
            int timer = 0, length = images.length;
            Dimension size = FLVEngine.matchSize(width, height);
            FileUtils.makedirs(fileName);
            OutputStream out = new FileOutputStream(fileName);
            FLVContainer container = FLVEngine.getFLVContainer(out, size);
            for (int i = 0; i < length; i++) {
                container.writeFrame(GraphicsUtils.matchBufferedImage(images[i], size), timer += sleep);
            }
            out.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
	 * 获得一个flv容器
	 * 
	 * @param out
	 * @param w
	 * @param h
	 * @return
	 * @throws IOException
	 */
    public static FLVContainer getFLVContainer(OutputStream out, int w, int h) throws IOException {
        return new FLVContainer(out, w, h);
    }

    /**
	 * 获得一个flv容器
	 * 
	 * @param out
	 * @param size
	 * @return
	 * @throws IOException
	 */
    public static FLVContainer getFLVContainer(OutputStream out, Dimension size) throws IOException {
        return new FLVContainer(out, size);
    }

    /**
	 * 匹配图像大小为FLV适应的格式
	 * 
	 * @param width
	 * @param height
	 * @return
	 */
    public static Dimension matchSize(int width, int height) {
        float nw = (float) width % ScreenVideoData.block;
        float nh = (float) height % ScreenVideoData.block;
        if (nw != 0) {
            nw = width - nw;
        } else {
            nw = width;
        }
        if (nh != 0) {
            nh = height - nh;
        } else {
            nh = height;
        }
        return new Dimension((int) nw, (int) nh);
    }
}
