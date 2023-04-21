package markgame2d.engine;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import markgame2d.engine.interfaces.Alive;
import markgame2d.engine.interfaces.Bounded;
import markgame2d.engine.interfaces.Moveable;

public class MarkSprite implements Alive, Moveable, Bounded {

    public BufferedImage image;

    public float x;

    public float y;

    public int width;

    public int height;

    private boolean alive = true;

    public MarkSprite(float x, float y, String path) {
        this(x, y, MarkGame.loadImage(path));
    }

    public MarkSprite(float x, float y, BufferedImage image) {
        this.image = image;
        this.x = x;
        this.y = y;
        this.width = image.getWidth(null);
        this.height = image.getHeight(null);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setXY(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void centerOnXY(float x, float y) {
        this.x = x - width / 2f;
        this.y = y - height / 2f;
    }

    public float getCenterX() {
        return x + width / 2f;
    }

    public float getCenterY() {
        return y + height / 2f;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void move(float dx, float dy) {
        x += dx;
        y += dy;
    }

    public int getRoundX() {
        return Math.round(x);
    }

    public int getRoundY() {
        return Math.round(y);
    }

    public void draw(Graphics g) {
        g.drawImage(this.image, this.getRoundX(), this.getRoundY(), null);
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public boolean isAlive() {
        return alive;
    }

    public Rectangle getBounds() {
        return new Rectangle(getRoundX(), getRoundY(), width, height);
    }

    public void makeTransparendByCornerColor() {
        BufferedImage other = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        other.getGraphics().drawImage(image, 0, 0, null);
        int cornerColor = image.getRGB(0, 0);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (image.getRGB(i, j) == cornerColor) {
                    other.setRGB(i, j, 0x00000000);
                }
            }
        }
        this.image = other;
    }
}
