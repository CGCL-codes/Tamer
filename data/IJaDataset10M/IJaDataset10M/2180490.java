package utility;

import java.awt.Color;
import main.Sprite;

public class ContraPlatform extends Sprite {

    Color myColor;

    public ContraPlatform(int x, int y, int width, int height, Color color) {
        super();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        myColor = color;
    }

    @Override
    public void draw() {
        g.setColor(myColor);
        g.fillRect(x, y, width, height);
    }

    @Override
    public void think() {
    }

    @Override
    public boolean doesCollide(Sprite s) {
        if (s instanceof ContraMan) {
            if (x <= s.getX() + (s.getWidth() / 2) && x + width >= s.getX() + (s.getWidth() / 2) && y < s.getY() + s.getHeight() && y + height > s.getY()) {
                if (((ContraMan) s).isJumping()) {
                    if (((ContraMan) s).isFalling()) {
                        s.setY(y - s.getHeight());
                        ((ContraMan) s).jumpInterrupted(true);
                        ((ContraMan) s).setOnPlatform(true, x, x + width);
                        return true;
                    } else {
                        ((ContraMan) s).jumpInterrupted(false);
                        s.setDx(0);
                        s.setY(y + height);
                        return true;
                    }
                } else {
                    if (s.getDx() > 0) {
                        s.setX(x - s.getWidth() / 2 - 1);
                    } else if (s.getDx() < 0) {
                        s.setX(x + width - (s.getWidth() / 2) + 1);
                    }
                }
            }
        } else if (s instanceof ContraBullit) {
            int sLeftEdge = s.getX();
            int sRightEdge = s.getX() + s.getWidth();
            int sTopEdge = s.getY();
            int sBottomEdge = s.getY() + s.getHeight();
            int leftEdge = this.x;
            int rightEdge = this.x + this.width;
            int topEdge = this.y;
            int bottomEdge = this.y + this.height;
            if (sLeftEdge <= rightEdge && sRightEdge >= leftEdge && sTopEdge <= bottomEdge && sBottomEdge >= topEdge) {
                s.die();
            }
        }
        return false;
    }
}
