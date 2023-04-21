package com.peterhi.player.shapes;

import java.util.UUID;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Image extends Shape {

    public java.awt.Image image;

    public int width;

    public int height;

    public Image(boolean assignName) {
        if (assignName) {
            name = UUID.randomUUID().toString();
        }
    }

    @Override
    public void draw(Graphics2D g) {
        g.drawImage(image, x, y, width, height, null);
    }

    public Rectangle bounds() {
        return new Rectangle(x, y, width, height);
    }
}
