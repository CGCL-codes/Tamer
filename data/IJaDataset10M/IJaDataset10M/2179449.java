package com.jdev.jsprite;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author jdeverna
 */
public class ImageFile {

    private BufferedImage image;

    private int width;

    private int height;

    private long fileSize;

    private String name;

    public ImageFile(String name, File f) {
        try {
            image = ImageIO.read(f);
            width = image.getWidth();
            height = image.getHeight();
            fileSize = f.length();
            this.name = name;
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
