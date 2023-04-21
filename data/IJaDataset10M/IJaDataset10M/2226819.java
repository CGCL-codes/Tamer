package elf;

import java.awt.Dimension;
import java.awt.Image;

public class GfxItem {

    private String name;

    private Image image;

    private int sizeX, sizeY;

    GfxItem(String name, Image image, int sizeX, int sizeY) {
        this.name = name;
        this.image = image;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }

    public String getName() {
        return name;
    }

    public Image getImage() {
        return image;
    }

    public Dimension getDimension() {
        return new Dimension(sizeX, sizeY);
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }
}
